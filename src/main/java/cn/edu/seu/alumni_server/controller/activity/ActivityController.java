package cn.edu.seu.alumni_server.controller.activity;

import cn.edu.seu.alumni_server.annotation.web_response.WebResponseAPIMethod;
import cn.edu.seu.alumni_server.common.CONST;
import cn.edu.seu.alumni_server.common.Utils;
import cn.edu.seu.alumni_server.common.web_response_dto.WebResponse;
import cn.edu.seu.alumni_server.common.web_response_dto.WebServiceSuccessMessage;
import cn.edu.seu.alumni_server.controller.dto.*;
import cn.edu.seu.alumni_server.controller.dto.enums.MessageType;
import cn.edu.seu.alumni_server.dao.entity.Activity;
import cn.edu.seu.alumni_server.dao.entity.ActivityMember;
import cn.edu.seu.alumni_server.dao.mapper.AccountMapper;
import cn.edu.seu.alumni_server.dao.mapper.ActivityMapper;
import cn.edu.seu.alumni_server.dao.mapper.ActivityMemberMapper;
import cn.edu.seu.alumni_server.exceptions.ActivityMemberServiceException;
import cn.edu.seu.alumni_server.exceptions.ActivityServiceException;
import cn.edu.seu.alumni_server.interceptor.registration.RegistrationRequired;
import cn.edu.seu.alumni_server.interceptor.token.Acl;
import cn.edu.seu.alumni_server.service.ActivityMemberService;
import cn.edu.seu.alumni_server.service.ActivityService;
import cn.edu.seu.alumni_server.service.MessageService;
import cn.edu.seu.alumni_server.service.SecurityService;
import cn.edu.seu.alumni_server.service.SubscribeMessageService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executor;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tk.mybatis.mapper.entity.Example;

@Acl
@RestController
public class ActivityController {

	@Autowired
	HttpServletRequest request;

	@Autowired
	private ActivityService activityService;

	@Autowired
	private ActivityMemberService activityMemberService;

	@Autowired
	MessageService messageService;

	@Autowired
	ActivityMapper activityMapper;
	@Autowired
	ActivityMemberMapper activityMemberMapper;
	@Autowired
	SubscribeMessageService subscribeMessageService;
	@Autowired
	SecurityService securityService;
	@Autowired
	AccountMapper accountMapper;
	/**
	 * 创建一个活动
	 */
	@PostMapping("/activities/test")
	public WebResponse test(@RequestBody DemoDTO demoDTO
	) {
		return new WebResponse();
	}

	/**
	 * 创建一个活动
	 *
	 * @param visibleStatus 0圈内可见，1全部可见，默认1
	 */
	@RegistrationRequired
	@PostMapping("/activities/adapter")
	public WebResponse createActivityAdapter(
		@RequestParam Long alumniCircleId,
		@RequestParam String activityName,
		@RequestParam String activityDesc,
		@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date activityTime,
		@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date expirationTime,
		@RequestParam(required = false) String img1,
		@RequestParam(required = false) String img2,
		@RequestParam(required = false) String img3,
		@RequestParam(required = false) String img4,
		@RequestParam(required = false) String img5,
		@RequestParam(required = false) String img6,
		@RequestParam(required = false) Boolean visibleStatus
	) {
		ActivityDTO t = new ActivityDTO();
		t.setAlumniCircleId(alumniCircleId);
		t.setAccountId(
			(Long) request.getAttribute("accountId")
		);
		t.setActivityName(activityName);
		t.setActivityDesc(activityDesc);
		t.setActivityTime(Utils.addEightHours(activityTime));
		t.setExpirationTime(Utils.addEightHours(expirationTime));
		t.setImg1(img1);
		t.setImg2(img2);
		t.setImg3(img3);
		t.setImg4(img4);
		t.setImg5(img5);
		t.setImg6(img6);
		t.setValidStatus(true);

		if (visibleStatus == null) {
			visibleStatus = true;
		}

		t.setVisibleStatus(visibleStatus);
		return createActivity(t);
	}


	/**
	 * 创建一个活动
	 */
	@PostMapping("/activities")
	public WebResponse createActivity(
		@RequestBody ActivityDTO activityDTO
	) {
		//检验用户创建的活动的文字和图片有没有敏感内容
		boolean isLegal = securityService.checkoutActivityContentSecurity(activityDTO);
		if (!isLegal) {
			return new WebResponse().fail("文字或图片含有敏感信息");
		}
		Long accountId = (Long) request.getAttribute("accountId");
		activityDTO.setAccountId(accountId);
		// 在这里完成对于创建活动的构建
		try {
			Activity targetActivity = this.activityService.checkInputtedActivityForCreate(
				activityDTO
			);
			// TODO 这里应该有一个判断是否全网有效的逻辑

			// 执行插入
			this.activityService.insertActivity(targetActivity);
			// 注意是现有活动, 再有成员, 将当前用户加入到这个活动中
			this.activityMemberService.addAccountToActivity(
				targetActivity.getActivityId(), targetActivity.getAccountId()
			);
			return new WebResponse<>().success(new ActivityDTO(targetActivity));
		} catch (ActivityServiceException | Exception | ActivityMemberServiceException e) {
			return new WebResponse().fail(e.getMessage());
		}
	}

	/**
	 * 修改一个活动的信息.
	 */
	@PutMapping("/activities")
	@WebResponseAPIMethod
	public Object updateActivity(
		@RequestParam Long activityId,
		@RequestParam(required = false)
			String activityName,
		@RequestParam(required = false)
			String activityDesc,
		@RequestParam(required = false)
		@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
			Date activityTime,
		@RequestParam(required = false)
		@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
			Date expirationTime,
		@RequestParam(required = false) String img1,
		@RequestParam(required = false) String img2,
		@RequestParam(required = false) String img3,
		@RequestParam(required = false) String img4,
		@RequestParam(required = false) String img5,
		@RequestParam(required = false) String img6
	) throws ActivityServiceException {
		//检验用户创建的活动的文字和图片有没有敏感内容
		ActivityDTO t = new ActivityDTO();
		t.setActivityName(activityName);
		t.setActivityDesc(activityDesc);
		t.setImg1(img1);
		t.setImg2(img2);
		t.setImg3(img3);
		t.setImg4(img4);
		t.setImg5(img5);
		t.setImg6(img6);
		boolean isLegal = securityService.checkoutActivityContentSecurity(t);

		if (!isLegal) {
			return new WebResponse().fail("文字或图片含有敏感信息");
		}

		this.activityService.updateActivity(
			activityId, activityName, activityDesc,
			activityTime, expirationTime,
			img1, img2, img3, img4, img5, img6
		);
		return new WebServiceSuccessMessage("修改活动信息成功!");
	}

	/**
	 * 删除一个活动及其成员的所有信息.
	 *
	 * @param activityId 活动的 id.
	 * @return 响应结果.
	 */
	@DeleteMapping("/activities")
	public WebResponse deleteActivity(@RequestParam Long activityId) {
		ActivityDTO activityDTO = new ActivityDTO();
		activityDTO.setActivityId(activityId);
		try {
			Activity activity = this.activityService.deleteActivityDAO(activityDTO);
			this.activityService.deleteActivity(activity);
			return new WebResponse().success(activityDTO);
		} catch (ActivityServiceException | Exception e) {
			return new WebResponse().fail(e.getMessage());
		}
	}

	/**
	 * 查询一个活动的基本信息.
	 *
	 * @param activityId 活动的 id.
	 * @return 注意返回一个 json 格式的对象, 其中的 Datetime 是一个时间戳.
	 */
	@GetMapping("/activities")
	public WebResponse getBasicInfosOfActivityByActivityId(
		@RequestParam Long activityId
	) {
		try {
			ActivityBasicInfoDTO basicInfos =
				this.activityService.queryBasicInfoOfActivityByActivityId(activityId);
			// 还需要判断当前的用户是否在这个活动中
			Boolean hasEnrolled = this.activityMemberService.hasEnrolledInto(
				activityId, (Long) this.request.getAttribute("accountId")
			);
			ActivityBasicInfoWithCurrentAccountEnrollState ans =
				new ActivityBasicInfoWithCurrentAccountEnrollState(basicInfos, hasEnrolled);
			return new WebResponse().success(ans);
		} catch (ActivityServiceException | Exception e) {
			return new WebResponse().fail(e.getMessage());
		}
	}

	@RequestMapping("/activities/startedActivities")
	public WebResponse getBasicInfosOfActivitiesByStartedAccountId(
		@RequestParam int pageIndex,
		@RequestParam int pageSize
	) {
		Long accountId = (Long) request.getAttribute("accountId");
		try {
			PageHelper.startPage(pageIndex, pageSize);
			List<StartedOrEnrolledActivityInfoDTO> infos =
				this.activityService.queryBasicInfoOfActivityByStartedAccountId(
					accountId
				);
			return new WebResponse().success(
				new PageResult<>(((Page) infos).getTotal(), infos)
			);
		} catch (ActivityServiceException | Exception e) {
			return new WebResponse().fail(e.getMessage());
		}
	}


	@GetMapping(value = "/activities/enrolledActivities")
	public WebResponse getBasicInfosOfActivitiesByEnrolledAccountId(
		@RequestParam int pageIndex,
		@RequestParam int pageSize
	) {
		Long accountId = (
			(Long) request.getAttribute("accountId")
		);
		try {
			PageHelper.startPage(pageIndex, pageSize);
			List<StartedOrEnrolledActivityInfoDTO> infos =
				this.activityService.queryBasicInfosOfActivityByEnrolledAccountId(
					accountId
				);
			return new WebResponse().success(
				new PageResult<>(((Page) infos).getTotal(), infos)
			);
		} catch (ActivityServiceException | Exception e) {
			return new WebResponse().fail(e.getMessage());
		}
	}

	@GetMapping("/search/activities")
	public WebResponse searchByActivityName(
		@RequestParam(value = "activityName") String activityName,
		@RequestParam(value = "fuzzy", required = false, defaultValue = "true") Boolean fuzzy,
		@RequestParam int pageIndex,
		@RequestParam int pageSize
	) {
		try {
			PageHelper.startPage(pageIndex, pageSize);
			List<SearchedActivityInfoDTO> ans = (
				fuzzy ?
					this.activityService.queryActivitiesFuzzilyByActivityNameKeyWord(activityName) :
					this.activityService.queryActivitiesByActivityNameKeyWord(activityName)
			);
			return new WebResponse().success(
				new PageResult<>(((Page) ans).getTotal(), ans)
			);
		} catch (ActivityServiceException | Exception e) {
			return new WebResponse().fail(e.getMessage());
		}
	}

	@RequestMapping("/activities/recommend")
	public WebResponse recommend(@RequestParam int pageIndex,
		@RequestParam int pageSize) {
		Long accountId = (Long) request.getAttribute("accountId");

		PageResult res = activityService.recommend(pageIndex, pageSize, accountId);
		return new WebResponse().success(
			res
		);
	}


	@Autowired
	Executor executor;

	@WebResponseAPIMethod
	@GetMapping("/activity/notify")
	public Object notify(
		@RequestParam Long activityId,
		@RequestParam String title,
		@RequestParam String content,
		@RequestParam(required = false) String img
	) {
		Long accountId = (Long) request.getAttribute("accountId");

		List<Long> result;
		String messageType;

		ActivityBasicInfoDTO basicInfoDTO = activityMapper.getBasicInfosByActivityId(activityId);
		//发送系统消息，条件是活动的名称是seuAlumni系统活动并且发起人是王宗辉
		if(basicInfoDTO.getActivityName().equals("seuAlumni系统活动") && basicInfoDTO.getStarterName().equals("王宗辉")){
			result = accountMapper.selectAllValidAccountsId();
			messageType = CONST.SYSTEM_MESSAGE;
		}
		//普通的系统通知
		else{
			result = activityMemberMapper.getActivityAvailableMemberId(activityId);
			messageType = CONST.ACTIVITY_MESSAGE;
		}

		List<Long> finalResult = result;
		String finalMessageType = messageType;
		executor.execute(
			() -> {
				finalResult.forEach(
					(e) -> {
						messageService.newMessage(
							activityId, e,
							MessageType.ACTIVITY_NOTIFY.getValue(),
							title, content, img
						);
						subscribeMessageService.sendSubscribeMessage(
							e,
							activityId,
								finalMessageType
						);
					}
				);
			}
        );
		return "活动通知推送成功!";
	}

}
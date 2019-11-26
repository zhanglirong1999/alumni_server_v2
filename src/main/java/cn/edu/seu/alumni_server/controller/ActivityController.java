package cn.edu.seu.alumni_server.controller;

import cn.edu.seu.alumni_server.common.dto.WebResponse;
import cn.edu.seu.alumni_server.common.exceptions.ActivityServiceException;
import cn.edu.seu.alumni_server.controller.dto.ActivityBasicInfoDTO;
import cn.edu.seu.alumni_server.controller.dto.ActivityDTO;
import cn.edu.seu.alumni_server.controller.dto.ActivityWithMultipartFileDTO;
import cn.edu.seu.alumni_server.controller.dto.PageResult;
import cn.edu.seu.alumni_server.dao.entity.Activity;
import cn.edu.seu.alumni_server.dao.entity.ActivityMember;
import cn.edu.seu.alumni_server.service.ActivityMemberService;
import cn.edu.seu.alumni_server.service.ActivityService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

// TODO 之后需要加上 Acl 注解用来判断 token
//@Acl
@RestController
@SuppressWarnings("ALL")
public class ActivityController {

	@Autowired
	private ActivityService activityService;

	@Autowired
	private ActivityMemberService activityMemberService;

	/**
	 * 创建一个活动
	 */
	@PostMapping("/activities")
	public WebResponse createActivity(
		@RequestBody ActivityWithMultipartFileDTO activityDTO
	) {
		try {
			// 创建一个活动.
			ActivityWithMultipartFileDTO activityMPFFTO =
				this.activityService.checkInputtedActivityWithMultipartFileDTO(
					activityDTO
				);
			// 发送给 qcloud.
			Activity ans = this.activityService.insertActivityDAO(
				activityMPFFTO
			);
			// 插入数据库
			this.activityService.insertActivity(ans);
			// 注意创建活动的人应该被加入到活动中去.
			ActivityMember activityMember = new ActivityMember();
			activityMember.setAccountId(activityMPFFTO.getAccountId());
			activityMember.setActivityId(activityMPFFTO.getActivityId());
			// 刚刚创建的时候一定不用读消息.
			activityMember.setReadStatus(true);
			this.activityMemberService.insertActivityMember(activityMember);
			// 返回结果里面包括需要的 id, 以及处理完成的图片地址.
			return new WebResponse().success(new ActivityDTO(ans));
		} catch (ActivityServiceException e) {
			return new WebResponse().fail(e.getMessage());
		} catch (Exception e) {
			return new WebResponse().fail(
				"Severe exception which may caused by sql query when add member to an activity."
			);
		}
	}

	/**
	 * 修改一个活动的信息.
	 */
	@PutMapping("/activities")
	public WebResponse updateActivity(
		@RequestBody ActivityWithMultipartFileDTO activityWMPFDTO
	) {
		try {
			// 首先判断是否有效, 并且发送不为 null 的图片.
			Activity ans = this.activityService.updateActivityDAO(activityWMPFDTO);
			// 然后选择性更新.
			this.activityService.updateActivity(ans);
			return new WebResponse().success(new ActivityDTO(ans));
		} catch (ActivityServiceException | Exception e) {
			return new WebResponse().fail(e.getMessage());
		}
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
			return new WebResponse().success(basicInfos);
		} catch (ActivityServiceException | Exception e) {
			return new WebResponse().fail(e.getMessage());
		}
	}

	/**
	 * 查询一个发起者发起的所有活动的信息.
	 *
	 * @param accountId 发起者的账户 id
	 * @return 响应.
	 */
	@GetMapping("/activities/startedActivities")
	public WebResponse getBasicInfosOfActivitiesByStartedAccountId(
		@RequestParam Long accountId,
		@RequestParam int pageIndex,
		@RequestParam int pageSize
	) {
		try {
			PageHelper.startPage(pageIndex, pageSize);
			List<ActivityBasicInfoDTO> infos =
				this.activityService.queryBasicInfoOfActivityByStartedAccountId(
					accountId
				);
			return new WebResponse().success(
				new PageResult<>(((Page) infos).getTotal(), infos)
			);
		} catch (ActivityServiceException e) {
			return new WebResponse().fail(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			return new WebResponse().fail(
				String.format(
					"Severe exception may caused by sql query when using the account id %d",
					accountId
				)
			);
		}
	}

	/**
	 * 获取用户参与的活动的信息.
	 *
	 * @param accountId 账户的账号.
	 * @return 参与的活动的基本信息.
	 */
	@GetMapping("/activities/enrolledActivities")
	public WebResponse getBasicInfosOfActivitiesByEnrolledAccountId(
		@RequestParam Long accountId,
		@RequestParam int pageIndex,
		@RequestParam int pageSize
	) {
		try {
			PageHelper.startPage(pageIndex, pageSize);
			List<ActivityBasicInfoDTO> infos =
				this.activityService.queryBasicInfosOfActivityByEnrolledAccountId(
					accountId
				);
			return new WebResponse().success(
				new PageResult<>(((Page) infos).getTotal(), infos)
			);
		} catch (ActivityServiceException e) {
			return new WebResponse().fail(e.getMessage());
		} catch (Exception e) {
			return new WebResponse().fail(
				String.format(
					"Severe exception may caused by sql query when using the account id %d",
					accountId
				)
			);
		}
	}

	@GetMapping("/search/activities")
	public WebResponse searchByActivityName(
		@RequestParam(value = "activityName", required = true) String activityName,
		@RequestParam(value = "fuzzy", required = false, defaultValue = "true") Boolean fuzzy,
		@RequestParam int pageIndex,
		@RequestParam int pageSize
	) {
		try {
			PageHelper.startPage(pageIndex, pageSize);
			List<ActivityBasicInfoDTO> ans = (
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
}

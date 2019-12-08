package cn.edu.seu.alumni_server.controller;

import cn.edu.seu.alumni_server.common.dto.WebResponse;
import cn.edu.seu.alumni_server.common.exceptions.ActivityMemberServiceException;
import cn.edu.seu.alumni_server.common.token.Acl;
import cn.edu.seu.alumni_server.controller.dto.AccountDTO;
import cn.edu.seu.alumni_server.controller.dto.ActivityMemberBasicInfoDTO;
import cn.edu.seu.alumni_server.controller.dto.ActivityMemberDTO;
import cn.edu.seu.alumni_server.controller.dto.PageResult;
import cn.edu.seu.alumni_server.dao.entity.Account;
import cn.edu.seu.alumni_server.dao.entity.ActivityMember;
import cn.edu.seu.alumni_server.service.ActivityMemberService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Acl
public class ActivityMemberController {

	@Autowired
	HttpServletRequest request;

	@Autowired
	private ActivityMemberService activityMemberService;

	@PostMapping("/activities/members")
	public WebResponse addAccount2Activity(
		@RequestParam(value = "accountId", required = false)
			Long _accountId,
		@RequestParam(value = "activityId")
			Long activityId
	) {
		Long accountId = (
			(_accountId == null || _accountId.equals("")) ?
				(Long) request.getAttribute("accountId") :
				_accountId
		);
		ActivityMemberDTO activityMemberDTO = new ActivityMemberDTO();
		activityMemberDTO.setActivityId(activityId);
		activityMemberDTO.setAccountId(accountId);
		try {
			// 输入参数条件检验
			ActivityMember activityMember =
				this.activityMemberService.addMember2ActivityDAO(activityMemberDTO);
			// 执行插入
			this.activityMemberService.insertActivityMember(activityMember);
			return new WebResponse().success(activityMember);
		} catch (ActivityMemberServiceException | Exception e) {
			return new WebResponse().fail(e.getMessage());
		}
	}

	@GetMapping("/activities/members")
	public WebResponse getMembersOfActivityByActivityId(
		@RequestParam(value = "activityId") Long activityId,
		@RequestParam int pageIndex,
		@RequestParam int pageSize
	) {
		try {
			PageHelper.startPage(pageIndex, pageSize);
			List<ActivityMemberBasicInfoDTO> accountDAOs =
				this.activityMemberService.queryActivityMemberAccountInfosByAccountId(
					activityId
				);
			return new WebResponse().success(
				new PageResult<>(((Page) accountDAOs).getTotal(), accountDAOs)
			);
		} catch (ActivityMemberServiceException | Exception e) {
			return new WebResponse().fail(e.getMessage());
		}
	}

//	@PutMapping("/activities/informAllMembers")

	@PutMapping("/activities/members")
	public WebResponse updateActivityMemberReadStatus(
		@RequestParam(value = "activityId") Long activityId,
		@RequestParam(value = "accountId", required = false) Long accountId,
		@RequestParam(value = "readStatus") Boolean readStatus
	) {
		if (null == accountId || accountId.equals("")) {  // 通知全体
			try {
				this.activityMemberService.updateAllActivityMembersReadStatus(
					activityId,
					readStatus
				);
				return new WebResponse().success();
			} catch (ActivityMemberServiceException | Exception e) {
				return new WebResponse().fail(e.getMessage());
			}
		} else {  // 修改一位成员的读取通知状态
			try {
				this.activityMemberService.updateOneActivityMemberReadStatus(
					activityId, accountId, readStatus
				);
				return new WebResponse().success();
			} catch (ActivityMemberServiceException | Exception e) {
				return new WebResponse().fail(e.getMessage());
			}
		}
	}

	@DeleteMapping("/activities/members")
	public WebResponse deleteOneActivityMemberFromActivity(
		@RequestParam(value = "accountId", required = false)
			Long _accountId,
		@RequestParam(value = "activityId") Long activityId
	) {
		Long accountId = (
			(_accountId == null || _accountId.equals("")) ?
				(Long) request.getAttribute("accountId") :
				_accountId
		);
		try {
			ActivityMember activityMember =
				this.activityMemberService.removeOneActivityMemberDAO(activityId, accountId);
			this.activityMemberService.removeOneActivityMember(activityMember);
			return new WebResponse().success();
		} catch (ActivityMemberServiceException e) {
			return new WebResponse().fail(e.getMessage());
		}
	}
}

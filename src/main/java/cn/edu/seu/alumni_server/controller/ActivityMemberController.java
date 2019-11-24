package cn.edu.seu.alumni_server.controller;

import cn.edu.seu.alumni_server.common.dto.WebResponse;
import cn.edu.seu.alumni_server.common.exceptions.ActivityMemberServiceException;
import cn.edu.seu.alumni_server.controller.dto.AccountDTO;
import cn.edu.seu.alumni_server.controller.dto.ActivityMemberDTO;
import cn.edu.seu.alumni_server.controller.dto.PageResult;
import cn.edu.seu.alumni_server.dao.entity.Account;
import cn.edu.seu.alumni_server.dao.entity.ActivityMember;
import cn.edu.seu.alumni_server.service.ActivityMemberService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

// TODO 之后要加上 acl 注解来实现 token 的检验
@RestController
@SuppressWarnings("ALL")
//@Acl
public class ActivityMemberController {

	@Autowired
	private ActivityMemberService activityMemberService;

	@PostMapping("/activities/members")
	public WebResponse addAccount2Activity(
		@RequestParam(value = "activityId", required = true)
			Long activityId,
		@RequestParam(value = "accountId", required = true)
			Long accountId
	) {
		ActivityMemberDTO activityMemberDTO = new ActivityMemberDTO();
		activityMemberDTO.setActivityId(activityId);
		activityMemberDTO.setAccountId(accountId);
		try {
			ActivityMember activityMember =
				this.activityMemberService.addMember2ActivityDAO(activityMemberDTO);
			this.activityMemberService.insertActivityMember(activityMember);
			return new WebResponse().success(activityMember);
		} catch (ActivityMemberServiceException e) {
			return new WebResponse().fail(e.getMessage());
		} catch (Exception e) {
			return new WebResponse().fail(
				"Severe exception may be caused by sql query when add member to an activity."
			);
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
			List<Account> accountDAOs =
				this.activityMemberService.queryActivityMemberAccountInfosByAccountId(activityId);
			List<AccountDTO> accountDTOS = new ArrayList<>();
			for (Account accountDAO : accountDAOs) {
				accountDTOS.add(new AccountDTO(accountDAO));
			}
			return new WebResponse().success(
				new PageResult<>(((Page) accountDAOs).getTotal(), accountDTOS)
			);
		} catch (ActivityMemberServiceException e) {
			return new WebResponse().fail(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			return new WebResponse().fail(
				String.format(
					"Severe exception may be caused by sql query when using a wrong activity id %d.",
					activityId
				)
			);
		}
	}

	@PutMapping("/activities/members")
	public WebResponse updateActivityMemberReadStatus(
		@RequestParam(value = "activityId", required = true) Long activityId,
		@RequestParam(value = "accountId", required = false) Long accountId,
		@RequestParam(value = "readStatus", required = true) Boolean readStatus
	) {
		if (null == accountId || accountId.equals("")) {  // 通知全体
			try {
				this.activityMemberService.updateAllActivityMembersReadStatus(
					activityId,
					readStatus
				);
				return new WebResponse().success();
			} catch (ActivityMemberServiceException e) {
				return new WebResponse().fail(e.getMessage());
			} catch (Exception e) {
				return new WebResponse().fail(
					String.format(
						"Severe exception may be caused by sql query when using a wrong activity id %d.",
						activityId
					)
				);
			}
		} else {  // 修改一位成员的某一个读取状态
			try {
				this.activityMemberService.updateOneActivityMemberReadStatus(
					activityId, accountId, readStatus
				);
				return new WebResponse().success();
			} catch (ActivityMemberServiceException e) {
				return new WebResponse().fail(e.getMessage());
			} catch (Exception e) {
				return new WebResponse().fail(
					String.format(
						"Severe exception may be caused by sql query when using a wrong activity id %d or a wrong account id %d.",
						activityId, accountId
					)
				);
			}
		}
	}

	@DeleteMapping("/activities/members")
	public WebResponse deleteOneActivityMemberFromActivity(
		@RequestParam(value = "activityId", required = true) Long activityId,
		@RequestParam(value = "accountId", required = true) Long accountId
	) {
		try {
			ActivityMember activityMember =
				this.activityMemberService.removeOneActivityMemberDAO(activityId, accountId);
			this.activityMemberService.removeOneActivityMember(activityMember);
			return new WebResponse().success();
		} catch (ActivityMemberServiceException e) {
			if (e.getMessage().startsWith("___")) {
				return new WebResponse().fail("___CREATOR_QUIT_ERROR___");
			} else {
				return new WebResponse().fail(e.getMessage());
			}
		}
	}
}

package cn.edu.seu.alumni_server.controller;

import cn.edu.seu.alumni_server.common.dto.WebResponse;
import cn.edu.seu.alumni_server.common.exceptions.ActivityMemberServiceException;
import cn.edu.seu.alumni_server.controller.dto.AccountDTO;
import cn.edu.seu.alumni_server.controller.dto.ActivityMemberDTO;
import cn.edu.seu.alumni_server.dao.entity.Account;
import cn.edu.seu.alumni_server.dao.entity.ActivityMember;
import cn.edu.seu.alumni_server.service.ActivityMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;
import java.util.List;

// TODO 之后要加上 acl 注解来实现 token 的检验
@RestController
@SuppressWarnings("ALL")
//@Acl
public class ActivityMemberController {
    @Autowired
    private ActivityMemberService activityMemberService;

    @PostMapping("/activities/{activityId}/members")
    public WebResponse addAccount2Activity(
        @PathVariable(name="activityId")
        Long activityId,
        @RequestBody ActivityMemberDTO activityMemberDTO
    ) {
        activityMemberDTO.setActivityId(activityId);
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

    @GetMapping("/activities/{activityId}/members")
    public WebResponse getMembersOfActivityByActivityId(
        @PathVariable(name="activityId") Long activityId
    ) {
        try {
            List<Account> accountDAOs =
                this.activityMemberService.queryActivityMemberAccountInfosByAccountId(activityId);
            List<AccountDTO> accountDTOS = new LinkedList<>();
            for (Account accountDAO: accountDAOs) {
                accountDTOS.add(new AccountDTO(accountDAO));
            }
            return new WebResponse().success(accountDTOS);
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
    }

    @PutMapping("/activities/{activityId}/members")
    public WebResponse informAllMembersOfActivity(
        @PathVariable(name="activityId")
        Long activityId
    ) {
        try {
            this.activityMemberService.updateAllActivityMembersReadStatusUnread(
                activityId
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
    }
}

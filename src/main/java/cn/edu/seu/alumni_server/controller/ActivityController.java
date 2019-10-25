package cn.edu.seu.alumni_server.controller;

import cn.edu.seu.alumni_server.common.dto.WebResponse;
import cn.edu.seu.alumni_server.common.exceptions.ActivityServiceException;
import cn.edu.seu.alumni_server.controller.dto.ActivityDTO;
import cn.edu.seu.alumni_server.dao.entity.Activity;
import cn.edu.seu.alumni_server.dao.entity.ActivityMember;
import cn.edu.seu.alumni_server.service.ActivityMemberService;
import cn.edu.seu.alumni_server.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.HashMap;
import java.util.List;

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
     * @param activityDTO
     * @return
     */
    @PostMapping("/activities")
    public WebResponse createActivity(@RequestBody ActivityDTO activityDTO) {
        try {
            Activity activity = this.activityService.createActivityDAO(activityDTO);
            this.activityService.insertActivity(activity);
            // 注意创建活动的人应该被加入到活动中去.
            ActivityMember activityMember = new ActivityMember();
            activityMember.setAccountId(activity.getAccountId());
            activityMember.setActivityId(activity.getActivityId());
            activityMember.setReadStatus(true);
            this.activityMemberService.insertActivityMember(activityMember);
            return new WebResponse().success(new ActivityDTO(activity));
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
     * @param activityDTO
     * @return
     */
    @PutMapping("/activities")
    public WebResponse updateActivity(@RequestBody ActivityDTO activityDTO) {
        try {
            Activity activity = this.activityService.updateActivityDAO(activityDTO);
            this.activityService.updateActivity(activity);
            return new WebResponse().success(new ActivityDTO(activity));
        } catch (ActivityServiceException|Exception e) {
            return new WebResponse().fail(e.getMessage());
        }
    }

    /**
     * 彻底删除一个活动及其成员的所有信息.
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
        } catch (ActivityServiceException|Exception e) {
            return new WebResponse().fail(e.getMessage());
        }
    }

    /**
     * 查询一个活动的基本信息.
     * @param activityId 活动的 id.
     * @return 注意返回一个 json 格式的对象, 其中的 Datetime 是一个时间戳.
     */
    @GetMapping("/activities")
    public WebResponse getBasicInfosOfActivityByActivityId(
        @RequestParam Long activityId
    ) {
        try {
            HashMap<String, Object> basicInfos =
                this.activityService.queryBasicInfoOfActivityByActivityId(activityId);
            return new WebResponse().success(basicInfos);
        } catch (ActivityServiceException|Exception e) {
            return new WebResponse().fail(e.getMessage());
        }
    }

    /**
     * 查询一个发起者发起的所有活动的信息.
     * @param accountId 发起者的账户 id
     * @return 响应.
     */
    @GetMapping("/accounts/{accountId}/activities/startedActivities")
    public WebResponse getBasicInfosOfActivitiesByStarterAccountId(
        @PathVariable(name="accountId") Long accountId
    ) {
        try {
            List<HashMap<String, Object>> infos =
                this.activityService.queryBasicInfoOfActivityByStarterAccountId(
                    accountId
                );
            return new WebResponse().success(infos);
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

    /**
     * 获取用户参与的活动的信息.
     * @param accountId 账户的账号.
     * @return 参与的活动的基本信息.
     */
    @GetMapping("/accounts/{accountId}/activities/enrolledActivities")
    public WebResponse getBasicInfosOfActivitiesByEnrolledAccountId(
        @PathVariable(name="accountId") Long accountId
    ) {
        try {
            List<HashMap<String, Object>> infos =
                this.activityService.queryBasicInfosOfActivityByEnrolledAccountId(
                    accountId
                );
            return new WebResponse().success(infos);
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
}

package cn.edu.seu.alumni_server.controller;

import cn.edu.seu.alumni_server.common.CONST;

import cn.edu.seu.alumni_server.common.web_response_dto.WebResponse;
import cn.edu.seu.alumni_server.controller.dto.ActivityMemberBasicInfoDTO;
import cn.edu.seu.alumni_server.controller.dto.AddActivity;
import cn.edu.seu.alumni_server.controller.dto.PageResult;
import cn.edu.seu.alumni_server.dao.entity.ActivityMember;
import cn.edu.seu.alumni_server.dao.mapper.Activity2Mapper;
import cn.edu.seu.alumni_server.exceptions.ActivityMemberServiceException;
import cn.edu.seu.alumni_server.interceptor.token.Acl;
import cn.edu.seu.alumni_server.interceptor.token.TokenUtil;
import cn.edu.seu.alumni_server.service.Activity2Service;
import cn.edu.seu.alumni_server.service.ActivityMemberService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/activity")

public class Activity2Controller {
    @Autowired
    HttpServletRequest request;

    @Autowired
    private Activity2Service activity2Service;

    @Autowired
    private ActivityMemberService activityMemberService;

    @Autowired
    private Activity2Mapper activity2Mapper;

    @PostMapping("/getToken")
    public Object getToken(){
        String accountId = "27509995628544";
        return TokenUtil.createToken(accountId);
    }

    /**
     * 新增活动
     * @param addActivity
     * @return
     */
    @Acl
    @PostMapping("/add")
    public WebResponse addActivity(
            @RequestBody AddActivity addActivity
            ){
        Long accountId = (Long) request.getAttribute(CONST.ACL_ACCOUNTID);
        activity2Service.addActivity(addActivity,accountId);
        return new WebResponse().success("新增成功");
    }

    /**
     * 活动详情
     * @param id
     * @return
     */
    @Acl
    @GetMapping("/detail")
    public WebResponse activityDetail(
            @RequestParam Long id
    ){
        Long accountId = (Long) request.getAttribute(CONST.ACL_ACCOUNTID);
        return new WebResponse().success(activity2Service.getActivityDetail(id,accountId));
    }


    /**
     * 活动列表
     * @return
     */
    @Acl
    @GetMapping("/list")
    public WebResponse getActivityList(
            @RequestParam int pageIndex,
            @RequestParam int pageSize,
            @RequestParam int tag
    ) throws Exception {
        return new WebResponse().success(activity2Service.getList(pageIndex, pageSize,tag));
    }

    @Acl
    @DeleteMapping("/delete")
    public WebResponse deleteActivity(
            @RequestParam Long id
    ) throws Exception {
        Long accountId = (Long) request.getAttribute(CONST.ACL_ACCOUNTID);
        activity2Service.deleteActivity(id,accountId);
        return new WebResponse().success("删除成功");
    }

    @Acl
    @PostMapping("/join")
    public WebResponse joinActivity(
            @RequestParam Long id
    ){
        Long accountId = (Long) request.getAttribute(CONST.ACL_ACCOUNTID);
        activity2Service.joinActivity(id,accountId);
        return new WebResponse().success("加入成功");
    }

    @Acl
    @GetMapping("/people")
    public WebResponse getPeopleList(
            @RequestParam Long id,
            @RequestParam int pageIndex,
            @RequestParam int pageSize
    ){
        try {
            PageHelper.startPage(pageIndex, pageSize);
            List<ActivityMemberBasicInfoDTO> accountDAOs =
                    this.activityMemberService.queryActivity2MemberAccountInfosByAccountId(
                            id
                    );
            return new WebResponse().success(
                    new PageResult<>(((Page) accountDAOs).getTotal(), accountDAOs)
            );
        } catch (ActivityMemberServiceException | Exception e) {
            return new WebResponse().fail(e.getMessage());
        }
//        return new WebResponse().success(activity2Service.getPeopleList(id));
    }

    /**
     * 取消活动报名
     * @param id
     * @return
     */
    @Acl
    @PostMapping("/cancel")
    public WebResponse cancelActivity(
            @RequestParam Long id
            ) throws Exception {
        Long accountId = (
                (Long) request.getAttribute("accountId")
        );
        activity2Service.cancelActivity(id,accountId);
        return new WebResponse().success("取消成功");
    }

    /**
     * 我发起的活动列表
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @Acl
    @GetMapping("/startedActivities")
    public WebResponse getBasicInfosOfActivitiesByStartedAccountId(
            @RequestParam int pageIndex,
            @RequestParam int pageSize
    ) {
        Long accountId = (
                (Long) request.getAttribute("accountId")
        );
        PageHelper.startPage(pageIndex, pageSize);
        return new WebResponse().success(activity2Mapper.getStarted(accountId));
    }

    /**
     * 我参与的活动
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @Acl
    @GetMapping("/enrolledActivities")
    public WebResponse getBasicInfosOfActivitiesByEnrolledAccountId(
            @RequestParam int pageIndex,
            @RequestParam int pageSize
    ) {
        Long accountId = (
                (Long) request.getAttribute("accountId")
        );
        PageHelper.startPage(pageIndex, pageSize);
        return new WebResponse().success(activity2Mapper.getEnrolled(accountId));

    }

}










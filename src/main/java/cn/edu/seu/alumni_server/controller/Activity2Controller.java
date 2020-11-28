package cn.edu.seu.alumni_server.controller;

import cn.edu.seu.alumni_server.common.CONST;

import cn.edu.seu.alumni_server.common.web_response_dto.WebResponse;
import cn.edu.seu.alumni_server.controller.dto.AddActivity;
import cn.edu.seu.alumni_server.interceptor.token.Acl;
import cn.edu.seu.alumni_server.interceptor.token.TokenUtil;
import cn.edu.seu.alumni_server.service.Activity2Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/activity")

public class Activity2Controller {
    @Autowired
    HttpServletRequest request;

    @Autowired
    private Activity2Service activity2Service;

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
        return new WebResponse().success(activity2Service.getActivityDetail(id));
    }


    /**
     * 活动列表
     * @return
     */
    @Acl
    @GetMapping("/list")
    public WebResponse getActivityList(){
        return new WebResponse().success(activity2Service.getList());
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
            @RequestParam Long id
    ){
        return new WebResponse().success(activity2Service.getPeopleList(id));
    }








}

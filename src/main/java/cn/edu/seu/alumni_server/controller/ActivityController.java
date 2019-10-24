package cn.edu.seu.alumni_server.controller;

import cn.edu.seu.alumni_server.common.Utils;
import cn.edu.seu.alumni_server.common.dto.WebResponse;
import cn.edu.seu.alumni_server.common.token.Acl;
import cn.edu.seu.alumni_server.controller.dto.ActivityDTO;
import cn.edu.seu.alumni_server.dao.entity.Activity;
import cn.edu.seu.alumni_server.dao.mapper.ActivityMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
// TODO 之后需要加上 Acl 注解用来判断 token
//@Acl
@SuppressWarnings("ALL")
public class ActivityController {

    @Autowired
    private ActivityMapper activityMapper;

    /**
     * 创建一个活动
     * @param activityDTO
     * @return
     */
    @PostMapping("/activities")
    public WebResponse createActivity(@RequestBody ActivityDTO activityDTO) {
        if (
            activityDTO.getAccountId() != null &&
            activityDTO.getAccountId().equals("")
        )
            // 发现输入带有 id, 已经创建了一个活动对象
            return new WebResponse().fail(
                String.format(
                    "This activity id %d has already existed.",
                    activityDTO.getAccountId()
                )
            );
        Activity activity = activityDTO.toActivity();
        activity.setValidStatus(true);
        activity
    }
}

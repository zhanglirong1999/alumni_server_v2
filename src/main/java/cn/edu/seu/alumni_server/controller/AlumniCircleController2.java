package cn.edu.seu.alumni_server.controller;

import cn.edu.seu.alumni_server.common.dto.WebResponse;
import cn.edu.seu.alumni_server.controller.dto.alumnicircle.AlumniCircleDTO;
import cn.edu.seu.alumni_server.dao.entity.Activity;
import cn.edu.seu.alumni_server.dao.entity.AlumniCircleMember;
import cn.edu.seu.alumni_server.dao.mapper.ActivityMapper;
import cn.edu.seu.alumni_server.dao.mapper.AlumniCircleMapper;
import cn.edu.seu.alumni_server.dao.mapper.AlumniCircleMemberMapper;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@RestController
@RequestMapping("/alumniCircle")
public class AlumniCircleController2 {

    @Autowired
    AlumniCircleMemberMapper alumniCircleMemberMapper;

    @Autowired
    AlumniCircleMapper alumniCircleMapper;
    @Autowired
    ActivityMapper activityMapper;

    @PostMapping("/join")
    public WebResponse join(@RequestParam Long alumniCircleId,
                            @RequestParam Long accountId) {

        AlumniCircleMember alumniCircleMember = new AlumniCircleMember();
        alumniCircleMember.setAccountId(accountId);
        alumniCircleMember.setAlumniCircleId(alumniCircleId);
        alumniCircleMemberMapper.insert(alumniCircleMember);

        return new WebResponse();
    }

    @PostMapping("/leave")
    public WebResponse leave(@RequestParam Long alumniCircleId,
                             @RequestParam Long accountId) {

        AlumniCircleMember alumniCircleMember = new AlumniCircleMember();
        alumniCircleMember.setAccountId(accountId);
        alumniCircleMember.setAlumniCircleId(alumniCircleId);
        alumniCircleMember.setValidStatus(false);
        alumniCircleMemberMapper.insert(alumniCircleMember);
        return new WebResponse();
    }

    @GetMapping("/members")
    public WebResponse members(
            @RequestParam Long alumniCircleId,
            @RequestParam int pageIndex,
            @RequestParam int pageSize
    ) {
        PageHelper.startPage(pageIndex, pageSize);

        Example example = new Example(AlumniCircleMember.class);
        example.createCriteria().andEqualTo("alumniCircleId", alumniCircleId);
        List<AlumniCircleMember> res = alumniCircleMemberMapper.selectByExample(example);

        return new WebResponse(
//                new PageResult<FavoriteDTO>(1, )
        );
    }

    /**
     * 活动列表
     *
     * @return
     */
    @PostMapping("/activities")
    public WebResponse activities(@RequestParam Long alumniCircleId,
                                  @RequestParam int pageIndex,
                                  @RequestParam int pageSize
    ) {
        Activity activity = new Activity();
        activity.setAlumniCircleId(alumniCircleId);

        PageHelper.startPage(pageIndex, pageSize);
        List<Activity> activities = activityMapper.select(activity);

        return new WebResponse();
    }

    @PutMapping("/")
    public WebResponse maintain(@RequestBody AlumniCircleDTO alumniCircleDTO) {

        alumniCircleMapper.updateByPrimaryKey(alumniCircleDTO.toAlumniCircle());
        return new WebResponse();
    }

}

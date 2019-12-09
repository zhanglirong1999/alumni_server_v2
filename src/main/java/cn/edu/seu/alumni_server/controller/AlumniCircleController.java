package cn.edu.seu.alumni_server.controller;


import cn.edu.seu.alumni_server.common.CONST;
import cn.edu.seu.alumni_server.common.Utils;
import cn.edu.seu.alumni_server.common.dto.WebResponse;
import cn.edu.seu.alumni_server.common.exceptions.AlumniCircleServiceException;
import cn.edu.seu.alumni_server.common.token.Acl;
import cn.edu.seu.alumni_server.controller.dto.ActivityDTO;
import cn.edu.seu.alumni_server.controller.dto.MyAlumniCircleInfoDTO;
import cn.edu.seu.alumni_server.controller.dto.PageResult;
import cn.edu.seu.alumni_server.controller.dto.alumnicircle.AlumniCircleBasicInfoDTO;
import cn.edu.seu.alumni_server.controller.dto.alumnicircle.AlumniCircleDTO;
import cn.edu.seu.alumni_server.controller.dto.alumnicircle.AlumniCircleMemberDTO;
import cn.edu.seu.alumni_server.dao.entity.Activity;
import cn.edu.seu.alumni_server.dao.entity.AlumniCircle;
import cn.edu.seu.alumni_server.dao.entity.AlumniCircleMember;
import cn.edu.seu.alumni_server.dao.mapper.ActivityMapper;
import cn.edu.seu.alumni_server.dao.mapper.AlumniCircleMapper;
import cn.edu.seu.alumni_server.dao.mapper.AlumniCircleMemberMapper;
import cn.edu.seu.alumni_server.service.AlumniCircleService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@SuppressWarnings("ALL")
@RestController
@Acl
@RequestMapping("/alumniCircle")
public class AlumniCircleController {

    @Autowired
    HttpServletRequest request;

    @Autowired
    AlumniCircleService alumniCircleService;

    @Autowired
    ActivityMapper activityMapper;

    @Autowired
    AlumniCircleMapper alumniCircleMapper;


    @Autowired
    AlumniCircleMemberMapper alumniCircleMemberMapper;

    @GetMapping("/enrolledAlumniCircles")
    public WebResponse getEnrolledAlumniCirclesByAccountId(
            @RequestParam(value = "accountId", required = false)
                    Long _accountId,
            @RequestParam int pageIndex,
            @RequestParam int pageSize
    ) {
        Long accountId = (
                (_accountId == null || _accountId.equals("")) ?
                        (Long) request.getAttribute("accountId") :
                        _accountId
        );
        try {
            PageHelper.startPage(pageIndex, pageSize);
            List<AlumniCircleBasicInfoDTO> alumniCircleDTOList =
                    this.alumniCircleService.queryEnrolledAlumniCircleByAccountId(accountId);
            List<MyAlumniCircleInfoDTO> ans = new LinkedList<>();
            for (AlumniCircleBasicInfoDTO t : alumniCircleDTOList)
                ans.add(t.toMyAlumniCircleInfoDTO());
            return new WebResponse().success(
                    new PageResult<>(((Page) alumniCircleDTOList).getTotal(), ans)
            );
        } catch (AlumniCircleServiceException | Exception e) {
            return new WebResponse().fail(e.getMessage());
        }
    }

    @GetMapping("/search")
    public WebResponse searchByActivityName(
            @RequestParam(value = "alumniCircleName", required = true) String alumniCircleName,
            @RequestParam(value = "fuzzy", required = false, defaultValue = "true") Boolean fuzzy,
            @RequestParam int pageIndex,
            @RequestParam int pageSize
    ) {
        try {
            PageHelper.startPage(pageIndex, pageSize);
            List<AlumniCircleBasicInfoDTO> ans = (
                    fuzzy ?
                            this.alumniCircleService
                                    .queryAlumniCircleInfosFuzzilyByAluCirName(alumniCircleName) :
                            this.alumniCircleService
                                    .queryAlumniCircleInfosByAlumniCircleName(alumniCircleName)
            );
            List<MyAlumniCircleInfoDTO> finalAns = new LinkedList<>();
            for (AlumniCircleBasicInfoDTO t : ans)
                finalAns.add(t.toMyAlumniCircleInfoDTO());
            return new WebResponse().success(
                    new PageResult<>(((Page) ans).getTotal(), finalAns)
            );
        } catch (AlumniCircleServiceException e) {
            return new WebResponse().fail(e.getMessage());
        }
    }

    @RequestMapping("/recommend")
    public WebResponse alumniCirclesRecommend(@RequestParam int pageIndex,
                                              @RequestParam int pageSize) {
        PageHelper.startPage(pageIndex, pageSize);

        List<AlumniCircleBasicInfoDTO> res = alumniCircleService.alumniCirclesRecommend();
        return new WebResponse().success(
                new PageResult(((Page) res).getTotal(), res)
        );
    }

    @GetMapping("/information")
    public WebResponse information(@RequestParam Long alumniCircleId) {
        alumniCircleMapper.selectByPrimaryKey(alumniCircleId);
        return new WebResponse().success(
                alumniCircleMapper.selectByPrimaryKey(alumniCircleId));
    }


    @PostMapping("/join")
    public WebResponse join(@RequestParam Long alumniCircleId,
                            @RequestParam Long accountId) {

        AlumniCircleMember alumniCircleMember = new AlumniCircleMember();
        alumniCircleMember.setAccountId(accountId);
        alumniCircleMember.setAlumniCircleId(alumniCircleId);

        try {
            alumniCircleMemberMapper.insertSelective(alumniCircleMember);
        } catch (DuplicateKeyException e) {
            alumniCircleMemberMapper.joinUpdate(alumniCircleId, accountId);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new WebResponse();
    }

    @GetMapping("/leave")
    public WebResponse leave(@RequestParam Long alumniCircleId,
                             @RequestParam(required = false) Long accountId) {
        // TODO 仅测试用
        if (accountId == null)
            accountId = (Long) request.getAttribute(CONST.ACL_ACCOUNTID);

//        AlumniCircleMember alumniCircleMember = new AlumniCircleMember();
//        alumniCircleMember.setAccountId(accountId);
//        alumniCircleMember.setAlumniCircleId(alumniCircleId);
//        alumniCircleMember.setValidStatus(false);
//         TODO 退圈再加圈的逻辑
//        alumniCircleMemberMapper.updateByPrimaryKeySelective(alumniCircleMember);
        alumniCircleMemberMapper.leave(alumniCircleId, accountId);
        return new WebResponse();
    }

    @GetMapping("/members")
    public WebResponse members(
            @RequestParam Long alumniCircleId,
            @RequestParam int pageIndex,
            @RequestParam int pageSize
    ) {
        PageHelper.startPage(pageIndex, pageSize);
        List<AlumniCircleMemberDTO> res = alumniCircleMapper.getAlumniCircleMembers(alumniCircleId);

        return new WebResponse().success(
                new PageResult<AlumniCircleMemberDTO>(((Page) res).getTotal(), res));
    }

    /**
     * 当前校友圈的活动列表
     *
     * @return
     */
    @GetMapping("/activities")
    public WebResponse activities(@RequestParam Long alumniCircleId,
                                  @RequestParam int pageIndex,
                                  @RequestParam int pageSize
    ) {
        List<ActivityDTO> res = new ArrayList<>();

        Activity activity = new Activity();
        activity.setAlumniCircleId(alumniCircleId);

        PageHelper.startPage(pageIndex, pageSize);
        List<Activity> activities = activityMapper.select(activity);
        activities.forEach((e) -> {
            res.add(new ActivityDTO(e));
        });

        return new WebResponse().success(
                new PageResult<>(((Page) activities).getTotal(), res)
        );
    }

    @PutMapping("")
    public WebResponse maintain(@RequestBody AlumniCircleDTO alumniCircleDTO) {

        alumniCircleMapper.updateByPrimaryKey(alumniCircleDTO.toAlumniCircle());
        return new WebResponse();
    }

    @PostMapping("")
    public WebResponse create(@RequestBody AlumniCircleDTO alumniCircleDTO,
                              @RequestParam(required = false) Long accountId) {
        // TODO 仅测试用
        if (accountId == null)
            accountId = (Long) request.getAttribute(CONST.ACL_ACCOUNTID);

        if (accountId == null) {
            return new WebResponse().fail("accountId is null");
        }
        AlumniCircle alumniCircle = new AlumniCircle();
        alumniCircle.setAlumniCircleDesc(alumniCircleDTO.getAlumniCircleDesc());
        alumniCircle.setAlumniCircleName(alumniCircleDTO.getAlumniCircleName());
        alumniCircle.setAlumniCircleAnnouncement(alumniCircleDTO.getAlumniCircleAnnouncement());
        alumniCircle.setAvatar(alumniCircleDTO.getAvatar());

        alumniCircle.setCreatorId(accountId);
        alumniCircle.setAlumniCircleId(Utils.generateId());
        alumniCircleMapper.insertSelective(alumniCircle);
        return new WebResponse();
    }


}

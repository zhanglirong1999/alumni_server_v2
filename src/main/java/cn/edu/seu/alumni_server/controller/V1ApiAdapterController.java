package cn.edu.seu.alumni_server.controller;

import cn.edu.seu.alumni_server.common.CONST;
import cn.edu.seu.alumni_server.controller.dto.EducationDTO;
import cn.edu.seu.alumni_server.controller.dto.UserInfoDTO;
import cn.edu.seu.alumni_server.controller.dto.WorkDTO;
import cn.edu.seu.alumni_server.controller.dto.common.WebResponse;
import cn.edu.seu.alumni_server.dao.entity.*;
import cn.edu.seu.alumni_server.dao.mapper.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tk.mybatis.mapper.entity.Example;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("ALL")
@RestController
public class V1ApiAdapterController {

    @Autowired
    V1ApiAdapterMapper v1ApiAdapterMapper;

    @Autowired
    UserMapper userMapper;
    @Autowired
    UserInfoMapper userInfoMapper;
    @Autowired
    UserWorkMapper userWorkMapper;
    @Autowired
    UserFriendMapper userFriendMapper;
    @Autowired
    UserEducationMapper userEducationMapper;


    @RequestMapping("/query/detect/{openid}")
    public WorkDTO checkExsist(@PathVariable String openid) {
        return v1ApiAdapterMapper.checkExsist(openid);
    }


    @RequestMapping("/search/search/{content}/{way}/{page}/{limit}")
    public List<EducationDTO> search(
            @PathVariable String content,
            @PathVariable String way,
            @PathVariable int page,
            @PathVariable int limit) {
        return v1ApiAdapterMapper.search(content, page, limit);
    }
//    @RequestMapping("/search/searchdirect/{content}")
//    public List<EducationDTO> search(
//            @PathVariable String content) {
//        return v1ApiAdapterMapper.searchdirect(content);
//    }

    @RequestMapping("/friend/getallfriend/{openid}/{page}/{limit}")
    public List<EducationDTO> getallfriend(
            @PathVariable String content,
            @PathVariable String way,
            @PathVariable int page,
            @PathVariable int limit) {
        return v1ApiAdapterMapper.search(content, page, limit);
    }


    // 我的
    @RequestMapping("/query/getall/{openid}")
    public HashMap getall(
            @PathVariable String openid) {
        HashMap res = new HashMap();
        res.put("Base", v1ApiAdapterMapper.getUserInfos(openid));
        res.put("Education", v1ApiAdapterMapper.getUserInfos(openid));
        res.put("Work", v1ApiAdapterMapper.getWorks(openid));
        res.put("Personal", v1ApiAdapterMapper.getUser(openid));
        res.put("Friend", v1ApiAdapterMapper.getFriends(openid));
        return res;
    }

    @RequestMapping("/query/getbase/{openid}")
    public HashMap getbase(
            @PathVariable String openid) {
        HashMap res = new HashMap();
        res.put("Base", v1ApiAdapterMapper.getUserInfos(openid));
        res.put("Education", v1ApiAdapterMapper.getUserInfos(openid));
        res.put("Work", v1ApiAdapterMapper.getWorks(openid));
        res.put("Personal", v1ApiAdapterMapper.getUser(openid));
        res.put("Friend", v1ApiAdapterMapper.getFriends(openid));
        return res;
    }

    @RequestMapping("/query/getwork/{openid}")
    public List<WorkDTO> getwork(
            @PathVariable String openid) {
        return v1ApiAdapterMapper.getWorks(openid);
    }

    @RequestMapping("/query/geteducation/{openid}")
    public List<EducationDTO> geteducation(
            @PathVariable String openid) {
        return v1ApiAdapterMapper.getEducations(openid);
    }



    /* 基本信息、工作、教育经历增删改查 */

    @RequestMapping("/edit/editbase")
    public WebResponse editbase(
            @RequestBody UserInfoDTO userInfoDTO) {
        User user = new User();
        user.setOpenid(userInfoDTO.getOpenid());
        user.setPhone(userInfoDTO.getPhone());
        user.setEmail(userInfoDTO.getEmail());
        user.setWechat(userInfoDTO.getWechat());


        if (userMapper.select(user).size() > 0) {
            userMapper.updateByExampleSelective(user,
                    new Example(User.class).selectProperties("openid"));
        } else {
            userMapper.insertSelective(user);
        }

        UserInfo userInfo = new UserInfo();
        BeanUtils.copyProperties(userInfoDTO, userInfo);

        if (userInfoMapper.selectByExample(
                new Example(UserInfo.class).selectProperties("openid")).size() > 0) {
            userInfoMapper.updateByExampleSelective(userInfo,
                    new Example(User.class).selectProperties("openid"));
        } else {
            userInfoMapper.insertSelective(userInfo);
        }


        return new WebResponse();
    }


    @RequestMapping("/edit/addwork")
    public WebResponse addwork(
            @RequestBody WorkDTO workDTO) {
        UserWork userWork = new UserWork();
        userWork.setOpenid(workDTO.getOpenid());
        userWork.setCompany(workDTO.getCompany());
        userWork.setJob(workDTO.getJob());
        userWork.setEndYear(workDTO.getEnd_year());
        userWork.setStartYear(workDTO.getStart_year());

        userWorkMapper.insertSelective(userWork);
        return new WebResponse();
    }

    @RequestMapping("/edit/addeducation")
    public WebResponse addeducation(
            @RequestBody EducationDTO educationDTO) {
        UserEducation userEducation = new UserEducation();
        userEducation.setOpenid(educationDTO.getOpenid());
        userEducation.setBackground(educationDTO.getBackground());
        userEducation.setDepartment(educationDTO.getDepartment());
        userEducation.setSchool(educationDTO.getSchool());
        userEducation.setEndYear(educationDTO.getEnd_year());
        userEducation.setStartYear(educationDTO.getStart_year());
        userEducationMapper.insertSelective(userEducation);
        return new WebResponse();
    }

    @RequestMapping("/edit/deletework")
    public WebResponse deletework(
            @RequestBody Map<String, String> params) {
        String num = params.get("num");
        String openid = params.get("openid");
        v1ApiAdapterMapper.deletework(openid, num);
        return new WebResponse();
    }

    @RequestMapping("/edit/deleteeducation")
    public WebResponse deleteEducation(
            @RequestBody Map<String, String> params) {
        String num = params.get("num");
        String openid = params.get("openid");
        v1ApiAdapterMapper.deleteEducation(openid, num);
        return new WebResponse();
    }

    /*好友相关*/
    @RequestMapping("/friend/invite/{openid}/{friendid}")
    public WebResponse friendInvite(
            @PathVariable String openid,
            @PathVariable String friendid) {
        UserFriend userFriend = new UserFriend();
        userFriend.setOpenid(openid);
        userFriend.setFriendId(friendid);
        userFriend.setState(CONST.FriendStatus_2);
        userFriendMapper.updateByExampleSelective(userFriend,
                new Example(UserFriend.class)
                        .selectProperties("openid", "friend_id"));
        return new WebResponse();
    }

    @RequestMapping("/friend/accept/{openid}/{friendid}")
    public WebResponse friendAccept(
            @PathVariable String openid,
            @PathVariable String friendid) {
        UserFriend userFriend = new UserFriend();
        userFriend.setOpenid(openid);
        userFriend.setFriendId(friendid);
        userFriend.setState(CONST.FriendStatus_2);
        userFriendMapper.updateByExampleSelective(userFriend,
                new Example(UserFriend.class)
                        .selectProperties("openid", "friend_id"));

        return new WebResponse();
    }

    @RequestMapping("/friend/refuse/{openid}/{friendid}")
    public WebResponse friendRefuse(
            @PathVariable String openid,
            @PathVariable String friendid) {
        UserFriend userFriend = new UserFriend();
        userFriend.setOpenid(openid);
        userFriend.setFriendId(friendid);
        userFriend.setState(CONST.FriendStatus_2);
        userFriendMapper.updateByExampleSelective(userFriend,
                new Example(UserFriend.class)
                        .selectProperties("openid", "friend_id"));

        return new WebResponse();
    }

    @RequestMapping("/Card/readcard/{openid}/{cardid}")
    public UserInfo friendReadcard(
            @PathVariable String openid,
            @PathVariable String cardid) {

        UserFriend userFriend = new UserFriend();
        userFriend.setOpenid(openid);
        userFriend.setFriendId(cardid);

        List<UserFriend> res = userFriendMapper.select(userFriend);
        String state = CONST.FriendStatus_0;
        if (res.size() > 0) {
            state = res.get(0).getState();
        }

        if (state.equals(CONST.FriendStatus_2) ||
                state.equals(CONST.FriendStatus_1)
        ) {
            UserInfo userInfo = new UserInfo();
            return userInfoMapper.select(userInfo).get(0);
//            v1ApiAdapterMapper.getCardInfo(cardid);
        }
        return new UserInfo();
    }

}

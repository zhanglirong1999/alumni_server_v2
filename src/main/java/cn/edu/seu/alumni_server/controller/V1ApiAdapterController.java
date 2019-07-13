package cn.edu.seu.alumni_server.controller;

import cn.edu.seu.alumni_server.controller.dto.EducationDTO;
import cn.edu.seu.alumni_server.controller.dto.WorkDTO;
import cn.edu.seu.alumni_server.dao.mapper.UserEducationMapper;
import cn.edu.seu.alumni_server.dao.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class V1ApiAdapterController {

    @Autowired
    UserMapper userMapper;

    @Autowired
    UserEducationMapper userEducationMapper;

    @RequestMapping("/query/detect/{openid}")
    public WorkDTO checkExsist(@PathVariable String openid) {
        return userMapper.checkExsist(openid);
    }
//
//    @RequestMapping("/Card/readcard/{openid}/{cardid}")
//    public UserInfoDTO getCard() {
//
//    }

    @RequestMapping("/search/search/{content}/{way}/{page}/{limit}")
    public List<EducationDTO> search(
            @PathVariable String content,
            @PathVariable String way,
            @PathVariable int page,
            @PathVariable int limit) {
//        PageHelper.startPage(page, limit);
//        userEducationMapper.search(content, way, page, limit);
        return userEducationMapper.search(content, page, limit);
    }
}

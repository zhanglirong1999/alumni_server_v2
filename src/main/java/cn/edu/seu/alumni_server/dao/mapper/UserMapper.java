package cn.edu.seu.alumni_server.dao.mapper;

import cn.edu.seu.alumni_server.controller.dto.UserInfoDTO;
import cn.edu.seu.alumni_server.controller.dto.WorkDTO;
import cn.edu.seu.alumni_server.dao.entity.User;
import tk.mybatis.mapper.common.Mapper;

public interface UserMapper extends Mapper<User> {
    WorkDTO checkExsist(String openid);
}
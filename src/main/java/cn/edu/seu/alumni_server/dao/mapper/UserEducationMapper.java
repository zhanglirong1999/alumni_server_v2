package cn.edu.seu.alumni_server.dao.mapper;

import cn.edu.seu.alumni_server.controller.dto.EducationDTO;
import cn.edu.seu.alumni_server.dao.entity.UserEducation;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface UserEducationMapper extends Mapper<UserEducation> {
    List<EducationDTO> search(String content, int page, int limit);
}
package cn.edu.seu.alumni_server.dao.mapper;

import cn.edu.seu.alumni_server.controller.dto.alumnicircle.AlumniCircleMemberDTO;
import cn.edu.seu.alumni_server.dao.entity.AlumniCircleMember;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Repository
public interface AlumniCircleMemberMapper extends Mapper<AlumniCircleMember> {
    public List<AlumniCircleMemberDTO> getAlumniCircleMembers();
}

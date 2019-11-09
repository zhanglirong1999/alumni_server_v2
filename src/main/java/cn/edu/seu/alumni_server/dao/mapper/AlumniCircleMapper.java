package cn.edu.seu.alumni_server.dao.mapper;

import cn.edu.seu.alumni_server.controller.dto.AlumniCircleDTO;
import cn.edu.seu.alumni_server.dao.entity.AlumniCircle;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface AlumniCircleMapper extends Mapper<AlumniCircle> {

    /**
     * 根据用户账号来查询所需要的校友圈信息.
     * @param accountId 用户账号.
     * @return 所有参与的校友圈信息.
     */
    public List<AlumniCircleDTO> queryAlumniCircleInfosByAccountId(Long accountId);
}

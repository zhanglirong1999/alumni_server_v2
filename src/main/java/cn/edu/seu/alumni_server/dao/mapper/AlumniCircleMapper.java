package cn.edu.seu.alumni_server.dao.mapper;

import cn.edu.seu.alumni_server.controller.dto.AlumniCircleDTO;
import cn.edu.seu.alumni_server.dao.entity.AlumniCircle;
import tk.mybatis.mapper.common.Mapper;

import java.util.HashMap;
import java.util.List;

public interface AlumniCircleMapper extends Mapper<AlumniCircle> {

    /**
     * 根据用户账号来查询所参加的校友圈信息.
     * @param accountId 用户账号.
     * @return 所有参与的校友圈信息.
     */
    public List<HashMap<String, Object> > queryAlumniCircleInfosByAccountId(Long accountId);

    /**
     * 根据校友圈的名字来模糊查询.
     * @param name 校友圈的名字.
     * @return 模糊查询结果.
     */
    public List<HashMap<String, Object> > queryAlumniCircleInfosFuzzilyByAluCirName(String name);

    /**
     * 根据校友圈的名字来严格匹配查询查询.
     * @param name 校友圈的名字.
     * @return 模糊查询结果.
     */
    public List<HashMap<String, Object> > queryAlumniCircleInfosByAluCirName(String name);
}

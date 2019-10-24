package cn.edu.seu.alumni_server.dao.mapper;


import cn.edu.seu.alumni_server.dao.entity.Activity;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.HashMap;

@Repository
public interface ActivityMapper extends Mapper<Activity> {

    /**
     * 根据输入的活动 id 输出基本信息, 注意要输出基本
     * @param activityId 活动 id.
     * @return 查询的结果 json 对象.
     */
    HashMap<String, Object> getBasicInfos(Long activityId);
}

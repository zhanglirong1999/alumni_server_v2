package cn.edu.seu.alumni_server.dao.mapper;


import cn.edu.seu.alumni_server.dao.entity.Activity;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

@Repository
public interface ActivityMapper extends Mapper<Activity> {

    /**
     * 根据输入的活动 id 输出基本信息, 注意要输出基本
     * @param activityId 活动 id.
     * @return 查询的结果 json 对象.
     */
    HashMap<String, Object> getBasicInfosByActivityId(Long activityId);

    /**
     * 根据输入的发起成员的 id, 获取到所有发起的活动的基本信息.
     * @param accountId 发起成员 id.
     * @return 该成员所有发起的活动.
     */
    List<HashMap<String, Object>> getBasicInfosByStartedAccountId(Long accountId);

    /**
     * 获取该用户所有参与的活动的基本信息.
     * @param accountId 参与活动的 id.
     * @return list
     */
    List<HashMap<String, Object>> getBasicInfosByEnrolledAccountId(Long accountId);

}

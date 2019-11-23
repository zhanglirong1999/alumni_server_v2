package cn.edu.seu.alumni_server.dao.mapper;


import cn.edu.seu.alumni_server.dao.entity.Activity;
import java.util.HashMap;
import java.util.List;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

@Repository
public interface ActivityMapper extends Mapper<Activity> {

	/**
	 * 根据输入的活动 id 输出基本信息, 注意要输出基本
	 *
	 * @param activityId 活动 id.
	 * @return 查询的结果 json 对象.
	 */
	HashMap<String, Object> getBasicInfosByActivityId(Long activityId);

	/**
	 * 根据输入的发起成员的 id, 获取到所有发起的活动的基本信息.
	 *
	 * @param accountId 发起成员 id.
	 * @return 该成员所有发起的活动.
	 */
	List<HashMap<String, Object>> getBasicInfosByStartedAccountId(Long accountId);

	/**
	 * 获取该用户所有参与的活动的基本信息.
	 *
	 * @param accountId 参与活动的 id.
	 * @return list
	 */
	List<HashMap<String, Object>> getBasicInfosByEnrolledAccountId(Long accountId);

	/**
	 * 向数据库模糊查询索要的活动的名字.
	 *
	 * @param nameKeyWord 活动名关键字.
	 * @return 被查到的所有的活动的信息.
	 */
	List<HashMap<String, Object>> getActivitiesFuzzilyByActivityNameKeyWord(String nameKeyWord);

	/**
	 * 向数据库严格匹配查询活动名.
	 *
	 * @param activityName 活动名.
	 * @return 查询出的所有活动的基本信息.
	 */
	List<HashMap<String, Object>> getActivitiesByActivityNameKeyWord(String activityName);

	// 判断是不是有一个
	Long hasAvailableActivity(Long activityId);

	// 逻辑删除
	void deleteActivityLogically(Long activityId);
}

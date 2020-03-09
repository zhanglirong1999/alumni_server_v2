package cn.edu.seu.alumni_server.service;

import cn.edu.seu.alumni_server.exceptions.ActivityServiceException;
import cn.edu.seu.alumni_server.controller.dto.ActivityBasicInfoDTO;
import cn.edu.seu.alumni_server.controller.dto.ActivityDTO;
import cn.edu.seu.alumni_server.controller.dto.PageResult;
import cn.edu.seu.alumni_server.controller.dto.SearchedActivityInfoDTO;
import cn.edu.seu.alumni_server.controller.dto.StartedOrEnrolledActivityInfoDTO;
import cn.edu.seu.alumni_server.dao.entity.Activity;
import cn.edu.seu.alumni_server.validation.annotaion.ValidWebParameter;
import java.util.Date;
import java.util.List;
import org.springframework.validation.annotation.Validated;

@Validated
public interface ActivityService {

	/**
	 * 判断报名时间以及截止时间是否合法.
	 *
	 * @param activityDTO 前端的输入.
	 * @return 是否合法.
	 */
	Boolean isLegalDateTime(ActivityDTO activityDTO);

	/**
	 * 判断活动时间是不是小于当前时间.
	 *
	 * @param activityDTO 活动传输对象.
	 * @return boolean
	 * @throws NullPointerException 空指针异常.
	 */
	Boolean isValidStatus(ActivityDTO activityDTO);

	/**
	 * 判断是否含有活动 id.
	 *
	 * @param activityDTO 活动传输对象.
	 * @return 是否含有活动对象 id.
	 */
	Boolean hasActivityId(ActivityDTO activityDTO);

	/**
	 * 根据输入的参数, 判断是否是一个合法的前端输入.
	 *
	 * @param activity 输入的对象
	 * @return 输出一个经过逻辑检验的对象
	 * @throws NullPointerException 空指针异常
	 * @throws ActivityServiceException 服务异常
	 */
	Activity checkInputtedActivityForCreate(
		ActivityDTO activity)
		throws ActivityServiceException;

	void insertActivity(Activity activity);

	/**
	 * 根据前端的输入判断生成一个合法的 Activity 对象, 用于删除记录. 注意: 这里的删除逻辑是彻底删除一条活动的记录.
	 *
	 * @param activityDTO 前端的输入.
	 * @return 可以被执行删除的输出.
	 * @throws NullPointerException 属性含有给空指针.
	 * @throws ActivityServiceException 由属性不合法.
	 */
	Activity deleteActivityDAO(ActivityDTO activityDTO)
		throws ActivityServiceException;

	/**
	 * 向数据库彻底删除一个活动记录. 注意我修改了数据库的外键属性, 保证级联删除.
	 *
	 * @param activity 输入要删除的活动的信息.
	 */
	void deleteActivity(Activity activity);

	/**
	 * 通过活动 id, 向数据库查询基本关于某个活动的基本信息.
	 *
	 * @param activityId 活动的 id.
	 * @return 对应的一个 Map.
	 * @throws ActivityServiceException 活动服务异常.
	 */
	ActivityBasicInfoDTO queryBasicInfoOfActivityByActivityId(Long activityId)
		throws ActivityServiceException;

	/**
	 * 通过发起者账户 id, 向数据库查询其发起的活动的基本信息.
	 *
	 * @param accountId 发起者账户
	 * @return 所有发起活动的基本信息.
	 */
	List<StartedOrEnrolledActivityInfoDTO> queryBasicInfoOfActivityByStartedAccountId(
		Long accountId
	) throws ActivityServiceException;

	/**
	 * 通过参与者的账户 id, 向数据库查询其参与的活动的基本信息.
	 *
	 * @param accountId 参与者的账户 id.
	 * @return 活动的基本信息.
	 * @throws ActivityServiceException 可能出现的账号 null 异常.
	 */
	List<StartedOrEnrolledActivityInfoDTO> queryBasicInfosOfActivityByEnrolledAccountId(
		Long accountId
	) throws ActivityServiceException;

	/**
	 * 根据关键词进行模糊查询.
	 *
	 * @param activityNameKeyWord 关键词.
	 * @return activities
	 */
	List<SearchedActivityInfoDTO> queryActivitiesFuzzilyByActivityNameKeyWord(
		String activityNameKeyWord
	) throws ActivityServiceException;

	/**
	 * 严格匹配活动名称的查询.
	 *
	 * @param activityNameKeyWord 活动的名称.
	 * @return 查询到的活动结果列表.
	 * @throws ActivityServiceException 异常处理.
	 */
	List<SearchedActivityInfoDTO> queryActivitiesByActivityNameKeyWord(
		String activityNameKeyWord
	) throws ActivityServiceException;

	// 查询是否有一个指定的活动下的活动是否存在
	boolean hasActivity(Activity activity);

	PageResult recommend(int pageIndex, int pageSize, Long accountId);

	void updateActivity(
		@ValidWebParameter Long activityId,
		String activityName, String activityDesc,
		Date activityTime, Date expirationTime,
		String img1, String img2, String img3,
		String img4, String img5, String img6
	) throws ActivityServiceException;
}

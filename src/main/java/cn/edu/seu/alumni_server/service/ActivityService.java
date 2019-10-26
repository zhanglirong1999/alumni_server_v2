package cn.edu.seu.alumni_server.service;

import cn.edu.seu.alumni_server.common.exceptions.ActivityServiceException;
import cn.edu.seu.alumni_server.controller.dto.ActivityDTO;
import cn.edu.seu.alumni_server.dao.entity.Activity;

import java.util.HashMap;
import java.util.List;

public interface ActivityService {

    /**
     * 判断报名时间以及截止时间是否合法.
     * @param activityDTO 前端的输入.
     * @return 是否合法.
     */
    public Boolean isLegalDatetime(ActivityDTO activityDTO);

    /**
     * 判断活动时间是不是小于当前时间.
     * @param activityDTO 活动传输对象.
     * @return boolean
     * @throws NullPointerException 空指针异常.
     */
    public Boolean isValidStatus(ActivityDTO activityDTO);

    /**
     * 判断是否含有活动 id.
     * @param activityDTO 活动传输对象.
     * @return 是否含有活动对象 id.
     */
    public Boolean hasActivityId(ActivityDTO activityDTO);

    /**
     * 根据前端的输入来判断生成一个合法的 Activity 对象.
     * @param activityDTO 前端输入的对象.
     * @return 合法的 Activity 对象.
     * @throws ActivityServiceException 输入数据异常.
     */
    public Activity createActivityDAO(ActivityDTO activityDTO)
        throws NullPointerException, ActivityServiceException;

    /**
     * 向数据库插入一个活动对象.
     * @param activity 活动.
     */
    public void insertActivity(Activity activity);

    /**
     * 根据前端的输入判断生成一个合法的 Activity 对象, 用于修改记录.
     * 1. 注意若要修改就在那个字段设置为 null
     * 2. 若不为 null, 则即为需要修改的字段, 校友圈 id 以及 创建 id 除外.
     * 3. 暂时不允许对校友圈以及创建者进行修改, 必须要对其传入值.
     * 4. 注意对于报名时间以及开始时间, 要两个都被请求过来.
     * @param activityDTO 需要修改的信息.
     * @return 修改后的结果.
     * @throws ActivityServiceException 自定义异常信息.
     */
    public Activity updateActivityDAO(ActivityDTO activityDTO)
        throws NullPointerException, ActivityServiceException;

    /**
     * 向数据修改活动的信息.
     * @param activity 活动.
     */
    public void updateActivity(Activity activity);

    /**
     * 根据前端的输入判断生成一个合法的 Activity 对象, 用于删除记录.
     * 注意: 这里的删除逻辑是彻底删除一条活动的记录.
     * @param activityDTO 前端的输入.
     * @return 可以被执行删除的输出.
     * @throws NullPointerException 属性含有给空指针.
     * @throws ActivityServiceException 由属性不合法.
     */
    public Activity deleteActivityDAO(ActivityDTO activityDTO)
        throws NullPointerException, ActivityServiceException;

    /**
     * 向数据库彻底删除一个活动记录.
     * 注意我修改了数据库的外键属性, 保证级联删除.
     * @param activity 输入要删除的活动的信息.
     */
    public void deleteActivity(Activity activity);

    /**
     * 通过活动 id, 向数据库查询基本关于某个活动的基本信息.
     * @param activityId 活动的 id.
     * @return 对应的一个 Map.
     * @throws ActivityServiceException 活动服务异常.
     */
    public HashMap<String, Object> queryBasicInfoOfActivityByActivityId(Long activityId)
        throws ActivityServiceException;

    /**
     * 通过发起者账户 id, 向数据库查询其发起的活动的基本信息.
     * @param accountId 发起者账户
     * @return 所有发起活动的基本信息.
     */
    public List<HashMap<String, Object>> queryBasicInfoOfActivityByStartedAccountId(
        Long accountId
    ) throws ActivityServiceException;

    /**
     * 通过参与者的账户 id, 向数据库查询其参与的活动的基本信息.
     * @param accountId 参与者的账户 id.
     * @return 活动的基本信息.
     * @throws ActivityServiceException 可能出现的账号 null 异常.
     */
    public List<HashMap<String, Object>> queryBasicInfosOfActivityByEnrolledAccountId(
        Long accountId
    ) throws ActivityServiceException;

    /**
     * 将 put 的可变参数根据是否有值解析为活动的 dto
     * @param activityId id
     * @param activityName name
     * @param activityDesc desc
     * @param activityTime 活动时间
     * @param expirationTime 截止时间
     * @param img1 1
     * @param img2 2
     * @param img3 3
     * @param img4 4
     * @param img5 5
     * @param img6 6
     * @return ActivityDTO
     */
    public ActivityDTO parseParams2ActivityDTO(
        Long activityId,
        String activityName,
        String activityDesc,
        String activityTime,
        String expirationTime,
        String img1,
        String img2,
        String img3,
        String img4,
        String img5,
        String img6,
        Boolean visibleStatus
    ) throws ActivityServiceException;

}

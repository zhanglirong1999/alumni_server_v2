package cn.edu.seu.alumni_server.service;

import cn.edu.seu.alumni_server.common.exceptions.ActivityServiceException;
import cn.edu.seu.alumni_server.controller.dto.ActivityDTO;
import cn.edu.seu.alumni_server.dao.entity.Activity;

import java.util.HashMap;

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
     * 向数据库查询基本关于某个活动的基本信息的基本信息.
     * @param activityId 活动的 id.
     * @return 对应的一个 Map.
     * @throws ActivityServiceException 活动服务异常.
     */
    public HashMap<String, Object> queryBasicInfoOfActivity(Long activityId)
        throws ActivityServiceException;
}
package cn.edu.seu.alumni_server.service;

import cn.edu.seu.alumni_server.common.exceptions.ActivityMemberServiceException;
import cn.edu.seu.alumni_server.controller.dto.ActivityMemberDTO;
import cn.edu.seu.alumni_server.dao.entity.Account;
import cn.edu.seu.alumni_server.dao.entity.ActivityMember;

import java.util.List;

public interface ActivityMemberService {

    /**
     * 判断前端的输入的参数值是否正确.
     * @param activityMemberDTO 前端输入的参数结构.
     * @return 判断是否真确的结果.
     */
    public Boolean isLegalPrimaryKey(ActivityMemberDTO activityMemberDTO);

    /**
     * 根据前端输入的数据, 判断并生成一个合法的活动成员对象.
     * @param activityMemberDTO 前端的输入.
     * @return 合法的成员对象.
     */
    public ActivityMember addMember2ActivityDAO(ActivityMemberDTO activityMemberDTO)
        throws ActivityMemberServiceException;

    /**
     * 向数据库插入一条活动成员的信息.
     * @param activityMember 活动成员数据.
     */
    public void insertActivityMember(ActivityMember activityMember);

    /**
     * 从数据库中获取到指定活动 id 的成员的信息.
     * @param activityId 活动 id.
     * @return 参加活动成员
     */
    public List<Account> queryActivityMemberAccountInfosByAccountId(Long activityId)
        throws ActivityMemberServiceException;

    /**
     * 根据活动的 id 来设置所有这个活动的成员的状态为未读.
     * @param activityId 活动 id.
     */
    public void updateAllActivityMembersReadStatus(Long activityId, Boolean readStatus)
        throws ActivityMemberServiceException;

    /**
     * 根据活动 id 以及账户 id 来设置一个活动中一个成员的读取状态.
     * @param activityId 活动
     * @param accountId 成员
     * @param readStatus 状态
     */
    public void updateOneActivityMemberReadStatus(Long activityId, Long accountId, Boolean readStatus) throws ActivityMemberServiceException;

}

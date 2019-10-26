package cn.edu.seu.alumni_server.service.impl;

import cn.edu.seu.alumni_server.common.exceptions.ActivityMemberServiceException;
import cn.edu.seu.alumni_server.controller.dto.ActivityMemberDTO;
import cn.edu.seu.alumni_server.dao.entity.Account;
import cn.edu.seu.alumni_server.dao.entity.ActivityMember;
import cn.edu.seu.alumni_server.dao.mapper.ActivityMemberMapper;
import cn.edu.seu.alumni_server.service.ActivityMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@SuppressWarnings("ALL")
public class ActivityMemberServiceImpl implements ActivityMemberService {
    @Autowired
    private ActivityMemberMapper activityMemberMapper;

    @Override
    public Boolean isLegalPrimaryKey(ActivityMemberDTO activityMemberDTO) {
        return (
            activityMemberDTO.getActivityId() != null &&
            !activityMemberDTO.equals("") &&
            activityMemberDTO.getAccountId() != null &&
            !activityMemberDTO.equals("")
        );
    }

    @Override
    public ActivityMember addMember2ActivityDAO(ActivityMemberDTO activityMemberDTO)
        throws ActivityMemberServiceException {
        if (!this.isLegalPrimaryKey(activityMemberDTO))
            throw new ActivityMemberServiceException(
                "The activity or account id is null or empty."
            );
        activityMemberDTO.setReadStatus(true);  // 加入的时候, 不需要被通知.
        return activityMemberDTO.toActivityMember();
    }

    @Override
    public void insertActivityMember(ActivityMember activityMember) {
        this.activityMemberMapper.insertSelective(activityMember);
    }

    @Override
    public List<Account> queryActivityMemberAccountInfosByAccountId(Long activityId)
        throws ActivityMemberServiceException {
        if (activityId == null)
            throw new ActivityMemberServiceException("The activity id is null");
        return this.activityMemberMapper.getActivityMemberInfosByActivityId(activityId);
    }

    @Override
    public void updateAllActivityMembersReadStatus(Long activityId, Boolean readStatus)
        throws ActivityMemberServiceException {
        if (activityId == null)
            throw new ActivityMemberServiceException("The activity id is null");
        this.activityMemberMapper.updateAllActivityMembersExceptStarterReadStatusByActivityId(
            activityId, readStatus  // 设置全体状态为未读.
        );
    }

    @Override
    public void updateOneActivityMemberReadStatus(
        Long activityId, Long accountId, Boolean readStatus
    ) throws ActivityMemberServiceException {
        if (accountId == null)
            throw new ActivityMemberServiceException("The account id is null or empty.");
        this.activityMemberMapper.updateByPrimaryKey(new ActivityMemberDTO(activityId, accountId, readStatus).toActivityMember());
    }
}

package cn.edu.seu.alumni_server.service.impl;

import cn.edu.seu.alumni_server.common.exceptions.ActivityMemberServiceException;
import cn.edu.seu.alumni_server.controller.dto.ActivityMemberBasicInfoDTO;
import cn.edu.seu.alumni_server.controller.dto.ActivityMemberDTO;
import cn.edu.seu.alumni_server.dao.entity.ActivityMember;
import cn.edu.seu.alumni_server.dao.mapper.ActivityMapper;
import cn.edu.seu.alumni_server.dao.mapper.ActivityMemberMapper;
import cn.edu.seu.alumni_server.service.ActivityMemberService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@SuppressWarnings("ALL")
public class ActivityMemberServiceImpl implements ActivityMemberService {

	@Autowired
	private ActivityMemberMapper activityMemberMapper;

	@Autowired
	private ActivityMapper activityMapper;

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
		if (!this.isLegalPrimaryKey(activityMemberDTO)) {
			throw new ActivityMemberServiceException(
				"The activity or account id is null or empty."
			);
		}
		activityMemberDTO.setReadStatus(true);  // 加入的时候, 不需要被通知.
		ActivityMember ans = activityMemberDTO.toActivityMember();
		ans.setIsAvailable(true);
		return ans;
	}

	@Override
	public void insertActivityMember(ActivityMember activityMember) {
		this.activityMemberMapper.insertSelective(activityMember);
	}

	@Override
	public List<ActivityMemberBasicInfoDTO> queryActivityMemberAccountInfosByAccountId(Long activityId)
		throws ActivityMemberServiceException {
		if (activityId == null) {
			throw new ActivityMemberServiceException("The activity id is null");
		}
		List<ActivityMemberBasicInfoDTO> ans =
			this.activityMemberMapper.getActivityMemberInfosByActivityId(activityId);
		for (ActivityMemberBasicInfoDTO t : ans)
			t.calculateStarterEducationGrade();
		return ans;
	}

	@Override
	public void updateAllActivityMembersReadStatus(Long activityId, Boolean readStatus)
		throws ActivityMemberServiceException {
		if (activityId == null) {
			throw new ActivityMemberServiceException("The activity id is null");
		}
		// 首先应该看一下这个活动是否存在
		if (this.activityMapper.hasAvailableActivity(activityId) != null)
			this.activityMemberMapper.updateAllActivityMembersReadStatus(
				activityId, readStatus  // 设置全体状态为未读.
			);
		else throw new ActivityMemberServiceException("This activity is not available which means earlier deleted.");
	}

	@Override
	public void updateOneActivityMemberReadStatus(
		Long activityId, Long accountId, Boolean readStatus
	) throws ActivityMemberServiceException {
		if (accountId == null) {
			throw new ActivityMemberServiceException("The account id is null or empty.");
		}
		// 首先应该看一下这个活动是否存在
		if (this.activityMapper.hasAvailableActivity(activityId) != null)
			this.activityMemberMapper.updateOneActivityMemberReadStatus(
				activityId, accountId, readStatus  // 设置全体状态为未读.
			);
		else throw new ActivityMemberServiceException("This activity is not available which means earlier deleted.");
	}

	@Override
	public boolean isCreatorOf(Long activityId, Long accountId) {
		Long creatorId =
			this.activityMemberMapper.getAvailableCreatorIdOfActivity(activityId);
		return creatorId != null && creatorId.compareTo(accountId) == 0;
	}

	@Override
	public void removeOneActivityMember(ActivityMember activityMember)
		throws ActivityMemberServiceException {
		// 首先要判断这个活动是否还存在
		if (
			null ==
				this.activityMapper.hasAvailableActivity(activityMember.getActivityId())
		) throw new ActivityMemberServiceException("This activity is not available which means earlier deleted.");
		// 然后判断这个成员是不是活动发起人, 发起人无法退出
		if (this.isCreatorOf(activityMember.getActivityId(), activityMember.getAccountId()))
			throw new ActivityMemberServiceException("___The activity starter cannot quit the activity.");
		// 最后都不是, 执行逻辑删除
		activityMember.setIsAvailable(false);
		this.activityMemberMapper.updateByPrimaryKey(activityMember);
	}

	@Override
	public ActivityMember removeOneActivityMemberDAO(Long activityId, Long accountId)
		throws ActivityMemberServiceException {
		ActivityMember activityMember = new ActivityMember();
		activityMember.setActivityId(activityId);
		activityMember.setAccountId(accountId);
		if (!this.isLegalPrimaryKey(new ActivityMemberDTO(activityMember)))
			throw new ActivityMemberServiceException(
				"The activity or account id is null or empty."
			);
		return activityMember;
	}
}

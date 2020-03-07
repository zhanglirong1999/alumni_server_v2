package cn.edu.seu.alumni_server.service.impl;

import cn.edu.seu.alumni_server.common.Utils;
import cn.edu.seu.alumni_server.common.exceptions.ActivityServiceException;
import cn.edu.seu.alumni_server.controller.dto.ActivityBasicInfoDTO;
import cn.edu.seu.alumni_server.controller.dto.ActivityDTO;
import cn.edu.seu.alumni_server.controller.dto.PageResult;
import cn.edu.seu.alumni_server.controller.dto.SearchedActivityInfoDTO;
import cn.edu.seu.alumni_server.controller.dto.StartedOrEnrolledActivityInfoDTO;
import cn.edu.seu.alumni_server.dao.entity.Activity;
import cn.edu.seu.alumni_server.dao.mapper.ActivityMapper;
import cn.edu.seu.alumni_server.service.ActivityService;
import cn.edu.seu.alumni_server.service.fail.ActivityFailPrompt;
import cn.edu.seu.alumni_server.validation.annotaion.ValidWebParameter;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Validated
@Service
public class ActivityServiceImpl implements ActivityService {

	@Autowired
	private ActivityMapper activityMapper;

	@Autowired
	private ActivityFailPrompt activityFailPrompt;

	@Override
	public Boolean isLegalDateTime(ActivityDTO activityDTO) {
		return activityDTO.getExpirationTime().before(activityDTO.getActivityTime());
	}

	@Override
	public Boolean isValidStatus(ActivityDTO activityDTO) {
		Date current = new Date();
		return current.before(activityDTO.getActivityTime());
	}

	@Override
	public Boolean hasActivityId(ActivityDTO activityDTO) {
		return activityDTO.getActivityId() != null &&
			!activityDTO.getActivityId().equals("");
	}

	@Override
	public Activity checkInputtedActivityForCreate(
		ActivityDTO activityDTO
	) throws ActivityServiceException {
		// 转换对象
		// 确定当前的活动名称
		String sname = "创建活动";
		if (this.hasActivityId(activityDTO)) {
			throw new ActivityServiceException(
				this.activityFailPrompt.getUserPrompt(
					sname, 1
				)
			);
		}
		// 活动的名字不可以为空.
		if (activityDTO.getActivityName() == null || activityDTO.getActivityName().equals("")) {
			throw new ActivityServiceException(
				this.activityFailPrompt.getUserPrompt(
					sname, 13
				)
			);
		}
		if (
			activityDTO.getExpirationTime() == null ||
				activityDTO.getExpirationTime().equals("")
		) {
			throw new ActivityServiceException(
				this.activityFailPrompt.getUserPrompt(
					sname, 2
				)
			);
		}
		if (
			activityDTO.getActivityTime() == null ||
				activityDTO.getActivityTime().equals("")
		) {
			throw new ActivityServiceException(
				this.activityFailPrompt.getUserPrompt(
					sname, 3
				)
			);
		}
		if (
			!this.isLegalDateTime(activityDTO)  // 报名截止时间一定要是早于活动时间的
		) {
			throw new ActivityServiceException(
				this.activityFailPrompt.getUserPrompt(
					sname, 4
				)
			);
		}
		if (
			!this.isValidStatus(activityDTO)  // 新创建的活动一定要是有效的, 不可以创建无效历史记录
		) {
			throw new ActivityServiceException(
				this.activityFailPrompt.getUserPrompt(
					sname, 5
				)
			);
		}
		activityDTO.setActivityId(Utils.generateId());
		activityDTO.setValidStatus(true);
		return activityDTO.toActivity();
	}

	@Override
	public void insertActivity(Activity activity) {
		this.activityMapper.insertSelective(activity);
	}

	@Override
	public PageResult recommend(int pageIndex, int pageSize, Long accountId) {
		PageHelper.startPage(pageIndex, pageSize);
// TODO
		List<ActivityBasicInfoDTO> temp = activityMapper.recommend(accountId);

		PageResult pageResult = new PageResult(((Page) temp).getTotal(), temp);
		return pageResult;
	}

	@Override
	public void updateActivity(
		@ValidWebParameter Long activityId,
		String activityName, String activityDesc,
		Date activityTime, Date expirationTime,
		String img1, String img2, String img3,
		String img4, String img5, String img6
	) throws ActivityServiceException {

		Activity activity = new Activity(
			activityId, null, null, activityName, activityDesc,
			activityTime, expirationTime,
			img1, img2, img3, img4, img5, img6,
			null, null, null, null, null
		);
		if (!activity.isValidActAndExpDateTime()) {
			throw new ActivityServiceException(
				this.activityFailPrompt.getUserPrompt(
					"修改活动", 4
				)
			);
		}
		activity.calculateValidStatus();
		this.activityMapper.updateByPrimaryKeySelective(
			activity
		);
	}

	@Override
	public Activity deleteActivityDAO(ActivityDTO activityDTO)
		throws ActivityServiceException {
		if (!this.hasActivityId(activityDTO)) {
			throw new ActivityServiceException(
				this.activityFailPrompt.getUserPrompt(
					"删除活动", 1
				)
			);
		}
		return activityDTO.toActivity();
	}

	@Override
	public void deleteActivity(Activity activity) {
		this.activityMapper.deleteActivityLogically(activity.getActivityId());
	}

	@Override
	public ActivityBasicInfoDTO queryBasicInfoOfActivityByActivityId(Long activityId)
		throws ActivityServiceException {
		if (activityId == null || activityId.equals("")) {
			throw new ActivityServiceException(
				this.activityFailPrompt.getUserPrompt(
					"查询活动基本信息", 1
				)
			);
		}
		ActivityBasicInfoDTO ans =
			this.activityMapper.getBasicInfosByActivityId(activityId);
		if (ans == null) {
			throw new ActivityServiceException(
				this.activityFailPrompt.getUserPrompt(
					"查询活动基本信息", 14
				)
			);
		}
		ans.calculateStarterEducationGrade();
		return ans;
	}

	@Override
	public List<StartedOrEnrolledActivityInfoDTO> queryBasicInfoOfActivityByStartedAccountId(
		Long accountId
	) throws ActivityServiceException {
		if (accountId == null || accountId.equals("")) {
			throw new ActivityServiceException(
				this.activityFailPrompt.getUserPrompt(
					"查询我发起的活动", 1
				)
			);
		}
		List<StartedOrEnrolledActivityInfoDTO> ans =
			this.activityMapper.getBasicInfosByStartedAccountId(accountId);
		for (StartedOrEnrolledActivityInfoDTO t : ans) {
			t.calculateActivityState();
		}
		return ans;
	}

	@Override
	public List<StartedOrEnrolledActivityInfoDTO> queryBasicInfosOfActivityByEnrolledAccountId(
		Long accountId
	) throws ActivityServiceException {
		if (accountId == null || accountId.equals("")) {
			throw new ActivityServiceException(
				this.activityFailPrompt.getUserPrompt(
					"查询我参与的", 1
				)
			);
		}
		List<StartedOrEnrolledActivityInfoDTO> ans =
			this.activityMapper.getBasicInfosByEnrolledAccountId(accountId);
		for (StartedOrEnrolledActivityInfoDTO t : ans) {
			t.calculateActivityState();
		}
		return ans;
	}

	@Override
	public List<SearchedActivityInfoDTO> queryActivitiesFuzzilyByActivityNameKeyWord(
		String activityNameKeyWord
	) throws ActivityServiceException {
		String sname = "查询活动";
		if (activityNameKeyWord == null || activityNameKeyWord.equals("")
			|| activityNameKeyWord.compareTo("") == 0) {
			throw new ActivityServiceException(
				this.activityFailPrompt.getUserPrompt(sname, 12)
			);
		}
		List<SearchedActivityInfoDTO> ans =
			this.activityMapper.getActivitiesFuzzilyByActivityNameKeyWord(
				activityNameKeyWord
			);
		for (SearchedActivityInfoDTO t : ans) {
			t.calculateActivityState();
		}
		return ans;
	}

	@Override
	public List<SearchedActivityInfoDTO> queryActivitiesByActivityNameKeyWord(
		String activityNameKeyWord
	) throws ActivityServiceException {
		String sname = "查询活动";
		if (activityNameKeyWord == null || activityNameKeyWord.equals("")
			|| activityNameKeyWord.compareTo("") == 0) {
			throw new ActivityServiceException(
				this.activityFailPrompt.getUserPrompt(sname, 12)
			);
		}
		List<SearchedActivityInfoDTO> ans =
			this.activityMapper.getActivitiesByActivityNameKeyWord(
				activityNameKeyWord
			);
		for (SearchedActivityInfoDTO t : ans) {
			t.calculateActivityState();
		}
		return ans;
	}

	@Override
	public boolean hasActivity(Activity activity) {
		Long ans = this.activityMapper.hasAvailableActivity(activity.getActivityId());
		return ans != null;
	}
}

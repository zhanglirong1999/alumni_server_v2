package cn.edu.seu.alumni_server.service.impl;

import cn.edu.seu.alumni_server.common.Utils;
import cn.edu.seu.alumni_server.common.exceptions.ActivityServiceException;
import cn.edu.seu.alumni_server.controller.dto.ActivityDTO;
import cn.edu.seu.alumni_server.dao.entity.Activity;
import cn.edu.seu.alumni_server.dao.mapper.ActivityMapper;
import cn.edu.seu.alumni_server.service.ActivityService;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@SuppressWarnings("ALL")
public class ActivityServiceImpl implements ActivityService {

	@Autowired
	private ActivityMapper activityMapper;

	@Override
	public Boolean isLegalDatetime(ActivityDTO activityDTO) {
		return activityDTO.getExpirationTime().getTime() < activityDTO.getActivityTime().getTime();
	}

	@Override
	public Boolean isValidStatus(ActivityDTO activityDTO) {
		return (new Date().getTime()) < activityDTO.getActivityTime().getTime();
	}

	@Override
	public Boolean hasActivityId(ActivityDTO activityDTO) {
		return activityDTO.getActivityId() != null && !activityDTO.getActivityId().equals("");
	}

	@Override
	public Activity createActivityDAO(ActivityDTO activityDTO)
		throws NullPointerException, ActivityServiceException {
		if (this.hasActivityId(activityDTO)) {
			throw new ActivityServiceException("This activity id is not null or empty.");
		}
		if (
			activityDTO.getExpirationTime() == null ||
				activityDTO.getActivityTime() == null
		) {
			throw new NullPointerException("Time cannot be null.");
		}
		if (
			!this.isLegalDatetime(activityDTO) ||  // 报名截止时间一定要是早于活动时间的
				!this.isValidStatus(activityDTO)  // 新创建的活动一定要是有效的, 不可以创建无效历史记录
		) {
			throw new ActivityServiceException("Activity/Expiration time exception.");
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
	public Activity updateActivityDAO(ActivityDTO activityDTO)
		throws NullPointerException, ActivityServiceException {
		if (!this.hasActivityId(activityDTO)) {
			throw new ActivityServiceException("This activity id is null or empty.");
		}
		if ((activityDTO.getActivityTime() == null) ^ (activityDTO.getExpirationTime() == null)) {
			throw new ActivityServiceException(
				"Expiration and Activity datetime must be both null or both filed."
			);
		}
		if (activityDTO.getActivityTime() != null && activityDTO.getExpirationTime() != null) {
			if (!this.isLegalDatetime(activityDTO)) {
				throw new ActivityServiceException(
					"Expiration datetime is later than activity time."
				);
			}
			// 需要重新计算活动是否有效
			activityDTO.setValidStatus(this.isValidStatus(activityDTO));
		}
		return activityDTO.toActivity();
	}

	@Override
	public void updateActivity(Activity activity) throws ActivityServiceException {
		// TODO 要更新的字段必须是 is_av 的
		if (this.hasActivity(activity)) {
			this.activityMapper.updateByPrimaryKeySelective(activity);
		} else {
			throw new ActivityServiceException("The activity is not available");
		}
	}

	@Override
	public Activity deleteActivityDAO(ActivityDTO activityDTO)
		throws NullPointerException, ActivityServiceException {
		if (!this.hasActivityId(activityDTO)) {
			throw new ActivityServiceException("The activity id is null or empty.");
		}
		return activityDTO.toActivity();
	}

	@Override
	public void deleteActivity(Activity activity) {
		this.activityMapper.deleteActivityLogically(activity.getActivityId());
	}

	@Override
	public HashMap<String, Object> queryBasicInfoOfActivityByActivityId(Long activityId)
		throws ActivityServiceException {
		if (activityId == null) {
			throw new ActivityServiceException("Activity id is null.");
		}
		HashMap<String, Object> ans =
			this.activityMapper.getBasicInfosByActivityId(activityId);
		if ((Integer) ans.get("activity_members_num") == 0) {
			throw new ActivityServiceException("The result of this query is empty.");
		} else {
			return ans;
		}
	}

	@Override
	public List<HashMap<String, Object>> queryBasicInfoOfActivityByStartedAccountId(
		Long accountId
	) throws ActivityServiceException {
		if (accountId == null) {
			throw new ActivityServiceException("The account id is null");
		}
		List<HashMap<String, Object>> ans =
			this.activityMapper.getBasicInfosByStartedAccountId(accountId);
		return ans;
	}

	@Override
	public List<HashMap<String, Object>> queryBasicInfosOfActivityByEnrolledAccountId(
		Long accountId
	) throws ActivityServiceException {
		if (accountId == null) {
			throw new ActivityServiceException("The account id is null");
		}
		return this.activityMapper.getBasicInfosByEnrolledAccountId(accountId);
	}

	@Override
	public ActivityDTO parseParams2ActivityDTO(
		Long activityId,
		String activityName, String activityDesc,
		String activityTime, String expirationTime,
		String img1, String img2, String img3,
		String img4, String img5, String img6,
		Boolean visibleStatus
	) throws ActivityServiceException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		sdf.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
		Date expirationDatetime = null;
		Date activityDatetime = null;
		try {
			expirationDatetime =
				(null == expirationTime ? null : sdf.parse(expirationTime));
			activityDatetime =
				(null == activityTime ? null : sdf.parse(activityTime));
		} catch (ParseException e) {
			throw new ActivityServiceException("The date time format is not yyyy-MM-dd HH:mm:ss");
		}
		// 加上 8 个小时
		if (expirationDatetime != null) {
			expirationDatetime.setTime(expirationDatetime.getTime() + 8 * 3600 * 1000);
		}
		if (activityDatetime != null) {
			activityDatetime.setTime(activityDatetime.getTime() + 8 * 3600 * 1000);
		}
		ActivityDTO activityDTO = new ActivityDTO(
			activityId, null, null,
			activityName, activityDesc,
			activityDatetime, expirationDatetime,
			img1, img2, img3,
			img4, img5, img6,
			visibleStatus, false
		);
		return activityDTO;
	}

	@Override
	public List<HashMap<String, Object>> queryActivitiesFuzzilyByActivityNameKeyWord(
		String activityNameKeyWord
	) throws ActivityServiceException {
		if (activityNameKeyWord == null || activityNameKeyWord.equals("")
			|| activityNameKeyWord.compareTo("") == 0) {
			throw new ActivityServiceException("The fuzzily search key word is none or empty.");
		}
		return this.activityMapper.getActivitiesFuzzilyByActivityNameKeyWord(
			activityNameKeyWord
		);
	}

	@Override
	public List<HashMap<String, Object>> queryActivitiesByActivityNameKeyWord(
		String activityNameKeyWord
	) throws ActivityServiceException {
		if (activityNameKeyWord == null || activityNameKeyWord.equals("")
			|| activityNameKeyWord.compareTo("") == 0) {
			throw new ActivityServiceException("The fuzzily search key word is none or empty.");
		}
		return this.activityMapper.getActivitiesByActivityNameKeyWord(
			activityNameKeyWord
		);
	}

	@Override
	public boolean hasActivity(Activity activity) {
		Long ans = this.activityMapper.hasAvailableActivity(activity.getActivityId());
		return ans != null;
	}
}

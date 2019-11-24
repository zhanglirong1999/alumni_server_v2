package cn.edu.seu.alumni_server.service.impl;

import cn.edu.seu.alumni_server.common.Utils;
import cn.edu.seu.alumni_server.common.exceptions.ActivityServiceException;
import cn.edu.seu.alumni_server.controller.dto.ActivityBasicInfoDTO;
import cn.edu.seu.alumni_server.controller.dto.ActivityDTO;
import cn.edu.seu.alumni_server.controller.dto.ActivityWithMultipartFileDTO;
import cn.edu.seu.alumni_server.dao.entity.Activity;
import cn.edu.seu.alumni_server.dao.mapper.ActivityMapper;
import cn.edu.seu.alumni_server.service.ActivityService;
import cn.edu.seu.alumni_server.service.QCloudFileManager;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.List;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ActivityServiceImpl implements ActivityService {

	@Autowired
	private ActivityMapper activityMapper;

	@Autowired
	private QCloudFileManager qCloudFileManager;


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
	public ActivityWithMultipartFileDTO checkInputtedActivityWithMultipartFileDTO(
		ActivityWithMultipartFileDTO activityWithMPFDTO)
		throws NullPointerException, ActivityServiceException {
		ActivityDTO temp = activityWithMPFDTO.toActivityDTO();
		if (this.hasActivityId(temp)) {
			throw new ActivityServiceException("This activity id is not null or empty.");
		}
		if (
			activityWithMPFDTO.getExpirationTime() == null ||
				activityWithMPFDTO.getActivityTime() == null
		) {
			throw new NullPointerException("Date times cannot be null.");
		}
		if (
			!this.isLegalDatetime(temp) ||  // 报名截止时间一定要是早于活动时间的
				!this.isValidStatus(temp)  // 新创建的活动一定要是有效的, 不可以创建无效历史记录
		) {
			throw new ActivityServiceException(
				"Activity/Expiration time is not logical exception.");
		}
		activityWithMPFDTO.setActivityId(Utils.generateId());
		activityWithMPFDTO.setValidStatus(true);
		return activityWithMPFDTO;
	}

	@Override
	public Activity insertActivityDAO(ActivityWithMultipartFileDTO x)
		throws IOException, ActivityServiceException,
		InvocationTargetException, IllegalAccessException {
		Activity activity = x.toActivityDTO().toActivity();
		// 首先发送图片获取需要的地址
		String[] imgUrls = this.uploadActivityImgs(
			activity.getActivityId(),
			x.getImg1(), x.getImg2(), x.getImg3(),
			x.getImg4(), x.getImg5(), x.getImg6()
		);
		// 然后设置六个地址
		for (int i = 0; i < 6; ++i) {
			BeanUtils.setProperty(activity, "img" + (i + 1), imgUrls[i]);
		}
//		this.activityMapper.insertSelective(activity);
		return activity;
	}

	@Override
	public void insertActivity(Activity activity) {
		this.activityMapper.insertSelective(activity);
	}

	@Override
	public Activity updateActivityDAO(
		ActivityWithMultipartFileDTO activityWMPFDTO
	) throws NullPointerException, ActivityServiceException, IOException,
		InvocationTargetException, IllegalAccessException {
		// 首先获取到一个结果对象的配置.
		ActivityDTO activityDTO = activityWMPFDTO.toActivityDTO();
		// 判断是否有 id
		if (!this.hasActivityId(activityDTO)) {
			throw new ActivityServiceException("The activity id is null or empty.");
		}
		// 注意更新操作要判断是否还有这个活动
		if (
			!activityDTO.getActivityId().equals(
				this.activityMapper.hasAvailableActivity(activityDTO.getActivityId())
			)
		) {
			throw new ActivityServiceException(
				"The activity is not available, which means earlier deleted."
			);
		}
		// 首先确定时间的改变.
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
		// 然后确定文件的改变, 这里没有确定直接再次上传
		String[] ansUrls = this.uploadActivityImgs(
			activityDTO.getActivityId(),
			activityWMPFDTO.getImg1(),
			activityWMPFDTO.getImg2(),
			activityWMPFDTO.getImg3(),
			activityWMPFDTO.getImg4(),
			activityWMPFDTO.getImg5(),
			activityWMPFDTO.getImg6()
		);
		// 然后设置六个地址
		for (int i = 0; i < 6; ++i) {
			BeanUtils.setProperty(activityDTO, "img" + (i + 1), ansUrls[i]);
		}
		// 返回结果
		return activityDTO.toActivity();
	}

	@Override
	public void updateActivity(Activity activity) {
		this.activityMapper.updateByPrimaryKeySelective(activity);
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
	public ActivityBasicInfoDTO queryBasicInfoOfActivityByActivityId(Long activityId)
		throws ActivityServiceException {
		if (activityId == null) {
			throw new ActivityServiceException("Activity id is null.");
		}
		ActivityBasicInfoDTO ans =
			this.activityMapper.getBasicInfosByActivityId(activityId);
		return ans;
	}

	@Override
	public List<ActivityBasicInfoDTO> queryBasicInfoOfActivityByStartedAccountId(
		Long accountId
	) throws ActivityServiceException {
		if (accountId == null) {
			throw new ActivityServiceException("The account id is null");
		}
		List<ActivityBasicInfoDTO> ans =
			this.activityMapper.getBasicInfosByStartedAccountId(accountId);
		return ans;
	}

	@Override
	public List<ActivityBasicInfoDTO> queryBasicInfosOfActivityByEnrolledAccountId(
		Long accountId
	) throws ActivityServiceException {
		if (accountId == null) {
			throw new ActivityServiceException("The account id is null");
		}
		return this.activityMapper.getBasicInfosByEnrolledAccountId(accountId);
	}

	@Override
	public List<ActivityBasicInfoDTO> queryActivitiesFuzzilyByActivityNameKeyWord(
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
	public List<ActivityBasicInfoDTO> queryActivitiesByActivityNameKeyWord(
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

	@Override
	public String encodeForActivityImg(Long activityId, Integer imgIndex) {
		return String.valueOf(activityId) + imgIndex;
	}

	private String createNewName(
		Long activityId, Integer imgIndex,
		MultipartFile multipartFile
	) throws ActivityServiceException {
		String originalName = multipartFile.getOriginalFilename();
		if (originalName == null) {
			throw new ActivityServiceException(
				"The original multipart file does not contain a name.");
		}
		String suffix = originalName.substring(originalName.lastIndexOf("."));
		return "" + activityId + "." + imgIndex + suffix;
	}

	@Override
	public Long[] decodeForActivityImg(String keyString) {
		Long[] ans = new Long[2];
		ans[1] = Long.valueOf(keyString.substring(keyString.length() - 1));
		ans[0] = Long.valueOf(keyString.substring(0, keyString.length() - 1));
		return ans;
	}

	@Override
	public String[] uploadActivityImgs(Long activityId, MultipartFile... multipartFiles)
		throws ActivityServiceException, IOException {
		// 所有的结果码
		String[] ansStrings = new String[multipartFiles.length];
		// 判断是否有异常.
		if (multipartFiles.length <= 0) {
			throw new ActivityServiceException("The imgs are empty.");
		}
		// url base
		String urlBaseString = "/activities/imgs/";
		// 遍历所有的图片, 如果是不合法的, 那么就把他结果路径设置为 null
		for (int i = 0; i < multipartFiles.length; ++i) {
			if (!this.qCloudFileManager.isLegalMultipartFile(multipartFiles[i])) {
				ansStrings[i] = null;
			} else {
				// 转换文件格式并且重命名
				String newName = this.createNewName(activityId, i + 1, multipartFiles[i]);
				File ans =
					this.qCloudFileManager.convertMultipartFileToFile(
						multipartFiles[i], newName
					);
				// 获取新的文件的新编码.
				String keyString = this.encodeForActivityImg(activityId, i + 1);
				ansStrings[i] =
					this.qCloudFileManager.uploadFileToQCloud(ans, urlBaseString + keyString);
			}
		}
		return ansStrings;
	}
}

package cn.edu.seu.alumni_server.controller.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActivityBasicInfoDTO {

	private Long activityId = null;
	private String activityName = null;
	private String accountName = null;
	private String activityDesc = null;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date activityTime = null;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date expirationTime = null;
	private String img1;

	private String img2;

	private String img3;

	private String img4;

	private String img5;

	private String img6;
	private Long activityMembersNum = null;
	// 群组的头像
	private String alumniCircleAvatar;
}

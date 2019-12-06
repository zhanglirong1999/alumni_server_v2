package cn.edu.seu.alumni_server.controller.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Calendar;
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
	private String activityDesc = null;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date activityDateTime = null;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date expirationDateTime = null;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date activityCreatedDateTime = null;
	private String img1;

	private String img2;

	private String img3;

	private String img4;

	private String img5;

	private String img6;
	// 参与的人数
	private Long enrolledNumber = null;

	// 群组的信息
	private Long alumniCircleId = null;
	private String alumniCircleAvatar = null;

	// 额外的信息
	private Long starterId = null;  // 发起者 id
	private String starterName = null;  // 发起者姓名
	private String starterAvatar = null;  // 发起者头像
	// 教育信息
	private Long starterEducationId = null;
	private String starterEducationLevel = null;
	private String starterEducationSchool = null;
	private String starterEducationCollege = null;
	private Long starterStartEducationYear = null;  // 几几 级
	private String starterEducationGrade = null;  // 需要计算

	public void calculateStarterEducationGrade() {
		Date date = new Date(starterStartEducationYear);
		if (date.getTime() <= 0)
			this.starterEducationGrade = null;
		else {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			Integer y = calendar.get(Calendar.YEAR);
			String sy = String.valueOf(y);
			if (sy.length() != 4) starterEducationGrade = sy;
			else starterEducationGrade = sy.substring(2) + "级";
		}
	}
}

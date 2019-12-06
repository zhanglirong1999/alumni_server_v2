package cn.edu.seu.alumni_server.controller.dto.alumnicircle;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AlumniCircleBasicInfoDTO {
	private Integer alumniCircleMembersNum = null;
	private Long alumniCircleId = null;
	private Long creatorId = null;
	// 群组的类型
	private Integer alumniCircleType = null;
	private String alumniCircleName = null;
	private String alumniCircleDesc = null;
	// 群组的头像
	private String avatar = null;
	// 群组的验证状态
	private Boolean authorizationStatus = null;
	private String creatorName = null;
}

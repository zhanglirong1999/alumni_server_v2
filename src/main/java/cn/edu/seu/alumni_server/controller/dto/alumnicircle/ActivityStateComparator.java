package cn.edu.seu.alumni_server.controller.dto.alumnicircle;

import cn.edu.seu.alumni_server.controller.dto.StartedOrEnrolledActivityInfoDTO;
import cn.edu.seu.alumni_server.controller.dto.enums.ActivityState;
import java.util.Comparator;

public class ActivityStateComparator implements Comparator<StartedOrEnrolledActivityInfoDTO> {

	int getOrder(ActivityState activityState) {
		if (activityState == ActivityState.ONGOING) {
			return 0;
		} else if (activityState == ActivityState.AFTER_ENROLLING_BEFORE_ONGOING) {
			return 1;
		} else if (activityState == ActivityState.ENROLLING) {
			return 2;
		} else if (activityState == ActivityState.FINISHED) {
			return 3;
		} else {
			return 4;
		}
	}

	@Override
	public int compare(StartedOrEnrolledActivityInfoDTO o1, StartedOrEnrolledActivityInfoDTO o2) {
		return this.getOrder(o1.getActivityState()) - this.getOrder(o2.getActivityState());
	}
}

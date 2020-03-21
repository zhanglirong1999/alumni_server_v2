package cn.edu.seu.alumni_server.service;

import cn.edu.seu.alumni_server.controller.dto.ActivityDTO;

public interface SecurityService {
    boolean checkoutActivityContentSecurity(ActivityDTO activityDTO);
}

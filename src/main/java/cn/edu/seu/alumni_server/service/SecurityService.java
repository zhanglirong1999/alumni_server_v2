package cn.edu.seu.alumni_server.service;

import cn.edu.seu.alumni_server.controller.dto.*;
import cn.edu.seu.alumni_server.controller.dto.alumnicircle.AlumniCircleDTO;

public interface SecurityService {
    boolean checkoutActivityContentSecurity(ActivityDTO activityDTO);

    boolean checkoutDemandContentSecurity(DemandCreateDTO demandCreateDTO);

    boolean checkoutAlumniCircleDTOSecurity(AlumniCircleDTO alumniCircleDTO);

    boolean checkoutAccountDTOSecurity(AccountDTO accountDTO);

    boolean checkoutEducationDTOSecurity(EducationDTO educationDTO);
    //JobDTO
    boolean checkoutJobDTOSecurity(JobDTO obDTO);
}

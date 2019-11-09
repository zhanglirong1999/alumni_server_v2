package cn.edu.seu.alumni_server.controller;


import cn.edu.seu.alumni_server.common.dto.WebResponse;
import cn.edu.seu.alumni_server.common.exceptions.AlumniCircleServiceException;
import cn.edu.seu.alumni_server.common.token.Acl;
import cn.edu.seu.alumni_server.controller.dto.AlumniCircleDTO;
import cn.edu.seu.alumni_server.dao.entity.AlumniCircle;
import cn.edu.seu.alumni_server.service.AlumniCircleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@SuppressWarnings("ALL")
@RestController
//@Acl
public class AlumniCircleController {
    @Autowired
    HttpServletRequest request;

    @Autowired
    AlumniCircleService alumniCircleService;

    @PostMapping("/alumniCircle")
    public WebResponse changeAlumniCircle() {
        return new WebResponse().success();
    }

    @GetMapping("/alumniCircles/enrolledAlumniCircles")
    public WebResponse getEnrolledAlumniCirclesByAccountId(
        @RequestParam(value="accountId", required=true) Long accountId
    ) {
        try {
            List<AlumniCircleDTO> alumniCircleDTOList =
                this.alumniCircleService.queryAlumniCircleById(accountId);
            return new WebResponse().success(alumniCircleDTOList);
        } catch (AlumniCircleServiceException|Exception e) {
            return new WebResponse().fail(e.getMessage());
        }
    }
}

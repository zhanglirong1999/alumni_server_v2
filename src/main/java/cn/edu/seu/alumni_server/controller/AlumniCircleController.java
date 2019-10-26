package cn.edu.seu.alumni_server.controller;


import cn.edu.seu.alumni_server.common.dto.WebResponse;
import cn.edu.seu.alumni_server.common.token.Acl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@SuppressWarnings("ALL")
@RestController
@Acl
public class AlumniCircleController {
    @Autowired
    HttpServletRequest request;

    @PostMapping("/alumniCircle")
    public WebResponse changeAlumniCircle() {
        return new WebResponse().success();
    }
}

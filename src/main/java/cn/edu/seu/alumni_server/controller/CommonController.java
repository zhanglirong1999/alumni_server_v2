package cn.edu.seu.alumni_server.controller;

import cn.edu.seu.alumni_server.common.Utils;
import cn.edu.seu.alumni_server.common.token.Acl;
import cn.edu.seu.alumni_server.service.QCloudFileManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
//@Acl
public class CommonController {

    @Autowired
    QCloudFileManager qCloudFileManager;

    @PostMapping("/fileUpload")
    public String fileDemo(
            @RequestParam MultipartFile file
    ) throws IOException {
        return qCloudFileManager.uploadOneFile(
                file,
                String.valueOf(Utils.generateId()) + "." + file.getOriginalFilename());
    }
}

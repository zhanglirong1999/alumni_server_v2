package cn.edu.seu.alumni_server.controller;

import cn.edu.seu.alumni_server.controller.dto.common.WebResponse;
import cn.edu.seu.alumni_server.dao.entity.Account;
import cn.edu.seu.alumni_server.dao.mapper.V2ApiMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@SuppressWarnings("ALL")
public class TestController {

    @Autowired
    V2ApiMapper v2ApiMapper;

    @RequestMapping("/test")
    String demo() {
        return "ok";
    }

    @RequestMapping("/test/mysql")
    WebResponse test(@RequestParam long accountId) {
        List<Account> list = v2ApiMapper.test(accountId);
        return new WebResponse().success(list);
    }
}

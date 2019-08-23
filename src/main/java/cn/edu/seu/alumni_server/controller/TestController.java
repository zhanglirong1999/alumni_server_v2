package cn.edu.seu.alumni_server.controller;

import cn.edu.seu.alumni_server.common.Utils;
import cn.edu.seu.alumni_server.controller.dto.common.WebResponse;
import cn.edu.seu.alumni_server.dao.entity.Account;
import cn.edu.seu.alumni_server.dao.entity.ConstMajor;
import cn.edu.seu.alumni_server.dao.entity.ConstSchool;
import cn.edu.seu.alumni_server.dao.mapper.AccountMapper;
import cn.edu.seu.alumni_server.dao.mapper.ConstMajorMapper;
import cn.edu.seu.alumni_server.dao.mapper.ConstSchoolMapper;
import cn.edu.seu.alumni_server.dao.mapper.V2ApiMapper;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/v2")
@SuppressWarnings("ALL")
public class TestController {

    @Autowired
    V2ApiMapper v2ApiMapper;
    @Autowired
    AccountMapper accountMapper;

    @Autowired
    ConstSchoolMapper constSchoolMapper;
    @Autowired
    ConstMajorMapper constMajorMapper;

    @RequestMapping("/test")
    String demo() {
        return "ok";
    }

    @RequestMapping("/test/mysql")
    WebResponse test(@RequestParam long accountId) {
        List<Account> list = v2ApiMapper.test(accountId);
        return new WebResponse().success(list);
    }

    @GetMapping("/test/account")
    WebResponse demo2(@RequestParam String openid) {
        Account accountNew = new Account();
        accountNew.setOpenid(openid);
        accountNew.setAccountId(Utils.generateId());
        accountMapper.insertSelective(accountNew);
        return new WebResponse().success(accountNew.getAccountId().toString());
    }

    @GetMapping("/test/school")
    WebResponse school(@RequestBody List<ConstSchool> constSchools) {
        constSchools.stream().forEach(constSchool -> {
            constSchoolMapper.insertSelective(constSchool);
        });
        return new WebResponse().success();
    }

    @GetMapping("/test/major")
    WebResponse major(@RequestBody List<ConstMajor> constMajors) {
        constMajors.stream().forEach(constMajor -> {
            constMajorMapper.insertSelective(constMajor);
        });
        return new WebResponse().success();
    }

    @PostMapping("/test/office/callback")
    WebResponse officeCallback(@RequestBody Map req) {

        System.out.println(new Gson().toJson(req));
        return new WebResponse().success(req);
    }
}

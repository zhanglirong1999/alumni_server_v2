package cn.edu.seu.alumni_server.dataSync;

import cn.edu.seu.alumni_server.common.Utils;
import cn.edu.seu.alumni_server.common.dto.WebResponse;
import cn.edu.seu.alumni_server.dao.entity.Account;
import cn.edu.seu.alumni_server.dao.mapper.AccountMapper;
import cn.edu.seu.alumni_server.dataSync.entity.Personal;
import cn.edu.seu.alumni_server.dataSync.mapper.*;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/data")
@Slf4j
public class DataSyncController {

    // V1
    @Autowired
    @Qualifier(value = "EducationMapperV1")
    EducationMapper educationMapperV2;

    @Autowired
    @Qualifier(value = "FriendMapperV1")
    FriendMapper friendMapperV2;

    @Autowired
    PersonalMapper personalMapper;

    @Autowired
    PersonalinforMapper personalinforMapper;

    @Autowired
    WorkMapper workMapper;

    //v2
    @Autowired
    AccountMapper accountMapper;


    @GetMapping("/sync")
    public WebResponse dataSync() {
        account();

        return new WebResponse();
    }

    void account() {

        PageHelper.startPage(1, 10);
        List<Personal> res = personalMapper.selectAll();

        long total = ((Page) res).getTotal();
        int pageNum = 0;
        int pageSize = 10;

        while (pageNum * pageSize < total) {
            pageNum++;

            PageHelper.startPage(pageNum, pageSize);
            List<Personal> resTemp = personalMapper.selectAll();

            resTemp.forEach(personal -> {
                Account account = new Account();
                account.setAccountId(Utils.generateId());

                account.setOpenid(personal.getOpenid());
                account.setWechat(personal.getWechat());
                account.setPhone(personal.getPhone());
                account.setEmail(personal.getEmail());

                try {
                    if (accountMapper.insertSelective(account) != 1) {
                        log.error("account 插入失败");
                    }
                } catch (Throwable t) {
                    log.info(t.getMessage());
                }
            });
        }

    }
}



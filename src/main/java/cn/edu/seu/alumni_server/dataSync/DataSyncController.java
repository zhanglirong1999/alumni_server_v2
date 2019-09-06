package cn.edu.seu.alumni_server.dataSync;

import cn.edu.seu.alumni_server.common.Utils;
import cn.edu.seu.alumni_server.common.dto.WebResponse;
import cn.edu.seu.alumni_server.dao.entity.Account;
import cn.edu.seu.alumni_server.dao.mapper.AccountMapper;
import cn.edu.seu.alumni_server.dataSync.entity.Personal;
import cn.edu.seu.alumni_server.dataSync.entity.Personalinfor;
import cn.edu.seu.alumni_server.dataSync.mapper.*;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tk.mybatis.mapper.entity.Example;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

@RestController
@RequestMapping("/data")
@Slf4j
public class DataSyncController {

    // V1
    @Autowired
    @Qualifier(value = "EducationMapperV1")
    EducationMapper educationMapperV1;

    @Autowired
    @Qualifier(value = "FriendMapperV1")
    FriendMapper friendMapperV1;

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
        personal2account();

        personalInfor2Account();
        return new WebResponse();
    }

    void personal2account() {

        PageHelper.startPage(1, 1);
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
                    if (accountMapper.selectByPrimaryKey(account.getAccountId()) == null) {
                        return;
                    }
                    if (accountMapper.insertSelective(account) != 1) {
                        log.error("account 插入失败");
                    }
                } catch (Throwable t) {
                    log.info(t.getMessage());
                }
            });
        }

    }

    void personalInfor2Account() {

        PageHelper.startPage(1, 1);
        List<Personalinfor> res = personalinforMapper.selectAll();

        long total = ((Page) res).getTotal();
        int pageNum = 0;
        int pageSize = 10;
        Integer count = 0;

        while (pageNum * pageSize < total) {
            pageNum++;
            System.out.println(count++);

            PageHelper.startPage(pageNum, pageSize);
            List<Personalinfor> resTemp = personalinforMapper.selectAll();

            resTemp.forEach(personalinfor -> {

                Account account = null;
                try {
                    Example example = new Example(Account.class);
                    example.createCriteria().andEqualTo("openid", personalinfor.getOpenid());
                    account = accountMapper.selectOneByExample(example);

                    if (account == null) {
                        return;
                    }
                    account.setName(personalinfor.getRealName());
                    account.setGender(
                            Integer.parseInt(personalinfor.getGender())
                    );
                } catch (Throwable e) {
                    e.printStackTrace();
                }
                account.setSelfDesc(personalinfor.getDescr());
                account.setCity(personalinfor.getCity());
                account.setAvatar(personalinfor.getHeadUrl());
                if (!StringUtils.isEmpty(personalinfor.getBirth())) {
                    try {
                        account.setBirthday(
                                new SimpleDateFormat("yyyy-MM-dd")
                                        .parse(personalinfor.getBirth()).getTime());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }

                try {
                    if (accountMapper.updateByPrimaryKeySelective(account) != 1) {
                        log.error("account 插入失败");
                    }
                } catch (Throwable t) {
                    log.info(t.getMessage());
                }
            });
        }

    }
}



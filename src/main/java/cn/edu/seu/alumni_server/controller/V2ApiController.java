package cn.edu.seu.alumni_server.controller;

import cn.edu.seu.alumni_server.common.SnowflakeIdGenerator;
import cn.edu.seu.alumni_server.common.Utils;
import cn.edu.seu.alumni_server.controller.dto.AccountAllDTO;
import cn.edu.seu.alumni_server.controller.dto.SearchResultDTO;
import cn.edu.seu.alumni_server.controller.dto.common.WebResponse;
import cn.edu.seu.alumni_server.dao.entity.Account;
import cn.edu.seu.alumni_server.dao.entity.Education;
import cn.edu.seu.alumni_server.dao.entity.Friend;
import cn.edu.seu.alumni_server.dao.entity.Job;
import cn.edu.seu.alumni_server.dao.mapper.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("ALL")
@RestController
@RequestMapping("/v2")
public class V2ApiController {

    @Autowired
    AccountMapper accountMapper;

    @Autowired
    EducationMapper educationMapper;

    @Autowired
    JobMapper jobMapper;
    @Autowired
    FriendMapper friendMapper;
    @Autowired
    V2ApiMapper v2ApiMapper;

    @RequestMapping("/account/create")
    public WebResponse createAccount(Account account) {
        account.setAccountId(SnowflakeIdGenerator.getInstance().nextId());
        accountMapper.insertSelective(account);

        return new WebResponse();
    }

    @RequestMapping("/account/complete")
    public WebResponse completeAccount(@RequestBody AccountAllDTO accountAllDTO) {

        // account
        Account account = accountAllDTO.getAccount();
        if (account.getAccountId() != null &&
                accountMapper.selectByPrimaryKey(account.getAccountId()) != null) {

            accountMapper.updateByPrimaryKeySelective(account);

        } else {
            account.setAccountId(Utils.generateId());
            accountMapper.insertSelective(account);
        }


        // education
        List<Education> educations = accountAllDTO.getEducations();
        educations.parallelStream().forEach(e -> {
            if (e.getEducationId() != null &&
                    educationMapper.selectByPrimaryKey(e.getEducationId()) != null) {
                educationMapper.updateByPrimaryKeySelective(e);
            } else {
                e.setAccountId(Utils.generateId());
                educationMapper.insertSelective(e);
            }
        });

        // job
        List<Job> jobs = accountAllDTO.getJobs();
        jobs.parallelStream().forEach(e -> {
            if (e.getJobId() != null &&
                    jobMapper.selectByPrimaryKey(e.getJobId()) != null) {
                jobMapper.updateByPrimaryKeySelective(e);
            } else {
                e.setJobId(Utils.generateId());
                jobMapper.insertSelective(e);
            }
        });

        return new WebResponse();
    }

    @RequestMapping("/account")
    public WebResponse<AccountAllDTO> getAccountInfo(
            @RequestParam Long accountId,
            @RequestParam String openid) {

        AccountAllDTO accountAllDTO = new AccountAllDTO();
        if (accountId == null && openid != null) {
            Account a = new Account();
            a.setOpenid(openid);
            accountId = accountMapper.select(a).get(0).getAccountId();
        } else {
            return new WebResponse().fail();
        }
        getAccountAllDTOById(accountId);

        return new WebResponse<AccountAllDTO>().success(accountAllDTO);
    }

    public AccountAllDTO getAccountAllDTOById(Long accountId) {

        AccountAllDTO accountAllDTO = new AccountAllDTO();

        accountAllDTO.setAccount(accountMapper.selectByPrimaryKey(accountId));

        Education e = new Education();
        e.setAccountId(accountId);
        accountAllDTO.setEducations(educationMapper.select(e));

        Job j = new Job();
        j.setAccountId(accountId);
        accountAllDTO.setJobs(jobMapper.select(j));
        return accountAllDTO;
    }

    @RequestMapping("/friends")
    public WebResponse getFriends(@RequestParam Long accountId) {

        List<AccountAllDTO> res = new ArrayList<AccountAllDTO>();
        Friend f = new Friend();
        f.setAccountId(accountId);
        friendMapper.select(f).stream().forEach(e -> {
            res.add(getAccountAllDTOById(e.getFriendAccountId()));
        });

        return new WebResponse().success(res);
    }

    @RequestMapping("/education/{educationId}")
    public WebResponse deleteEducation(@PathVariable Long educationId) {
        educationMapper.deleteByPrimaryKey(educationId);
        return new WebResponse();
    }

    @RequestMapping("/education/{jobId}")
    public WebResponse deleteJobExperience(@PathVariable Long jobId) {
        jobMapper.deleteByPrimaryKey(jobId);
        return new WebResponse();
    }

    @RequestMapping("/query/{content}")
    public WebResponse query(@PathVariable String content) {
        SearchResultDTO res = new SearchResultDTO();
        res.setCity(v2ApiMapper.searchByCity(content));
        res.setCollege(v2ApiMapper.searchByCollege(content));
        res.setName(v2ApiMapper.searchByName(content));
        res.setPosition(v2ApiMapper.searchByPosition(content));
        res.setSelfDesc(v2ApiMapper.searchBySelfDesc(content));
        res.setCompany(v2ApiMapper.searchByCompany(content));
        res.setSchool(v2ApiMapper.searchBySchool(content));
        return new WebResponse().success(res);
    }
}

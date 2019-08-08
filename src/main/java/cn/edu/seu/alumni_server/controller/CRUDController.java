package cn.edu.seu.alumni_server.controller;

import cn.edu.seu.alumni_server.common.Utils;
import cn.edu.seu.alumni_server.controller.dto.AccountDTO;
import cn.edu.seu.alumni_server.controller.dto.EducationDTO;
import cn.edu.seu.alumni_server.controller.dto.JobDTO;
import cn.edu.seu.alumni_server.controller.dto.common.WebResponse;
import cn.edu.seu.alumni_server.dao.entity.Account;
import cn.edu.seu.alumni_server.dao.entity.Education;
import cn.edu.seu.alumni_server.dao.entity.Job;
import cn.edu.seu.alumni_server.dao.mapper.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@SuppressWarnings("ALL")
@RestController
@RequestMapping("/v2")
public class CRUDController {

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


    @GetMapping("/account")
    public WebResponse getAccount(@RequestParam Long accountId) {
        Account account = accountMapper.selectByPrimaryKey(accountId);
        return new WebResponse().success(new AccountDTO(account));
    }

    @PostMapping("/account")
    public WebResponse changeAccount(@RequestBody AccountDTO accountDTO) {
        if (accountDTO.getAccountId() != null &&
                !accountDTO.getAccountId().equals("")) {
            accountMapper.
                    updateByPrimaryKeySelective(accountDTO.toAccount());
        } else {
            Account account = accountDTO.toAccount();
            account.setAccountId(Utils.generateId());
            accountMapper.insert(account);
        }
        return new WebResponse().success();
    }

    @GetMapping("/education")
    public WebResponse getEducation(@RequestParam Long educationId) {
        Education education = educationMapper.selectByPrimaryKey(educationId);
        return new WebResponse().success(education);
    }

    @PostMapping("/education")
    public WebResponse changeEducation(@RequestBody EducationDTO educationDTO) {
        if (educationDTO.getEducationId() != null &&
                !educationDTO.getEducationId().equals("")) {
            educationMapper.
                    updateByPrimaryKeySelective(educationDTO.toEducation());
        } else {
            Education education = educationDTO.toEducation();
            education.setAccountId(Utils.generateId());
            educationMapper.insert(education);
        }

        return new WebResponse().success();
    }

    @DeleteMapping("/education")
    public WebResponse deleteEducation(@RequestParam Long educationId) {
        educationMapper.deleteByPrimaryKey(educationId);
        return new WebResponse();
    }

    @GetMapping("/job")
    public WebResponse getJobExperience(@RequestParam Long jobId) {
        Job job = jobMapper.selectByPrimaryKey(jobId);
        return new WebResponse().success(job);
    }

    @PostMapping("/job")
    public WebResponse changeJobExperience(@RequestParam JobDTO jobDTO) {
        if (jobDTO.getJobId() != null &&
                !jobDTO.getJobId().equals("")) {
            jobMapper.updateByPrimaryKeySelective(jobDTO.toJob());
        } else {
            Job job = jobDTO.toJob();
            job.setAccountId(Utils.generateId());
            jobMapper.insert(job);
        }

        jobMapper.updateByPrimaryKeySelective(jobDTO.toJob());
        return new WebResponse().success();
    }

    @DeleteMapping("/job")
    public WebResponse deleteJobExperience(@RequestParam Long jobId) {
        jobMapper.deleteByPrimaryKey(jobId);
        return new WebResponse();
    }
}

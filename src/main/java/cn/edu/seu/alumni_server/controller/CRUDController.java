package cn.edu.seu.alumni_server.controller;

import cn.edu.seu.alumni_server.common.Utils;
import cn.edu.seu.alumni_server.controller.dto.*;
import cn.edu.seu.alumni_server.controller.dto.common.WebResponse;
import cn.edu.seu.alumni_server.dao.entity.Account;
import cn.edu.seu.alumni_server.dao.entity.Education;
import cn.edu.seu.alumni_server.dao.entity.Job;
import cn.edu.seu.alumni_server.dao.entity.Message;
import cn.edu.seu.alumni_server.dao.mapper.*;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;


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
    @Autowired
    MessageMapper messageMapper;


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
        return new WebResponse().success(new EducationDTO(education));
    }

    @PostMapping("/education")
    public WebResponse changeEducation(@RequestBody EducationDTO educationDTO) {
        if (educationDTO.getEducationId() != null &&
                !educationDTO.getEducationId().equals("")) {
            educationMapper.
                    updateByPrimaryKeySelective(educationDTO.toEducation());
        } else {
            Education education = educationDTO.toEducation();
            education.setEducationId(Utils.generateId());
            educationMapper.insert(education);
        }

        return new WebResponse().success();
    }

    @DeleteMapping("/education")
    public WebResponse deleteEducation(@RequestBody EducationDTO educationDTO) {
        Education education = educationDTO.toEducation();
        education.setValidStatus(false);
        educationMapper.updateByPrimaryKeySelective(education);
        return new WebResponse();
    }

    @GetMapping("/job")
    public WebResponse getJobExperience(@RequestParam Long jobId) {
        Job job = jobMapper.selectByPrimaryKey(jobId);
        return new WebResponse().success(new JobDTO(job));
    }

    @PostMapping("/job")
    public WebResponse changeJobExperience(@RequestBody JobDTO jobDTO) {
        if (jobDTO.getJobId() != null &&
                !jobDTO.getJobId().equals("")) {
            jobMapper.updateByPrimaryKeySelective(jobDTO.toJob());
        } else {
            Job job = jobDTO.toJob();
            job.setJobId(Utils.generateId());
            jobMapper.insert(job);
        }
        return new WebResponse().success();
    }

    @DeleteMapping("/job")
    public WebResponse deleteJobExperience(@RequestBody JobDTO jobDTO) {
        Job job = jobDTO.toJob();
        job.setValidStatus(false);
        jobMapper.updateByPrimaryKeySelective(job);
        return new WebResponse();
    }

    @GetMapping("/message")
    public WebResponse readMessgae(@RequestParam Long accountId,
                                   @RequestParam int pageIndex,
                                   @RequestParam int pageSize) {
        PageHelper.startPage(pageIndex, pageSize);

        Message message = new Message();
        message.setToUser(accountId);
        List<Message> temp = messageMapper.select(message);

        List<MessageDTO> res = temp
                .stream().map(m -> {
                    MessageDTO messageDTO = new MessageDTO(m);
                    messageDTO.setFromUserName(
                            accountMapper.selectByPrimaryKey(
                                    messageDTO.getFromUser()
                            ).getName()
                    );
                    return messageDTO;
                }).collect(Collectors.toList());
        return new WebResponse().success(new PageResult<MessageDTO>(
                ((Page) temp).getTotal(), res
        ));
    }

    @PostMapping("/message/changeStatus")
    public WebResponse readMessgae(@RequestBody MessageDTO messageDTO) {
        Message message = new Message();
        message.setMessageId(messageDTO.getMessageId());
        message.setStatus(messageDTO.getStatus());
        messageMapper.updateByPrimaryKeySelective(message);
        return new WebResponse();
    }
}

package cn.edu.seu.alumni_server.controller;

import cn.edu.seu.alumni_server.common.CONST;
import cn.edu.seu.alumni_server.common.Utils;
import cn.edu.seu.alumni_server.common.dto.WebResponse;
import cn.edu.seu.alumni_server.common.token.Acl;
import cn.edu.seu.alumni_server.controller.dto.AccountAllDTO;
import cn.edu.seu.alumni_server.controller.dto.AccountDTO;
import cn.edu.seu.alumni_server.controller.dto.EducationDTO;
import cn.edu.seu.alumni_server.controller.dto.JobDTO;
import cn.edu.seu.alumni_server.controller.dto.enums.FriendStatus;
import cn.edu.seu.alumni_server.dao.entity.*;
import cn.edu.seu.alumni_server.dao.mapper.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tk.mybatis.mapper.entity.Example;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;


@SuppressWarnings("ALL")
@RestController
@Acl
public class AccountAllController {

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
    @Autowired
    HttpServletRequest request;
    @Autowired
    FavoriteMapper favoriteMapper;

    @GetMapping("/account")
    public WebResponse getAccount() {
        Long accountId = (Long) request.getAttribute(CONST.ACL_ACCOUNTID);
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


    /**
     * 获取个人信息大对象
     *
     * @param accountId
     * @return
     */
    @Acl
    @RequestMapping("/accountAll")
    public WebResponse<AccountAllDTO> getAccountInfo(@RequestParam Long accountId) {

        Long myAccountId = (Long) request.getAttribute(CONST.ACL_ACCOUNTID);
        AccountAllDTO accountAllDTO = getAccountAllDTOById(accountId);
        if (!myAccountId.equals(accountId)) {
            Friend relationShip = v2ApiMapper.getRelationShip(myAccountId, accountId);
            if (relationShip != null) {
                accountAllDTO.setRelationShip(relationShip.getStatus());
                if (relationShip.getStatus() != FriendStatus.friend.getStatus()) {
                    accountAllDTO.getAccount().setBirthday(null);
                    accountAllDTO.getAccount().setWechat(null);
                    accountAllDTO.getAccount().setPhone(null);
                }
            }
            Favorite favorite = new Favorite();
            favorite.setAccountId(myAccountId);
            favorite.setFavoriteAccountId(accountId);
            List<Favorite> temp = favoriteMapper.select(favorite);
            if (temp.size() > 0) {
                accountAllDTO.setFavorite(temp.get(0).getStatus());
            }
        }

        return new WebResponse<AccountAllDTO>().success(accountAllDTO);
    }

    public AccountAllDTO getAccountAllDTOById(Long accountId) {

        AccountAllDTO accountAllDTO = new AccountAllDTO();
        // 查询 account 信息
        accountAllDTO.setAccount(new AccountDTO(accountMapper.selectByPrimaryKey(accountId)));

        // 查询 education 信息
//        Education e = new Education();
//        e.setAccountId(accountId);

        Example example1 = new Example(Education.class);
        example1.orderBy("endTime").desc();
        example1.createCriteria().andEqualTo("accountId", accountId);
        accountAllDTO.setEducations(educationMapper.selectByExample(example1)
                .stream().map(education -> {
                    return new EducationDTO(education);
                }).collect(Collectors.toList()));

        // 查询 job 信息
//        Job j = new Job();
//        j.setAccountId(accountId);
        Example example2 = new Example(Job.class);
        example2.orderBy("endTime").desc();
        example2.createCriteria().andEqualTo("accountId", accountId);
        accountAllDTO.setJobs(jobMapper.selectByExample(example2)
                .stream().map(job -> {
                    return new JobDTO(job);
                }).collect(Collectors.toList()));
        return accountAllDTO;
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


}

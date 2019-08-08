package cn.edu.seu.alumni_server.controller;

import cn.edu.seu.alumni_server.common.CONST;
import cn.edu.seu.alumni_server.common.SnowflakeIdGenerator;
import cn.edu.seu.alumni_server.common.Utils;
import cn.edu.seu.alumni_server.common.enums.FriendStatus;
import cn.edu.seu.alumni_server.controller.dto.*;
import cn.edu.seu.alumni_server.controller.dto.common.WebResponse;
import cn.edu.seu.alumni_server.dao.entity.Account;
import cn.edu.seu.alumni_server.dao.entity.Education;
import cn.edu.seu.alumni_server.dao.entity.Friend;
import cn.edu.seu.alumni_server.dao.entity.Job;
import cn.edu.seu.alumni_server.dao.mapper.*;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    @Autowired
    RestTemplate restTemplate;

    @RequestMapping("/wechat/code2Session")
    public WebResponse code2Session(@RequestParam String js_code) {
        String url = "https://api.weixin.qq.com/sns/jscode2session?" +
                "appid=" + CONST.appId +
                "&secret=" + CONST.appSecret +
                "&js_code=" + js_code +
                "&grant_type=authorization_code";
        String respronse = restTemplate.getForObject(url, String.class);
        Map res = new Gson().fromJson(respronse, Map.class);
        String openid = (String) res.get("openid");
//        String openid = "oUTaL5Qkz-ClVPPa1b9MZgp-CDRQ";

        if (openid != null && !openid.equals("")) {
            Account account = new Account();
            account.setOpenid(openid);
            List<Account> resAccounts = accountMapper.select(account);
            if (resAccounts.size() > 0) {

                return new WebResponse().success(resAccounts.get(0).getAccountId().toString());
            } else {
                Account accountNew = new Account();
                accountNew.setOpenid(openid);
                accountNew.setAccountId(Utils.generateId());
                accountMapper.insertSelective(accountNew);
                return new WebResponse().success(resAccounts.get(0).getAccountId().toString());
            }
        }
//        String session_key = (String) res.get("session_key");
        return new WebResponse().fail("获取openid失败", null);
    }

    @RequestMapping("/account/create")
    public WebResponse createAccount(@RequestBody Account account) {
        Account temp = new Account();
        temp.setOpenid(account.getOpenid());
        if (accountMapper.selectCount(temp) > 0) {
            return new WebResponse().fail("当前openid已注册", null);
        }
        account.setAccountId(SnowflakeIdGenerator.getInstance().nextId());
        accountMapper.insertSelective(account);

        return new WebResponse().success(account.getAccountId());
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
        List<EducationDTO> educationDTOS = accountAllDTO.getEducations();
        educationDTOS.parallelStream().forEach(educationDTO -> {
            if (educationDTO.getEducationId() != null &&
                    educationMapper.selectByPrimaryKey(educationDTO.getEducationId()) != null) {
                educationMapper.updateByPrimaryKeySelective(educationDTO.toEducation());
            } else {
                educationDTO.setAccountId(Utils.generateId());
                educationMapper.insertSelective(educationDTO.toEducation());
            }
        });

        // job
        List<JobDTO> jobDTOs = accountAllDTO.getJobs();
        jobDTOs.parallelStream().forEach(jobDTO -> {
            if (jobDTO.getJobId() != null &&
                    jobMapper.selectByPrimaryKey(jobDTO.getJobId()) != null) {
                jobMapper.updateByPrimaryKeySelective(jobDTO.toJob());
            } else {
                jobMapper.insertSelective(jobDTO.toJob());
            }
        });
        return new WebResponse();
    }

    /**
     * 获取个人信息大对象
     *
     * @param accountId
     * @return
     */
    @RequestMapping("/accountAll")
    public WebResponse<AccountAllDTO> getAccountInfo(@RequestParam Long accountId) {

        AccountAllDTO accountAllDTO = getAccountAllDTOById(accountId);

        return new WebResponse<AccountAllDTO>().success(accountAllDTO);
    }

    public AccountAllDTO getAccountAllDTOById(Long accountId) {

        AccountAllDTO accountAllDTO = new AccountAllDTO();
        // 查询 account 信息
        accountAllDTO.setAccount(accountMapper.selectByPrimaryKey(accountId));

        // 查询 education 信息
        Education e = new Education();
        e.setAccountId(accountId);
        accountAllDTO.setEducations(educationMapper.select(e)
                .stream().map(education -> {
                    return new EducationDTO(education);
                }).collect(Collectors.toList()));

        // 查询 job 信息
        Job j = new Job();
        j.setAccountId(accountId);
        accountAllDTO.setJobs(jobMapper.select(j)
                .stream().map(job -> {
                    return new JobDTO(job);
                }).collect(Collectors.toList()));
        return accountAllDTO;
    }


    @PostMapping("/friend/apply")
    public WebResponse friendApply(@RequestParam Long A,
                                   @RequestParam Long B) {
        Friend f = new Friend();
        f.setAccountId(A);
        f.setFriendAccountId(B);
        f.setStatus(FriendStatus.apply.getStatus());
        friendMapper.insertSelective(f);
        return new WebResponse();
    }

    @PostMapping("/friend/manage")
    public WebResponse friendAction(@RequestParam Long A,
                                    @RequestParam Long B,
                                    @RequestParam int action) {

        if (action == CONST.FRIEND_ACTION_Y) {
            Friend f = new Friend();
            f.setStatus(FriendStatus.friend.getStatus());

            Example e1 = new Example(Friend.class);
            e1.createCriteria()
                    .andEqualTo("account_id", A)
                    .andEqualTo("friend_account_id", B);
            friendMapper.updateByExampleSelective(f, e1);
        }

        if (action == CONST.FRIEND_ACTION_N) {
            Friend f = new Friend();
            f.setStatus(FriendStatus.stranger.getStatus());

            Example e1 = new Example(Friend.class);
            e1.createCriteria()
                    .andEqualTo("account_id", A)
                    .andEqualTo("friend_account_id", B);
            friendMapper.updateByExampleSelective(f, e1);
        }

        return new WebResponse();
    }

    @GetMapping("/friends")
    public WebResponse getFriends(@RequestParam Long accountId) {

        List<FriendDTO> friends = v2ApiMapper.getFriends(accountId);
        return new WebResponse().success(friends);
    }

    @RequestMapping("/query")
    public WebResponse query(@RequestParam String content) {
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

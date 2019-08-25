package cn.edu.seu.alumni_server.controller;

import cn.edu.seu.alumni_server.common.CONST;
import cn.edu.seu.alumni_server.common.Utils;
import cn.edu.seu.alumni_server.controller.dto.*;
import cn.edu.seu.alumni_server.controller.dto.common.WebResponse;
import cn.edu.seu.alumni_server.controller.dto.enums.FriendStatus;
import cn.edu.seu.alumni_server.controller.dto.enums.SearchType;
import cn.edu.seu.alumni_server.dao.entity.*;
import cn.edu.seu.alumni_server.dao.mapper.*;
import cn.edu.seu.alumni_server.service.AccountService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.google.gson.Gson;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@SuppressWarnings("ALL")
@RestController
public class V2ApiController {

    @Autowired
    AccountService accountService;

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
    @Autowired
    FavoriteMapper favoriteMapper;

    /**
     * @param js_code
     * @return
     */
    @RequestMapping("/login/wechat")
    public WebResponse login(@RequestParam String js_code) {
        // 微信登陆，获取openid
        String url = "https://api.weixin.qq.com/sns/jscode2session?" +
                "appid=" + CONST.appId +
                "&secret=" + CONST.appSecret +
                "&js_code=" + js_code +
                "&grant_type=authorization_code";
        String respronse = restTemplate.getForObject(url, String.class);
        Map res = new Gson().fromJson(respronse, Map.class);
        String openid = (String) res.get("openid");
//        String openid = "oUTaL5Qkz-ClVPPa1b9MZgp-CDRQ";
        // 获取accountId,有则返回，无则新增（注册）
        LoginResTemp loginResTemp = new LoginResTemp();
        if (openid != null && !openid.equals("")) {
            Account account = new Account();
            account.setOpenid(openid);
            List<Account> resAccounts = accountMapper.select(account);
            if (resAccounts.size() > 0) {
                Account accountTemp = resAccounts.get(0);
                loginResTemp.setAccountId(accountTemp.getAccountId());
                loginResTemp.setRegistered(accountTemp.getRegistered());
                loginResTemp.setStep1Finished(accountTemp.getStep1Finished());
                return new WebResponse().success(loginResTemp);
            } else {
                Account accountNew = new Account();
                accountNew.setOpenid(openid);
                accountNew.setAccountId(Utils.generateId());
                accountMapper.insertSelective(accountNew);

                loginResTemp.setAccountId(accountNew.getAccountId());
                loginResTemp.setRegistered(false);
                loginResTemp.setStep1Finished(false);
                return new WebResponse().success(loginResTemp);
            }
        } else {
            new WebResponse().fail("获取openid失败", null);
        }
        return new WebResponse().fail("获取用户信息失败", null);
    }

    @Data
    class LoginResTemp {
        Long accountId;
        Boolean step1Finished;
        Boolean registered;
        String token;
        Long expireTime;
    }

    @RequestMapping("/account/step1")
    public WebResponse createAccount(@RequestBody AccountDTO accountDTO) {

        Account account = accountDTO.toAccount();
        account.setStep1Finished(true);
        accountMapper.updateByPrimaryKeySelective(account);

        return new WebResponse().success(account.getAccountId());
    }

    @RequestMapping("/account/step2")
    public WebResponse completeAccount(@RequestBody AccountAllDTO accountAllDTO) {
        // account
        AccountDTO accountDTO = accountAllDTO.getAccount();

        accountService.checkAccountInfo(accountDTO);

        // education
        List<EducationDTO> educationDTOS = accountAllDTO.getEducations();
        if (educationDTOS.size() > 0) {
            educationDTOS.parallelStream().forEach(educationDTO -> {
                if (educationDTO.getEducationId() != null &&
                        educationMapper.selectByPrimaryKey(educationDTO.getEducationId()) != null) {
                    educationMapper.updateByPrimaryKeySelective(educationDTO.toEducation());
                } else {
                    educationDTO.setEducationId(Utils.generateId());
                    educationMapper.insertSelective(educationDTO.toEducation());
                }
            });
        } else {
            return new WebResponse().fail("至少需要一条教育信息", null);
        }

        // job
        List<JobDTO> jobDTOs = accountAllDTO.getJobs();
        if (accountDTO.getType()) {
            if (jobDTOs.size() > 0) {
                jobDTOs.parallelStream().forEach(jobDTO -> {
                    if (jobDTO.getJobId() != null &&
                            jobMapper.selectByPrimaryKey(jobDTO.getJobId()) != null) {
                        jobMapper.updateByPrimaryKeySelective(jobDTO.toJob());
                    } else {
                        jobDTO.setJobId(Utils.generateId());
                        jobMapper.insertSelective(jobDTO.toJob());
                    }
                });
            } else {
                return new WebResponse().fail("至少需要一条工作经历", null);
            }
        }

        // account
        if (accountDTO.getAccountId() != null && accountMapper.selectByPrimaryKey(accountDTO.getAccountId()) != null) {
            Account account = accountDTO.toAccount();
            account.setRegistered(true);
            accountMapper.updateByPrimaryKeySelective(account);
        } else {
            return new WebResponse().fail("accountId 不存在", null);
        }
        return new WebResponse();
    }

    /**
     * 获取个人信息大对象
     *
     * @param accountId
     * @return
     */
    @RequestMapping("/accountAll")
    public WebResponse<AccountAllDTO> getAccountInfo(@RequestParam Long myAccountId,
                                                     @RequestParam Long accountId) {
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

    @RequestMapping("/query")
    public WebResponse query(@RequestParam String content,
                             @RequestParam String type,
                             @RequestParam int pageSize,
                             @RequestParam int pageIndex) {
        List<SearchResultDTO> res = new ArrayList<SearchResultDTO>();

        if (type.equals("") || type == null || type.equals(SearchType.name.getValue())) {
            PageHelper.startPage(pageIndex, pageSize);
            List<BriefInfo> temp = v2ApiMapper.searchByName(content);
            res.add(new SearchResultDTO(
                    ((Page) temp).getTotal(),
                    SearchType.name,
                    temp));
        }
        if (type.equals("") || type == null || type.equals(SearchType.selfDesc.getValue())) {
            PageHelper.startPage(pageIndex, pageSize);
            List<BriefInfo> temp = v2ApiMapper.searchBySelfDesc(content);
            res.add(new SearchResultDTO(
                    ((Page) temp).getTotal(),
                    SearchType.selfDesc,
                    temp));
        }

        if (type.equals("") || type == null || type.equals(SearchType.city.getValue())) {
            PageHelper.startPage(pageIndex, pageSize);
            List<BriefInfo> temp = v2ApiMapper.searchByCity(content);
            res.add(new SearchResultDTO(
                    ((Page) temp).getTotal(),
                    SearchType.city,
                    temp));
        }
        if (type.equals("") || type == null || type.equals(SearchType.company.getValue())) {
            PageHelper.startPage(pageIndex, pageSize);
            List<BriefInfo> temp = v2ApiMapper.searchByCompany(content);
            res.add(new SearchResultDTO(
                    ((Page) temp).getTotal(),
                    SearchType.company,
                    temp));
        }
        if (type.equals("") || type == null || type.equals(SearchType.position.getValue())) {
            PageHelper.startPage(pageIndex, pageSize);
            List<BriefInfo> temp = v2ApiMapper.searchByPosition(content);
            res.add(new SearchResultDTO(
                    ((Page) temp).getTotal(),
                    SearchType.position,
                    temp));
        }
        if (type.equals("") || type == null || type.equals(SearchType.school.getValue())) {
            PageHelper.startPage(pageIndex, pageSize);
            List<BriefInfo> temp = v2ApiMapper.searchBySchool(content);
            res.add(new SearchResultDTO(
                    ((Page) temp).getTotal(),
                    SearchType.school,
                    temp));
        }
        if (type.equals("") || type == null || type.equals(SearchType.college.getValue())) {
            PageHelper.startPage(pageIndex, pageSize);
            List<BriefInfo> temp = v2ApiMapper.searchByCollege(content);
            res.add(new SearchResultDTO(
                    ((Page) temp).getTotal(),
                    SearchType.college,
                    temp));
        }
        return new WebResponse().success(res);
    }

    @RequestMapping("/recommand")
    public WebResponse recommand(@RequestParam long accountId,
                                 @RequestParam int pageSize,
                                 @RequestParam int pageIndex) {
        BriefInfo briefInfo = new BriefInfo();
        AccountAllDTO accountAllDTO = getAccountAllDTOById(accountId);
        briefInfo.setAccountId(accountId);
        briefInfo.setCity(accountAllDTO.getAccount().getCity());
        if (accountAllDTO.getEducations() != null &&
                accountAllDTO.getEducations().size() > 0) {
            briefInfo.setSchool(
                    accountAllDTO.getEducations().get(0).getSchool());
            briefInfo.setCollege(
                    accountAllDTO.getEducations().get(0).getCollege());
        }

        PageHelper.startPage(pageIndex, pageSize);
        List<BriefInfo> temp = v2ApiMapper.recommand(briefInfo);

        return new WebResponse().success(
                new PageResult<BriefInfo>(
                        ((Page) temp).getTotal(),
                        temp));
    }

}
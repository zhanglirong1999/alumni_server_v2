package cn.edu.seu.alumni_server.controller;

import cn.edu.seu.alumni_server.annotation.web_response.WebResponseAPIMethod;
import cn.edu.seu.alumni_server.common.CONST;
import cn.edu.seu.alumni_server.common.Utils;
import cn.edu.seu.alumni_server.common.web_response_dto.WebResponse;
import cn.edu.seu.alumni_server.interceptor.token.Acl;
import cn.edu.seu.alumni_server.interceptor.token.TokenUtil;
import cn.edu.seu.alumni_server.controller.dto.*;
import cn.edu.seu.alumni_server.controller.dto.enums.SearchType;
import cn.edu.seu.alumni_server.dao.entity.Account;
import cn.edu.seu.alumni_server.dao.mapper.*;
import cn.edu.seu.alumni_server.service.CommonService;
import cn.edu.seu.alumni_server.service.QCloudFileManager;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.google.gson.Gson;
import java.io.IOException;
import lombok.Data;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.Sqls;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

@SuppressWarnings("ALL")
@RestController
public class V2ApiController {
    String access_token = "";
    long expireTime = 0l;

    @Autowired
    QCloudFileManager qCloudFileManager;

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
    FavoriteMapper favoriteMapper;

    @Autowired
    RestTemplate restTemplate;
    @Autowired
    CommonService commonService;
    @Autowired
    HttpServletRequest request;

    String getAccessToken(String openid) {
        if (System.currentTimeMillis() > expireTime) {
            // 使用appid和secret访问接口.获取公众号的access_token
            String wxApiUrl = "https://api.weixin.qq.com/cgi-bin/token?" +
                    "openid=" + openid +
                    "&secret=" + CONST.appSecret +
                    "&grant_type=client_credential";
            String respronse = restTemplate.getForObject(wxApiUrl, String.class);

        }
        return access_token;
    }

    /**
     * @param js_code
     * @return
     */
    @RequestMapping("/login/wechat")
    public WebResponse login(@RequestParam String js_code) throws InvocationTargetException, IllegalAccessException {
        // 微信登陆，获取openid
        String wxApiUrl = "https://api.weixin.qq.com/sns/jscode2session?" +
                "appid=" + CONST.appId +
                "&secret=" + CONST.appSecret +
                "&js_code=" + js_code +
                "&grant_type=authorization_code";
        String respronse = restTemplate.getForObject(wxApiUrl, String.class);
        Map res = new Gson().fromJson(respronse, Map.class);
        String openid = (String) res.get("openid");

        if (openid != null && !openid.equals("")) {
            LoginResTemp loginResTemp = new LoginResTemp();
            // 获取accountId,有则返回，无则新增（注册）
            Account resAccount = accountMapper.selectOneByExample(
                    Example.builder(Account.class)
                            .where(Sqls.custom().andEqualTo("openid", openid))
                            .build()
            );
            if (resAccount != null) {
                org.springframework.beans.BeanUtils.copyProperties(resAccount, loginResTemp);
                loginResTemp.setToken(TokenUtil.createToken(resAccount.getAccountId().toString()));
                return new WebResponse().success(loginResTemp);
            } else {
                Account accountNew = new Account();
                accountNew.setOpenid(openid);
                accountNew.setAccountId(Utils.generateId());
                accountMapper.insertSelective(accountNew);

                loginResTemp.setToken(TokenUtil.createToken(accountNew.getAccountId().toString()));
                loginResTemp.setAccountId(accountNew.getAccountId());
                loginResTemp.setRegistered(false);
                loginResTemp.setStep1Finished(false);
                return new WebResponse().success(loginResTemp);
            }
        } else {
            return new WebResponse().fail("获取openid失败", null);
        }
    }

    @Data
    class LoginResTemp {
        Long accountId;
        Boolean step1Finished;
        Boolean registered;
        String token;
    }

    @Acl
    @RequestMapping("/account/step1")
    @WebResponseAPIMethod
    public Object createAccount(
        @RequestBody AccountDTO accountDTO
    ) throws IOException {
        Account account = accountDTO.toAccount();
        if (null != account.getAvatar() && !account.getAvatar().equals("")) {
            String avartar = this.qCloudFileManager.saveAccountAvatar(
                account.getAvatar(),
                Utils.generateId() + ".png"
            );
            account.setAvatar(avartar);
        }
        account.setStep1Finished(true);
        accountMapper.updateByPrimaryKeySelective(account);
        return account.getAccountId();
    }

    @Acl
    @RequestMapping("/account/step2")
    public WebResponse completeAccount(@RequestBody AccountAllDTO accountAllDTO) {
        // account
        AccountDTO accountDTO = accountAllDTO.getAccount();

//        accountService.checkAccountInfo(accountDTO);

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


    @Acl
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

    /**
     * @param request
     * @param filter    0 同学院
     *                  1 同城市
     *                  2 可能认识
     * @param pageSize
     * @param pageIndex
     * @return
     */
    private void addRecommandHelper(List<BriefInfo> res,List<BriefInfo> temp,AccountAllDTO accountAllDTO){
        for (int i = 0; i < temp.size(); ++i) {
            /**
             * 城市   str1
             * 公司   str2
             * 职位   str3
             * 专业   str4
             */
            String city=temp.get(i).getCity();
            String company=temp.get(i).getCompany();
            String position=temp.get(i).getPosition();
            String college=temp.get(i).getCollege();
            String str2="",str3="",str4="",str5="";
            double is1=0.0,is2=0.0,is3=0.0,is4=0.0;
            try {
                str2 = accountAllDTO.getAccount().getCity();
                is1= CharacterStringAcquaintanceDegree.levenshtein(city,str2);
            }catch (Exception e){}
            for(int j=0;j<accountAllDTO.getJobs().size();++i) {
                try {
                    str3 = accountAllDTO.getJobs().get(j).getCompany();
                    is2 = (is2+CharacterStringAcquaintanceDegree.levenshtein(company, str3))/2;
                } catch (Exception e) {
                }
                try {
                    str4 = accountAllDTO.getJobs().get(j).getPosition();
                    is3 = (is2+CharacterStringAcquaintanceDegree.levenshtein(position, str4))/2;
                } catch (Exception e) {
                }
            }
            for(int j=0;j<accountAllDTO.getEducations().size();++j) {
                try {
                    str4 = accountAllDTO.getEducations().get(i).getCollege();
                    is4 = (is3+CharacterStringAcquaintanceDegree.levenshtein(college, str4))/2;
                } catch (Exception e) {
                }
            }
            if(is1+is2+is3+is4>=0.0){res.add(temp.get(i));}
        }
    }
    @Acl
    @RequestMapping("/recommand")
    public WebResponse recommand(HttpServletRequest request,
                                 @RequestParam int filter,
                                 @RequestParam int pageSize,
                                 @RequestParam int pageIndex)
            throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        Long accountId = (Long) request.getAttribute("accountId");

        BriefInfo briefInfo = new BriefInfo();
        AccountAllDTO accountAllDTO = commonService.getAccountAllDTOById(accountId);
        briefInfo.setAccountId(accountId);
        briefInfo.setCity(accountAllDTO.getAccount().getCity());
        if (accountAllDTO.getEducations() != null &&
                accountAllDTO.getEducations().size() > 0) {
            briefInfo.setSchool(
                    accountAllDTO.getEducations().get(0).getSchool());
            briefInfo.setCollege(
                    accountAllDTO.getEducations().get(0).getCollege());
        }

        Map filterMap = BeanUtils.describe(briefInfo);
        filterMap.put("filter", filter);
        Random random=new Random();
        pageIndex=pageIndex+random.nextInt(100);
        pageSize=pageSize+random.nextInt(200);
        PageHelper.startPage(pageIndex, pageSize);
        List<BriefInfo> temp = v2ApiMapper.recommandWithFilter(filterMap);
        List<BriefInfo> res=new Page<BriefInfo>();
        while(temp.size()<10){
            pageIndex=pageIndex+random.nextInt(100);
            pageSize=pageSize+random.nextInt(200);
            PageHelper.startPage(pageIndex, pageSize);
            temp = v2ApiMapper.recommandWithFilter(filterMap);
        }
        while(res.size()<20){
            pageIndex=pageIndex+random.nextInt(100);
            pageSize=pageSize+random.nextInt(200);
            PageHelper.startPage(pageIndex, pageSize);
            temp = v2ApiMapper.recommandWithFilter(filterMap);
            addRecommandHelper(res,temp,accountAllDTO);
        }
        return new WebResponse().success(
                new PageResult<BriefInfo>(
                        ((Page) res).getTotal(),
                        res));
    }

}
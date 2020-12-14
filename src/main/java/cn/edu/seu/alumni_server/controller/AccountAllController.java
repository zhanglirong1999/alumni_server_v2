package cn.edu.seu.alumni_server.controller;

import cn.edu.seu.alumni_server.annotation.web_response.WebResponseAPIMethod;
import cn.edu.seu.alumni_server.common.CONST;
import cn.edu.seu.alumni_server.common.Utils;
import cn.edu.seu.alumni_server.common.web_response_dto.WebResponse;
import cn.edu.seu.alumni_server.interceptor.token.Acl;
import cn.edu.seu.alumni_server.controller.dto.AccountAllDTO;
import cn.edu.seu.alumni_server.controller.dto.AccountDTO;
import cn.edu.seu.alumni_server.controller.dto.EducationDTO;
import cn.edu.seu.alumni_server.controller.dto.JobDTO;
import cn.edu.seu.alumni_server.controller.dto.enums.FriendStatus;
import cn.edu.seu.alumni_server.dao.entity.*;
import cn.edu.seu.alumni_server.dao.mapper.*;
import cn.edu.seu.alumni_server.service.AccountService;
import cn.edu.seu.alumni_server.service.CommonService;
import java.io.IOException;

import cn.edu.seu.alumni_server.service.SecurityService;
import com.qcloud.cos.utils.Base64;
import com.vdurmont.emoji.EmojiParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;


@SuppressWarnings("ALL")
@RestController
@Acl
public class AccountAllController {

    @Autowired
    HttpServletRequest request;
    @Autowired
    V2ApiMapper v2ApiMapper;
    @Autowired
    CommonService commonService;

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
    MessageMapper messageMapper;
    @Autowired
    FavoriteMapper favoriteMapper;

    @Autowired
    HttpServletResponse httpServletResponse;

    @Autowired
    SecurityService securityService;

    @GetMapping("/account")
    public WebResponse getAccount() {
        Long accountId = (Long) request.getAttribute(CONST.ACL_ACCOUNTID);
        Account account = accountMapper.selectByPrimaryKey(accountId);
        String selfDesc = account.getSelfDesc();
        if(selfDesc!=null) {
            account.setSelfDesc(EmojiParser.parseToUnicode(selfDesc));
        }
        return new WebResponse().success(new AccountDTO(account));
    }

    @PostMapping("/account")
    public WebResponse changeAccount(@RequestBody AccountDTO accountDTO) {
        //检验用户创建的文字和图片有没有敏感内容
        boolean isLegal = securityService.checkoutAccountDTOSecurity(accountDTO);
        if (!isLegal) {
            return new WebResponse().fail("文字或图片含有敏感信息");
        }
        System.out.println("111112222HHH");
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
     * 获取指定accountId的个人信息大对象，根据关系，返回值会不同
     *
     * @param accountId
     * @return
     */
    @Acl
    @RequestMapping("/accountAll")
    public WebResponse<AccountAllDTO> getAccountInfo(@RequestParam Long accountId) {

        Long myAccountId = (Long) request.getAttribute(CONST.ACL_ACCOUNTID);

        AccountAllDTO accountAllDTO = commonService.getAccountAllDTOById(accountId);
        if (!myAccountId.equals(accountId)) {
            // 获取两人关系
            Friend relationShip = friendMapper.getRelationShip(myAccountId, accountId);
            if (relationShip != null) {
                accountAllDTO.setRelationShip(relationShip.getStatus());
                if (relationShip.getStatus() != FriendStatus.friend.getStatus()) {
                    accountAllDTO.getAccount().setBirthday(null);
                    accountAllDTO.getAccount().setWechat(null);
                    accountAllDTO.getAccount().setPhone(null);
                }
            }
            // 获取收藏状态
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

    @GetMapping("/education")
    public WebResponse getEducation(@RequestParam Long educationId) {
        Education education = educationMapper.selectByPrimaryKey(educationId);
        return new WebResponse().success(new EducationDTO(education));
    }

    @PostMapping("/education")
    public WebResponse changeEducation(@RequestBody EducationDTO educationDTO) {
        //检验用户创建的文字和图片有没有敏感内容
        boolean isLegal = securityService.checkoutEducationDTOSecurity(educationDTO);
        if (!isLegal) {
            return new WebResponse().fail("文字或图片含有敏感信息");
        }

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
        //检验用户创建的文字和图片有没有敏感内容
        boolean isLegal = securityService.checkoutJobDTOSecurity(jobDTO);
        if (!isLegal) {
            return new WebResponse().fail("文字或图片含有敏感信息");
        }

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

    @WebResponseAPIMethod
    @GetMapping("/account/fileAvatar")
    public Object updateAccountAvatarByFile(
        @RequestParam MultipartFile multipartFile
    ) throws IOException {
        // 使用文件修改用户的头像
        return this.accountService.updateAccountAvatarByFile(
            (Long) request.getAttribute(CONST.ACL_ACCOUNTID),
            multipartFile
        );
    }

    @WebResponseAPIMethod
    @GetMapping("/account/urlAvatar")
    public Object updateAccountAvatarByURL(
        @RequestParam String url
    ) throws IOException {
        // 使用 url 修改用户的头像
        return this.accountService.updateAccountAvatarByURL(
            (Long) request.getAttribute(CONST.ACL_ACCOUNTID),
            url
        );
    }

    @PostMapping("/getPhoneNumber")
    public String getPhoneNumber(String encryptedData, String iv, String sessionKey) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, UnsupportedEncodingException, InvalidKeyException {

        System.out.println(encryptedData + "-------" + iv + "-------" + sessionKey);

        byte[] encData = Base64.decode(encryptedData);
        byte[] keyByte = Base64.decode(iv);
        byte[] key = Base64.decode(sessionKey);

        AlgorithmParameterSpec ivSpec = new IvParameterSpec(keyByte);
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);// 初始化
        byte[] resultByte = cipher.doFinal(encData);
        if (null != resultByte && resultByte.length > 0) {
            String result = new String(resultByte, "UTF-8");
            System.out.println(result);
            return result;
        }
        return null;
    }

}

package cn.edu.seu.alumni_server.service.impl;

import cn.edu.seu.alumni_server.common.Utils;
import cn.edu.seu.alumni_server.common.config.AccessTokenConfig;
import cn.edu.seu.alumni_server.controller.dto.ActivityDTO;
import cn.edu.seu.alumni_server.service.SecurityService;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@Slf4j
public class SecurityServiceImpl implements SecurityService {
    @Autowired
    AccessTokenConfig accessTokenConfig;

    @Override
    public boolean checkoutActivityContentSecurity(ActivityDTO activityDTO) {
        return  checkoutText(activityDTO.getActivityName()) &&
                checkoutText(activityDTO.getActivityDesc()) &&
                checkoutPicture(activityDTO.getImg1())&&
                checkoutPicture(activityDTO.getImg2())&&
                checkoutPicture(activityDTO.getImg3())&&
                checkoutPicture(activityDTO.getImg4())&&
                checkoutPicture(activityDTO.getImg5())&&
                checkoutPicture(activityDTO.getImg6());
    }

    /**
     * 使用微信文本安全内容检测接口检测传入的String字符串是否合法
     * @param text
     * @return
     */
    private boolean checkoutText(String text){
        if(StringUtils.isEmpty(text))
            return true;
        String url = "https://api.weixin.qq.com/wxa/msg_sec_check?access_token=" + Utils.getAccessToken();
        RestTemplate rest = new RestTemplate();
        Map<String, String> param = new HashMap<>();
        param.put("content", text);
        String response = rest.postForObject(url, param, String.class);
        String code = JSONObject.parseObject(response).getString("errcode");
        log.info("文字或图片敏感检测，" + text + ": " + response);
        if (code.equals("40001")){
            accessTokenConfig.regainAccessToken();
            response = rest.postForObject(url, param, String.class);
            code = JSONObject.parseObject(response).getString("errcode");
            log.info("文字或图片敏感检测，" + text + ": " + response);
        }
        return !code.equals("87014");
    }

    /**
     * 使用微信图片安全内容检测接口检测图片是否合法
     * @param path 图片的url路径
     * @return
     */
    private boolean checkoutPicture(String path){
        if(StringUtils.isEmpty(path))
            return true;
        String url = "https://api.weixin.qq.com/wxa/img_sec_check?access_token=" + Utils.getAccessToken();

        InputStream headimgIs = null;
        String picturePath  = null;
        try {
            URL pictureUrl = new URL(path);
            URLConnection con = pictureUrl.openConnection();
            headimgIs = con.getInputStream();
            byte[] bytes = new byte[1024];
            String rootDirectory = System.getProperty("user.dir");
            picturePath = rootDirectory + "/src/main/resources/picture/msg" + UUID.randomUUID() + ".jpg";
            FileOutputStream downloadFile = new FileOutputStream(picturePath);
            int index;
            while ((index = headimgIs.read(bytes)) != -1) {
                downloadFile.write(bytes, 0, index);
                downloadFile.flush();
            }
            downloadFile.close();
            headimgIs.close();
            RestTemplate rest = new RestTemplate();
            FileSystemResource resource = new FileSystemResource(picturePath);
            MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();
            param.add("media", resource);
            String response = rest.postForObject(url, param, String.class);
            String code = JSONObject.parseObject(response).getString("errcode");
            //删除图片
            new File(picturePath).delete();
            log.info("文字或图片敏感检测，" + path + ": " + response);
            if (code.equals("40001")){
                accessTokenConfig.regainAccessToken();
                response = rest.postForObject(url, param, String.class);
                code = JSONObject.parseObject(response).getString("errcode");
                log.info("文字或图片敏感检测，" + path + ": " + response);
            }
            return !code.equals("87014");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }
}

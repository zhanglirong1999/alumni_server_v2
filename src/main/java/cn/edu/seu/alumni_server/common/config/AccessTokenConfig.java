package cn.edu.seu.alumni_server.common.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class AccessTokenConfig {
    private static String ACCESS_TOKEN = "";

    /**
     * 自获取access_token算起，7200s（两小时）后access_token失效，
     * 利用spring boot的定时器，每过一小时再次获取access_token,防止access_token失效
     */
    @Scheduled(initialDelay=1000, fixedDelay=3600000)
    private void updateAccessToken() {
        RestTemplate restTemplate = new RestTemplate();
        Map<String, String> params = new HashMap<>();
        params.put("APPID", "wxebacdf49b73f338b");
        params.put("APPSECRET", "4daee58bdef643b2ff1b29e8c219715a");
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(
                "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid={APPID}&secret={APPSECRET}", String.class, params);
        String body = responseEntity.getBody();
        JSONObject object = JSON.parseObject(body);
        ACCESS_TOKEN = object.getString("access_token");
        String expires_in = object.getString("expires_in");
        log.info("updateAccessToken ACCESS_TOKEN: "+ ACCESS_TOKEN + "expires_in: "+ expires_in);
        if(!expires_in.equals("7200"))
            log.warn("官方的access_token过期时长发生了变化，为" + expires_in + "min,默认的过期时长为7200min");
    }

    public void regainAccessToken(){
        updateAccessToken();
    }

    public static String getAccessToken(){
        return ACCESS_TOKEN;
    }
}

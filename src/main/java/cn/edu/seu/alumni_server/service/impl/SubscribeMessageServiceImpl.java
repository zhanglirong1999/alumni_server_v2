package cn.edu.seu.alumni_server.service.impl;

import cn.edu.seu.alumni_server.common.CONST;
import cn.edu.seu.alumni_server.common.Utils;
import cn.edu.seu.alumni_server.controller.dto.SubscribeMessage;
import cn.edu.seu.alumni_server.controller.dto.TemplateData;
import cn.edu.seu.alumni_server.dao.mapper.AccountMapper;
import cn.edu.seu.alumni_server.dao.mapper.ActivityMapper;
import cn.edu.seu.alumni_server.service.SubscribeMessageService;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@Log4j
public class SubscribeMessageServiceImpl implements SubscribeMessageService {
    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private ActivityMapper activityMapper;

    /**
     * @param id          接收推送的客户id
     * @param sender      发送者的openId,发送者可能是活动，也可能是用户
     * @param messageType 信息的类型，比如活动信息，请求添加好友信息，接受添加好友，拒绝添加好友，使用CONST类中的常量
     * @return
     */
    @Override
    public String sendSubscribeMessage(long id, long sender, String messageType) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            //这里简单起见我们每次都获取最新的access_token（时间开发中，应该在access_token快过期时再重新获取）
            String url = "https://api.weixin.qq.com/cgi-bin/message/subscribe/send?access_token=" + Utils.getAccessToken();
            //拼接推送的模版
            SubscribeMessage subscribeMessage = new SubscribeMessage();
            //根据用户的id查表获得openid
            System.out.println(id);
            subscribeMessage.setTouser(accountMapper.selectByAccountId(id).getOpenid());//用户的openid（要发送给那个用户，通常这里应该动态传进来的）
            subscribeMessage.setTemplate_id("b4KhLPwI1zJIq5KmZ0IzCV_TD9nS3CS3MEzjf8i0McA");//订阅消息模板id
            subscribeMessage.setPage("pages/noticeList/noticeList");

            //需要发送的data消息，应该从数据库获取
            Map<String, TemplateData> m = new HashMap<>(3);
            //发送人
            //从数据库中根据id查询发送者的名字，名字要求：10个以内纯汉字或20个以内纯字母或符号
            String name;
            if (messageType.equals(CONST.ACTIVITY_MESSAGE)) {
                name = activityMapper.getBasicInfosByActivityId(sender).getActivityName();
            } else {
                //现有四种推送信息，除了if中的活动信息的推送的发送者是活动，剩下的三个发送者都是人，从数据库中找出人的名字
                name = accountMapper.selectByAccountId(sender).getName();
            }
            name = name.length() > 10 ? name.substring(0, 10) : name;
            m.put("name1", new TemplateData(name));
            //发送时间
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
            m.put("time2", new TemplateData(df.format(new Date())));
            //消息内容
            m.put("thing3", new TemplateData(messageType));
            subscribeMessage.setData(m);

            ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, subscribeMessage, String.class);

            log.info(responseEntity.getBody());
            return responseEntity.getBody();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}

package cn.edu.seu.alumni_server.service.impl;

import cn.edu.seu.alumni_server.common.CONST;
import cn.edu.seu.alumni_server.common.Utils;
import cn.edu.seu.alumni_server.common.config.AccessTokenConfig;
import cn.edu.seu.alumni_server.controller.dto.SubscribeMessage;
import cn.edu.seu.alumni_server.controller.dto.TemplateData;
import cn.edu.seu.alumni_server.dao.mapper.AccountMapper;
import cn.edu.seu.alumni_server.dao.mapper.ActivityMapper;
import cn.edu.seu.alumni_server.service.SubscribeMessageService;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.log4j.Log4j;
import org.apache.commons.lang3.tuple.MutablePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executor;

@Service
@Log4j
public class SubscribeMessageServiceImpl implements SubscribeMessageService {
    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private ActivityMapper activityMapper;

    @Autowired
    AccessTokenConfig accessTokenConfig;

    @Autowired
    Executor executor;

    private ConcurrentLinkedQueue<MutablePair<SubscribeMessage,Integer>> failQueue = new ConcurrentLinkedQueue<>();

    /**
     * @param id          接收推送的客户id
     * @param sender      发送者的openId,发送者可能是活动，也可能是用户
     * @param messageType 信息的类型，比如活动信息，请求添加好友信息，接受添加好友，拒绝添加好友，使用CONST类中的常量
     * @return
     */
    @Override
    public String sendSubscribeMessage(long id, long sender, String messageType) {
        try {
            //向微信请求发送推送的url，access_token在快过期时再重新获取
            String url = "https://api.weixin.qq.com/cgi-bin/message/subscribe/send?access_token=" + Utils.getAccessToken();
            //拼接推送的模版
            SubscribeMessage subscribeMessage = new SubscribeMessage();

            log.info("subscribe url：" + url);
            log.info("receive id：" + Long.toString(id));
            //根据用户的id查表获得openid
            subscribeMessage.setTouser(accountMapper.selectByAccountId(id).getOpenid());//用户的openid（要发送给哪个用户）
            subscribeMessage.setTemplate_id("b4KhLPwI1zJIq5KmZ0IzCV_TD9nS3CS3MEzjf8i0McA");//订阅消息模板id
            subscribeMessage.setPage("pages/noticeList/noticeList");

            Map<String, TemplateData> m = new HashMap<>(3);
            //发送人
            //从数据库中根据id查询发送者的名字，名字要求：10个以内纯汉字或20个以内纯字母或符号
            String name;
            if (messageType.equals(CONST.ACTIVITY_MESSAGE)) {
                name = activityMapper.getBasicInfosByActivityId(sender).getStarterName();
            } else {
                //现有四种推送信息，除了if中的活动信息的推送的发送者是活动，剩下的三个发送者都是人，从数据库中找出人的名字
                name = accountMapper.selectByAccountId(sender).getName();
            }
            name = isLetterOrChinese(name)? name : "某用户";
            m.put("name1", new TemplateData(name));
            //发送时间
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
            m.put("time2", new TemplateData(df.format(new Date())));
            //消息内容
            m.put("thing3", new TemplateData(messageType));
            subscribeMessage.setData(m);

            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, subscribeMessage, String.class);

            String response = responseEntity.getBody();
            String errcode = JSONObject.parseObject(response).getString("errcode");
            log.info("subscribe message: " + subscribeMessage + " subscribe message response ：" + response);
            //判断access_token是否有效，无效重新刷新accesstoken并重新发送
            if ("40001".equals(errcode)){
                accessTokenConfig.regainAccessToken();
                responseEntity = restTemplate.postForEntity(url, subscribeMessage, String.class);
            }
            //如果还是失败，则放到队列中去进行发送
            response = responseEntity.getBody();
           if("40001".equals(JSONObject.parseObject(response).getString("errcode"))){
               response = responseEntity.getBody();
               log.info("subscribe message: " + subscribeMessage + " subscribe message response ：" + response);
               executor.execute(() -> {
                   failQueue.add(MutablePair.of(subscribeMessage,0));
               });
           }
            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 有的推送发送失败，存到了队列中，定时进行发送，定2分钟发送一次，发送5次还发不出去就丢弃数据，不再发送
     */
    @Scheduled(initialDelay=1000, fixedDelay=120000)
    private void retryFailedSubscribeMessage(){
        Iterator<MutablePair<SubscribeMessage,Integer>> iterator = failQueue.iterator();
        while(iterator.hasNext()){
            MutablePair<SubscribeMessage,Integer> m = iterator.next();
            String url = "https://api.weixin.qq.com/cgi-bin/message/subscribe/send?access_token=" + Utils.getAccessToken();
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, m.left, String.class);
            String errcode = JSONObject.parseObject(responseEntity.getBody()).getString("errcode");
            log.info("subscribe message: " + m.left + " subscribe message response ：" + responseEntity.getBody());
            if("40001".equals(errcode) && m.right == 5) {
                log.info("subscribe message: " + m.left + "重发三次均失败，放弃发送");
                iterator.remove();
            }
            else if ("40001".equals(errcode) && m.right < 5){
                m.right++;
                log.info("subscribe message: " + m.left + "进行第" + m.right + "次重发");
            }
            else {
                log.info("subscribe message: " + m.left +"重发成功");
                iterator.remove();
            }

        }
    }

    //模板消息中的name.data需要符合 “中文名10个汉字内；纯英文名20个字母内；中文和字母混合按中文名算，10个字内” 的要求
    private boolean isLetterOrChinese(String str) {
        String regex = "[a-zA-Z]{1,20}|[[a-zA-Z]*|[\u4e00-\u9fa5]+|[a-zA-Z]*]{1,10}";
        return str.matches(regex);
    }
}

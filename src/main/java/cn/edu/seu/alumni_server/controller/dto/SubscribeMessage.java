package cn.edu.seu.alumni_server.controller.dto;

import lombok.Data;

import java.util.Map;

/**
 * 发送模板消息的内容所要传递的信息
 */
@Data
public class SubscribeMessage {

    private String touser;//用户openid

    private String template_id;//订阅消息模版id

    private String page;//默认跳到小程序首页

    private Map<String, TemplateData> data;//推送文字
}

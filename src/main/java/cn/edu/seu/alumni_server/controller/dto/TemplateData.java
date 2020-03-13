package cn.edu.seu.alumni_server.controller.dto;

import lombok.Data;

/**
 * 发送推送时要传递的数据(SubscribeMessage)中的data内容
 */
@Data
public class TemplateData {
    private String value;

    public TemplateData(String value) {
        this.value = value;
    }
}

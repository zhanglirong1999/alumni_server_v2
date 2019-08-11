package cn.edu.seu.alumni_server.controller.dto;

import cn.edu.seu.alumni_server.dao.entity.Message;
import lombok.Data;
import org.springframework.beans.BeanUtils;

@Data
public class MessageDTO {
    private Long messageId;

    private Integer type;

    private String payload;

    /**
     * 0未读1已读
     */
    private Integer status;


    private Long from;

    private Long to;

    MessageDTO() {

    }

    MessageDTO(Message message) {
        BeanUtils.copyProperties(message, this);
    }

    public Message toMessage() {
        Message message = new Message();
        BeanUtils.copyProperties(this, message);
        return message;
    }
}

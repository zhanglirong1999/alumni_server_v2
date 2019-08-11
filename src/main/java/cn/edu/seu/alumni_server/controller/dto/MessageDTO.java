package cn.edu.seu.alumni_server.controller.dto;

import cn.edu.seu.alumni_server.dao.entity.Message;
import lombok.Data;
import org.springframework.beans.BeanUtils;

@Data
public class MessageDTO {
    private Long messageId;

    private Integer type;

//    private String payload;

    /**
     * 0未读1已读
     */
    private Integer status;

    private Long fromUser;

    private String fromUserName;

    private Long toUser;

    private Long createTime;

    MessageDTO() {

    }

    public MessageDTO(Message message) {
        BeanUtils.copyProperties(message, this);
        this.setCreateTime(message.getcTime().getTime());
    }

    public Message toMessage() {
        Message message = new Message();
        BeanUtils.copyProperties(this, message);
        return message;
    }
}

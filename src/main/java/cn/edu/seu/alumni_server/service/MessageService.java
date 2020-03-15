package cn.edu.seu.alumni_server.service;

import cn.edu.seu.alumni_server.controller.dto.MessageTemp;

import java.util.List;

public interface MessageService {

    void newMessage(Long fromUser, Long toUser, Integer type);

    void newMessage(Long fromUser, Long toUser, Integer type, String title, String content);

    void newMessageBatch(List<MessageTemp> messageTemps);
}

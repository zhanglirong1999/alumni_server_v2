package cn.edu.seu.alumni_server.service;

public interface SubscribeMessageService {
    String sendSubscribeMessage(long id, long sender, String messageType);
}

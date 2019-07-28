package cn.edu.seu.alumni_server.controller.dto;

import lombok.Data;

@Data
public class FriendDTO extends AccountAllDTO {
    private String accountId;
    private String friend_accountId;
}

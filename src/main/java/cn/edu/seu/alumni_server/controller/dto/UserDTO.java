package cn.edu.seu.alumni_server.controller.dto;

import lombok.Data;

@Data
public class UserDTO {
    String openid;
    String phone;
    String wechat;
    String email;
}

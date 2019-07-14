package cn.edu.seu.alumni_server.controller.dto;

import lombok.Data;

@Data
public class UserInfoDTO {
    String id;
    String openid;
    String head_url;
    String gender;
    String descr;
    String city;
    String real_name;
    String vocation;
    String phone;
    String wechat;
    String email;
}
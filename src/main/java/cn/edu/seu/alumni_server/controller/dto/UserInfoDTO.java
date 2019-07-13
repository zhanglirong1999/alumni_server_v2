package cn.edu.seu.alumni_server.controller.dto;

import lombok.Data;

@Data
public class UserInfoDTO {
    String openid;
    String id;

    String head_url;
    String gender;
    String descr;
    String city;
    String real_name;
    String vocation;
}

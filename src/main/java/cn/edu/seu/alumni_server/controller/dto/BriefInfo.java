package cn.edu.seu.alumni_server.controller.dto;

import lombok.Data;

@Data
public class BriefInfo {
    private Long accountId;
    private String openid;
    private String avatar;
    private String name;
    private String city;
    private String school_college;
    private String company;
    private String selfDesc;
    private String recommondReason;
}

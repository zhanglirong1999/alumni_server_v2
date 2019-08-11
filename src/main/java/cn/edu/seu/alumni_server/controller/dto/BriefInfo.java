package cn.edu.seu.alumni_server.controller.dto;

import lombok.Data;

@Data
public class BriefInfo {
    private Long accountId;
    private String name;
    private String city;
    private String avatar;
    private String selfDesc;
    private String company;
    private String position;
    private String school;
    private String college;
    private String recommondReason;
}

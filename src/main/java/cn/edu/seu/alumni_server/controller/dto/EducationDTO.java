package cn.edu.seu.alumni_server.controller.dto;

import lombok.Data;

@Data
public class EducationDTO {
    private String accountId;
    private String educationId;
    private String education;
    private String school;
    private String college;
    private String startTime;
    private String endTime;
}

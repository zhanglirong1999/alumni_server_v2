package cn.edu.seu.alumni_server.controller.dto;

import lombok.Data;

@Data
public class JobDTO {
    private String accountId;
    private String jobId;
    private String company;
    private String position;
    private String startTime;
    private String endTime;
}
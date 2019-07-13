package cn.edu.seu.alumni_server.controller.dto;

import lombok.Data;

@Data
public class WorkDTO {
    String openid;
    String company;
    String job;
    String start_year;
    String end_year;
    String id;
}

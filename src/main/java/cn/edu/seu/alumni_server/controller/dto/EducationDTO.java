package cn.edu.seu.alumni_server.controller.dto;

import lombok.Data;

@Data
public class EducationDTO {
    String openid;
    String id;
    String school;
    String background;
    String department;
    String profession;
    String start_year;
    String end_year;
}

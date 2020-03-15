package cn.edu.seu.alumni_server.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MessageTemp {
    Long activityId;
    Long accountId;
    Integer type;
    String title;
    String content;
}
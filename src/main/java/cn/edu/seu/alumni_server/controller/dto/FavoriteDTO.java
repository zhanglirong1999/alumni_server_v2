package cn.edu.seu.alumni_server.controller.dto;

import lombok.Data;

@Data
public class FavoriteDTO {
    private Long accountId;
    private Long favoriteAccountId;
    private Integer status;
    private String name;
    private String city;
    private String school;
    private String college;
    private String company;
    private String position;
}

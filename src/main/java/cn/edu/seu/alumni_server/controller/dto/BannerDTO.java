package cn.edu.seu.alumni_server.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BannerDTO {
    private Long bannerId;
    private String img;
    private String link;
}

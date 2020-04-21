package cn.edu.seu.alumni_server.controller.dto;

import lombok.Data;

@Data
public class DemandCreateDTO {
    /**
     * 创建人
     */
    private String accountId;
    /**
     * 需求的名字，限制15个字以内
     */
    private String demandName;
    private String type;
    /**
     * 标签之间用，分隔
     */
    private String tags;
    /**
     * 需求的详情，限制500个汉字
     */
    private String details;
    private String img1;
    private String img2;
    private String img3;
    private String img4;
    private String img5;
    private String img6;
}

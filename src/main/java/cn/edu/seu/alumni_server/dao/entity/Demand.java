package cn.edu.seu.alumni_server.dao.entity;

import lombok.Data;

import javax.persistence.Column;
import java.util.Date;

@Data
public class Demand {
    @Column(name = "demand_id")
    private Long demandId;
    @Column(name = "account_id")
    private Long accountId;
    private String demandName;
    private String type;
    private String tag1;
    private String tag2;
    private String tag3;
    private String tag4;
    private String tag5;
    private String details;
    private String img1;
    private String img2;
    private String img3;
    private String img4;
    private String img5;
    private String img6;
    /**
     * 1有效0无效
     */
    @Column(name = "valid_status")
    private Boolean validStatus;

    @Column(name = "c_time", insertable = false)
    private Date c_time;

    @Column(name = "u_time", insertable = false)
    private Date u_time;
}

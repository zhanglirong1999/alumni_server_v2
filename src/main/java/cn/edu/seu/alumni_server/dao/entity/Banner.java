package cn.edu.seu.alumni_server.dao.entity;

import lombok.Data;

import javax.persistence.Column;
import java.util.Date;

@Data
public class Banner {
    private Long bannerId;
    private String img;
    private String link;
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

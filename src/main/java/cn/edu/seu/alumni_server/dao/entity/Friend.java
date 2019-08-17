package cn.edu.seu.alumni_server.dao.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

@Data
public class Friend implements Serializable {
    @Column(name = "account_id")
    private Long accountId;

    @Column(name = "friend_account_id")
    private Long friendAccountId;

    private Integer status;

    @Column(name = "c_time", insertable = false)
    private Date cTime;

    @Column(name = "u_time", insertable = false)
    private Date uTime;

    @Column(name = "valid_status", insertable = false)
    private Boolean validStatus;

    private static final long serialVersionUID = 1L;
}
package cn.edu.seu.alumni_server.dao.entity;

import lombok.Data;

import javax.persistence.Id;
import java.io.Serializable;

@Data
public class ActivityMember implements Serializable {
    @Id
    private Long activityId;

    @Id
    private Long accountId;

    private Boolean readStatus;

    private static final long serialVersionUID = 1L;
}

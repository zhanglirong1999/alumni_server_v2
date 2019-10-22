package cn.edu.seu.alumni_server.dao.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class ActivityMember implements Serializable {
    private Long activityId;

    private Long accountId;

    private Boolean validStatus;

    private Boolean readStatus;

    private static final long serialVersionUID = 1L;
}

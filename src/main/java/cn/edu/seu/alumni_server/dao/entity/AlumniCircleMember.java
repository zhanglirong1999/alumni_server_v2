package cn.edu.seu.alumni_server.dao.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class AlumniCircleMember implements Serializable {

    private Long alumniCircleId;

    private Long accountId;

    private Boolean validStatus;

    private Boolean pushStatus;

    private static final long serialVersionUID = 1L;
}

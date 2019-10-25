package cn.edu.seu.alumni_server.dao.entity;


import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class AlumniCircle implements Serializable {

    private Long alumniCircleId;

    private Long creatorId;

    private Integer alumniCircleType;

    private String alumniCircleName;

    private String alumniCircleDesc;

    private String avatar;

    private Boolean authorizationStatus;

    private Date cTime;

    private Date uTime;

    private static final long serialVersionUID = 1L;

}
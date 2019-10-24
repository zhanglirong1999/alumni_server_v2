package cn.edu.seu.alumni_server.dao.entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.Column;
import java.io.Serializable;
import java.util.Date;


@Data
public class Activity implements Serializable {
    private Long activityId;

    private Long alumniCircleId;

    private Long accountId;

    private String activityName;

    private String activityDesc;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date activityTime;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date expirationTime;

    private String img1;

    private String img2;

    private String img3;

    private String img4;

    private String img5;

    private String img6;

    private Boolean visibleStatus;

    private Boolean validStatus;

    private Date cTime;  // 创建时间

    private Date uTime;  // 更新时间

    private static final long serialVersionUID = 1L;
}

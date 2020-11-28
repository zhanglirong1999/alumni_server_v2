package cn.edu.seu.alumni_server.dao.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Activity2 {
    Long id;

    Long accountId;

    String title;

    String description;

    String time;

    String expirationTime;

    String type;

    String location;

    String cost;

    Integer visible;

    Long circleId;

    String tag;

}

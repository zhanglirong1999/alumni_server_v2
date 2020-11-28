package cn.edu.seu.alumni_server.dao.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActivityImg {
    Integer id;

    Long aid;

    String img;
}

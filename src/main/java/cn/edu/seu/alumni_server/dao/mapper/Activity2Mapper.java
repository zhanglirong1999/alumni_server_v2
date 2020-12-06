package cn.edu.seu.alumni_server.dao.mapper;

import cn.edu.seu.alumni_server.controller.dto.StartedOrEnrolledActivityInfoDTO2;
import cn.edu.seu.alumni_server.dao.entity.Activity2;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Repository
public interface Activity2Mapper extends Mapper<Activity2> {

    @Select("SELECT * FROM activity2 WHERE id=${id}")
    Activity2 getActivity(Long id);

    @Delete("DELETE FROM activity2 WHERE id=${id}")
    Integer deleteActivity(Long id);

    @Select("SELECT count(*) FROM activity_member WHERE account_id=${accountId} and activity_id=${activity_id}")
    Integer getActivityMember(Long accountId,Long activity_id);

    @Delete("DELETE FROM activity_member WHERE account_id=${accountId} and activity_id=${activity_id}")
    Integer deleteActivityMember(Long accountId,Long activity_id);

    @Select(" SELECT ACT.id,\n" +
            "               ACT.title,\n" +
            "               ACT.expiration_time       AS expiration_date_time,\n" +
            "               ACT.time         AS time,\n" +
            "               COUNT(ACT_MEM.account_id) AS enrolled_number\n" +
            "        FROM activity2 AS ACT,\n" +
            "             activity_member AS ACT_MEM\n" +
            "        WHERE  ACT_MEM.is_available = 1\n" +
            "          AND ACT.id = ACT_MEM.activity_id\n" +
            "          AND ACT.id IN (\n" +
            "            SELECT ACT1.id\n" +
            "            FROM activity2 AS ACT1\n" +
            "            WHERE ACT1.account_id = #{accountId}\n" +
            "        )\n" +
            "        GROUP BY ACT.id")
    List<StartedOrEnrolledActivityInfoDTO2> getStarted(Long accountId);

    @Select(" SELECT ACT.id,\n" +
            "               ACT.title,\n" +
            "               ACT.expiration_time       AS expiration_date_time,\n" +
            "               ACT.time         AS time,\n" +
            "               COUNT(ACT_MEM.account_id) AS enrolled_number\n" +
            "        FROM activity2 AS ACT,\n" +
            "             activity_member AS ACT_MEM\n" +
            "        WHERE  ACT_MEM.is_available = 1\n" +
            "          AND ACT.id = ACT_MEM.activity_id\n" +
            "          AND ACT.id IN (\n" +
            "            SELECT AM1.activity_id\n" +
            "            FROM activity_member AS AM1\n" +
            "            WHERE AM1.account_id = #{accountId}\n" +
            "        )\n" +
            "        GROUP BY ACT.id")
    List<StartedOrEnrolledActivityInfoDTO2> getEnrolled(Long accountId);

}

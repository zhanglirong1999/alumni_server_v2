package cn.edu.seu.alumni_server.dao.mapper;

import cn.edu.seu.alumni_server.dao.entity.Activity2;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;
@Repository
public interface Activity2Mapper extends Mapper<Activity2> {

    @Select("SELECT * FROM activity2 WHERE id=${id}")
    Activity2 getActivity(Long id);

    @Delete("DELETE FROM activity2 WHERE id=${id}")
    Integer deleteActivity(Long id);
}

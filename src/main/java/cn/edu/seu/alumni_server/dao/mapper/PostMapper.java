package cn.edu.seu.alumni_server.dao.mapper;

import cn.edu.seu.alumni_server.dao.entity.Post;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

@Repository
public interface PostMapper extends Mapper<Post> {
}
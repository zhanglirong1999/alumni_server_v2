package cn.edu.seu.alumni_server.dao.mapper;

import cn.edu.seu.alumni_server.controller.internalRecommend.dto.CommentDTO;
import cn.edu.seu.alumni_server.dao.entity.Comment;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

@Repository
public interface CommentMapper extends Mapper<Comment> {

    List<CommentDTO> getCommentList(Long postId);
}

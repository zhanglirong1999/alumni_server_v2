package cn.edu.seu.alumni_server.service;

import cn.edu.seu.alumni_server.controller.internalRecommend.dto.CommentDTO;
import cn.edu.seu.alumni_server.controller.internalRecommend.dto.TopCommentDTO;

import java.util.List;

public interface CommentService {

    List<TopCommentDTO> getCommentList(Long postId);

    int insertComment(CommentDTO commentDTO);
}

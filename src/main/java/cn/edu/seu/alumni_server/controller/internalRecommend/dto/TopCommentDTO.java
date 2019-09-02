package cn.edu.seu.alumni_server.controller.internalRecommend.dto;

import lombok.Data;

import java.sql.Date;
import java.util.List;

@Data
public class TopCommentDTO {
    //评论Id
    private Long commentId;
    //用户Id
    private Long accountId;
    //用户名字
    private String name;
    //帖子Id
    private Long postId;
    //评论内容
    private String content;
    //评论创建时间
    private Date cTime;
    //子评论列表
    private List<CommentDTO> commentList;

}

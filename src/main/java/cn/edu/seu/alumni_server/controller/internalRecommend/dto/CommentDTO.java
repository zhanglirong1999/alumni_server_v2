package cn.edu.seu.alumni_server.controller.internalRecommend.dto;

import lombok.Data;

import java.sql.Date;

@Data
public class CommentDTO {
    //评论id
    private Long commentId;
    //帖子id
    private Long postId;
    //根评论id
    private Long topCommentId;
    //父评论id
    private Long fatherCommentId;
    //用户id
    private Long accountId;
    //用户名字
    private String name;
    //父评论用户id
    private Long fatherCommentAccountId;
    //父评论用户名字
    private String fatherCommentAccountName;
    //创建时间
    private Date cTime;
}

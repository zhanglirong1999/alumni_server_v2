package cn.edu.seu.alumni_server.controller.internalRecommend;

import cn.edu.seu.alumni_server.common.Utils;
import cn.edu.seu.alumni_server.common.dto.WebResponse;
import cn.edu.seu.alumni_server.controller.internalRecommend.dto.CommentDTO;
import cn.edu.seu.alumni_server.controller.internalRecommend.dto.TopCommentDTO;
import cn.edu.seu.alumni_server.service.CommentService;
import cn.edu.seu.alumni_server.service.impl.CommentServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @GetMapping("/query")
    public WebResponse getCommentList(@RequestParam long postId) {
        List<TopCommentDTO> list = commentService.getCommentList(postId);
        return new WebResponse().success(list);
    }

    @PostMapping("/")
    public WebResponse newPost(@RequestBody CommentDTO commentDTO) {
        commentDTO.setCommentId(Utils.generateId());
        commentService.insertComment(commentDTO);
        return new WebResponse().success();
    }
}

package cn.edu.seu.alumni_server.controller.internalRecommend;

import cn.edu.seu.alumni_server.common.Utils;
import cn.edu.seu.alumni_server.common.dto.WebResponse;
import cn.edu.seu.alumni_server.controller.dto.PageResult;
import cn.edu.seu.alumni_server.controller.internalRecommend.dto.PostDTO;
import cn.edu.seu.alumni_server.dao.entity.Post;
import cn.edu.seu.alumni_server.dao.mapper.PostMapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/post")
public class PostController {

    @Autowired
    PostMapper postMapper;

    @GetMapping("/")
    public WebResponse getPost(
            @RequestParam long postId,
            @RequestParam int pageIndex,
            @RequestParam int pageSize) {
        Post post = postMapper.selectByPrimaryKey(postId);

        return new WebResponse().success(new PostDTO(post));
    }

    @GetMapping("/list")
    public WebResponse getPosts(
            @RequestParam int type,
            @RequestParam String city,
            @RequestParam String province,
            @RequestParam String industry,
            @RequestParam int pageIndex,
            @RequestParam int pageSize) {
        PageHelper.startPage(pageIndex, pageSize);
        Post post = new Post();
        post.setCity(city);
        post.setIndustry(industry);
        post.setProvince(province);
        post.setType(type);
        List<Post> temp = postMapper.select(post);

        return new WebResponse().success(
                new PageResult<>(((Page) temp).getTotal(), temp));
    }

    @PostMapping("/")
    public WebResponse newPost(@RequestBody PostDTO postDTO) {
        // TODO ,直接新增，目前暂无修改操作
        postDTO.setPostId(Utils.generateId());
        postMapper.insertSelective(postDTO.toPost());
        return new WebResponse().success();
    }
}

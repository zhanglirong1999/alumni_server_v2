package cn.edu.seu.alumni_server.service;

import cn.edu.seu.alumni_server.dao.mapper.CommentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentService {
    @Autowired
    private CommentMapper commentDao;




}

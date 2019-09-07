package cn.edu.seu.alumni_server.controller;

import cn.edu.seu.alumni_server.common.dto.WebResponse;
import cn.edu.seu.alumni_server.dao.mapper.MyCollectionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class MyCollectionController {

    @Autowired
    HttpServletRequest request;

    @Autowired
    MyCollectionMapper myCollectionMapper;

//    @GetMapping("/collection")
//    public WebResponse getCollection(@RequestParam int pageIndex,
//                                     @RequestParam int pageSize){
//        Long accountId = (Long) request.getAttribute("accountId");
//
//
//    }

}

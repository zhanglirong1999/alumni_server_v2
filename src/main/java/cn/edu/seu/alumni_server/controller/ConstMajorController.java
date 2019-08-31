package cn.edu.seu.alumni_server.controller;

import cn.edu.seu.alumni_server.common.dto.WebResponse;
import cn.edu.seu.alumni_server.common.token.Acl;
import cn.edu.seu.alumni_server.controller.dto.ConstMajorDTO;
import cn.edu.seu.alumni_server.dao.entity.ConstMajor;
import cn.edu.seu.alumni_server.dao.mapper.ConstMajorMapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
public class ConstMajorController {
    @Autowired
    private ConstMajorMapper constMajorDao;

    @GetMapping("/queryConstMajor")
    public WebResponse queryConstMajor(@RequestParam String majorName,
                                       @RequestParam int pageSize,
                                       @RequestParam int pageIndex){
        System.out.println("进入查询专业函数");
        PageHelper.startPage(pageIndex, pageSize);
        List<ConstMajor> list = constMajorDao.queryConstMajorList(majorName);
        return new WebResponse().success(wrapPageInfo(list));
    }

    private PageInfo<ConstMajorDTO> wrapPageInfo(List<ConstMajor> list) {
        Page<ConstMajorDTO> resultList = new Page<>();
        for(ConstMajor constMajor:list) {
            try {
                ConstMajorDTO constMajorDTO = new ConstMajorDTO();
                BeanUtils.copyProperties(constMajor, constMajorDTO);
                resultList.add(constMajorDTO);
            }
            catch(Exception e){
                throw new RuntimeException(e);
            }
        }
        if(list instanceof Page) {
            Page page = (Page) list;
            resultList.setPageNum(page.getPageNum());
            resultList.setPageSize(page.getPageSize());
            resultList.setStartRow(page.getStartRow());
            resultList.setEndRow(page.getEndRow());
            resultList.setTotal(page.getTotal());
            resultList.setPages(page.getPages());
            resultList.setCount(page.isCount());
            resultList.setOrderBy(page.getOrderBy());
            resultList.setOrderByOnly(page.isOrderByOnly());
            resultList.reasonable(page.getReasonable());
            resultList.pageSizeZero(page.getPageSizeZero());
        }
        PageInfo<ConstMajorDTO> pageInfo = new PageInfo<>(resultList);
        return pageInfo;
    }
}

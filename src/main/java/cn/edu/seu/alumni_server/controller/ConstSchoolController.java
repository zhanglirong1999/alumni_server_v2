package cn.edu.seu.alumni_server.controller;

import cn.edu.seu.alumni_server.common.dto.WebResponse;
import cn.edu.seu.alumni_server.common.token.Acl;

import cn.edu.seu.alumni_server.controller.dto.ConstSchoolDTO;
import cn.edu.seu.alumni_server.dao.entity.ConstSchool;
import cn.edu.seu.alumni_server.dao.mapper.ConstSchoolMapper;
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
public class ConstSchoolController {

    @Autowired
    ConstSchoolMapper constSchoolDao;

    @GetMapping("/queryConstSchool")
    public WebResponse queryConstSchool(@RequestParam String schoolName,
                                        @RequestParam int pageSize,
                                        @RequestParam int pageIndex) {
        PageHelper.startPage(pageIndex, pageSize);
        List<ConstSchool> list = constSchoolDao.queryConstSchoolList(schoolName);
        return new WebResponse().success(wrapPageInfo(list));
    }

    private PageInfo<ConstSchoolDTO> wrapPageInfo(List<ConstSchool> list) {
        Page<ConstSchoolDTO> resultList = new Page<>();
        for(ConstSchool constMajor:list) {
            try {
                ConstSchoolDTO constMajorDTO = new ConstSchoolDTO();
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
        PageInfo<ConstSchoolDTO> pageInfo = new PageInfo<>(resultList);
        return pageInfo;
    }
}

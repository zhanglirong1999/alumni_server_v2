package cn.edu.seu.alumni_server.controller;

import cn.edu.seu.alumni_server.common.logAspect.annotation.LogController;
import cn.edu.seu.alumni_server.common.web_response_dto.WebResponse;
import cn.edu.seu.alumni_server.controller.dto.BannerDTO;
import cn.edu.seu.alumni_server.dao.entity.Banner;
import cn.edu.seu.alumni_server.dao.mapper.BannerMapper;
import cn.edu.seu.alumni_server.interceptor.token.Acl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;

@LogController
@RestController
@Acl
public class BannerController {
    @Autowired
    BannerMapper bannerMapper;
    /**
     * 获得有效的banner
     * @return
     */
    @GetMapping("/banner/validList")
    public WebResponse list(){
        // 查询 education 信息

        Example example1 = new Example(Banner.class);
        example1.createCriteria().andEqualTo("validStatus", true);
        List<Banner> banners = bannerMapper.selectByExample(example1);
        List<BannerDTO> bannerDTOS = transform(banners);
        return new WebResponse().success(bannerDTOS);
    }

    private List<BannerDTO> transform(List<Banner> banners){
        List<BannerDTO> result = new ArrayList<>();
        for (Banner x : banners){
            BannerDTO bannerDTO = new BannerDTO(x.getBannerId(),x.getImg(),x.getLink());
            result.add(bannerDTO);
        }
        return  result;
    }
}

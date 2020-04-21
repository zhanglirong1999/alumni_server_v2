package cn.edu.seu.alumni_server.controller;

import cn.edu.seu.alumni_server.common.web_response_dto.WebResponse;
import cn.edu.seu.alumni_server.controller.dto.DemandCreateDTO;
import cn.edu.seu.alumni_server.controller.dto.DemandDetailDTO;
import cn.edu.seu.alumni_server.controller.dto.DemandListDTO;
import cn.edu.seu.alumni_server.controller.dto.PageResult;
import cn.edu.seu.alumni_server.dao.entity.Demand;
import cn.edu.seu.alumni_server.interceptor.token.Acl;
import cn.edu.seu.alumni_server.service.DemandService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@Acl
public class DemandController {
    @Autowired
    DemandService demandService;


    @Autowired
    HttpServletRequest request;
//
//    @PostMapping("/demand/create")
//    public WebResponse create(
//            @RequestParam String demandName,
//            @RequestParam String type,
//            @RequestParam String tags,
//            @RequestParam(required = false) String img1,
//            @RequestParam(required = false) String img2,
//            @RequestParam(required = false) String img3,
//            @RequestParam(required = false) String img4,
//            @RequestParam(required = false) String img5,
//            @RequestParam(required = false) String img6,
//            @RequestParam String details
//    ){
//        DemandCreateDTO
//    }

    @PostMapping("/demand/create")
    public WebResponse create1(@RequestBody DemandCreateDTO demandCreateDTO){
        try {
            Demand demand = demandService.checkInputtedDemandForCreate(demandCreateDTO);
            demand.setAccountId((Long) request.getAttribute("accountId"));
            demandService.insertDemand(demand);
            return new WebResponse<>().success("创建需求成功");
        } catch (Exception e) {
            return new WebResponse().fail(e.getMessage());
        }

    }

    @GetMapping("/demand/detail")
    public WebResponse detail(
            @RequestParam String demandId
    ){
        try {
            DemandDetailDTO detailDto = demandService.queryDetailDemandByDemandId(demandId);
            return new WebResponse().success(detailDto);
        } catch (Exception e) {
            return new WebResponse().fail(e.getMessage());
        }

    }

    @GetMapping("/demand/list")
    public WebResponse list(
            @RequestParam String type,
            @RequestParam(required = false) String accountName,
            @RequestParam String pageIndex,
            @RequestParam String pageSize
    ){
        try{
            PageHelper.startPage(Integer.parseInt(pageIndex), Integer.parseInt(pageSize));
            PageHelper.orderBy("DMD.c_time DESC,demand_id DESC");
            List<DemandListDTO> demandListDTOList = demandService.queryDemandList(type,accountName);
            return new WebResponse().success(new PageResult<>(((Page) demandListDTOList).getTotal(), demandListDTOList));
        }catch (Exception e){
            return new WebResponse().fail(e.getMessage());
        }
    }

}

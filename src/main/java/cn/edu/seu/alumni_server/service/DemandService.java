package cn.edu.seu.alumni_server.service;


import cn.edu.seu.alumni_server.controller.dto.DemandCreateDTO;
import cn.edu.seu.alumni_server.controller.dto.DemandDetailDTO;
import cn.edu.seu.alumni_server.controller.dto.DemandListDTO;
import cn.edu.seu.alumni_server.dao.entity.Demand;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface DemandService {
    Demand checkInputtedDemandForCreate (DemandCreateDTO demandCreateDTO) throws Exception;
    void insertDemand(Demand demand);
    DemandDetailDTO queryDetailDemandByDemandId (String demandId) throws Exception;
    List<DemandListDTO> queryDemandList(String type,String name);
}

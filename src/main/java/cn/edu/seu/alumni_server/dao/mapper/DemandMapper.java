package cn.edu.seu.alumni_server.dao.mapper;

import cn.edu.seu.alumni_server.controller.dto.DemandDetailDTO;
import cn.edu.seu.alumni_server.controller.dto.DemandListDTO;
import cn.edu.seu.alumni_server.dao.entity.Demand;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Repository
public interface DemandMapper extends Mapper<Demand> {
    DemandDetailDTO selectDetailDemandByDemandId(String demandId);
    List<DemandListDTO> queryDemandList(@Param("type") String t,@Param("name")String name);
}

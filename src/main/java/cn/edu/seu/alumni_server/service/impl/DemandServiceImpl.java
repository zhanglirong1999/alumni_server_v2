package cn.edu.seu.alumni_server.service.impl;

import cn.edu.seu.alumni_server.common.Utils;
import cn.edu.seu.alumni_server.controller.dto.DemandCreateDTO;
import cn.edu.seu.alumni_server.controller.dto.DemandDetailDTO;
import cn.edu.seu.alumni_server.controller.dto.DemandListDTO;
import cn.edu.seu.alumni_server.dao.entity.Demand;
import cn.edu.seu.alumni_server.dao.mapper.DemandMapper;
import cn.edu.seu.alumni_server.service.DemandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

import static cn.edu.seu.alumni_server.common.CONST.ALL_DEMAND_TYPE;

@Service
public class DemandServiceImpl implements DemandService {

    @Autowired
    DemandMapper demandMapper;

    @Override
    public Demand checkInputtedDemandForCreate(DemandCreateDTO demandCreateDTO) throws Exception {
        //检查需求名称是否小于15个字符
        if(demandCreateDTO.getDemandName().length() > 15){
            throw new Exception("需求名称需要不大于15字符");
        }

        //检查需求详情是否小于500字符
        if(demandCreateDTO.getDetails().length() > 500){
            throw new Exception("需求详细需要不大于500字符");
        }

        //检查每一个标签的字数是不是小于等于五个字符，并且标签个数要小于等于5
        String temp[] = demandCreateDTO.getTags().split("，|,");
        if(temp.length > 5)
            throw new Exception("标签个数应不大于5");
        for(String x : temp){
            if(x.length() > 5){
                throw new  Exception(x + "：标签长度需要不大于5个字符");
            }
        }
        return demandCreateDto2Demand(demandCreateDTO);
    }


    @Override
    public void insertDemand(Demand demand) {
        demandMapper.insertSelective(demand);
    }

    @Override
    public DemandDetailDTO queryDetailDemandByDemandId(String demandId) throws Exception {
        if (demandId == null || demandId.equals("")) {
            throw new Exception("查询需求详情：需求id不能为空");
        }
        DemandDetailDTO detailDTO = demandMapper.selectDetailDemandByDemandId(demandId);
        detailDTO.calculateStarterEducationGrade();
        return detailDTO;
    }

    @Override
    public List<DemandListDTO> queryDemandList(String t, String name) {
        String type = null;
        if (!t.equals(ALL_DEMAND_TYPE))
            type = t;
        List<DemandListDTO> demandListDTOList = demandMapper.queryDemandList(type,name);
        for (DemandListDTO x: demandListDTOList){
            x.calculateStarterEducationGrade();
        }
        return demandListDTOList;
    }

    private Demand demandCreateDto2Demand(DemandCreateDTO demandCreateDTO){
        Demand result = new Demand();
        //此处返回的demand没有包含demandId
        result.setDemandId(Utils.generateId());
        result.setDemandName(demandCreateDTO.getDemandName());
        result.setType(demandCreateDTO.getType());
        //设置标签
        String temp[] = new String[5];
        String temp1[] = demandCreateDTO.getTags().split("[,，]");
        int min = Math.min(5,temp1.length);
        System.arraycopy(temp1, 0, temp, 0, min);
        result.setTag1(temp[0]);
        result.setTag2(temp[1]);
        result.setTag3(temp[2]);
        result.setTag4(temp[3]);
        result.setTag5(temp[4]);

        result.setDetails(demandCreateDTO.getDetails());
        result.setImg1(demandCreateDTO.getImg1());
        result.setImg2(demandCreateDTO.getImg2());
        result.setImg3(demandCreateDTO.getImg3());
        result.setImg4(demandCreateDTO.getImg4());
        result.setImg5(demandCreateDTO.getImg5());
        result.setImg6(demandCreateDTO.getImg6());

        result.setValidStatus(true);
        return result;
    }
}

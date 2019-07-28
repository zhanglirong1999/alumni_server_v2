package cn.edu.seu.alumni_server.dao.mapper;

import cn.edu.seu.alumni_server.controller.dto.BriefInfo;

import java.util.List;

public interface V2ApiMapper {
    List<BriefInfo> searchByName(String content);

    List<BriefInfo> searchBySchool(String content);

    List<BriefInfo> searchByCollege(String content);

    List<BriefInfo> searchByCity(String content);

    List<BriefInfo> searchByCompany(String content);

    List<BriefInfo> searchByPosition(String content);

    List<BriefInfo> searchBySelfDesc(String content);
}

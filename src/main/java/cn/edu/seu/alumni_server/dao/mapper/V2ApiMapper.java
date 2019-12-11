package cn.edu.seu.alumni_server.dao.mapper;

import cn.edu.seu.alumni_server.controller.dto.BriefInfo;
import cn.edu.seu.alumni_server.dao.entity.Account;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface V2ApiMapper {
    List<BriefInfo> searchByName(String content);

    List<BriefInfo> searchBySchool(String content);

    List<BriefInfo> searchByCollege(String content);

    List<BriefInfo> searchByCity(String content);

    List<BriefInfo> searchByCompany(String content);

    List<BriefInfo> searchByPosition(String content);

    List<BriefInfo> searchBySelfDesc(String content);

    List<Account> test(long accountId);

    List<BriefInfo> recommand(BriefInfo briefInfo);

    List<BriefInfo> recommandWithFilter(BriefInfo briefInfo, String filter);

    BriefInfo getBriefInfo();
}

package cn.edu.seu.alumni_server.dao.mapper;

import cn.edu.seu.alumni_server.controller.dto.BriefInfo;
import cn.edu.seu.alumni_server.controller.dto.FriendDTO;
import cn.edu.seu.alumni_server.dao.entity.Account;
import cn.edu.seu.alumni_server.dao.entity.Friend;

import java.util.List;

public interface V2ApiMapper {
    List<BriefInfo> searchByName(String content);

    List<BriefInfo> searchBySchool(String content);

    List<BriefInfo> searchByCollege(String content);

    List<BriefInfo> searchByCity(String content);

    List<BriefInfo> searchByCompany(String content);

    List<BriefInfo> searchByPosition(String content);

    List<BriefInfo> searchBySelfDesc(String content);

    List<FriendDTO> getFriends(Long accountId);

    Friend getRelationShip(long myAccountId, long accountId);

    List<Account> test(long accountId);

    List<BriefInfo> recommand(BriefInfo briefInfo);
    BriefInfo getBriefInfo ();
}

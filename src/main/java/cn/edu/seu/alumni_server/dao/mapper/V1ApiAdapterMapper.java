package cn.edu.seu.alumni_server.dao.mapper;

import cn.edu.seu.alumni_server.controller.dto.*;

import java.util.List;

public interface V1ApiAdapterMapper {
    WorkDTO checkExsist(String openid);

    List<EducationDTO> search(String content, int page, int limit);
//    List<EducationDTO> searchdirect(String content);

    List<UserDTO> getUser(String openid);

    List<UserInfoDTO> getUserInfos(String openid);

    List<FriendDTO> getFriends(String openid);

//    upsertBaseInfo();

    List<WorkDTO> getWorks(String openid);

    List<EducationDTO> getEducations(String openid);

    boolean deletework(String openid, String id);

    boolean deleteEducation(String openid, String id);

    UserInfoDTO getCardInfo(String friendOpenId);
}

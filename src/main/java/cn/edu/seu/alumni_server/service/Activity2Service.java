package cn.edu.seu.alumni_server.service;

import cn.edu.seu.alumni_server.controller.dto.AddActivity;
import org.springframework.web.multipart.MultipartFile;

public interface Activity2Service {
    void addActivity(AddActivity addActivity, Long accountId);
    String uploadFile(MultipartFile file);
    Object getActivityDetail(Long activityId,Long accountId);
    Object getList(int pageIndex,int pageSize,int tags) throws Exception;
    void deleteActivity(Long id,Long accountId) throws Exception;
    void joinActivity(Long id,Long accountId);
    Object getPeopleList(Long id,int pageIndex,int pageSize);
    void cancelActivity(Long id ,Long accountId) throws Exception;
}

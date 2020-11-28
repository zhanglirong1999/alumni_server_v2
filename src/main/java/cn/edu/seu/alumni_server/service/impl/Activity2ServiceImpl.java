package cn.edu.seu.alumni_server.service.impl;

import cn.edu.seu.alumni_server.controller.dto.AddActivity;
import cn.edu.seu.alumni_server.dao.entity.Account;
import cn.edu.seu.alumni_server.dao.entity.Activity2;
import cn.edu.seu.alumni_server.dao.entity.ActivityImg;
import cn.edu.seu.alumni_server.dao.entity.ActivityMember;
import cn.edu.seu.alumni_server.dao.mapper.AccountMapper;
import cn.edu.seu.alumni_server.dao.mapper.Activity2Mapper;
import cn.edu.seu.alumni_server.dao.mapper.ActivityImgMapper;
import cn.edu.seu.alumni_server.dao.mapper.ActivityMemberMapper;
import cn.edu.seu.alumni_server.service.Activity2Service;
import cn.edu.seu.alumni_server.service.QCloudFileManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import cn.edu.seu.alumni_server.common.Utils;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.Sqls;

import java.io.IOException;
import java.util.*;

@Service
public class Activity2ServiceImpl implements Activity2Service {

    @Autowired
    private QCloudFileManager qCloudFileManager;

    @Autowired
    private Activity2Mapper activity2Mapper;

    @Autowired
    private ActivityImgMapper activityImgMapper;

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private ActivityMemberMapper activityMemberMapper;

    @Override
    public void addActivity(AddActivity addActivity, Long accountId) {
        String title=addActivity.getTitle();
        String description=addActivity.getDescription();
        String time=addActivity.getTime();
        String expiration=addActivity.getExpiration();
        String type=addActivity.getType();
        String location=addActivity.getLocation();
        String cost=addActivity.getCost();
        Integer visible=addActivity.getVisible();
        String tag=addActivity.getTag();
        MultipartFile[] imgs=addActivity.getImg();

        Long aid = Utils.generateId();
        Activity2 activity = new Activity2();
        activity.setAccountId(accountId);
        activity.setId(aid);
        activity.setTitle(title);
        activity.setDescription(description);
        activity.setTime(time);
        activity.setExpirationTime(expiration);
        activity.setType(type);
        activity.setLocation(location);
        activity.setCost(cost);
        activity.setVisible(visible);
        activity.setTag(tag);
        activity2Mapper.insert(activity);
        if(imgs!=null) {
            for (int i = 0; i < imgs.length; i++) {
                String img = uploadFile(imgs[i]);
                ActivityImg activityImg = new ActivityImg();
                activityImg.setAid(aid);
                activityImg.setImg(img);
                activityImgMapper.insert(activityImg);
            }
        }

    }

    @Override
    public String uploadFile(MultipartFile file) {
        // 首先获取 newName
        String newNameWithoutType = String.valueOf(Utils.generateId());
        String newNameWithType = this.qCloudFileManager.buildNewFileNameWithType(
                file, newNameWithoutType
        );
        String ansUrl = null;
        try {
            ansUrl = qCloudFileManager.uploadOneFile(
                    file,
                    newNameWithoutType
            );
        } catch ( IOException e) {
            return "上传文件失败";
        }
        // 要删除文件
        Utils.deleteFileUnderProjectDir(newNameWithType);
        // 返回最终结果
        return ansUrl;
    }

    @Override
    public Object getActivityDetail(Long id) {
        Activity2 activity = activity2Mapper.selectOneByExample(
                Example.builder(Activity2.class).where(Sqls.custom().andEqualTo("id", id))
                        .build()
        );
        String title = activity.getTitle();
        String type = activity.getType();
        String time = activity.getTime();
        String expiration = activity.getExpirationTime();
        String description = activity.getDescription();
        String location = activity.getLocation();
        String cost = activity.getCost();
        String tag = activity.getTag();
        String name = accountMapper.selectOneByExample(
                Example.builder(Account.class).where(Sqls.custom().andEqualTo("accountId", activity.getAccountId()))
                        .build()
        ).getName();
        Integer visible = activity.getVisible();
        Iterator<ActivityImg> activityImgList = activityImgMapper.selectByExample(
                Example.builder(ActivityImg.class).where(Sqls.custom().andEqualTo("aid", id))
                        .build()
        ).iterator();
        List<String> imgs = new LinkedList<>();
        while (activityImgList.hasNext()){
            ActivityImg activityImg = activityImgList.next();
            imgs.add(activityImg.getImg());
        }
        Map<String,Object> map = new HashMap<>();
        map.put("id",id);
        map.put("title",title);
        map.put("type",type);
        map.put("time",time);
        map.put("expiration",expiration);
        map.put("description",description);
        map.put("location",location);
        map.put("cost",cost);
        map.put("tag",tag);
        map.put("visible",visible);
        map.put("img",imgs);
        map.put("name",name);
        Integer count = activityMemberMapper.selectByExample(
                Example.builder(ActivityMember.class).where(Sqls.custom().andEqualTo("activityId", id))
                        .build()
        ).size();
        map.put("count",count);
        return map;
    }

    @Override
    public Object getList() {
        Iterator<Activity2> iterator = activity2Mapper.selectByExample(
                Example.builder(Activity2.class).where(Sqls.custom().andEqualTo("visible", 1))
                        .build()
        ).iterator();
        List list = new LinkedList();
        while (iterator.hasNext()){
            Activity2 activity = iterator.next();
            Map<String,Object> map = new HashMap<>();
            String title = activity.getTitle();
            String type = activity.getType();
            String time = activity.getTime();
            String expiration = activity.getExpirationTime();
            String description = activity.getDescription();
            String location = activity.getLocation();
            String cost = activity.getCost();
            String tag = activity.getTag();
            String name = accountMapper.selectOneByExample(
                    Example.builder(Account.class).where(Sqls.custom().andEqualTo("accountId", activity.getAccountId()))
                            .build()
            ).getName();
            map.put("id",activity.getId());
            map.put("title",title);
            map.put("type",type);
            map.put("time",time);
            map.put("expiration",expiration);
            map.put("description",description);
            map.put("location",location);
            map.put("cost",cost);
            map.put("tag",tag);
            map.put("name",name);
            map.put("distance",0);
            list.add(map);
        }

        return list;
    }

    @Override
    public void deleteActivity(Long id,Long accountId) throws Exception {
        Activity2 activity = activity2Mapper.selectOneByExample(
                Example.builder(Activity2.class).where(Sqls.custom().andEqualTo("id", id))
                        .build()
        );
        if(!accountId.equals(activity.getAccountId())){
            throw new Exception("权限不足");
        }
        activity2Mapper.deleteActivity(id);
    }

    @Override
    public void joinActivity(Long id, Long accountId) {
        ActivityMember activityMember = new ActivityMember();
        activityMember.setAccountId(accountId);
        activityMember.setActivityId(id);
        activityMember.setIsAvailable(true);
        activityMember.setReadStatus(false);
        activityMemberMapper.insert(activityMember);
    }

    @Override
    public Object getPeopleList(Long id) {
        Iterator<ActivityMember> iterator = activityMemberMapper.selectByExample(
                Example.builder(ActivityMember.class).where(Sqls.custom().andEqualTo("activityId", id))
                        .build()
        ).iterator();
        List list = new LinkedList();
        while (iterator.hasNext()){
            ActivityMember activityMember = iterator.next();
            Map<String ,Object> map = new HashMap<>();
            map.put("account_id",activityMember.getAccountId());
            String name = accountMapper.selectOneByExample(
                    Example.builder(Account.class).where(Sqls.custom().andEqualTo("accountId", activityMember.getAccountId()))
                            .build()
            ).getName();
            map.put("name",name);
            list.add(map);
        }
        return list;
    }


}

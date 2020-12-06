package cn.edu.seu.alumni_server.service.impl;

import cn.edu.seu.alumni_server.controller.dto.AddActivity;
import cn.edu.seu.alumni_server.dao.entity.*;
import cn.edu.seu.alumni_server.dao.mapper.*;
import cn.edu.seu.alumni_server.service.Activity2Service;
import cn.edu.seu.alumni_server.service.ActivityMemberService;
import cn.edu.seu.alumni_server.service.QCloudFileManager;
import com.github.pagehelper.PageHelper;
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
    private EducationMapper educationMapper;

    @Autowired
    private ActivityMemberMapper activityMemberMapper;

    @Autowired
    private ActivityMemberService activityMemberService;
    private String accountStartEducationGrade;
    private long accountStartEducationYear;

    public void calculateStarterEducationGrade() {
        System.out.println(accountStartEducationYear);
        Date date = new Date(accountStartEducationYear);
        System.out.println(date);
        if (date.getTime() <= 0) {
            this.accountStartEducationGrade = null;
        } else {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            Integer y = calendar.get(Calendar.YEAR);
            String sy = String.valueOf(y);
            if (sy.length() != 4) {
                accountStartEducationGrade = sy;
            } else {
                accountStartEducationGrade = sy.substring(2) + "级";
            }
        }
    }

    @Override
    public void addActivity(AddActivity addActivity, Long accountId) {
        String title=addActivity.getTitle();
        String description=addActivity.getDescription();
        String time=addActivity.getTime();
        String expiration=addActivity.getExpiration();
        String type=addActivity.getType();
        String location=addActivity.getLocation();
        String cost=addActivity.getCost();
        Integer visible;
        if (addActivity.getVisible()==null)
        {
            visible=1;
        }else {
            visible=addActivity.getVisible();
        }
        String tag=addActivity.getTag();
        String[] imgs=addActivity.getImg();

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
                String img = imgs[i];
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
    public Object getActivityDetail(Long id,Long accountId) {
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
        Account account = accountMapper.selectOneByExample(
                Example.builder(Account.class).where(Sqls.custom().andEqualTo("accountId", activity.getAccountId()))
                        .build()
        );
        Education education = educationMapper.selectOneByExample(
                Example.builder(Education.class).where(Sqls.custom().andEqualTo("accountId", activity.getAccountId()))
                        .build()
        );
        String name = account.getName();
        String avatar = account.getAvatar();
        String educationLevel = education.getEducation();
        String educationCollege = education.getCollege();
        String educationSchool = education.getSchool();
        accountStartEducationYear= education.getStartTime();
        calculateStarterEducationGrade();
        String educationGrade = accountStartEducationGrade;
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
        map.put("avatar",avatar);
        map.put("educationLevel",educationLevel);
        map.put("educationCollege",educationCollege);
        map.put("educationSchool",educationSchool);
        map.put("educationGrade",educationGrade);
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
        Boolean hasEnrolled = this.activityMemberService.hasEnrolledInto2(
                id, accountId
        );
        map.put("hasEnrolled",hasEnrolled);
        return map;
    }

    @Override
    public Object getList(int pageIndex,int pageSize,int tags) throws Exception {
        Map<String, Object> result = new HashMap<>();
        if(tags==3) {
            PageHelper.startPage(pageIndex, pageSize);
            Iterator<Activity2> iterator = activity2Mapper.selectByExample(
                    Example.builder(Activity2.class).where(Sqls.custom().andEqualTo("visible", 1))
                            .build()
            ).iterator();
            List list = new LinkedList();
            while (iterator.hasNext()) {
                Activity2 activity = iterator.next();
                Map<String, Object> map = new HashMap<>();
                String title = activity.getTitle();
                String type = activity.getType();
                String time = activity.getTime();
                String expiration = activity.getExpirationTime();
                String description = activity.getDescription();
                String location = activity.getLocation();
                String cost = activity.getCost();
                String tag = activity.getTag();
                Account account = accountMapper.selectOneByExample(
                        Example.builder(Account.class).where(Sqls.custom().andEqualTo("accountId", activity.getAccountId()))
                                .build()
                );
                Education education = educationMapper.selectOneByExample(
                        Example.builder(Education.class).where(Sqls.custom().andEqualTo("accountId", activity.getAccountId()))
                                .build()
                );
                String name = account.getName();
                String avatar = account.getAvatar();
                String educationLevel = education.getEducation();
                String educationCollege = education.getCollege();
                String educationSchool = education.getSchool();
                accountStartEducationYear = education.getStartTime();
                calculateStarterEducationGrade();
                String educationGrade = accountStartEducationGrade;
                map.put("avatar", avatar);
                map.put("educationLevel", educationLevel);
                map.put("educationCollege", educationCollege);
                map.put("educationSchool", educationSchool);
                map.put("educationGrade", educationGrade);
                map.put("id", activity.getId());
                map.put("title", title);
                map.put("type", type);
                map.put("time", time);
                map.put("expiration", expiration);
                map.put("description", description);
                map.put("location", location);
                map.put("cost", cost);
                map.put("tag", tag);
                map.put("name", name);
                map.put("distance", 0);
                list.add(map);
            }
            Integer count = activity2Mapper.selectCountByExample(
                    Example.builder(Activity2.class).where(Sqls.custom().andEqualTo("visible", 1))
                            .build()
            );
            result.put("list", list);
            result.put("count", count);
        }else if(tags==0||tags==1||tags==2){
            PageHelper.startPage(pageIndex, pageSize);
            Iterator<Activity2> iterator = activity2Mapper.selectByExample(
                    Example.builder(Activity2.class).where(Sqls.custom().andEqualTo("visible", 1)
                    .andEqualTo("type",tags))
                            .build()
            ).iterator();
            List list = new LinkedList();
            while (iterator.hasNext()) {
                Activity2 activity = iterator.next();
                Map<String, Object> map = new HashMap<>();
                String title = activity.getTitle();
                String type = activity.getType();
                String time = activity.getTime();
                String expiration = activity.getExpirationTime();
                String description = activity.getDescription();
                String location = activity.getLocation();
                String cost = activity.getCost();
                String tag = activity.getTag();
                Account account = accountMapper.selectOneByExample(
                        Example.builder(Account.class).where(Sqls.custom().andEqualTo("accountId", activity.getAccountId()))
                                .build()
                );
                Education education = educationMapper.selectOneByExample(
                        Example.builder(Education.class).where(Sqls.custom().andEqualTo("accountId", activity.getAccountId()))
                                .build()
                );
                String name = account.getName();
                String avatar = account.getAvatar();
                String educationLevel = education.getEducation();
                String educationCollege = education.getCollege();
                String educationSchool = education.getSchool();
                accountStartEducationYear = education.getStartTime();
                calculateStarterEducationGrade();
                String educationGrade = accountStartEducationGrade;
                map.put("avatar", avatar);
                map.put("educationLevel", educationLevel);
                map.put("educationCollege", educationCollege);
                map.put("educationSchool", educationSchool);
                map.put("educationGrade", educationGrade);
                map.put("id", activity.getId());
                map.put("title", title);
                map.put("type", type);
                map.put("time", time);
                map.put("expiration", expiration);
                map.put("description", description);
                map.put("location", location);
                map.put("cost", cost);
                map.put("tag", tag);
                map.put("name", name);
                map.put("distance", 0);
                list.add(map);
            }
            Integer count = activity2Mapper.selectCountByExample(
                    Example.builder(Activity2.class).where(Sqls.custom().andEqualTo("visible", 1)
                            .andEqualTo("type",tags))
                            .build()
            );
            result.put("list", list);
            result.put("count", count);
        }else {
            throw new Exception("类型错误");
        }
        return result;

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
    public Object getPeopleList(Long id,int pageIndex,int pageSize) {
        PageHelper.startPage(pageIndex, pageSize);
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

    @Override
    public void cancelActivity(Long id, Long accountId) throws Exception {
        Integer count = activity2Mapper.getActivityMember(accountId,id);
        if(count==0)
        {
            throw new Exception("未加入活动");
        }
        activity2Mapper.deleteActivityMember(accountId,id);
    }


}

package cn.edu.seu.alumni_server.controller.dto;

import cn.edu.seu.alumni_server.dao.entity.Account;
import cn.edu.seu.alumni_server.dao.entity.Education;
import cn.edu.seu.alumni_server.dao.entity.Friend;
import cn.edu.seu.alumni_server.dao.entity.Job;
import lombok.Data;

import java.util.List;

@Data
public class AccountAllDTO {
    private Account account;
    private List<Education> educations;
    private List<Job> jobs;
    private List<Friend> friends;
}

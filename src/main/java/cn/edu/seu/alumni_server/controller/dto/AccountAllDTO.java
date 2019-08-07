package cn.edu.seu.alumni_server.controller.dto;

import cn.edu.seu.alumni_server.dao.entity.Account;
import cn.edu.seu.alumni_server.dao.entity.Education;
import cn.edu.seu.alumni_server.dao.entity.Friend;
import cn.edu.seu.alumni_server.dao.entity.Job;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class AccountAllDTO {
    @NotNull
    private Account account;
    @NotNull
    private List<Education> educations;
    @NotNull
    private List<Job> jobs;

    private List<Friend> friends;
}

package cn.edu.seu.alumni_server.controller.dto;

import cn.edu.seu.alumni_server.dao.entity.Job;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.sql.Timestamp;

@Data
public class JobDTO {
    private Long accountId;
    private Long jobId;
    private String company;
    private String position;
    private long startTime;
    private long endTime;

    public JobDTO() {
    }

    public JobDTO(Job job) {
        BeanUtils.copyProperties(job, this);
        this.setStartTime(job.getStartTime().getTime());
        this.setEndTime(job.getEndTime().getTime());
    }

    public Job toJob() {
        Job job = new Job();
        BeanUtils.copyProperties(this, job);
        job.setStartTime(new Timestamp(this.getStartTime()));
        job.setEndTime(new Timestamp(this.getEndTime()));
        return job;
    }
}
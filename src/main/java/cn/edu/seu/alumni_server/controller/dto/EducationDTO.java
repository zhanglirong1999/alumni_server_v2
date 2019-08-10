package cn.edu.seu.alumni_server.controller.dto;

import cn.edu.seu.alumni_server.dao.entity.Education;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.sql.Timestamp;

@Data
public class EducationDTO {
    private Long accountId;
    private Long educationId;
    private String education;
    private String school;
    private String college;
    private long startTime;
    private long endTime;

    public EducationDTO() {
    }

    public EducationDTO(Education education) {
        BeanUtils.copyProperties(education, this);
        this.setStartTime(education.getStartTime().getTime());
        this.setEndTime(education.getEndTime().getTime());
    }

    public Education toEducation() {
        Education education = new Education();
        BeanUtils.copyProperties(this, education);
        education.setStartTime(new Timestamp(this.getStartTime()));
        education.setEndTime(new Timestamp(this.getEndTime()));
        return education;
    }
}

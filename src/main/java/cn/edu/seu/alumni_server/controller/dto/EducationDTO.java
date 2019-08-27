package cn.edu.seu.alumni_server.controller.dto;

import cn.edu.seu.alumni_server.dao.entity.Education;
import lombok.Data;
import org.springframework.beans.BeanUtils;

@Data
public class EducationDTO {
    private Long accountId;
    private Long educationId;
    private String education;
    private String school;
    private String college;
    private Long startTime;
    private Long endTime;

    public EducationDTO() {
    }

    public EducationDTO(Education education) {
        BeanUtils.copyProperties(education, this);
        this.setStartTime(education.getStartTime());
        this.setEndTime(education.getEndTime());
    }

    public Education toEducation() {
        Education education = new Education();
        BeanUtils.copyProperties(this, education);
        education.setStartTime(this.getStartTime());
        education.setEndTime(this.getEndTime());
        return education;
    }
}

package cn.edu.seu.alumni_server.controller.dto;

import cn.edu.seu.alumni_server.dao.entity.Education;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.Date;

@Data
public class EducationDTO {
    private Long accountId;
    private Long educationId;
    private String education;
    private String school;
    private String college;
    private Date startTime;
    private Date endTime;
    private boolean validStatus;
    public EducationDTO() {
    }
    public EducationDTO(Education education) {
        BeanUtils.copyProperties(education, this);
    }

    public Education toEducation() {
        Education education = new Education();
        BeanUtils.copyProperties(this, education);
        return education;
    }
}

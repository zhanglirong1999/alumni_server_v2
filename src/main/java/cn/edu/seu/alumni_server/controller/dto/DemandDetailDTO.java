package cn.edu.seu.alumni_server.controller.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.Column;
import java.util.Calendar;
import java.util.Date;

@Data
public class DemandDetailDTO {
    private long demandId;
    private String demandName;
    private String type;
    private String tag1;
    private String tag2;
    private String tag3;
    private String tag4;
    private String tag5;
    private String details;
    private String img1;
    private String img2;
    private String img3;
    private String img4;
    private String img5;
    private String img6;
    private String cTime;
    private String accountId;
    private String accountName;
    private String accountAvatar;
    private String accountEducationLevel;
    private String accountEducationSchool;
    private String accountEducationCollege;
    private long accountStartEducationYear;
    private String accountStartEducationGrade;

    public void calculateStarterEducationGrade() {
        Date date = new Date(accountStartEducationYear);
        if (date.getTime() <= 0)
            this.accountStartEducationGrade = null;
        else {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            Integer y = calendar.get(Calendar.YEAR);
            String sy = String.valueOf(y);
            if (sy.length() != 4) accountStartEducationGrade = sy;
            else accountStartEducationGrade = sy.substring(2) + "级";
        }
    }
}

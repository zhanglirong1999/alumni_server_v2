package cn.edu.seu.alumni_server.controller.dto;

import lombok.Data;

import java.util.Calendar;
import java.util.Date;

@Data
public class DemandListDTO {
    private String demandId;
    private String demandName;
    private String tag1;
    private String tag2;
    private String tag3;
    private String tag4;
    private String tag5;
    private String accountId;
    private String type;
    private String accountName;
    private long accountStartEducationYear;
    private String accountEducationCollege;
    private String cTime;
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

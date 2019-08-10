package cn.edu.seu.alumni_server.dao.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

public class Education implements Serializable {
    @Column(name = "account_id")
    private Long accountId;

    @Column(name = "education_id")
    @Id
    private Long educationId;

    private String education;

    private String school;

    private String college;

    @Column(name = "start_time")
    private Date startTime;

    @Column(name = "end_time")
    private Date endTime;

    @Column(name = "c_time", insertable = false)
    private Date cTime;

    /**
     * update
     */
    @Column(name = "u_time", insertable = false)
    private Date uTime;

    @Column(name = "valid_status")
    private Boolean validStatus;

    private static final long serialVersionUID = 1L;

    /**
     * @return account_id
     */
    public Long getAccountId() {
        return accountId;
    }

    /**
     * @param accountId
     */
    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    /**
     * @return education_id
     */
    public Long getEducationId() {
        return educationId;
    }

    /**
     * @param educationId
     */
    public void setEducationId(Long educationId) {
        this.educationId = educationId;
    }

    /**
     * @return education
     */
    public String getEducation() {
        return education;
    }

    /**
     * @param education
     */
    public void setEducation(String education) {
        this.education = education == null ? null : education.trim();
    }

    /**
     * @return school
     */
    public String getSchool() {
        return school;
    }

    /**
     * @param school
     */
    public void setSchool(String school) {
        this.school = school == null ? null : school.trim();
    }

    /**
     * @return college
     */
    public String getCollege() {
        return college;
    }

    /**
     * @param college
     */
    public void setCollege(String college) {
        this.college = college == null ? null : college.trim();
    }

    /**
     * @return start_time
     */
    public Date getStartTime() {
        return startTime;
    }

    /**
     * @param startTime
     */
    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    /**
     * @return end_time
     */
    public Date getEndTime() {
        return endTime;
    }

    /**
     * @param endTime
     */
    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    /**
     * @return c_time
     */
    public Date getcTime() {
        return cTime;
    }

    /**
     * @param cTime
     */
    public void setcTime(Date cTime) {
        this.cTime = cTime;
    }

    /**
     * 获取update
     *
     * @return u_time - update
     */
    public Date getuTime() {
        return uTime;
    }

    /**
     * 设置update
     *
     * @param uTime update
     */
    public void setuTime(Date uTime) {
        this.uTime = uTime;
    }

    /**
     * @return valid_status
     */
    public Boolean getValidStatus() {
        return validStatus;
    }

    /**
     * @param validStatus
     */
    public void setValidStatus(Boolean validStatus) {
        this.validStatus = validStatus;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", accountId=").append(accountId);
        sb.append(", educationId=").append(educationId);
        sb.append(", education=").append(education);
        sb.append(", school=").append(school);
        sb.append(", college=").append(college);
        sb.append(", startTime=").append(startTime);
        sb.append(", endTime=").append(endTime);
        sb.append(", cTime=").append(cTime);
        sb.append(", uTime=").append(uTime);
        sb.append(", validStatus=").append(validStatus);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}
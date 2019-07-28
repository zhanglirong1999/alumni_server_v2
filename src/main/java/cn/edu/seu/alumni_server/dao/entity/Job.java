package cn.edu.seu.alumni_server.dao.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

public class Job implements Serializable {
    @Column(name = "account_id")
    private Long accountId;

    @Column(name = "job_id")
    private Long jobId;

    private String company;

    private String position;

    @Column(name = "start_time")
    private Date startTime;

    @Column(name = "ennd_time")
    private Date enndTime;

    @Column(name = "c_time")
    private Date cTime;

    @Column(name = "u_time")
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
     * @return job_id
     */
    public Long getJobId() {
        return jobId;
    }

    /**
     * @param jobId
     */
    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }

    /**
     * @return company
     */
    public String getCompany() {
        return company;
    }

    /**
     * @param company
     */
    public void setCompany(String company) {
        this.company = company == null ? null : company.trim();
    }

    /**
     * @return position
     */
    public String getPosition() {
        return position;
    }

    /**
     * @param position
     */
    public void setPosition(String position) {
        this.position = position == null ? null : position.trim();
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
     * @return ennd_time
     */
    public Date getEnndTime() {
        return enndTime;
    }

    /**
     * @param enndTime
     */
    public void setEnndTime(Date enndTime) {
        this.enndTime = enndTime;
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
     * @return u_time
     */
    public Date getuTime() {
        return uTime;
    }

    /**
     * @param uTime
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
        sb.append(", jobId=").append(jobId);
        sb.append(", company=").append(company);
        sb.append(", position=").append(position);
        sb.append(", startTime=").append(startTime);
        sb.append(", enndTime=").append(enndTime);
        sb.append(", cTime=").append(cTime);
        sb.append(", uTime=").append(uTime);
        sb.append(", validStatus=").append(validStatus);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}
package cn.edu.seu.alumni_server.dao.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

@Table(name = "my_collection")
public class MyCollection implements Serializable {
    @Column(name = "account_id")
    private Long accountId;

    @Column(name = "resource_id")
    private Long resourceId;

    private Integer status;

    @Column(name = "c_time")
    private Date cTime;

    @Column(name = "u_time")
    private Date uTime;

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
     * @return resource_id
     */
    public Long getResourceId() {
        return resourceId;
    }

    /**
     * @param resourceId
     */
    public void setResourceId(Long resourceId) {
        this.resourceId = resourceId;
    }

    /**
     * @return status
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * @param status
     */
    public void setStatus(Integer status) {
        this.status = status;
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", accountId=").append(accountId);
        sb.append(", resourceId=").append(resourceId);
        sb.append(", status=").append(status);
        sb.append(", cTime=").append(cTime);
        sb.append(", uTime=").append(uTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}
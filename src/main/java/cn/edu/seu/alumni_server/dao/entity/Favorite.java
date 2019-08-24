package cn.edu.seu.alumni_server.dao.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

public class Favorite implements Serializable {
    @Column(name = "account_id")
    private Long accountId;

    @Column(name = "favorite_account_id")
    private Long favoriteAccountId;

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
     * @return favorite_account_id
     */
    public Long getFavoriteAccountId() {
        return favoriteAccountId;
    }

    /**
     * @param favoriteAccountId
     */
    public void setFavoriteAccountId(Long favoriteAccountId) {
        this.favoriteAccountId = favoriteAccountId;
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
        sb.append(", favoriteAccountId=").append(favoriteAccountId);
        sb.append(", status=").append(status);
        sb.append(", cTime=").append(cTime);
        sb.append(", uTime=").append(uTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}
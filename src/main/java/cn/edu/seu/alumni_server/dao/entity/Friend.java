package cn.edu.seu.alumni_server.dao.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

public class Friend implements Serializable {
    @Column(name = "account_id")
    private Long accountId;

    @Column(name = "friend_account_id")
    private Long friendAccountId;

    /**
     *     stranger(0, "路人"),
    applying(2, "A向B发起好友申请，或者A有待处理的B的好友申请"),
    friend(1, "好友"),
    rejected(3, "被拒绝");
     */
    private Integer status;

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
     * @return friend_account_id
     */
    public Long getFriendAccountId() {
        return friendAccountId;
    }

    /**
     * @param friendAccountId
     */
    public void setFriendAccountId(Long friendAccountId) {
        this.friendAccountId = friendAccountId;
    }

    /**
     * 获取    stranger(0, "路人"),
    applying(2, "A向B发起好友申请，或者A有待处理的B的好友申请"),
    friend(1, "好友"),
    rejected(3, "被拒绝");
     *
     * @return status -     stranger(0, "路人"),
    applying(2, "A向B发起好友申请，或者A有待处理的B的好友申请"),
    friend(1, "好友"),
    rejected(3, "被拒绝");
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 设置    stranger(0, "路人"),
    applying(2, "A向B发起好友申请，或者A有待处理的B的好友申请"),
    friend(1, "好友"),
    rejected(3, "被拒绝");
     *
     * @param status     stranger(0, "路人"),
    applying(2, "A向B发起好友申请，或者A有待处理的B的好友申请"),
    friend(1, "好友"),
    rejected(3, "被拒绝");
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
        sb.append(", friendAccountId=").append(friendAccountId);
        sb.append(", status=").append(status);
        sb.append(", cTime=").append(cTime);
        sb.append(", uTime=").append(uTime);
        sb.append(", validStatus=").append(validStatus);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}
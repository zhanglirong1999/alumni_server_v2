package cn.edu.seu.alumni_server.dao.entity;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

public class Message implements Serializable {
    @Column(name = "message_id")
    @Id
    private Long messageId;

    private Integer type;

    private String payload;

    /**
     * 0未读1已读
     */
    private Integer status;

    @Column(name = "c_time", insertable = false)
    private Date cTime;

    @Column(name = "u_time", insertable = false)
    private Date uTime;

    @Column(name = "valid_status", insertable = false)
    private Integer validStatus;

    @Column(name = "from_user")
    private Long fromUser;

    @Column(name = "to_user")
    private Long toUser;

    private static final long serialVersionUID = 1L;

    /**
     * @return message_id
     */
    public Long getMessageId() {
        return messageId;
    }

    /**
     * @param messageId
     */
    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }

    /**
     * @return type
     */
    public Integer getType() {
        return type;
    }

    /**
     * @param type
     */
    public void setType(Integer type) {
        this.type = type;
    }

    /**
     * @return payload
     */
    public String getPayload() {
        return payload;
    }

    /**
     * @param payload
     */
    public void setPayload(String payload) {
        this.payload = payload == null ? null : payload.trim();
    }

    /**
     * 获取0未读1已读
     *
     * @return status - 0未读1已读
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 设置0未读1已读
     *
     * @param status 0未读1已读
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

    /**
     * @return valid_status
     */
    public Integer getValidStatus() {
        return validStatus;
    }

    /**
     * @param validStatus
     */
    public void setValidStatus(Integer validStatus) {
        this.validStatus = validStatus;
    }

    /**
     * @return from
     */
    public Long getFromUser() {
        return fromUser;
    }

    /**
     * @param fromUser
     */
    public void setFromUser(Long fromUser) {
        this.fromUser = fromUser;
    }

    /**
     * @return to
     */
    public Long getToUser() {
        return toUser;
    }

    /**
     * @param toUser
     */
    public void setToUser(Long toUser) {
        this.toUser = toUser;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", messageId=").append(messageId);
        sb.append(", type=").append(type);
        sb.append(", payload=").append(payload);
        sb.append(", status=").append(status);
        sb.append(", cTime=").append(cTime);
        sb.append(", uTime=").append(uTime);
        sb.append(", validStatus=").append(validStatus);
        sb.append(", from=").append(fromUser);
        sb.append(", to=").append(toUser);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}
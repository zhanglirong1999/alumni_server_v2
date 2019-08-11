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

    private Long from;

    private Long to;

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
    public Long getFrom() {
        return from;
    }

    /**
     * @param from
     */
    public void setFrom(Long from) {
        this.from = from;
    }

    /**
     * @return to
     */
    public Long getTo() {
        return to;
    }

    /**
     * @param to
     */
    public void setTo(Long to) {
        this.to = to;
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
        sb.append(", from=").append(from);
        sb.append(", to=").append(to);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}
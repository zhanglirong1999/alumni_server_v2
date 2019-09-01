package cn.edu.seu.alumni_server.dao.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

@Table(name = "history_event")
public class HistoryEvent implements Serializable {
    @Column(name = "event_id")
    private Long eventId;

    @Column(name = "account_id")
    private Long accountId;

    private Date timestamp;

    @Column(name = "event_type")
    private Integer eventType;

    @Column(name = "post_id")
    private Long postId;

    private static final long serialVersionUID = 1L;

    /**
     * @return event_id
     */
    public Long getEventId() {
        return eventId;
    }

    /**
     * @param eventId
     */
    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

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
     * @return timestamp
     */
    public Date getTimestamp() {
        return timestamp;
    }

    /**
     * @param timestamp
     */
    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * @return event_type
     */
    public Integer getEventType() {
        return eventType;
    }

    /**
     * @param eventType
     */
    public void setEventType(Integer eventType) {
        this.eventType = eventType;
    }

    /**
     * @return post_id
     */
    public Long getPostId() {
        return postId;
    }

    /**
     * @param postId
     */
    public void setPostId(Long postId) {
        this.postId = postId;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", eventId=").append(eventId);
        sb.append(", accountId=").append(accountId);
        sb.append(", timestamp=").append(timestamp);
        sb.append(", eventType=").append(eventType);
        sb.append(", postId=").append(postId);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}
package cn.edu.seu.alumni_server.dao.entity;

import java.io.Serializable;
import javax.persistence.*;

@Table(name = "const_major")
public class ConstMajor implements Serializable {
    @Column(name = "major_id")
    private Integer majorId;

    @Column(name = "major_name")
    private String majorName;

    private static final long serialVersionUID = 1L;

    /**
     * @return major_id
     */
    public Integer getMajorId() {
        return majorId;
    }

    /**
     * @param majorId
     */
    public void setMajorId(Integer majorId) {
        this.majorId = majorId;
    }

    /**
     * @return major_name
     */
    public String getMajorName() {
        return majorName;
    }

    /**
     * @param majorName
     */
    public void setMajorName(String majorName) {
        this.majorName = majorName == null ? null : majorName.trim();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", majorId=").append(majorId);
        sb.append(", majorName=").append(majorName);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}
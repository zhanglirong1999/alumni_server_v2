package cn.edu.seu.alumni_server.dao.entity;

import java.io.Serializable;
import javax.persistence.*;

public class Test implements Serializable {
    @Column(name = "column_1")
    private Long column1;

    private static final long serialVersionUID = 1L;

    /**
     * @return column_1
     */
    public Long getColumn1() {
        return column1;
    }

    /**
     * @param column1
     */
    public void setColumn1(Long column1) {
        this.column1 = column1;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", column1=").append(column1);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}
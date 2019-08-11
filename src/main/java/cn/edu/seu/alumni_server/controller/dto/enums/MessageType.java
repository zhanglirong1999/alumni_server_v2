package cn.edu.seu.alumni_server.controller.dto.enums;

public enum MessageType {
    /**
     *
     */
    APPLY(0, "申请"),
    AGREE(1, "同意"),
    REJECT(2, "拒绝");

    public Integer value;
    public String desc;

    MessageType(Integer status, String desc) {

    }
}

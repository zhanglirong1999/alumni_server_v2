package cn.edu.seu.alumni_server.controller.dto.enums;

public enum FriendStatus {

    /**
     *
     */
    stranger(0, "路人"),
    apply(1, "A向B发起好友申请"),
    friend(2, "好友"),

    // 这个只是给前台展示用，数据库没有这个状态
    todo(3, "A收到B的好友请求");

    private int status;
    private String desc;

    FriendStatus(int status, String desc) {
        this.status = status;
        this.desc = desc;
    }

    public int getStatus() {
        return status;
    }

    public String getDesc() {
        return desc;
    }
}

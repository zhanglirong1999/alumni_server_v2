package cn.edu.seu.alumni_server.common.enums;

public enum FriendStatus {

    stranger(0, "路人"),
    apply(1, "A向B发起好友申请"),
    todo(2, "A收到B的好友请求"),
    friend(3, "好友");

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

package cn.edu.seu.alumni_server.common.enums;

public enum FriendStatus {
    /**
     * 两个openid（A,B）之间的状态分类，
     * 系统设计上规避A，B同时发起好友申请的情况
     */
    stranger(0, "路人"),
    todo(1, "A向B发起好友申请"),
    friend(2, "好友"),
    rejected(3, "被拒绝");

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

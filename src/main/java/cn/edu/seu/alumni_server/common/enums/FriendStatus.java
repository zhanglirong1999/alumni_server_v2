package cn.edu.seu.alumni_server.common.enums;

public enum FriendStatus {
    /**
     * 两个openid之间的状态分类
     * A,B
     */
    stranger(0, "路人"),
    applying(2, "A向B发起好友申请，或者A有待处理的B的好友申请"),
    friend(1, "好友"),
    rejected(3, "被拒绝");

    private int status;
    private String desc;

    FriendStatus(int status, String desc) {
        this.status = status;
        this.desc = desc;
    }
}

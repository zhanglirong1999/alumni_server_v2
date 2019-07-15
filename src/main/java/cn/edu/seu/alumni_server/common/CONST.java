package cn.edu.seu.alumni_server.common;

public class CONST {
    public static final int SUCCESS_CODE = 1;
    public static final int FAIL_CODE = 0;
    public static final String SUCCESS_MESSAGE_DEFAULT = "SUCCESS";
    public static final String FAIL_MESSAGE_DEFAULT = "FAIL";

    /* 两个openid之间的状态分类 */
//    A，B openid
    // 陌生人
    public static final String FriendStatus_0 = "0";
    // 好友
    public static final String FriendStatus_1 = "1";
    // 被该B申请加为好友
    public static final String FriendStatus_2 = "2";
    // 申请B好友
    public static final String FriendStatus_3 = "3";
    // 被B同意
    public static final String FriendStatus_4 = "4";
    // 被B拒绝
    public static final String FriendStatus_5 = "5";

}

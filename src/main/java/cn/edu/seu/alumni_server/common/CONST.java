package cn.edu.seu.alumni_server.common;

public class CONST {
    public static final int SUCCESS_CODE = 200;
    public static final int FAIL_CODE = -1;
    public static final String SUCCESS_MESSAGE_DEFAULT = "SUCCESS";
    public static final String FAIL_MESSAGE_DEFAULT = "FAIL";

    /**
     * 同意1，拒绝0
     */
    public static final int FRIEND_ACTION_Y = 1;
    public static final int FRIEND_ACTION_N = 0;

    // wx 参数
    public static final String appId = "wx517c42c8b9dd029e";
    public static final String appSecret = "183737ca41a053d8f8bcf7cd558e3e2e";

    // ACL Key
    public static final String ACL_ACCOUNTID = "accountId";

    /**
     * 订阅消息提示类型
     */
    public static final String  ACTIVITY_MESSAGE = "活动消息";
    public static final String  SYSTEM_MESSAGE = "系统消息";
    public static final String  NEW_FRIEND_MESSAGE = "有新朋友请求交换名片";
    public static final String  REJECT_MESSAGE = "拒绝添加朋友";
    public static final String  AGREE_MESSAGE = "同意添加朋友";

    //需求类型
    public static final String  ALL_DEMAND_TYPE = "0";
    public static final String  JOBS_DEMAND_TYPE = "1";
    public static final String  PARTNER_DEMAND_TYPE = "2";
    public static final String  INFO_PUBLISH_DEMAND_TYPE = "3";
}

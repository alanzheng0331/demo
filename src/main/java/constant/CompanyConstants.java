package constant;

/**
 * 企业相关常量
 */
public class CompanyConstants {
    // 密码最小长度
    public static final int COMPANY_PWD_MIN_LENGTH = 6;
    // 找回密码验证码长度
    public static final int FORGET_PWD_CODE_LENGTH = 6;
    // 验证码有效期（分钟）
    public static final int VERIFY_CODE_EXPIRE_MINUTES = 5;

    // 企业状态：0-禁用 1-正常 2-待审核
    public static final int COMPANY_STATUS_DISABLE = 0;
    public static final int COMPANY_STATUS_NORMAL = 1;
    public static final int COMPANY_STATUS_AUDIT = 2;
}
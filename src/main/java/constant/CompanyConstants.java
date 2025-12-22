package constant;

/**
 * 企业端常量类
 */
public class CompanyConstants {
    // 企业密码最小长度
    public static final int COMPANY_PWD_MIN_LENGTH = 6;
    // 验证码有效期（分钟）
    public static final int VERIFY_CODE_EXPIRE_MINUTES = 5;
    // 企业状态：1-正常，0-禁用，2-待审核
    public static final int COMPANY_STATUS_NORMAL = 1;
    public static final int COMPANY_STATUS_DISABLE = 0;
    public static final int COMPANY_STATUS_AUDIT = 2;
    // 忘记密码验证码长度
    public static final int FORGET_PWD_CODE_LENGTH = 6;
    // 允许的企业头像类型（复用FileUploadUtil的常量，若没有则补充）
    public static final String[] ALLOWED_AVATAR_TYPES = {"jpg", "png", "jpeg", "gif"};
}
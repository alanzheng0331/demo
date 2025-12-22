package constant;

/**
 * 用户模块常量
 */
public class UserConstants {
    // 用户角色
    public static final String ROLE_JOB_SEEKER = "job_seeker";
    public static final String ROLE_ADMIN = "admin";

    // 用户状态
    public static final int USER_STATUS_NORMAL = 1;
    public static final int USER_STATUS_DISABLED = 0;

    // 简历默认状态
    public static final int RESUME_DEFAULT_NO = 0;
    public static final int RESUME_DEFAULT_YES = 1;

    // 投递状态
    public static final String APPLY_STATUS_WAITING = "waiting";
    public static final String APPLY_STATUS_ACCEPTED = "accepted";
    public static final String APPLY_STATUS_REJECTED = "rejected";

    // 文件上传常量
    public static final long MAX_FILE_SIZE = 2 * 1024 * 1024; // 2MB
    public static final String[] ALLOWED_AVATAR_TYPES = {"jpg", "jpeg", "png"};
    public static final String[] ALLOWED_RESUME_TYPES = {"pdf", "doc", "docx"};
}

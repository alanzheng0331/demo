package entity;

/**
 * 管理员实体类：对应数据库中的 admin 表
 * 存储管理员账号核心信息
 */
public class Admin {
    // 管理员ID（主键，自增）
    private Integer adminId;
    // 管理员登录账号（唯一）
    private String username;
    // 管理员登录密码（建议加密存储）
    private String password;
    // 管理员姓名（显示用）
    private String adminName;
    // 账号状态（1=正常，0=禁用，可选扩展）
    private Integer status = 1;

    // 无参构造器（MyBatis/数据库查询映射必需）
    public Admin() {}

    // 带参构造器（用于快速创建管理员对象）
    public Admin(String username, String password, String adminName) {
        this.username = username;
        this.password = password;
        this.adminName = adminName;
    }

    // getter/setter 方法（用于属性读写）
    public Integer getAdminId() {
        return adminId;
    }

    public void setAdminId(Integer adminId) {
        this.adminId = adminId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    // 可选：重写 toString()，方便打印管理员信息（调试用）
    @Override
    public String toString() {
        return "Admin{" +
                "adminId=" + adminId +
                ", username='" + username + '\'' +
                ", adminName='" + adminName + '\'' +
                ", status=" + status +
                '}';
    }
}
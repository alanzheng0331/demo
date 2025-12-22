package entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户实体（求职者/管理员）
 */
public class User implements Serializable {
    private Long id;                    // 主键ID
    private String role;                // 角色：job_seeker/admin
    private String name;                // 姓名
    private String phone;               // 手机号（唯一）
    private String email;               // 邮箱
    private String password;            // 密码（加密后）
    private String avatar;              // 头像URL
    private String skillTags;           // 技能标签（逗号分隔）
    private String expectedSalary;      // 期望薪资
    private Date createTime;            // 创建时间
    private Date updateTime;            // 更新时间
    private Integer status;             // 状态：0-禁用，1-正常

    // 空构造
    public User() {}

    // Getter & Setter
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getAvatar() { return avatar; }
    public void setAvatar(String avatar) { this.avatar = avatar; }
    public String getSkillTags() { return skillTags; }
    public void setSkillTags(String skillTags) { this.skillTags = skillTags; }
    public String getExpectedSalary() { return expectedSalary; }
    public void setExpectedSalary(String expectedSalary) { this.expectedSalary = expectedSalary; }
    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }
    public Date getUpdateTime() { return updateTime; }
    public void setUpdateTime(Date updateTime) { this.updateTime = updateTime; }
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
}

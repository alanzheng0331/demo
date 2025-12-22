package entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 简历实体
 */
public class Resume implements Serializable {
    private Long id;                    // 主键ID
    private Long userId;                // 用户ID
    private String resumeName;          // 简历名称
    private String filePath;            // 简历文件路径
    private String fileName;            // 简历文件名
    private String education;           // 学历
    private String workExperience;      // 工作经验
    private String skillTags;           // 技能标签
    private String expectedSalary;      // 期望薪资
    private Integer isDefault;          // 是否默认简历：0-否，1-是
    private Date createTime;            // 创建时间
    private Date updateTime;            // 更新时间

    // 空构造
    public Resume() {}

    // Getter & Setter
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getResumeName() { return resumeName; }
    public void setResumeName(String resumeName) { this.resumeName = resumeName; }
    public String getFilePath() { return filePath; }
    public void setFilePath(String filePath) { this.filePath = filePath; }
    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }
    public String getEducation() { return education; }
    public void setEducation(String education) { this.education = education; }
    public String getWorkExperience() { return workExperience; }
    public void setWorkExperience(String workExperience) { this.workExperience = workExperience; }
    public String getSkillTags() { return skillTags; }
    public void setSkillTags(String skillTags) { this.skillTags = skillTags; }
    public String getExpectedSalary() { return expectedSalary; }
    public void setExpectedSalary(String expectedSalary) { this.expectedSalary = expectedSalary; }
    public Integer getIsDefault() { return isDefault; }
    public void setIsDefault(Integer isDefault) { this.isDefault = isDefault; }
    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }
    public Date getUpdateTime() { return updateTime; }
    public void setUpdateTime(Date updateTime) { this.updateTime = updateTime; }
}
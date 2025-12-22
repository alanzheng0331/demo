package entity;
import java.io.Serializable;
import java.util.Date;

/**
 * 简历投递记录实体
 */
public class Apply implements Serializable {
    private Long id;                    // 主键ID
    private Long userId;                // 用户ID
    private Long jobId;                 // 兼职ID
    private Long resumeId;              // 简历ID
    private String attachInfo;          // 附加信息
    private String applyStatus;         // 投递状态：waiting-等待中，accepted-已接受，rejected-不合适
    private Date applyTime;             // 投递时间
    private Date updateTime;            // 状态更新时间

    // 空构造
    public Apply() {}

    // Getter & Setter
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public Long getJobId() { return jobId; }
    public void setJobId(Long jobId) { this.jobId = jobId; }
    public Long getResumeId() { return resumeId; }
    public void setResumeId(Long resumeId) { this.resumeId = resumeId; }
    public String getAttachInfo() { return attachInfo; }
    public void setAttachInfo(String attachInfo) { this.attachInfo = attachInfo; }
    public String getApplyStatus() { return applyStatus; }
    public void setApplyStatus(String applyStatus) { this.applyStatus = applyStatus; }
    public Date getApplyTime() { return applyTime; }
    public void setApplyTime(Date applyTime) { this.applyTime = applyTime; }
    public Date getUpdateTime() { return updateTime; }
    public void setUpdateTime(Date updateTime) { this.updateTime = updateTime; }
}
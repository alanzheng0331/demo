package entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 兼职收藏实体
 */
public class Collect implements Serializable {
    private Long id;                    // 主键ID
    private Long userId;                // 用户ID
    private Long jobId;                 // 兼职ID
    private Date createTime;            // 收藏时间

    // 空构造
    public Collect() {}

    // Getter & Setter
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public Long getJobId() { return jobId; }
    public void setJobId(Long jobId) { this.jobId = jobId; }
    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }
}

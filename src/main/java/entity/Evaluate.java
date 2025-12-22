package entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 评价实体
 */
public class Evaluate implements Serializable {
    private Long id;                    // 主键ID
    private Long userId;                // 评价用户ID
    private Long jobId;                 // 兼职ID
    private Integer salaryScore;        // 薪资发放及时性（1-5星）
    private Integer realScore;          // 工作真实性（1-5星）
    private Integer envScore;           // 工作环境（1-5星）
    private String content;             // 综合评价（文字）
    private String reply;               // 企业回复
    private Date createTime;            // 评价时间
    private Date replyTime;             // 回复时间

    // 空构造
    public Evaluate() {}

    // Getter & Setter
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public Long getJobId() { return jobId; }
    public void setJobId(Long jobId) { this.jobId = jobId; }
    public Integer getSalaryScore() { return salaryScore; }
    public void setSalaryScore(Integer salaryScore) { this.salaryScore = salaryScore; }
    public Integer getRealScore() { return realScore; }
    public void setRealScore(Integer realScore) { this.realScore = realScore; }
    public Integer getEnvScore() { return envScore; }
    public void setEnvScore(Integer envScore) { this.envScore = envScore; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public String getReply() { return reply; }
    public void setReply(String reply) { this.reply = reply; }
    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }
    public Date getReplyTime() { return replyTime; }
    public void setReplyTime(Date replyTime) { this.replyTime = replyTime; }
}

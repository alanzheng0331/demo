package entity;
import java.util.Date;

public class Application {
    private Integer applicationId; // 申请ID
    private Integer jobId;         // 岗位ID
    private Integer studentId;     // 学生ID
    private Date applyTime;        // 申请时间
    private Integer status;        // 状态（0-待审核，1-已录用，2-已拒绝）
    private String feedback;       // 反馈信息

    // Getter和Setter
    public Integer getApplicationId() { return applicationId; }
    public void setApplicationId(Integer applicationId) { this.applicationId = applicationId; }
    public Integer getJobId() { return jobId; }
    public void setJobId(Integer jobId) { this.jobId = jobId; }
    public Integer getStudentId() { return studentId; }
    public void setStudentId(Integer studentId) { this.studentId = studentId; }
    public Date getApplyTime() { return applyTime; }
    public void setApplyTime(Date applyTime) { this.applyTime = applyTime; }
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
    public String getFeedback() { return feedback; }
    public void setFeedback(String feedback) { this.feedback = feedback; }
}
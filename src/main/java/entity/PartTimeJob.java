package entity;
import java.util.Date;

public class PartTimeJob {
    private Integer jobId;         // 岗位ID
    private String jobName;        // 岗位名称
    private Integer categoryId;    // 类别ID（关联JobCategory）
    private String employer;       // 雇主名称
    private String location;       // 工作地点
    private double salary;         // 薪资（元/小时）
    private String requirements;   // 岗位要求
    private String description;    // 岗位描述
    private Date startDate;        // 开始日期
    private Date endDate;          // 结束日期
    private Integer status;        // 状态（1-招募中，0-已结束）

    // Getter和Setter
    public Integer getJobId() { return jobId; }
    public void setJobId(Integer jobId) { this.jobId = jobId; }
    public String getJobName() { return jobName; }
    public void setJobName(String jobName) { this.jobName = jobName; }
    public Integer getCategoryId() { return categoryId; }
    public void setCategoryId(Integer categoryId) { this.categoryId = categoryId; }
    public String getEmployer() { return employer; }
    public void setEmployer(String employer) { this.employer = employer; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public double getSalary() { return salary; }
    public void setSalary(double salary) { this.salary = salary; }
    public String getRequirements() { return requirements; }
    public void setRequirements(String requirements) { this.requirements = requirements; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Date getStartDate() { return startDate; }
    public void setStartDate(Date startDate) { this.startDate = startDate; }
    public Date getEndDate() { return endDate; }
    public void setEndDate(Date endDate) { this.endDate = endDate; }
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
}
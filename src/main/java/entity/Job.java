package entity;


import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 兼职信息实体类
 * 对应数据库表：t_job
 * 存储兼职的基础信息、地域、薪资、状态等核心数据
 *
 * @author 编程助手
 * @date 2025-12-20
 */
public class Job implements Serializable {
    /**
     * 序列化版本号（保证序列化/反序列化兼容性）
     */
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID（自增）
     */
    private Long id;

    /**
     * 兼职标题（如：周末家教（小学数学）、商超促销兼职）
     */
    private String title;

    /**
     * 兼职类型（枚举值：家教/促销/服务员/实习）
     * 对应常量：JobConstants.JOB_TYPE_*
     */
    private String type;

    /**
     * 省份（如：北京市、江苏省）
     */
    private String province;

    /**
     * 城市（如：北京市、南京市）
     */
    private String city;

    /**
     * 区县（如：海淀区、玄武区）
     */
    private String district;

    /**
     * 薪资（元/天），使用BigDecimal避免浮点精度问题
     */
    private BigDecimal salary;

    /**
     * 薪资区间（枚举值：0-100/100-200/200+）
     * 对应常量：JobConstants.SALARY_RANGE_*
     */
    private String salaryRange;

    /**
     * 工作内容（详细描述）
     */
    private String content;

    /**
     * 公司名称
     */
    private String companyName;

    /**
     * 已报名人数
     */
    private Integer applyCount;

    /**
     * 发布时间
     */
    private Date publishTime;

    /**
     * 有效时间（兼职截止报名时间）
     */
    private Date validTime;

    /**
     * 状态（枚举值：published-已发布/closed-已关闭）
     * 对应常量：JobConstants.JOB_STATUS_*
     */
    private String status;

    /**
     * 发布者ID（关联t_user表的管理员ID）
     */
    private Long publisherId;

    // ========== 空构造器（必须，适配DBUtils/反射实例化） ==========
    public Job() {
    }

    // ========== 全参构造器（便于快速创建对象） ==========
    public Job(Long id, String title, String type, String province, String city, String district,
               BigDecimal salary, String salaryRange, String content, String companyName,
               Integer applyCount, Date publishTime, Date validTime, String status, Long publisherId) {
        this.id = id;
        this.title = title;
        this.type = type;
        this.province = province;
        this.city = city;
        this.district = district;
        this.salary = salary;
        this.salaryRange = salaryRange;
        this.content = content;
        this.companyName = companyName;
        this.applyCount = applyCount;
        this.publishTime = publishTime;
        this.validTime = validTime;
        this.status = status;
        this.publisherId = publisherId;
    }

    // ========== Getter & Setter（规范命名，适配ORM框架） ==========
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }

    public String getSalaryRange() {
        return salaryRange;
    }

    public void setSalaryRange(String salaryRange) {
        this.salaryRange = salaryRange;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public Integer getApplyCount() {
        return applyCount;
    }

    public void setApplyCount(Integer applyCount) {
        this.applyCount = applyCount;
    }

    public Date getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(Date publishTime) {
        this.publishTime = publishTime;
    }

    public Date getValidTime() {
        return validTime;
    }

    public void setValidTime(Date validTime) {
        this.validTime = validTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getPublisherId() {
        return publisherId;
    }

    public void setPublisherId(Long publisherId) {
        this.publisherId = publisherId;
    }

    // ========== toString方法（便于日志打印、调试） ==========
    @Override
    public String toString() {
        return "Job{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", type='" + type + '\'' +
                ", province='" + province + '\'' +
                ", city='" + city + '\'' +
                ", district='" + district + '\'' +
                ", salary=" + salary +
                ", salaryRange='" + salaryRange + '\'' +
                ", content='" + content + '\'' +
                ", companyName='" + companyName + '\'' +
                ", applyCount=" + applyCount +
                ", publishTime=" + publishTime +
                ", validTime=" + validTime +
                ", status='" + status + '\'' +
                ", publisherId=" + publisherId +
                '}';
    }
}

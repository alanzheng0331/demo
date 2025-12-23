package entity;

import java.util.Date;

/**
 * 企业实体类（无MD5依赖，属性匹配MySQL表）
 */
public class Company {
    private Long id; // 主键ID
    private String companyName; // 企业名称
    private String creditCode; // 统一社会信用代码（对应数据库credit_t_code）
    private String legalPersonName; // 法人姓名
    private String companyAddress; // 企业地址
    private String companyPhone; // 企业电话
    private String companyPwd; // 登录密码（明文存储，无加密）
    private String companyAvatar; // 企业头像
    private Integer status; // 状态：0-待审核，1-已审核，2-禁用
    private Date createTime; // 创建时间
    private Date updateTime; // 更新时间
    private String verifyCode; // 忘记密码验证码
    private Date codeCreateTime; // 验证码创建时间

    // 无参构造
    public Company() {
    }

    // 有参构造（简化版，用于注册）
    public Company(String companyName, String creditCode, String legalPersonName, String companyAddress, String companyPhone, String companyPwd) {
        this.companyName = companyName;
        this.creditCode = creditCode;
        this.legalPersonName = legalPersonName;
        this.companyAddress = companyAddress;
        this.companyPhone = companyPhone;
        this.companyPwd = companyPwd;
    }

    // Getter和Setter方法
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCreditCode() {
        return creditCode;
    }

    public void setCreditCode(String creditCode) {
        this.creditCode = creditCode;
    }

    public String getLegalPersonName() {
        return legalPersonName;
    }

    public void setLegalPersonName(String legalPersonName) {
        this.legalPersonName = legalPersonName;
    }

    public String getCompanyAddress() {
        return companyAddress;
    }

    public void setCompanyAddress(String companyAddress) {
        this.companyAddress = companyAddress;
    }

    public String getCompanyPhone() {
        return companyPhone;
    }

    public void setCompanyPhone(String companyPhone) {
        this.companyPhone = companyPhone;
    }

    public String getCompanyPwd() {
        return companyPwd;
    }

    public void setCompanyPwd(String companyPwd) {
        this.companyPwd = companyPwd;
    }

    public String getCompanyAvatar() {
        return companyAvatar;
    }

    public void setCompanyAvatar(String companyAvatar) {
        this.companyAvatar = companyAvatar;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getVerifyCode() {
        return verifyCode;
    }

    public void setVerifyCode(String verifyCode) {
        this.verifyCode = verifyCode;
    }

    public Date getCodeCreateTime() {
        return codeCreateTime;
    }

    public void setCodeCreateTime(Date codeCreateTime) {
        this.codeCreateTime = codeCreateTime;
    }
}
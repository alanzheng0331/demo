package entity;

import java.util.Date;

/**
 * 企业实体类（对应test_company表）
 */
public class Company {
    private Long id;
    private String companyName;
    private String creditCode;
    private String legalPersonName;
    private String companyAddress;
    private String companyPhone;
    private String companyPwd;
    private String companyAvatar;
    private Integer status;
    private Date createTime;
    private Date updateTime;
    private String verifyCode;
    private Date codeCreateTime;

    // 无参构造
    public Company() {}

    // getter/setter
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }

    public String getCreditCode() { return creditCode; }
    public void setCreditCode(String creditCode) { this.creditCode = creditCode; }

    public String getLegalPersonName() { return legalPersonName; }
    public void setLegalPersonName(String legalPersonName) { this.legalPersonName = legalPersonName; }

    public String getCompanyAddress() { return companyAddress; }
    public void setCompanyAddress(String companyAddress) { this.companyAddress = companyAddress; }

    public String getCompanyPhone() { return companyPhone; }
    public void setCompanyPhone(String companyPhone) { this.companyPhone = companyPhone; }

    public String getCompanyPwd() { return companyPwd; }
    public void setCompanyPwd(String companyPwd) { this.companyPwd = companyPwd; }

    public String getCompanyAvatar() { return companyAvatar; }
    public void setCompanyAvatar(String companyAvatar) { this.companyAvatar = companyAvatar; }

    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }

    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }

    public Date getUpdateTime() { return updateTime; }
    public void setUpdateTime(Date updateTime) { this.updateTime = updateTime; }

    public String getVerifyCode() { return verifyCode; }
    public void setVerifyCode(String verifyCode) { this.verifyCode = verifyCode; }

    public Date getCodeCreateTime() { return codeCreateTime; }
    public void setCodeCreateTime(Date codeCreateTime) { this.codeCreateTime = codeCreateTime; }
}
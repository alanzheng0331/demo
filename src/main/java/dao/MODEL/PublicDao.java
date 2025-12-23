package dao.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 插入数据的数据源工具类
 * 规则：
 * 1. 移除自增主键字段（数据库自动生成，插入时无需传递）；
 * 2. 仅保留Map的Key（表业务字段名，严格对应数据库）；
 * 3. Value设为空值（数值类型null/字符类型空字符串/时间类型null）；
 * 4. 不使用所有字段（可选部分字段）。
 * DAO层可调用此类方法获取空值数据模板，后续由Servlet赋值后执行INSERT
 */
public class PublicDao {
    // ======================  用户表（t_user）字段名常量  ======================
    private String userRole = "role";          // 角色：job_seeker / company（字符类型）
    private String userName = "name";          // 姓名/昵称（字符类型）
    private String userPhone = "phone";        // 手机号（登录账号）（字符类型）
    private String userEmail = "email";        // 邮箱（字符类型）
    private String userPassword = "password";  // 密码（加密后）（字符类型）
    private String userSkillTags = "skill_tags"; // 技能标签（字符类型）
    private String userExpectedSalary = "expected_salary"; // 期望薪资（字符类型）
    private String userAvatar = "avatar";      // 头像URL（字符类型）
    private String userStatus = "status";      // 状态：0禁用 1正常（数值类型）
    private String userCreateTime = "create_time"; // 注册时间（时间类型）
    private String userUpdateTime = "update_time"; // 资料更新时间（时间类型）

    // ======================  企业表（t_company）字段名常量  ======================
    private String companyName = "company_name"; // 企业名称（字符类型）
    private String companyCreditCode = "credit_code"; // 统一社会信用代码（字符类型）
    private String companyLegalPersonName = "legal_person_name"; // 法人姓名（字符类型）
    private String companyAddress = "company_address"; // 企业地址（字符类型）
    private String companyPhone = "company_phone"; // 企业联系电话（字符类型）
    private String companyPwd = "company_pwd"; // 登录密码（MD5）（字符类型）
    private String companyAvatar = "company_avatar"; // 企业LOGO/头像URL（字符类型）
    private String companyStatus = "status"; // 状态：0未审核 1已通过 2已封禁（数值类型）
    private String companyCreateTime = "create_time"; // 注册时间（时间类型）
    private String companyUpdateTime = "update_time"; // 资料更新时间（时间类型）
    private String companyVerifyCode = "verify_code"; // 手机验证码（字符类型）
    private String companyCodeCreateTime = "code_create_time"; // 验证码生成时间（时间类型）

    // ======================  用户表字段名 get/set 方法  ======================
    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getUserSkillTags() {
        return userSkillTags;
    }

    public void setUserSkillTags(String userSkillTags) {
        this.userSkillTags = userSkillTags;
    }

    public String getUserExpectedSalary() {
        return userExpectedSalary;
    }

    public void setUserExpectedSalary(String userExpectedSalary) {
        this.userExpectedSalary = userExpectedSalary;
    }

    public String getUserAvatar() {
        return userAvatar;
    }

    public void setUserAvatar(String userAvatar) {
        this.userAvatar = userAvatar;
    }

    public String getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(String userStatus) {
        this.userStatus = userStatus;
    }

    public String getUserCreateTime() {
        return userCreateTime;
    }

    public void setUserCreateTime(String userCreateTime) {
        this.userCreateTime = userCreateTime;
    }

    public String getUserUpdateTime() {
        return userUpdateTime;
    }

    public void setUserUpdateTime(String userUpdateTime) {
        this.userUpdateTime = userUpdateTime;
    }

    // ======================  企业表字段名 get/set 方法  ======================
    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyCreditCode() {
        return companyCreditCode;
    }

    public void setCompanyCreditCode(String companyCreditCode) {
        this.companyCreditCode = companyCreditCode;
    }

    public String getCompanyLegalPersonName() {
        return companyLegalPersonName;
    }

    public void setCompanyLegalPersonName(String companyLegalPersonName) {
        this.companyLegalPersonName = companyLegalPersonName;
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

    public String getCompanyStatus() {
        return companyStatus;
    }

    public void setCompanyStatus(String companyStatus) {
        this.companyStatus = companyStatus;
    }

    public String getCompanyCreateTime() {
        return companyCreateTime;
    }

    public void setCompanyCreateTime(String companyCreateTime) {
        this.companyCreateTime = companyCreateTime;
    }

    public String getCompanyUpdateTime() {
        return companyUpdateTime;
    }

    public void setCompanyUpdateTime(String companyUpdateTime) {
        this.companyUpdateTime = companyUpdateTime;
    }

    public String getCompanyVerifyCode() {
        return companyVerifyCode;
    }

    public void setCompanyVerifyCode(String companyVerifyCode) {
        this.companyVerifyCode = companyVerifyCode;
    }

    public String getCompanyCodeCreateTime() {
        return companyCodeCreateTime;
    }

    public void setCompanyCodeCreateTime(String companyCodeCreateTime) {
        this.companyCodeCreateTime = companyCodeCreateTime;
    }

    // ======================  用户表插入模板方法  ======================
    public List<Map<String, Object>> getUserInsertTemplate() {
        List<Map<String, Object>> dataList = new ArrayList<>();
        Map<String, Object> dataMap = new HashMap<>();

        // 字符类型字段：空字符串
        dataMap.put(this.getUserRole(), "");
        dataMap.put(this.getUserName(), "");
        dataMap.put(this.getUserPhone(), "");
        dataMap.put(this.getUserEmail(), "");
        dataMap.put(this.getUserPassword(), "");
        dataMap.put(this.getUserSkillTags(), "");
        dataMap.put(this.getUserExpectedSalary(), "");
        dataMap.put(this.getUserAvatar(), "");

        // 数值类型字段：null
        dataMap.put(this.getUserStatus(), null);

        // 时间类型字段：null
        dataMap.put(this.getUserCreateTime(), null);
        dataMap.put(this.getUserUpdateTime(), null);

        dataList.add(dataMap);
        return dataList;
    }

    // ======================  企业表插入模板方法  ======================
    public List<Map<String, Object>> getCompanyInsertTemplate() {
        List<Map<String, Object>> dataList = new ArrayList<>();
        Map<String, Object> dataMap = new HashMap<>();

        // 字符类型字段：空字符串
        dataMap.put(this.getCompanyName(), "");
        dataMap.put(this.getCompanyCreditCode(), "");
        dataMap.put(this.getCompanyLegalPersonName(), "");
        dataMap.put(this.getCompanyAddress(), "");
        dataMap.put(this.getCompanyPhone(), "");
        dataMap.put(this.getCompanyPwd(), "");
        dataMap.put(this.getCompanyAvatar(), "");
        dataMap.put(this.getCompanyVerifyCode(), "");

        // 数值类型字段：null
        dataMap.put(this.getCompanyStatus(), null);

        // 时间类型字段：null
        dataMap.put(this.getCompanyCreateTime(), null);
        dataMap.put(this.getCompanyUpdateTime(), null);
        dataMap.put(this.getCompanyCodeCreateTime(), null);

        dataList.add(dataMap);
        return dataList;
    }
}
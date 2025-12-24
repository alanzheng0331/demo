package service;

import entity.Company;
import java.sql.SQLException;

/**
 * 企业业务层接口
 */
public interface CompanyService {
    /**
     * 企业注册
     */
    String register(Company company) throws SQLException;

    /**
     * 企业登录
     */
    String login(String phone, String pwd) throws SQLException;

    /**
     * 发送找回密码验证码
     */
    String sendForgetPwdCode(String phone) throws SQLException;

    /**
     * 校验找回密码验证码
     */
    boolean checkForgetPwdCode(String phone, String code) throws SQLException;

    /**
     * 重置企业密码
     */
    String resetPwd(String phone, String code, String newPwd) throws SQLException;

    Company getCompanyByNameAndCreditCode(String companyName, String creditCode) throws SQLException;

    int resetPwdByCompanyName(String companyName, String encryptPwd) throws SQLException;
}
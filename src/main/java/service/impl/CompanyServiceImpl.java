package service.impl;

import constant.CompanyConstants;
import dao.CompanyDao;
import entity.Company;
import service.CompanyService;
import util.MD5Util;

import java.sql.SQLException;
import java.util.Date;
import java.util.Random;

/**
 * 企业业务层实现类
 */
public class CompanyServiceImpl implements CompanyService {

    private final CompanyDao companyDao = new CompanyDao();

    @Override
    public String register(Company company) throws SQLException {
        System.out.println("=== 开始企业注册校验 ===");
        System.out.println("企业名称：" + company.getCompanyName());
        System.out.println("信用代码：" + company.getCreditCode());
        System.out.println("企业手机号：" + company.getCompanyPhone());

        String companyName = company.getCompanyName() == null ? "" : company.getCompanyName().trim();
        String creditCode = company.getCreditCode() == null ? "" : company.getCreditCode().trim();
        String companyPhone = company.getCompanyPhone() == null ? "" : company.getCompanyPhone().trim();
        String companyPwd = company.getCompanyPwd() == null ? "" : company.getCompanyPwd().trim();

        if (companyName.isEmpty()) {
            System.out.println("注册失败：企业名称为空");
            return "企业名称不能为空！";
        }
        if (creditCode.isEmpty()) {
            System.out.println("注册失败：信用代码为空");
            return "信用代码不能为空！";
        }
        if (creditCode.length() != 18) {
            System.out.println("注册失败：信用代码长度不是18位");
            return "信用代码必须为18位！";
        }
        if (companyPhone.isEmpty()) {
            System.out.println("注册失败：企业手机号为空");
            return "企业手机号不能为空！";
        }
        if (companyPwd.length() < CompanyConstants.COMPANY_PWD_MIN_LENGTH) {
            System.out.println("注册失败：密码长度不足");
            return "密码长度不能少于" + CompanyConstants.COMPANY_PWD_MIN_LENGTH + "位！";
        }

        if (companyDao.isCreditCodeExist(creditCode)) {
            System.out.println("注册失败：信用代码已存在");
            return "该信用代码已注册，请更换！";
        }

        Company existCompany = companyDao.findByPhone(companyPhone);
        if (existCompany != null) {
            System.out.println("注册失败：手机号已注册");
            return "该手机号已注册，请直接登录！";
        }

        if (company.getStatus() == null) {
            company.setStatus(CompanyConstants.COMPANY_STATUS_AUDIT);
            System.out.println("设置默认状态：待审核");
        }

        int rows = companyDao.register(company);
        System.out.println("DAO层注册返回行数：" + rows);
        if (rows > 0) {
            System.out.println("注册成功：企业名称=" + companyName);
            return "success";
        } else {
            System.out.println("注册失败：DAO层插入行数为0");
            return "注册失败，无数据插入！";
        }
    }

    @Override
    public String login(String phone, String pwd) throws SQLException {
        System.out.println("=== 开始企业登录校验 ===");
        System.out.println("登录手机号：" + phone);

        String phoneTrim = phone == null ? "" : phone.trim();
        String pwdTrim = pwd == null ? "" : pwd.trim();

        if (phoneTrim.isEmpty()) {
            return "手机号不能为空！";
        }
        if (pwdTrim.length() < CompanyConstants.COMPANY_PWD_MIN_LENGTH) {
            return "密码长度不能少于" + CompanyConstants.COMPANY_PWD_MIN_LENGTH + "位！";
        }

        Company company = companyDao.findByPhone(phoneTrim);
        if (company == null) {
            return "该手机号未注册！";
        }

        String encryptPwd = MD5Util.md5(pwdTrim);
        if (!encryptPwd.equals(company.getCompanyPwd())) {
            return "密码错误，请重试！";
        }

        Integer status = company.getStatus();
        if (status == CompanyConstants.COMPANY_STATUS_DISABLE) {
            return "该账号已被禁用，请联系管理员！";
        }
        if (status == CompanyConstants.COMPANY_STATUS_AUDIT) {
            return "该账号正在审核中，请耐心等待！";
        }
        if (status != CompanyConstants.COMPANY_STATUS_NORMAL) {
            return "账号状态异常，请联系管理员！";
        }

        return "success";
    }

    @Override
    public String sendForgetPwdCode(String phone) throws SQLException {
        System.out.println("=== 开始发送找回密码验证码 ===");
        System.out.println("接收手机号：" + phone);

        String phoneTrim = phone == null ? "" : phone.trim();
        if (phoneTrim.isEmpty()) {
            return "手机号不能为空！";
        }

        Company company = companyDao.findByPhone(phoneTrim);
        if (company == null) {
            return "该手机号未注册！";
        }

        String code = generateRandomCode(CompanyConstants.FORGET_PWD_CODE_LENGTH);
        System.out.println("生成验证码：" + code);

        int rows = companyDao.saveVerifyCode(phoneTrim, code);
        if (rows > 0) {
            System.out.println("验证码保存成功：手机号=" + phoneTrim);
            return "success";
        } else {
            return "验证码发送失败，请重试！";
        }
    }

    @Override
    public boolean checkForgetPwdCode(String phone, String code) throws SQLException {
        System.out.println("=== 开始校验找回密码验证码 ===");
        System.out.println("手机号：" + phone + "，输入验证码：" + code);

        String phoneTrim = phone == null ? "" : phone.trim();
        String codeTrim = code == null ? "" : code.trim();

        if (phoneTrim.isEmpty() || codeTrim.length() != CompanyConstants.FORGET_PWD_CODE_LENGTH) {
            return false;
        }

        Company company = companyDao.findByPhone(phoneTrim);
        if (company == null) {
            return false;
        }

        String dbCode = company.getVerifyCode() == null ? "" : company.getVerifyCode().trim();
        if (!codeTrim.equalsIgnoreCase(dbCode)) {
            return false;
        }

        Date codeCreateTime = company.getCodeCreateTime();
        if (codeCreateTime == null) {
            return false;
        }
        long currentTime = System.currentTimeMillis();
        long codeCreateTimeMs = codeCreateTime.getTime();
        long expireTimeMs = codeCreateTimeMs + CompanyConstants.VERIFY_CODE_EXPIRE_MINUTES * 60 * 1000;

        if (currentTime > expireTimeMs) {
            return false;
        }

        return true;
    }

    @Override
    public String resetPwd(String phone, String code, String newPwd) throws SQLException {
        System.out.println("=== 开始重置企业密码 ===");
        System.out.println("手机号：" + phone + "，新密码长度：" + (newPwd == null ? 0 : newPwd.length()));

        String newPwdTrim = newPwd == null ? "" : newPwd.trim();
        if (newPwdTrim.length() < CompanyConstants.COMPANY_PWD_MIN_LENGTH) {
            return "新密码长度不能少于" + CompanyConstants.COMPANY_PWD_MIN_LENGTH + "位！";
        }

        boolean codeValid = checkForgetPwdCode(phone, code);
        if (!codeValid) {
            return "验证码无效或已过期，请重新获取！";
        }

        int rows = companyDao.resetPwd(phone.trim(), newPwdTrim);
        if (rows > 0) {
            return "success";
        } else {
            return "密码重置失败，请重试！";
        }
    }

    private String generateRandomCode(int length) {
        Random random = new Random(System.currentTimeMillis());
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < length; i++) {
            code.append(random.nextInt(10));
        }
        return code.toString();
    }
}
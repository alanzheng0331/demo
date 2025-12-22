package service.impl;

import constant.CompanyConstants;
import dao.CompanyDao;
import entity.Company;
import service.CompanyService;

import java.sql.SQLException;
import java.util.Date;
import java.util.Random;

/**
 * 企业业务层实现类
 */
public class CompanyServiceImpl implements CompanyService {
    private final CompanyDao companyDao = new CompanyDao();
    private final Random random = new Random();

    @Override
    public String register(Company company) throws SQLException {
        // 1. 基础校验
        if (company.getCompanyName() == null || company.getCompanyName().trim().isEmpty()) {
            return "企业名称不能为空";
        }
        if (company.getCreditCode() == null || company.getCreditCode().trim().isEmpty()) {
            return "统一社会信用代码不能为空";
        }
        if (company.getLegalPersonName() == null || company.getLegalPersonName().trim().isEmpty()) {
            return "法人姓名不能为空";
        }
        if (company.getCompanyAddress() == null || company.getCompanyAddress().trim().isEmpty()) {
            return "企业地址不能为空";
        }
        if (company.getCompanyPhone() == null || company.getCompanyPhone().trim().isEmpty()) {
            return "联系电话不能为空";
        }
        if (company.getCompanyPwd() == null || company.getCompanyPwd().length() < CompanyConstants.COMPANY_PWD_MIN_LENGTH) {
            return "密码长度不能少于" + CompanyConstants.COMPANY_PWD_MIN_LENGTH + "位";
        }

        // 2. 校验信用代码是否已存在
        if (companyDao.isCreditCodeExist(company.getCreditCode())) {
            return "该统一社会信用代码已注册";
        }

        // 3. 校验手机号是否已注册
        if (companyDao.findByPhone(company.getCompanyPhone()) != null) {
            return "该手机号已注册";
        }

        // 4. 设置默认状态（待审核）
        company.setStatus(CompanyConstants.COMPANY_STATUS_AUDIT);

        // 5. 执行注册
        int rows = companyDao.register(company);
        return rows > 0 ? "success" : "注册失败，请稍后重试";
    }

    @Override
    public String login(String phone, String pwd) throws SQLException {
        // 1. 查询企业
        Company company = companyDao.findByPhone(phone);
        if (company == null) {
            return "该手机号未注册";
        }

        // 2. 校验状态
        if (company.getStatus() == CompanyConstants.COMPANY_STATUS_DISABLE) {
            return "账号已禁用，请联系管理员";
        }
        if (company.getStatus() == CompanyConstants.COMPANY_STATUS_AUDIT) {
            return "账号待审核，暂无法登录";
        }

        // 3. 校验密码
        String encryptPwd = company.getCompanyPwd();
        String inputEncryptPwd = util.MD5Util.md5(pwd);
        if (!encryptPwd.equals(inputEncryptPwd)) {
            return "密码错误";
        }

        return "success";
    }

    @Override
    public String sendForgetPwdCode(String phone) throws SQLException {
        // 1. 校验手机号是否注册
        Company company = companyDao.findByPhone(phone);
        if (company == null) {
            return "该手机号未注册";
        }

        // 2. 生成6位数字验证码
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < CompanyConstants.FORGET_PWD_CODE_LENGTH; i++) {
            code.append(random.nextInt(10));
        }
        String verifyCode = code.toString();

        // 3. 保存验证码到数据库（实际开发可替换为短信发送）
        int rows = companyDao.saveVerifyCode(phone, verifyCode);
        if (rows > 0) {
            // 模拟短信发送，实际开发需对接短信接口
            System.out.println("【忘记密码验证码】您的验证码是：" + verifyCode + "，有效期5分钟");
            return "success";
        } else {
            return "验证码发送失败，请稍后重试";
        }
    }

    @Override
    public boolean checkForgetPwdCode(String phone, String code) throws SQLException {
        // 1. 查询企业
        Company company = companyDao.findByPhone(phone);
        if (company == null) {
            return false;
        }

        // 2. 校验验证码
        String dbCode = company.getVerifyCode();
        if (dbCode == null || !dbCode.equals(code)) {
            return false;
        }

        // 3. 校验有效期（5分钟）
        Date codeCreateTime = company.getCodeCreateTime();
        if (codeCreateTime == null) {
            return false;
        }
        long diff = new Date().getTime() - codeCreateTime.getTime();
        long expireMs = CompanyConstants.VERIFY_CODE_EXPIRE_MINUTES * 60 * 1000;
        return diff <= expireMs;
    }

    @Override
    public String resetPwd(String phone, String code, String newPwd) throws SQLException {
        // 1. 校验验证码
        if (!checkForgetPwdCode(phone, code)) {
            return "验证码错误或已过期";
        }

        // 2. 校验新密码
        if (newPwd == null || newPwd.length() < CompanyConstants.COMPANY_PWD_MIN_LENGTH) {
            return "新密码长度不能少于" + CompanyConstants.COMPANY_PWD_MIN_LENGTH + "位";
        }

        // 3. 重置密码
        int rows = companyDao.resetPwd(phone, newPwd);
        return rows > 0 ? "success" : "密码重置失败，请稍后重试";
    }
}
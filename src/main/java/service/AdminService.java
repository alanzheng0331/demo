package service;

import entity.Admin;
import dao.AdminDAO;
import util.PasswordUtil; // 密码加密/校验工具
import util.DBUtil;       // 数据库连接工具（复用学生端）

/**
 * 管理员业务服务类：封装管理员核心操作逻辑
 * 给 AdminDemo 提供登录、密码校验、密码修改等服务
 */
public class AdminService {
    // 依赖数据访问层：操作管理员相关数据库表
    private AdminDAO adminDAO = new AdminDAO();

    /**
     * 管理员登录（核心方法）
     * @param username 登录账号
     * @param password 登录密码（明文）
     * @return 登录成功返回 Admin 对象，失败返回 null
     */
    public Admin login(String username, String password) {
        // 1. 先查数据库中是否存在该账号
        Admin admin = adminDAO.queryByUsername(username);
        if (admin == null) {
            System.out.println("❌ 账号不存在");
            return null;
        }

        // 2. 校验账号是否正常（1=正常，0=禁用）
        if (admin.getStatus() != 1) {
            System.out.println("❌ 账号已被禁用");
            return null;
        }

        // 3. 校验密码（明文加密后与数据库加密密码对比）
        boolean isPwdCorrect = PasswordUtil.check(password, admin.getPassword());
        if (!isPwdCorrect) {
            System.out.println("❌ 密码错误");
            return null;
        }

        // 4. 登录成功（清空密码字段，避免敏感信息泄露）
        admin.setPassword(null);
        return admin;
    }

    /**
     * 校验原密码（修改密码前用）
     * @param adminId 管理员ID
     * @param oldPwd 输入的原密码（明文）
     * @return 校验通过返回 true
     */
    public boolean checkOldPassword(Integer adminId, String oldPwd) {
        String dbEncryptedPwd = adminDAO.queryPasswordById(adminId);
        return PasswordUtil.check(oldPwd, dbEncryptedPwd);
    }

    /**
     * 修改管理员密码
     * @param adminId 管理员ID
     * @param newPwd 新密码（明文）
     * @return 修改成功返回 true
     */
    public boolean updatePassword(Integer adminId, String newPwd) {
        // 密码加密后再存入数据库
        String encryptedNewPwd = PasswordUtil.encrypt(newPwd);
        return adminDAO.updatePassword(adminId, encryptedNewPwd);
    }
}
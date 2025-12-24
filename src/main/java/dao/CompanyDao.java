package dao;

import entity.Company;
import util.DBUtil;
import util.MD5Util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 企业DAO层（数据访问）- 适配SQL Server
 * 已完善：支持企业名称登录（findByCompanyName方法适配实际表结构）
 */
public class CompanyDao {

    /**
     * 企业注册（插入数据）
     * 适配数据库的 credit_t_code 字段
     */
    public int register(Company company) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DBUtil.getConnection();
            // 关键修改：表名从t_company改为[t_company]（SQL Server中含特殊字符需用[]包裹）
            String sql = "INSERT INTO [t_company] (" +
                    "company_name, credit_t_code, legal_person_name, company_address, " +
                    "company_phone, company_pwd, company_avatar, status, create_time, update_time) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, GETDATE(), GETDATE())";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, company.getCompanyName());
            pstmt.setString(2, company.getCreditCode());
            pstmt.setString(3, company.getLegalPersonName());
            pstmt.setString(4, company.getCompanyAddress());
            pstmt.setString(5, company.getCompanyPhone());
            pstmt.setString(6, MD5Util.md5(company.getCompanyPwd()));
            pstmt.setString(7, company.getCompanyAvatar() == null ? "default_avatar.png" : company.getCompanyAvatar());
            pstmt.setInt(8, company.getStatus() == null ? 0 : company.getStatus());

            int rows = pstmt.executeUpdate();
            System.out.println("注册插入受影响行数：" + rows);
            return rows;
        } finally {
            DBUtil.close(conn, pstmt);
        }
    }

    /**
     * 根据手机号查询企业
     */
    public Company findByPhone(String phone) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            // 关键修改：表名从t_company改为[t_company]
            String sql = "SELECT [id], [company_name], [credit_t_code], [legal_person_name], " +
                    "[company_address], [company_phone], [company_pwd], [company_avatar], " +
                    "[status], [create_time], [update_time], [verify_code], [code_create_time] " +
                    "FROM [t_company] WHERE company_phone = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, phone);

            rs = pstmt.executeQuery();
            Company company = null;
            if (rs.next()) {
                company = new Company();
                company.setId(rs.getLong("id"));
                company.setCompanyName(rs.getString("company_name"));
                company.setCreditCode(rs.getString("credit_t_code"));
                company.setLegalPersonName(rs.getString("legal_person_name"));
                company.setCompanyAddress(rs.getString("company_address"));
                company.setCompanyPhone(rs.getString("company_phone"));
                company.setCompanyPwd(rs.getString("company_pwd"));
                company.setCompanyAvatar(rs.getString("company_avatar"));
                company.setStatus(rs.getInt("status"));
                company.setCreateTime(rs.getDate("create_time"));
                company.setUpdateTime(rs.getDate("update_time"));
                company.setVerifyCode(rs.getString("verify_code"));
                company.setCodeCreateTime(rs.getDate("code_create_time"));
            }
            return company;
        } finally {
            DBUtil.close(conn, pstmt, rs);
        }
    }

    /**
     * 根据企业名称查询企业（核心修改：适配[t_company]表结构，支持企业名称登录）
     * 完整封装所有字段，与findByPhone方法保持一致
     */
    public Company findByCompanyName(String companyName) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            // 关键修改：表名从t_company改为[t_company]
            String sql = "SELECT [id], [company_name], [credit_t_code], [legal_person_name], " +
                    "[company_address], [company_phone], [company_pwd], [company_avatar], " +
                    "[status], [create_time], [update_time], [verify_code], [code_create_time] " +
                    "FROM [t_company] WHERE company_name = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, companyName.trim()); // 去除空格，避免查询匹配失败

            rs = pstmt.executeQuery();
            Company company = null;
            if (rs.next()) {
                company = new Company();
                // 完整封装所有字段，与findByPhone方法一致，确保Service层能获取全部信息
                company.setId(rs.getLong("id"));
                company.setCompanyName(rs.getString("company_name"));
                company.setCreditCode(rs.getString("credit_t_code")); // 适配数据库的credit_t_code字段
                company.setLegalPersonName(rs.getString("legal_person_name"));
                company.setCompanyAddress(rs.getString("company_address"));
                company.setCompanyPhone(rs.getString("company_phone"));
                company.setCompanyPwd(rs.getString("company_pwd")); // MD5加密后的密码，用于登录校验
                company.setCompanyAvatar(rs.getString("company_avatar"));
                company.setStatus(rs.getInt("status")); // 账号状态：0-待审核，1-正常，2-禁用（按实际业务调整）
                company.setCreateTime(rs.getDate("create_time"));
                company.setUpdateTime(rs.getDate("update_time"));
                company.setVerifyCode(rs.getString("verify_code"));
                company.setCodeCreateTime(rs.getDate("code_create_time"));
            }
            System.out.println("根据企业名称[" + companyName + "]查询结果：" + (company != null ? "存在" : "不存在"));
            return company;
        } finally {
            // 统一关闭资源，避免连接泄漏
            DBUtil.close(conn, pstmt, rs);
        }
    }

    /**
     * 保存忘记密码验证码
     */
    public int saveVerifyCode(String phone, String code) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DBUtil.getConnection();
            // 关键修改：表名从t_company改为[t_company]
            String sql = "UPDATE [t_company] SET [verify_code] = ?, [code_create_time] = GETDATE() WHERE [company_phone] = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, code);
            pstmt.setString(2, phone);

            int rows = pstmt.executeUpdate();
            System.out.println("保存验证码受影响行数：" + rows);
            return rows;
        } finally {
            DBUtil.close(conn, pstmt);
        }
    }

    /**
     * 重置企业密码
     */
    public int resetPwd(String phone, String newPwd) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DBUtil.getConnection();
            // 关键修改：表名从t_company改为[t_company]
            String sql = "UPDATE [t_company] SET [company_pwd] = ?, [update_time] = GETDATE() WHERE [company_phone] = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, MD5Util.md5(newPwd));
            pstmt.setString(2, phone);

            int rows = pstmt.executeUpdate();
            System.out.println("重置密码受影响行数：" + rows);
            return rows;
        } finally {
            DBUtil.close(conn, pstmt);
        }
    }

    /**
     * 检查信用代码是否已存在
     */
    public boolean isCreditCodeExist(String creditCode) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            // 关键修改：表名从t_company改为[t_company]
            String sql = "SELECT 1 FROM [t_company] WHERE [credit_t_code] = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, creditCode);
            rs = pstmt.executeQuery();

            boolean exists = rs.next();
            System.out.println("信用代码" + creditCode + "是否存在：" + exists);
            return exists;
        } finally {
            DBUtil.close(conn, pstmt, rs);
        }
    }

    // 新增：根据企业名称+信用代码查询企业（仅修改表名为[t_company]，其他逻辑不变）
    public Company findByNameAndCreditCode(String companyName, String creditCode) throws SQLException {
        // 关键修改：表名从company改为[t_company]（SQL Server含特殊字符需用[]包裹）
        String sql = "SELECT * FROM [t_company] WHERE company_name = ? AND credit_t_code = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, companyName); // 第一个? → 企业名称
            ps.setString(2, creditCode);  // 第二个? → 信用代码

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                // 封装逻辑和findByCompanyName保持一致
                Company company = new Company();
                company.setCompanyName(rs.getString("company_name"));
                company.setCreditCode(rs.getString("credit_t_code"));
                company.setCompanyPwd(rs.getString("company_pwd"));
                company.setStatus(rs.getInt("status"));
                return company;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
        return null;
    }

    // 新增：根据企业名称更新密码（仅修改表名为[t_company]，其他逻辑不变）
    public int updatePwdByCompanyName(String companyName, String encryptPwd) throws SQLException {
        // 关键修改：表名从company改为[t_company]
        String sql = "UPDATE [t_company] SET company_pwd = ? WHERE company_name = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, encryptPwd); // 第一个? → 加密后的新密码
            ps.setString(2, companyName); // 第二个? → 企业名称

            return ps.executeUpdate(); // 返回受影响行数（>0则成功）
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }
}
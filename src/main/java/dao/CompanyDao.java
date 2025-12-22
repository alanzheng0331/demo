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
            String sql = "INSERT INTO test_company (" +
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
            String sql = "SELECT [id], [company_name], [credit_t_code], [legal_person_name], " +
                    "[company_address], [company_phone], [company_pwd], [company_avatar], " +
                    "[status], [create_time], [update_time], [verify_code], [code_create_time] " +
                    "FROM test_company WHERE company_phone = ?";
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
     * 保存忘记密码验证码
     */
    public int saveVerifyCode(String phone, String code) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DBUtil.getConnection();
            String sql = "UPDATE test_company SET [verify_code] = ?, [code_create_time] = GETDATE() WHERE [company_phone] = ?";
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
            String sql = "UPDATE test_company SET [company_pwd] = ?, [update_time] = GETDATE() WHERE [company_phone] = ?";
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
            String sql = "SELECT 1 FROM test_company WHERE [credit_t_code] = ?";
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
}
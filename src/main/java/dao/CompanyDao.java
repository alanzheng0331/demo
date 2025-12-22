package dao;

import entity.Company;
import util.DBUtil;
import util.MD5Util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

/**
 * 企业DAO层（数据访问）
 */
public class CompanyDao {

    /**
     * 企业注册（插入数据）
     */
    public int register(Company company) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DBUtil.getConnection();
            String sql = "INSERT INTO t_company (" +
                    "company_name, credit_code, legal_person_name, company_address, " +
                    "company_phone, company_pwd, company_avatar, status) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, company.getCompanyName());
            pstmt.setString(2, company.getCreditCode());
            pstmt.setString(3, company.getLegalPersonName());
            pstmt.setString(4, company.getCompanyAddress());
            pstmt.setString(5, company.getCompanyPhone());
            pstmt.setString(6, MD5Util.md5(company.getCompanyPwd())); // MD5加密密码
            pstmt.setString(7, company.getCompanyAvatar());
            pstmt.setInt(8, company.getStatus());
            return pstmt.executeUpdate();
        } finally {
            DBUtil.close(conn, pstmt);
        }
    }

    /**
     * 根据手机号查询企业（登录/忘记密码用）
     */
    public Company findByPhone(String phone) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT * FROM t_company WHERE company_phone = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, phone);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                Company company = new Company();
                company.setId(rs.getLong("id"));
                company.setCompanyName(rs.getString("company_name"));
                company.setCreditCode(rs.getString("credit_code"));
                company.setLegalPersonName(rs.getString("legal_person_name"));
                company.setCompanyAddress(rs.getString("company_address"));
                company.setCompanyPhone(rs.getString("company_phone"));
                company.setCompanyPwd(rs.getString("company_pwd"));
                company.setCompanyAvatar(rs.getString("company_avatar"));
                company.setStatus(rs.getInt("status"));
                company.setVerifyCode(rs.getString("verify_code"));
                company.setCodeCreateTime(rs.getDate("code_create_time"));
                return company;
            }
            return null;
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
            String sql = "UPDATE t_company SET verify_code = ?, code_create_time = GETDATE() WHERE company_phone = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, code);
            pstmt.setString(2, phone);
            return pstmt.executeUpdate();
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
            String sql = "UPDATE t_company SET company_pwd = ?, update_time = GETDATE() WHERE company_phone = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, MD5Util.md5(newPwd)); // MD5加密新密码
            pstmt.setString(2, phone);
            return pstmt.executeUpdate();
        } finally {
            DBUtil.close(conn, pstmt);
        }
    }

    /**
     * 检查信用代码是否已存在（注册去重）
     */
    public boolean isCreditCodeExist(String creditCode) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT 1 FROM t_company WHERE credit_code = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, creditCode);
            rs = pstmt.executeQuery();
            return rs.next();
        } finally {
            DBUtil.close(conn, pstmt, rs);
        }
    }
}
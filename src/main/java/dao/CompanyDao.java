package dao;

import entity.Company;
import util.DBConfig;
import util.DBUtil;
import util.MysqlDBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

/**
 * 企业数据访问层 - 支持双数据库（MySQL/SQL Server）
 * 包含企业注册、查询、验证码保存、密码重置等完整功能
 */
public class CompanyDao {

    /**
     * 根据配置获取数据库连接
     */
    private Connection getConnection() throws SQLException {
        String dbType = DBConfig.getCurrentDB();

        if (DBConfig.DB_MYSQL.equals(dbType)) {
            Connection conn = MysqlDBUtil.getConnection();
            if (conn == null) {
                throw new SQLException("MySQL连接失败");
            }
            return conn;
        } else {
            Connection conn = DBUtil.getConnection();
            if (conn == null) {
                throw new SQLException("SQL Server连接失败");
            }
            return conn;
        }
    }

    /**
     * 根据配置关闭资源（不含ResultSet）
     */
    private void close(Connection conn, PreparedStatement pstmt) {
        close(conn, pstmt, null);
    }

    /**
     * 根据配置关闭资源（含ResultSet）
     */
    private void close(Connection conn, PreparedStatement pstmt, ResultSet rs) {
        String dbType = DBConfig.getCurrentDB();

        if (DBConfig.DB_MYSQL.equals(dbType)) {
            MysqlDBUtil.close(conn, pstmt, rs);
        } else {
            DBUtil.close(conn, pstmt, rs);
        }
    }

    /**
     * 根据企业名称查询企业信息
     */
    public Company findByCompanyName(String companyName) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql = "SELECT * FROM t_company WHERE company_name = ?";
        Company company = null;

        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, companyName);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                company = this.convertResultSetToCompany(rs);
            }
            return company;
        } catch (SQLException e) {
            System.err.println("根据企业名称查询失败：" + e.getMessage());
            e.printStackTrace();
            throw e;
        } finally {
            close(conn, pstmt, rs);
        }
    }

    /**
     * 根据企业手机号查询企业信息
     */
    public Company findByPhone(String companyPhone) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql = "SELECT * FROM t_company WHERE company_phone = ?";
        Company company = null;

        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, companyPhone);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                company = this.convertResultSetToCompany(rs);
            }
            return company;
        } catch (SQLException e) {
            System.err.println("根据企业手机号查询失败：" + e.getMessage());
            e.printStackTrace();
            throw e;
        } finally {
            close(conn, pstmt, rs);
        }
    }

    /**
     * 校验统一社会信用代码是否已存在
     */
    public boolean isCreditCodeExist(String creditCode) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql = "SELECT COUNT(*) FROM t_company WHERE credit_code = ?";

        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, creditCode);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            return false;
        } catch (SQLException e) {
            System.err.println("校验统一社会信用代码存在性失败：" + e.getMessage());
            e.printStackTrace();
            throw e;
        } finally {
            close(conn, pstmt, rs);
        }
    }

    /**
     * 企业注册（插入企业信息到t_company表）
     */
    public int register(Company company) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        String sql = "INSERT INTO t_company(company_name, credit_code, legal_person_name, company_address, company_phone, company_pwd, company_avatar, status, create_time, update_time, verify_code, code_create_time) " +
                "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);

            // 设置参数
            pstmt.setString(1, company.getCompanyName());
            pstmt.setString(2, company.getCreditCode());
            pstmt.setString(3, company.getLegalPersonName());
            pstmt.setString(4, company.getCompanyAddress());
            pstmt.setString(5, company.getCompanyPhone());
            pstmt.setString(6, company.getCompanyPwd()); // 需在Service/Servlet层做MD5加密
            pstmt.setString(7, company.getCompanyAvatar());
            pstmt.setInt(8, company.getStatus());
            pstmt.setTimestamp(9, new java.sql.Timestamp(new Date().getTime()));
            pstmt.setTimestamp(10, new java.sql.Timestamp(new Date().getTime()));
            pstmt.setString(11, company.getVerifyCode());
            pstmt.setTimestamp(12, company.getCodeCreateTime() != null ? new java.sql.Timestamp(company.getCodeCreateTime().getTime()) : null);

            return pstmt.executeUpdate(); // 返回受影响行数
        } catch (SQLException e) {
            System.err.println("企业注册插入失败：" + e.getMessage());
            e.printStackTrace();
            throw e;
        } finally {
            close(conn, pstmt);
        }
    }

    /**
     * 保存找回密码验证码
     */
    public int saveVerifyCode(String companyPhone, String code) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        String sql = "UPDATE t_company SET verify_code = ?, code_create_time = ? WHERE company_phone = ?";

        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, code);
            pstmt.setTimestamp(2, new java.sql.Timestamp(new Date().getTime()));
            pstmt.setString(3, companyPhone);

            return pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("保存企业验证码失败：" + e.getMessage());
            e.printStackTrace();
            throw e;
        } finally {
            close(conn, pstmt);
        }
    }

    /**
     * 重置企业密码
     */
    public int resetPwd(String companyPhone, String newPwd) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        String sql = "UPDATE t_company SET company_pwd = ?, update_time = ? WHERE company_phone = ?";

        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, newPwd); // 需在Service/Servlet层做MD5加密
            pstmt.setTimestamp(2, new java.sql.Timestamp(new Date().getTime()));
            pstmt.setString(3, companyPhone);

            return pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("重置企业密码失败：" + e.getMessage());
            e.printStackTrace();
            throw e;
        } finally {
            close(conn, pstmt);
        }
    }

    /**
     * 结果集转换为Company实体（封装重复逻辑，减少冗余）
     */
    private Company convertResultSetToCompany(ResultSet rs) throws SQLException {
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
        company.setCreateTime(rs.getTimestamp("create_time"));
        company.setUpdateTime(rs.getTimestamp("update_time"));
        company.setVerifyCode(rs.getString("verify_code"));
        company.setCodeCreateTime(rs.getTimestamp("code_create_time"));
        return company;
    }
}
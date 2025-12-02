package dao;

import entity.Admin;
import util.DBUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class AdminDAO {
    /**
     * 根据账号查询管理员
     */
    public Admin getAdminByUsername(String username) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT admin_id, username, password, admin_name, status FROM admin WHERE username = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            rs = ps.executeQuery();

            if (rs.next()) {
                Admin admin = new Admin();
                admin.setAdminId(rs.getInt("admin_id"));
                admin.setUsername(rs.getString("username"));
                admin.setPassword(rs.getString("password")); // 数据库中是加密后的密码
                admin.setAdminName(rs.getString("admin_name"));
                admin.setStatus(rs.getInt("status"));
                return admin;
            }
        } catch (Exception e) {
            System.err.println("查询管理员失败：" + e.getMessage());
        } finally {
            DBUtil.close(conn, ps, rs);
        }
        return null;
    }

    /**
     * 根据ID查询加密后的密码
     */
    public String getPasswordById(Integer adminId) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT password FROM admin WHERE admin_id = ?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, adminId);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("password");
            }
        } catch (Exception e) {
            System.err.println("查询管理员密码失败：" + e.getMessage());
        } finally {
            DBUtil.close(conn, ps, rs);
        }
        return null;
    }

    /**
     * 更新管理员密码
     */
    public boolean updatePassword(Integer adminId, String encryptedPwd) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBUtil.getConnection();
            String sql = "UPDATE admin SET password = ? WHERE admin_id = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, encryptedPwd);
            ps.setInt(2, adminId);
            return ps.executeUpdate() > 0; // 影响行数>0则成功
        } catch (Exception e) {
            System.err.println("更新管理员密码失败：" + e.getMessage());
        } finally {
            DBUtil.close(conn, ps, null);
        }
        return false;
    }

    // 其他方法（addAdmin、updateStatus）实现逻辑类似，按需补充
}
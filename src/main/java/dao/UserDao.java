package dao;

import entity.User;
import util.DBUtil;
import org.apache.commons.dbutils.handlers.BeanHandler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

/**
 * 用户数据访问层
 */
public class UserDao {
    /**
     * 注册用户
     */
    public int register(User user) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        String sql = "INSERT INTO t_user(role, name, phone, email, password, skill_tags, expected_salary, create_time, update_time, status) " +
                "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, user.getRole());
            pstmt.setString(2, user.getName());
            pstmt.setString(3, user.getPhone());
            pstmt.setString(4, user.getEmail());
            pstmt.setString(5, user.getPassword());
            pstmt.setString(6, user.getSkillTags());
            pstmt.setString(7, user.getExpectedSalary());
            pstmt.setDate(8, new java.sql.Date(new Date().getTime()));
            pstmt.setDate(9, new java.sql.Date(new Date().getTime()));
            pstmt.setInt(10, 1); // 默认正常状态
            return pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        } finally {
            DBUtil.close(conn, pstmt);
        }
    }

    /**
     * 根据手机号查询用户
     */
    public User findByPhone(String phone) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql = "SELECT * FROM t_user WHERE phone = ?";
        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, phone);
            rs = pstmt.executeQuery();
            return new BeanHandler<>(User.class).handle(rs);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            DBUtil.close(conn, pstmt, rs);
        }
    }

    /**
     * 检查手机号是否存在
     */
    public boolean existsByPhone(String phone) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql = "SELECT COUNT(*) FROM t_user WHERE phone = ?";
        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, phone);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            DBUtil.close(conn, pstmt, rs);
        }
    }

    /**
     * 根据ID查询用户
     */
    public User findById(Long userId) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql = "SELECT * FROM t_user WHERE id = ?";
        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, userId);
            rs = pstmt.executeQuery();
            return new BeanHandler<>(User.class).handle(rs);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            DBUtil.close(conn, pstmt, rs);
        }
    }

    /**
     * 更新用户基本信息
     */
    public int updateUser(User user) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        String sql = "UPDATE t_user SET name = ?, email = ?, skill_tags = ?, expected_salary = ?, avatar = ?, update_time = ? WHERE id = ?";
        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, user.getName());
            pstmt.setString(2, user.getEmail());
            pstmt.setString(3, user.getSkillTags());
            pstmt.setString(4, user.getExpectedSalary());
            pstmt.setString(5, user.getAvatar());
            pstmt.setDate(6, new java.sql.Date(new Date().getTime()));
            pstmt.setLong(7, user.getId());
            return pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        } finally {
            DBUtil.close(conn, pstmt);
        }
    }

    /**
     * 更新头像
     */
    public int updateAvatar(User user) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        String sql = "UPDATE t_user SET avatar = ?, update_time = ? WHERE id = ?";
        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, user.getAvatar());
            pstmt.setDate(2, new java.sql.Date(new Date().getTime()));
            pstmt.setLong(3, user.getId());
            return pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        } finally {
            DBUtil.close(conn, pstmt);
        }
    }

    /**
     * 修改密码
     */
    public int updatePassword(Long userId, String newPassword) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        String sql = "UPDATE t_user SET password = ?, update_time = ? WHERE id = ?";
        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, newPassword);
            pstmt.setDate(2, new java.sql.Date(new Date().getTime()));
            pstmt.setLong(3, userId);
            return pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        } finally {
            DBUtil.close(conn, pstmt);
        }
    }

    /**
     * 更换手机号
     */
    public int updatePhone(Long userId, String newPhone) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        String sql = "UPDATE t_user SET phone = ?, update_time = ? WHERE id = ?";
        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, newPhone);
            pstmt.setDate(2, new java.sql.Date(new Date().getTime()));
            pstmt.setLong(3, userId);
            return pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        } finally {
            DBUtil.close(conn, pstmt);
        }
    }
}

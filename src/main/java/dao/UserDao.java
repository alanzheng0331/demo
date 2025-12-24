package dao;

import entity.User;
import util.DBUtil;           // SQL Server工具类
import util.MysqlDBUtil;      // MySQL工具类
import util.DBConfig;         // 数据库配置管理

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

/**
 * 用户数据访问层 - 支持双数据库
 */
public class UserDao {

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
     * 根据配置关闭资源
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
     * 注册用户
     */
    public int register(User user) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;

        // 注意：SQL语法在两个数据库中可能略有不同
        // 这里假设表结构相同
        String sql = "INSERT INTO t_user(role, name, phone, email, password, skill_tags, expected_salary, avatar, create_time, update_time, status) " +
                "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);

            // 设置参数
            pstmt.setString(1, user.getRole());
            pstmt.setString(2, user.getName());
            pstmt.setString(3, user.getPhone());
            pstmt.setString(4, user.getEmail());
            pstmt.setString(5, user.getPassword());
            pstmt.setString(6, user.getSkillTags());
            pstmt.setString(7, user.getExpectedSalary());
            pstmt.setString(8, user.getAvatar());

            // 时间参数 - 使用当前时间
            Date now = new Date();
            pstmt.setTimestamp(9, new java.sql.Timestamp(now.getTime()));
            pstmt.setTimestamp(10, new java.sql.Timestamp(now.getTime()));

            pstmt.setInt(11, 1); // 默认正常状态

            int result = pstmt.executeUpdate();
            System.out.println("数据库操作结果：" + result + " 行受影响");
            return result;

        } catch (SQLException e) {
            System.err.println("注册用户时数据库错误：" + e.getMessage());
            e.printStackTrace();
            return 0;
        } finally {
            close(conn, pstmt);
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
        User user = null;

        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, phone);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                user = new User();
                user.setId(rs.getLong("id"));
                user.setRole(rs.getString("role"));
                user.setName(rs.getString("name"));
                user.setPhone(rs.getString("phone"));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));
                user.setSkillTags(rs.getString("skill_tags"));
                user.setExpectedSalary(rs.getString("expected_salary"));
                user.setAvatar(rs.getString("avatar"));
                user.setCreateTime(rs.getTimestamp("create_time"));
                user.setUpdateTime(rs.getTimestamp("update_time"));
                user.setStatus(rs.getInt("status"));
            }
            return user;

        } catch (SQLException e) {
            System.err.println("查询用户时数据库错误：" + e.getMessage());
            e.printStackTrace();
            return null;
        } finally {
            close(conn, pstmt, rs);
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
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, phone);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            return false;

        } catch (SQLException e) {
            System.err.println("检查手机号存在性时数据库错误：" + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            close(conn, pstmt, rs);
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
        User user = null;

        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, userId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                user = new User();
                user.setId(rs.getLong("id"));
                user.setRole(rs.getString("role"));
                user.setName(rs.getString("name"));
                user.setPhone(rs.getString("phone"));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));
                user.setSkillTags(rs.getString("skill_tags"));
                user.setExpectedSalary(rs.getString("expected_salary"));
                user.setAvatar(rs.getString("avatar"));
                user.setCreateTime(rs.getTimestamp("create_time"));
                user.setUpdateTime(rs.getTimestamp("update_time"));
                user.setStatus(rs.getInt("status"));
            }
            return user;

        } catch (SQLException e) {
            System.err.println("根据ID查询用户时数据库错误：" + e.getMessage());
            e.printStackTrace();
            return null;
        } finally {
            close(conn, pstmt, rs);
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
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, user.getName());
            pstmt.setString(2, user.getEmail());
            pstmt.setString(3, user.getSkillTags());
            pstmt.setString(4, user.getExpectedSalary());
            pstmt.setString(5, user.getAvatar());
            pstmt.setTimestamp(6, new java.sql.Timestamp(new Date().getTime()));
            pstmt.setLong(7, user.getId());

            int result = pstmt.executeUpdate();
            System.out.println("更新用户信息结果：" + result + " 行受影响");
            return result;

        } catch (SQLException e) {
            System.err.println("更新用户信息时数据库错误：" + e.getMessage());
            e.printStackTrace();
            return 0;
        } finally {
            close(conn, pstmt);
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
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, user.getAvatar());
            pstmt.setTimestamp(2, new java.sql.Timestamp(new Date().getTime()));
            pstmt.setLong(3, user.getId());

            int result = pstmt.executeUpdate();
            System.out.println("更新头像结果：" + result + " 行受影响");
            return result;

        } catch (SQLException e) {
            System.err.println("更新头像时数据库错误：" + e.getMessage());
            e.printStackTrace();
            return 0;
        } finally {
            close(conn, pstmt);
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
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, newPassword);
            pstmt.setTimestamp(2, new java.sql.Timestamp(new Date().getTime()));
            pstmt.setLong(3, userId);

            int result = pstmt.executeUpdate();
            System.out.println("修改密码结果：" + result + " 行受影响");
            return result;

        } catch (SQLException e) {
            System.err.println("修改密码时数据库错误：" + e.getMessage());
            e.printStackTrace();
            return 0;
        } finally {
            close(conn, pstmt);
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
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, newPhone);
            pstmt.setTimestamp(2, new java.sql.Timestamp(new Date().getTime()));
            pstmt.setLong(3, userId);

            int result = pstmt.executeUpdate();
            System.out.println("更换手机号结果：" + result + " 行受影响");
            return result;

        } catch (SQLException e) {
            System.err.println("更换手机号时数据库错误：" + e.getMessage());
            e.printStackTrace();
            return 0;
        } finally {
            close(conn, pstmt);
        }
    }
}
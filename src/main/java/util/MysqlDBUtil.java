package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * MySQL数据库连接工具类（企业端/用户端通用，无MD5依赖）
 */
public class MysqlDBUtil {
    // MySQL数据库连接参数（修改为你的实际配置）
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String URL = "jdbc:mysql://localhost:3306/studentparttimejobdb?useSSL=false&serverTimezone=Asia/Shanghai&characterEncoding=UTF-8";
    private static final String USER = "root";
    private static final String PASSWORD = "040105"; // 改为你的MySQL密码

    // 加载驱动
    static {
        try {
            Class.forName(DRIVER);
            System.out.println("✅ MySQL驱动加载成功！");
        } catch (ClassNotFoundException e) {
            System.err.println("❌ MySQL驱动加载失败！请检查驱动包是否添加到WEB-INF/lib");
            e.printStackTrace();
        }
    }

    /**
     * 获取MySQL数据库连接
     */
    public static Connection getConnection() {
        try {
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("✅ MySQL连接成功！数据库：" + conn.getCatalog());
            return conn;
        } catch (SQLException e) {
            System.err.println("❌ MySQL连接失败！请检查以下配置：");
            System.err.println("URL: " + URL);
            System.err.println("User: " + USER);
            System.err.println("错误信息：" + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 关闭资源（无ResultSet）
     */
    public static void close(Connection conn, PreparedStatement pstmt) {
        close(conn, pstmt, null);
    }

    /**
     * 关闭资源（含ResultSet）
     */
    public static void close(Connection conn, PreparedStatement pstmt, ResultSet rs) {
        try {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
            if (conn != null && !conn.isClosed()) conn.close();
        } catch (SQLException e) {
            System.err.println("❌ 数据库资源关闭失败！");
            e.printStackTrace();
        }
    }

    /**
     * 开启事务
     */
    public static void beginTransaction(Connection conn) {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.setAutoCommit(false);
                System.out.println("✅ 事务已开启");
            }
        } catch (SQLException e) {
            System.err.println("❌ 开启事务失败！");
            e.printStackTrace();
        }
    }

    /**
     * 提交事务
     */
    public static void commit(Connection conn) {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.commit();
                conn.setAutoCommit(true);
                System.out.println("✅ 事务已提交");
            }
        } catch (SQLException e) {
            System.err.println("❌ 提交事务失败！");
            e.printStackTrace();
        }
    }

    /**
     * 回滚事务
     */
    public static void rollback(Connection conn) {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.rollback();
                conn.setAutoCommit(true);
                System.out.println("✅ 事务已回滚");
            }
        } catch (SQLException e) {
            System.err.println("❌ 回滚事务失败！");
            e.printStackTrace();
        }
    }

    /**
     * 测试MySQL连接（用于调试）
     */
    public static void testConnection() {
        Connection conn = null;
        try {
            conn = getConnection();
            if (conn != null && !conn.isClosed()) {
                System.out.println("=== 🎉 MySQL连接测试成功 ===");
                System.out.println("数据库: " + conn.getCatalog());
                System.out.println("数据库版本: " + conn.getMetaData().getDatabaseProductVersion());
                System.out.println("驱动版本: " + conn.getMetaData().getDriverVersion());
            } else {
                System.err.println("=== ❌ MySQL连接测试失败 ===");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
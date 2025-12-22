package util;

import java.sql.*;

/**
 * SQL Server数据库连接工具类
 */
public class DBUtil {
    // 数据库连接参数（根据你的实际环境修改）
    private static final String DRIVER_CLASS = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    private static final String DB_URL = "jdbc:sqlserver://localhost:1433;DatabaseName=StudentPartTimeJobDB;encrypt=true;trustServerCertificate=true";
    private static final String DB_USER = "sa";  // 你的SQL Server账号
    private static final String DB_PASSWORD = "123456";  // 你的账号密码

    // 加载驱动（静态代码块，仅执行一次）
    static {
        try {
            Class.forName(DRIVER_CLASS);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("加载SQL Server驱动失败", e);
        }
    }

    /**
     * 获取数据库连接
     */
    public static Connection getConnection() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        } catch (SQLException e) {
            throw new RuntimeException("获取数据库连接失败", e);
        }
        return conn;
    }

    /**
     * 关闭数据库连接
     */
    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                if (!conn.isClosed()) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 关闭连接+预处理语句
     */
    public static void close(Connection conn, java.sql.PreparedStatement pstmt) {
        closeConnection(conn);
        if (pstmt != null) {
            try {
                pstmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void commit(Connection conn) {
    }

    public static void rollback(Connection conn) {
    }

    public static void beginTransaction(Connection conn) {
    }

    public static void close(Connection conn, PreparedStatement pstmt, ResultSet rs) {
    }
}
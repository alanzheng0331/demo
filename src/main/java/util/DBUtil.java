package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 数据库连接工具类：负责获取连接、关闭资源
 */
public class DBUtil {
    // 数据库连接参数（请替换为你的SQL Server配置）
    private static final String URL = "jdbc:sqlserver://localhost:1433;DatabaseName=StudentPartTimeJobDB;encrypt=false";
    private static final String USER = "sa";  // 你的数据库用户名
    private static final String PASSWORD = "123456";  // 你的数据库密码

    // 静态代码块：加载SQL Server驱动（程序启动时执行一次）
    static {
        try {
            // 加载驱动类（SQL Server JDBC驱动的全类名）
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            System.out.println("✅ SQL Server驱动加载成功");
        } catch (ClassNotFoundException e) {
            System.err.println("❌ 驱动加载失败：" + e.getMessage());
            // 驱动加载失败会导致后续无法连接数据库，直接退出程序
            System.exit(1);
        }
    }

    /**
     * 获取数据库连接
     *
     * @return 数据库连接对象（Connection）
     */
    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            System.err.println("❌ 连接失败：" + e.getMessage());
            return null;
        }
    }

    public static void close(Connection conn, PreparedStatement ps, ResultSet rs) {
        try {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            if (conn != null) conn.close();
        } catch (SQLException e) {
            System.err.println("❌ 资源关闭失败：" + e.getMessage());
        }
    }
}
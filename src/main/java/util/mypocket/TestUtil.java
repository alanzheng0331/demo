package util.mypocket;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 极简版数据库工具类：仅处理连接、SQL执行、结果集解析（按你的要求，只做核心事情）
 */
public class TestUtil {
    // SQL Server连接配置（替换为你的实际配置）
    private static final String URL = "jdbc:sqlserver://localhost:1433;databaseName=StudentPartTimeJobDB;encrypt=true;trustServerCertificate=true;";
    private static final String USER = "sa";
    private static final String PASSWORD = "123456";
    private static final String DRIVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";

    // 加载驱动（仅执行一次）
    static {
        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("驱动加载失败", e);
        }
    }

    /**
     * 执行SQL（查询返回结果集，增删改返回空列表）
     * @param sql 要执行的SQL语句
     * @return List<Map>：查询结果（key=列名，value=值）；增删改返回空列表
     * @throws SQLException SQL执行异常
     */
    public static List<Map<String, Object>> executeSql(String sql) throws SQLException {
        List<Map<String, Object>> result = new ArrayList<>();
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            // 1. 获取连接（Util的核心工作1）
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
            // 2. 创建执行对象（Util的核心工作2）
            stmt = conn.createStatement();

            // 3. 执行SQL（Util的核心工作3）
            if (sql.trim().toUpperCase().startsWith("SELECT")) {
                // 查询：解析结果集
                rs = stmt.executeQuery(sql);
                ResultSetMetaData metaData = rs.getMetaData();
                int columnCount = metaData.getColumnCount();
                while (rs.next()) {
                    Map<String, Object> row = new HashMap<>();
                    for (int i = 1; i <= columnCount; i++) {
                        row.put(metaData.getColumnName(i), rs.getObject(i));
                    }
                    result.add(row);
                }
            } else {
                // 增删改：执行更新
                stmt.executeUpdate(sql);
            }
        } finally {
            // 4. 关闭资源（Util的核心工作4）
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        }
        return result;
    }
}
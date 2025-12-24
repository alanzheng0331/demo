package util;

/**
 * 数据库配置管理
 */
public class DBConfig {
    // 当前使用的数据库类型
    private static String currentDB = "mysql"; // 默认使用MySQL

    // 数据库类型常量
    public static final String DB_MYSQL = "mysql";
    public static final String DB_SQLSERVER = "sqlserver";

    /**
     * 获取当前使用的数据库类型
     */
    public static String getCurrentDB() {
        return currentDB;
    }

    /**
     * 切换数据库类型
     */
    public static void switchDB(String dbType) {
        if (DB_MYSQL.equals(dbType) || DB_SQLSERVER.equals(dbType)) {
            currentDB = dbType;
            System.out.println("已切换数据库到：" + dbType);
        } else {
            throw new IllegalArgumentException("不支持的数据库类型：" + dbType);
        }
    }

    /**
     * 测试所有数据库连接
     */

    /**
     * 获取当前数据库的详细信息
     */
    public static String getCurrentDatabaseInfo() {
        if (DB_MYSQL.equals(currentDB)) {
            return "MySQL: studentparttimejobdb";
        } else {
            return "SQL Server: StudentPartTimeJobDB";
        }
    }
}
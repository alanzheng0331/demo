package util;

import org.apache.commons.dbutils.DbUtils;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 数据库连接工具类
 */
public class DBUtil {
    /**
     * 获取数据库连接
     */
    public static Connection getConnection() {
        Connection conn = null;
        try {
            Context ctx = new InitialContext();
            DataSource ds = (DataSource) ctx.lookup("java:comp/env/jdbc/part_job");
            conn = ds.getConnection();
            conn.setAutoCommit(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conn;
    }

    /**
     * 关闭资源
     */
    public static void close(Connection conn, PreparedStatement pstmt, ResultSet rs) throws SQLException {
        DbUtils.close(conn);
    }

    public static void close(Connection conn, PreparedStatement pstmt) throws SQLException {
        DbUtils.close(conn);
    }

    /**
     * 开启事务
     */
    public static void beginTransaction(Connection conn) {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.setAutoCommit(false);
            }
        } catch (SQLException e) {
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
            }
        } catch (SQLException e) {
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
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
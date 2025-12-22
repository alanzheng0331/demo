package dao;

import entity.Collect;
import util.DBUtil;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

/**
 * 收藏数据访问层
 */
public class CollectDao {
    /**
     * 添加收藏
     */
    public int addCollect(Long userId, Long jobId) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        String sql = "INSERT INTO t_collect(user_id, job_id, create_time) VALUES(?, ?, ?)";
        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, userId);
            pstmt.setLong(2, jobId);
            pstmt.setDate(3, new java.sql.Date(new Date().getTime()));
            return pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        } finally {
            DBUtil.close(conn, pstmt);
        }
    }

    /**
     * 取消收藏
     */
    public int deleteCollect(Long userId, Long jobId) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        String sql = "DELETE FROM t_collect WHERE user_id = ? AND job_id = ?";
        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, userId);
            pstmt.setLong(2, jobId);
            return pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        } finally {
            DBUtil.close(conn, pstmt);
        }
    }

    /**
     * 检查是否已收藏
     */
    public boolean isCollected(Long userId, Long jobId) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql = "SELECT COUNT(*) FROM t_collect WHERE user_id = ? AND job_id = ?";
        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, userId);
            pstmt.setLong(2, jobId);
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
     * 获取用户收藏列表
     */
    public List<entity.Collect> findCollectList(Long userId) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql = "SELECT c.*, j.title, j.salary, j.company_name FROM t_collect c LEFT JOIN t_job j ON c.job_id = j.id WHERE c.user_id = ? ORDER BY c.create_time DESC";
        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, userId);
            rs = pstmt.executeQuery();
            return new BeanListHandler<>(Collect.class).handle(rs);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            DBUtil.close(conn, pstmt, rs);
        }
    }
}

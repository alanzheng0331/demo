package dao;

import entity.Apply;
import util.DBUtil;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

/**
 * 投递记录数据访问层
 */
public class ApplyDao {
    /**
     * 添加投递记录
     */
    public int addApply(Apply apply, Connection conn) throws SQLException {
        PreparedStatement pstmt = null;
        String sql = "INSERT INTO t_apply(user_id, job_id, resume_id, attach_info, apply_status, apply_time, update_time) " +
                "VALUES(?, ?, ?, ?, ?, ?, ?)";
        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, apply.getUserId());
            pstmt.setLong(2, apply.getJobId());
            pstmt.setLong(3, apply.getResumeId());
            pstmt.setString(4, apply.getAttachInfo());
            pstmt.setString(5, apply.getApplyStatus());
            pstmt.setDate(6, new java.sql.Date(new Date().getTime()));
            pstmt.setDate(7, new java.sql.Date(new Date().getTime()));
            return pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        } finally {
            DBUtil.close(null, pstmt);
        }
    }

    /**
     * 检查是否已投递
     */
    public boolean exists(Long userId, Long jobId) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql = "SELECT COUNT(*) FROM t_apply WHERE user_id = ? AND job_id = ?";
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
     * 获取用户投递记录列表
     */
    public List<Apply> findApplyList(Long userId, String status) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        StringBuilder sql = new StringBuilder("SELECT * FROM t_apply WHERE user_id = ?");
        if (status != null && !status.isEmpty()) {
            sql.append(" AND apply_status = ?");
        }
        sql.append(" ORDER BY apply_time DESC");

        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql.toString());
            pstmt.setLong(1, userId);
            if (status != null && !status.isEmpty()) {
                pstmt.setString(2, status);
            }
            rs = pstmt.executeQuery();
            return new BeanListHandler<>(Apply.class).handle(rs);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            DBUtil.close(conn, pstmt, rs);
        }
    }

    /**
     * 更新投递状态
     */
    public int updateStatus(Long applyId, String status) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        String sql = "UPDATE t_apply SET apply_status = ?, update_time = ? WHERE id = ?";
        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, status);
            pstmt.setDate(2, new java.sql.Date(new Date().getTime()));
            pstmt.setLong(3, applyId);
            return pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        } finally {
            DBUtil.close(conn, pstmt);
        }
    }
}
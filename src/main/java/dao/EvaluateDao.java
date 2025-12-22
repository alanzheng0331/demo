package dao;

import entity.Evaluate;
import util.DBUtil;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

/**
 * 评价数据访问层
 */
public class EvaluateDao {
    /**
     * 添加评价
     */
    public int addEvaluate(Evaluate evaluate) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        String sql = "INSERT INTO t_evaluate(user_id, job_id, salary_score, real_score, env_score, content, create_time) " +
                "VALUES(?, ?, ?, ?, ?, ?, ?)";
        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, evaluate.getUserId());
            pstmt.setLong(2, evaluate.getJobId());
            pstmt.setInt(3, evaluate.getSalaryScore());
            pstmt.setInt(4, evaluate.getRealScore());
            pstmt.setInt(5, evaluate.getEnvScore());
            pstmt.setString(6, evaluate.getContent());
            pstmt.setDate(7, new java.sql.Date(new Date().getTime()));
            return pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        } finally {
            DBUtil.close(conn, pstmt);
        }
    }

    /**
     * 回复评价
     */
    public int replyEvaluate(Long evaluateId, String reply) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        String sql = "UPDATE t_evaluate SET reply = ?, reply_time = ? WHERE id = ?";
        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, reply);
            pstmt.setDate(2, new java.sql.Date(new Date().getTime()));
            pstmt.setLong(3, evaluateId);
            return pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        } finally {
            DBUtil.close(conn, pstmt);
        }
    }

    /**
     * 获取兼职的评价列表
     */
    public List<Evaluate> findEvaluateList(Long jobId) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql = "SELECT * FROM t_evaluate WHERE job_id = ? ORDER BY create_time DESC";
        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, jobId);
            rs = pstmt.executeQuery();
            return new BeanListHandler<>(Evaluate.class).handle(rs);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            DBUtil.close(conn, pstmt, rs);
        }
    }

    /**
     * 获取用户的评价统计
     */
    public Evaluate getEvaluateStats(Long userId) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql = "SELECT AVG(salary_score) as salaryScore, AVG(real_score) as realScore, AVG(env_score) as envScore " +
                "FROM t_evaluate WHERE user_id = ?";
        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, userId);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                Evaluate stats = new Evaluate();
                stats.setSalaryScore(rs.getInt("salaryScore"));
                stats.setRealScore(rs.getInt("realScore"));
                stats.setEnvScore(rs.getInt("envScore"));
                return stats;
            }
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            DBUtil.close(conn, pstmt, rs);
        }
    }
}

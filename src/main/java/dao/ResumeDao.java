package dao;

import entity.Resume;
import util.DBUtil;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

/**
 * 简历数据访问层
 */
public class ResumeDao {
    /**
     * 创建简历
     */
    public int addResume(Resume resume) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        String sql = "INSERT INTO t_resume(user_id, resume_name, file_path, file_name, education, work_experience, skill_tags, expected_salary, is_default, create_time, update_time) " +
                "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, resume.getUserId());
            pstmt.setString(2, resume.getResumeName());
            pstmt.setString(3, resume.getFilePath());
            pstmt.setString(4, resume.getFileName());
            pstmt.setString(5, resume.getEducation());
            pstmt.setString(6, resume.getWorkExperience());
            pstmt.setString(7, resume.getSkillTags());
            pstmt.setString(8, resume.getExpectedSalary());
            pstmt.setInt(9, resume.getIsDefault());
            pstmt.setDate(10, new java.sql.Date(new Date().getTime()));
            pstmt.setDate(11, new java.sql.Date(new Date().getTime()));
            return pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        } finally {
            DBUtil.close(conn, pstmt);
        }
    }

    /**
     * 获取用户简历列表
     */
    public List<Resume> findResumeList(Long userId) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql = "SELECT * FROM t_resume WHERE user_id = ? ORDER BY is_default DESC, create_time DESC";
        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, userId);
            rs = pstmt.executeQuery();
            return new BeanListHandler<>(Resume.class).handle(rs);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            DBUtil.close(conn, pstmt, rs);
        }
    }

    /**
     * 根据ID查询简历
     */
    public Resume findById(Long resumeId) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql = "SELECT * FROM t_resume WHERE id = ?";
        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, resumeId);
            rs = pstmt.executeQuery();
            List<Resume> list = new BeanListHandler<>(Resume.class).handle(rs);
            return list != null && !list.isEmpty() ? list.get(0) : null;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            DBUtil.close(conn, pstmt, rs);
        }
    }

    /**
     * 更新简历
     */
    public int updateResume(Resume resume) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        String sql = "UPDATE t_resume SET resume_name = ?, education = ?, work_experience = ?, skill_tags = ?, expected_salary = ?, is_default = ?, update_time = ? WHERE id = ? AND user_id = ?";
        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, resume.getResumeName());
            pstmt.setString(2, resume.getEducation());
            pstmt.setString(3, resume.getWorkExperience());
            pstmt.setString(4, resume.getSkillTags());
            pstmt.setString(5, resume.getExpectedSalary());
            pstmt.setInt(6, resume.getIsDefault());
            pstmt.setDate(7, new java.sql.Date(new Date().getTime()));
            pstmt.setLong(8, resume.getId());
            pstmt.setLong(9, resume.getUserId());
            return pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        } finally {
            DBUtil.close(conn, pstmt);
        }
    }

    /**
     * 删除简历
     */
    public int deleteResume(Long resumeId, Long userId) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        String sql = "DELETE FROM t_resume WHERE id = ? AND user_id = ?";
        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, resumeId);
            pstmt.setLong(2, userId);
            return pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        } finally {
            DBUtil.close(conn, pstmt);
        }
    }

    /**
     * 取消其他简历的默认状态
     */
    public int cancelDefault(Long userId, Long excludeId) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        String sql = "UPDATE t_resume SET is_default = 0, update_time = ? WHERE user_id = ? AND id != ?";
        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setDate(1, new java.sql.Date(new Date().getTime()));
            pstmt.setLong(2, userId);
            pstmt.setLong(3, excludeId);
            return pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        } finally {
            DBUtil.close(conn, pstmt);
        }
    }
}

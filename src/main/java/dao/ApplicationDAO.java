
package dao;

import entity.Application;
import util.DBUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ApplicationDAO {
    /**
     * 提交兼职申请
     */
    public boolean addApplication(Application application) {
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = DBUtil.getConnection();
            String sql = "INSERT INTO Application (" +
                    "job_id, student_id, apply_time, status" +
                    ") VALUES (?, ?, GETDATE(), 0)";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, application.getJobId());
            ps.setInt(2, application.getStudentId());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("提交申请失败：" + e.getMessage());
            return false;
        } finally {
            DBUtil.close(conn, ps, null);
        }
    }

    /**
     * 查询学生的申请记录
     */
    public List<Application> getApplicationsByStudentId(Integer studentId) {
        List<Application> applications = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT * FROM Application WHERE student_id = ? ORDER BY apply_time DESC";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, studentId);
            rs = ps.executeQuery();

            while (rs.next()) {
                Application app = new Application();
                app.setApplicationId(rs.getInt("application_id"));
                app.setJobId(rs.getInt("job_id"));
                app.setStudentId(rs.getInt("student_id"));
                app.setApplyTime(rs.getDate("apply_time"));
                app.setStatus(rs.getInt("status"));
                app.setFeedback(rs.getString("feedback"));
                applications.add(app);
            }
        } catch (SQLException e) {
            System.err.println("查询申请记录失败：" + e.getMessage());
        } finally {
            DBUtil.close(conn, ps, rs);
        }
        return applications;
    }
}
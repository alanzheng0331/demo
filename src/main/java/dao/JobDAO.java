package dao;


import entity.PartTimeJob;
import util.DBUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JobDAO {
    /**
     * 查询所有可用岗位（状态为招募中）
     */
    public List<PartTimeJob> getAllAvailableJobs() {
        List<PartTimeJob> jobs = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT * FROM PartTimeJob WHERE status = 1";
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                PartTimeJob job = new PartTimeJob();
                job.setJobId(rs.getInt("job_id"));
                job.setJobName(rs.getString("job_name"));
                job.setCategoryId(rs.getInt("category_id"));
                job.setEmployer(rs.getString("employer"));
                job.setLocation(rs.getString("location"));
                job.setSalary(rs.getDouble("salary"));
                job.setRequirements(rs.getString("requirements"));
                job.setDescription(rs.getString("description"));
                job.setStartDate(rs.getDate("start_date"));
                job.setEndDate(rs.getDate("end_date"));
                job.setStatus(rs.getInt("status"));
                jobs.add(job);
            }
        } catch (SQLException e) {
            System.err.println("查询岗位失败：" + e.getMessage());
        } finally {
            DBUtil.close(conn, ps, rs);
        }
        return jobs;
    }

    /**
     * 根据ID查询岗位详情
     */
    public PartTimeJob getJobById(Integer jobId) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT * FROM PartTimeJob WHERE job_id = ?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, jobId);
            rs = ps.executeQuery();

            if (rs.next()) {
                PartTimeJob job = new PartTimeJob();
                job.setJobId(rs.getInt("job_id"));
                job.setJobName(rs.getString("job_name"));
                job.setCategoryId(rs.getInt("category_id"));
                job.setEmployer(rs.getString("employer"));
                job.setLocation(rs.getString("location"));
                job.setSalary(rs.getDouble("salary"));
                job.setRequirements(rs.getString("requirements"));
                job.setDescription(rs.getString("description"));
                job.setStartDate(rs.getDate("start_date"));
                job.setEndDate(rs.getDate("end_date"));
                job.setStatus(rs.getInt("status"));
                return job;
            }
        } catch (SQLException e) {
            System.err.println("查询岗位详情失败：" + e.getMessage());
        } finally {
            DBUtil.close(conn, ps, rs);
        }
        return null;
    }
}
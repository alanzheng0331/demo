package dao;


import constant.JobConstants;
import entity.Job;
import util.DBUtil;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * 兼职数据访问层
 * 负责兼职信息的查询、报名人数更新等数据库操作
 * 支持多条件筛选、分页、排序，兼容事务/非事务两种调用方式
 */
public class JobDao {

    /**
     * 分页查询兼职列表（支持多条件筛选、关键词搜索、排序）
     * @param province 省份筛选（可为空）
     * @param city 城市筛选（可为空）
     * @param district 区县筛选（可为空）
     * @param type 兼职类型筛选（可为空，值参考JobConstants.JOB_TYPE_*）
     * @param salaryRange 薪资区间筛选（可为空，值参考JobConstants.SALARY_RANGE_*）
     * @param timeRange 发布时间筛选（可为空，值参考JobConstants.TIME_RANGE_*）
     * @param keyword 关键词（标题/公司/内容，可为空）
     * @param sortType 排序方式（值参考JobConstants.SORT_TYPE_*）
     * @param pageNum 页码（从1开始）
     * @param pageSize 每页条数
     * @return 符合条件的兼职列表（空则返回null）
     */
    public List<Job> findJobList(String province, String city, String district,
                                 String type, String salaryRange, String timeRange,
                                 String keyword, String sortType, int pageNum, int pageSize) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        // 动态拼接SQL（仅查询已发布的兼职）
        StringBuilder sql = new StringBuilder("SELECT * FROM t_job WHERE status = ?");
        // 1. 地区筛选
        if (province != null && !province.isEmpty()) {
            sql.append(" AND province = ?");
        }
        if (city != null && !city.isEmpty()) {
            sql.append(" AND city = ?");
        }
        if (district != null && !district.isEmpty()) {
            sql.append(" AND district = ?");
        }
        // 2. 类型筛选
        if (type != null && !type.isEmpty()) {
            sql.append(" AND type = ?");
        }
        // 3. 薪资区间筛选
        if (salaryRange != null && !salaryRange.isEmpty()) {
            if (JobConstants.SALARY_RANGE_0_100.equals(salaryRange)) {
                sql.append(" AND salary BETWEEN 0 AND 100");
            } else if (JobConstants.SALARY_RANGE_100_200.equals(salaryRange)) {
                sql.append(" AND salary BETWEEN 100 AND 200");
            } else if (JobConstants.SALARY_RANGE_200_PLUS.equals(salaryRange)) {
                sql.append(" AND salary > 200");
            }
        }
        // 4. 发布时间筛选
        if (timeRange != null && !timeRange.isEmpty()) {
            if (JobConstants.TIME_RANGE_TODAY.equals(timeRange)) {
                sql.append(" AND CONVERT(date, publish_time) = CONVERT(date, GETDATE())");
            } else if (JobConstants.TIME_RANGE_3DAYS.equals(timeRange)) {
                sql.append(" AND publish_time >= DATEADD(day, -3, GETDATE())");
            } else if (JobConstants.TIME_RANGE_1WEEK.equals(timeRange)) {
                sql.append(" AND publish_time >= DATEADD(week, -1, GETDATE())");
            }
        }
        // 5. 关键词搜索（标题/公司名称/工作内容）
        if (keyword != null && !keyword.isEmpty()) {
            sql.append(" AND (title LIKE ? OR company_name LIKE ? OR content LIKE ?)");
        }
        // 6. 排序（默认按发布时间降序）
        if (JobConstants.SORT_TYPE_LATEST.equals(sortType)) {
            sql.append(" ORDER BY publish_time DESC");
        } else if (JobConstants.SORT_TYPE_HIGHEST_SALARY.equals(sortType)) {
            sql.append(" ORDER BY salary DESC");
        } else {
            sql.append(" ORDER BY publish_time DESC");
        }
        // 7. 分页（SQL Server 分页语法）
        sql.append(" OFFSET ? ROWS FETCH NEXT ? ROWS ONLY");

        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql.toString());

            // 设置SQL参数（按拼接顺序）
            int paramIndex = 1;
            pstmt.setString(paramIndex++, JobConstants.JOB_STATUS_PUBLISHED); // 固定筛选已发布
            // 地区参数
            if (province != null && !province.isEmpty()) {
                pstmt.setString(paramIndex++, province);
            }
            if (city != null && !city.isEmpty()) {
                pstmt.setString(paramIndex++, city);
            }
            if (district != null && !district.isEmpty()) {
                pstmt.setString(paramIndex++, district);
            }
            // 类型参数
            if (type != null && !type.isEmpty()) {
                pstmt.setString(paramIndex++, type);
            }
            // 关键词参数
            if (keyword != null && !keyword.isEmpty()) {
                String likeKeyword = "%" + keyword + "%";
                pstmt.setString(paramIndex++, likeKeyword);
                pstmt.setString(paramIndex++, likeKeyword);
                pstmt.setString(paramIndex++, likeKeyword);
            }
            // 分页参数
            pstmt.setInt(paramIndex++, (pageNum - 1) * pageSize); // 跳过的行数
            pstmt.setInt(paramIndex++, pageSize); // 每页显示行数

            // 执行查询并转换为Job列表
            rs = pstmt.executeQuery();
            return new BeanListHandler<>(Job.class).handle(rs);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            // 关闭数据库连接资源
            DBUtil.close(conn, pstmt, rs);
        }
    }

    /**
     * 根据ID查询兼职详情（仅查询已发布的兼职）
     * @param jobId 兼职ID
     * @return 兼职对象（不存在/已下架则返回null）
     */
    public Job findById(Long jobId) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql = "SELECT * FROM t_job WHERE id = ? AND status = ?";

        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, jobId);
            pstmt.setString(2, JobConstants.JOB_STATUS_PUBLISHED);

            rs = pstmt.executeQuery();
            // 优化：用BeanHandler直接获取单条数据
            return new BeanHandler<>(Job.class).handle(rs);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            DBUtil.close(conn, pstmt, rs);
        }
    }

    /**
     * 更新兼职报名人数（事务版，需外部传入连接）
     * @param jobId 兼职ID
     * @param count 新的报名人数
     * @param conn 数据库连接（由事务管理器控制）
     * @return 受影响行数（0=更新失败）
     */
    public int updateApplyCount(Long jobId, int count, Connection conn) throws SQLException {
        PreparedStatement pstmt = null;
        String sql = "UPDATE t_job SET apply_count = ? WHERE id = ?";

        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, count);
            pstmt.setLong(2, jobId);
            return pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        } finally {
            // 仅关闭Statement，连接由外部事务控制
            DBUtil.close(null, pstmt);
        }
    }

    /**
     * 更新兼职报名人数（非事务版，自动创建/关闭连接）
     * @param jobId 兼职ID
     * @param count 新的报名人数
     * @return 受影响行数（0=更新失败）
     */
    public int updateApplyCount(Long jobId, int count) throws SQLException {
        Connection conn = null;
        try {
            conn = DBUtil.getConnection();
            return updateApplyCount(jobId, count, conn);
        } finally {
            // 关闭连接
            DBUtil.close(conn, null);
        }
    }

    /**
     * 查询符合条件的兼职总数（用于分页计算总页数）
     * @param province 省份（可为空）
     * @param city 城市（可为空）
     * @param district 区县（可为空）
     * @param type 兼职类型（可为空）
     * @param salaryRange 薪资区间（可为空）
     * @param timeRange 发布时间（可为空）
     * @param keyword 关键词（可为空）
     * @return 符合条件的兼职总数
     */
    public int getJobCount(String province, String city, String district,
                           String type, String salaryRange, String timeRange, String keyword) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM t_job WHERE status = ?");

        // 拼接与findJobList一致的筛选条件（不含排序/分页）
        if (province != null && !province.isEmpty()) {
            sql.append(" AND province = ?");
        }
        if (city != null && !city.isEmpty()) {
            sql.append(" AND city = ?");
        }
        if (district != null && !district.isEmpty()) {
            sql.append(" AND district = ?");
        }
        if (type != null && !type.isEmpty()) {
            sql.append(" AND type = ?");
        }
        if (salaryRange != null && !salaryRange.isEmpty()) {
            if (JobConstants.SALARY_RANGE_0_100.equals(salaryRange)) {
                sql.append(" AND salary BETWEEN 0 AND 100");
            } else if (JobConstants.SALARY_RANGE_100_200.equals(salaryRange)) {
                sql.append(" AND salary BETWEEN 100 AND 200");
            } else if (JobConstants.SALARY_RANGE_200_PLUS.equals(salaryRange)) {
                sql.append(" AND salary > 200");
            }
        }
        if (timeRange != null && !timeRange.isEmpty()) {
            if (JobConstants.TIME_RANGE_TODAY.equals(timeRange)) {
                sql.append(" AND CONVERT(date, publish_time) = CONVERT(date, GETDATE())");
            } else if (JobConstants.TIME_RANGE_3DAYS.equals(timeRange)) {
                sql.append(" AND publish_time >= DATEADD(day, -3, GETDATE())");
            } else if (JobConstants.TIME_RANGE_1WEEK.equals(timeRange)) {
                sql.append(" AND publish_time >= DATEADD(week, -1, GETDATE())");
            }
        }
        if (keyword != null && !keyword.isEmpty()) {
            sql.append(" AND (title LIKE ? OR company_name LIKE ? OR content LIKE ?)");
        }

        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql.toString());

            // 设置参数（与findJobList逻辑一致）
            int paramIndex = 1;
            pstmt.setString(paramIndex++, JobConstants.JOB_STATUS_PUBLISHED);
            if (province != null && !province.isEmpty()) {
                pstmt.setString(paramIndex++, province);
            }
            if (city != null && !city.isEmpty()) {
                pstmt.setString(paramIndex++, city);
            }
            if (district != null && !district.isEmpty()) {
                pstmt.setString(paramIndex++, district);
            }
            if (type != null && !type.isEmpty()) {
                pstmt.setString(paramIndex++, type);
            }
            if (keyword != null && !keyword.isEmpty()) {
                String likeKeyword = "%" + keyword + "%";
                pstmt.setString(paramIndex++, likeKeyword);
                pstmt.setString(paramIndex++, likeKeyword);
                pstmt.setString(paramIndex++, likeKeyword);
            }

            // 执行查询并获取总数
            rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
            return 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        } finally {
            DBUtil.close(conn, pstmt, rs);
        }
    }
}

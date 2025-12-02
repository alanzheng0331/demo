package dao;

import entity.Student;
import util.DBUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 学生数据访问类：负责Student表的CRUD操作
 */
public class StudentDAO {
    /**
     * 根据用户名查询学生（用于登录）
     * @param username 登录用户名
     * @return 学生对象（查询不到返回null）
     */
    public Student getByUsername(String username) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            // SQL：查询状态正常的用户（status=1）
            String sql = "SELECT * FROM Student WHERE username = ? AND status = 1";
            ps = conn.prepareStatement(sql);
            ps.setString(1, username); // 填充参数
            rs = ps.executeQuery();

            if (rs.next()) { // 找到用户，封装成Student对象
                Student student = new Student();
                student.setStudentId(rs.getInt("student_id"));
                student.setUsername(rs.getString("username"));
                student.setPassword(rs.getString("password")); // 加密后的密码
                student.setStudentName(rs.getString("student_name"));
                student.setStudentNo(rs.getString("student_no"));
                student.setMajor(rs.getString("major"));
                student.setGrade(rs.getString("grade"));
                student.setPhone(rs.getString("phone"));
                student.setEmail(rs.getString("email"));
                return student;
            }
        } catch (SQLException e) {
            System.err.println("查询学生失败：" + e.getMessage());
        } finally {
            DBUtil.close(conn, ps, rs);
        }
        return null; // 查询不到或异常时返回null
    }

    /**
     * 新增学生（用于注册）
     * @param student 学生对象
     * @return true：注册成功；false：注册失败
     */
    public boolean addStudent(Student student) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBUtil.getConnection();
            // SQL：插入新学生（忽略自增字段student_id和默认值字段）
            String sql = "INSERT INTO Student (" +
                    "username, password, student_name, student_no, " +
                    "major, grade, phone, email, create_time, update_time" +
                    ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, GETDATE(), GETDATE())";
            ps = conn.prepareStatement(sql);
            // 按顺序填充参数
            ps.setString(1, student.getUsername());
            ps.setString(2, student.getPassword()); // 已加密的密码
            ps.setString(3, student.getStudentName());
            ps.setString(4, student.getStudentNo());
            ps.setString(5, student.getMajor());
            ps.setString(6, student.getGrade());
            ps.setString(7, student.getPhone());
            ps.setString(8, student.getEmail());

            int rows = ps.executeUpdate(); // 执行插入，返回影响行数
            return rows > 0; // 行数>0表示插入成功
        } catch (SQLException e) {
            System.err.println("新增学生失败：" + e.getMessage());
            return false;
        } finally {
            DBUtil.close(conn, ps, null);
        }
    }

    /**
     * 检查用户名是否已存在（注册时用）
     * @param username 待检查的用户名
     * @return true：已存在；false：不存在
     */
    public boolean usernameExists(String username) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT * FROM Student WHERE username = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            rs = ps.executeQuery();
            return rs.next(); // 有结果表示用户名已存在
        } catch (SQLException e) {
            System.err.println("检查用户名失败：" + e.getMessage());
            return false;
        } finally {
            DBUtil.close(conn, ps, rs);
        }
    }
    /**
     * 更新学生信息
     */
    public boolean updateStudent(Student student) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBUtil.getConnection();
            String sql = "UPDATE Student SET " +
                    "student_name = ?, major = ?, grade = ?, " +
                    "phone = ?, email = ?, update_time = GETDATE() " +
                    "WHERE student_id = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, student.getStudentName());
            ps.setString(2, student.getMajor());
            ps.setString(3, student.getGrade());
            ps.setString(4, student.getPhone());
            ps.setString(5, student.getEmail());
            ps.setInt(6, student.getStudentId());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("更新学生信息失败：" + e.getMessage());
            return false;
        } finally {
            DBUtil.close(conn, ps, null);
        }
    }
}
package servlet;

import util.JsonUtil;
import util.Result;
import util.MysqlDBUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 求职者登录Servlet（适配Gson工具类）
 */
@WebServlet("/user/login") // 对应前端AJAX请求路径
public class UserLoginServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        // 设置响应格式为JSON，防止中文乱码
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter writer = response.getWriter();

        // 1. 获取前端传递的参数
        String phone = request.getParameter("phone");
        String password = request.getParameter("password");
        String role = request.getParameter("role");

        // 2. 参数校验
        if (phone == null || phone.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            String errorJson = JsonUtil.toJson(Result.error("手机号和密码不能为空"));
            writer.write(errorJson);
            writer.flush();
            writer.close();
            return;
        }
        if (phone.trim().length() != 11) {
            String errorJson = JsonUtil.toJson(Result.error("请输入11位有效手机号"));
            writer.write(errorJson);
            writer.flush();
            writer.close();
            return;
        }

        // 3. 连接数据库校验登录信息
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = MysqlDBUtil.getConnection();
            // 匹配t_user表查询
            String sql = "SELECT id, name, phone, role, status FROM t_user WHERE phone = ? AND password = ? AND role = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, phone.trim());
            pstmt.setString(2, password.trim());
            pstmt.setString(3, role != null ? role : "job_seeker");

            rs = pstmt.executeQuery();
            if (rs.next()) {
                // 校验账号状态
                int status = rs.getInt("status");
                if (status != 1) {
                    String errorJson = JsonUtil.toJson(Result.error("账号已被禁用，请联系管理员"));
                    writer.write(errorJson);
                    writer.flush();
                    writer.close();
                    return;
                }

                // 登录成功，存入Session
                HttpSession session = request.getSession();
                session.setAttribute("userId", rs.getLong("id"));
                session.setAttribute("userName", rs.getString("name"));
                session.setAttribute("userPhone", rs.getString("phone"));
                session.setAttribute("userRole", rs.getString("role"));

                // 返回成功响应
                String successJson = JsonUtil.toJson(Result.success("登录成功"));
                writer.write(successJson);
            } else {
                // 手机号或密码错误
                String errorJson = JsonUtil.toJson(Result.error("手机号或密码错误"));
                writer.write(errorJson);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            String errorJson = JsonUtil.toJson(Result.error("数据库异常，登录失败"));
            writer.write(errorJson);
        } finally {
            // 关闭资源
            MysqlDBUtil.close(conn, pstmt, rs);
            // 关闭输出流
            writer.flush();
            writer.close();
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // GET请求直接返回错误
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter writer = response.getWriter();
        String errorJson = JsonUtil.toJson(Result.error("不支持GET请求，请使用POST请求"));
        writer.write(errorJson);
        writer.flush();
        writer.close();
    }
}
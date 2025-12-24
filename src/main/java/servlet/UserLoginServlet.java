package servlet;

import dao.UserDao;
import entity.User;
import util.JsonUtil;
import util.Result;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

/**
 * 求职者登录Servlet
 */
public class UserLoginServlet extends HttpServlet {

    private UserDao userDao = new UserDao(); // 注入UserDao

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter writer = response.getWriter();

        // 1. 获取参数
        String phone = request.getParameter("phone");
        String password = request.getParameter("password");
        String role = request.getParameter("role");

        // 2. 参数校验（保持不变）
        if (phone == null || phone.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            writer.write(JsonUtil.toJson(Result.error("手机号和密码不能为空")));
            closeWriter(writer);
            return;
        }
        if (phone.trim().length() != 11) {
            writer.write(JsonUtil.toJson(Result.error("请输入11位有效手机号")));
            closeWriter(writer);
            return;
        }

        // 3. 复用UserDao查询用户
        try {
            User user = userDao.findByPhone(phone.trim());
            if (user == null) {
                // 手机号不存在
                writer.write(JsonUtil.toJson(Result.error("手机号或密码错误")));
            } else {
                // 校验密码、角色、账号状态
                if (!password.trim().equals(user.getPassword())) {
                    writer.write(JsonUtil.toJson(Result.error("手机号或密码错误")));
                } else if (!user.getRole().equals(role != null ? role : "job_seeker")) {
                    writer.write(JsonUtil.toJson(Result.error("账号角色不匹配")));
                } else if (user.getStatus() != 1) {
                    writer.write(JsonUtil.toJson(Result.error("账号已被禁用，请联系管理员")));
                } else {
                    // 登录成功，存入Session
                    HttpSession session = request.getSession();
                    // 原有Session存储（可保留，也可删除，后续直接通过loginUser获取即可）
                    session.setAttribute("userId", user.getId());
                    session.setAttribute("userName", user.getName());
                    session.setAttribute("userPhone", user.getPhone());
                    session.setAttribute("userRole", user.getRole());

                    // ******************** 新增：将完整User对象存入Session，key为loginUser ********************
                    session.setAttribute("loginUser", user);

                    writer.write(JsonUtil.toJson(Result.success("登录成功")));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            writer.write(JsonUtil.toJson(Result.error("数据库异常，登录失败")));
        } finally {
            closeWriter(writer);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter writer = response.getWriter();
        writer.write(JsonUtil.toJson(Result.error("不支持GET请求，请使用POST请求")));
        closeWriter(writer);
    }

    // 封装关闭流的逻辑，避免重复代码
    private void closeWriter(PrintWriter writer) {
        if (writer != null) {
            writer.flush();
            writer.close();
        }
    }
}
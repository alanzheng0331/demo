package servlet;

import service.CompanyService;
import service.impl.CompanyServiceImpl;
import util.JsonUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * 企业登录Servlet
 */
@WebServlet("/company/login")
public class CompanyLoginServlet extends HttpServlet {
    private final CompanyService companyService = new CompanyServiceImpl();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 设置请求和响应编码，防止中文乱码
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");

        // 1. 获取参数并做空值校验（新增：避免空指针）
        String phone = request.getParameter("phone");
        String pwd = request.getParameter("pwd");

        // 空值校验
        if (phone == null || phone.trim().isEmpty()) {
            Map<String, Object> resp = new HashMap<>();
            resp.put("code", 400);
            resp.put("msg", "手机号不能为空");
            response.getWriter().write(JsonUtil.toJson(resp));
            return; // 终止方法执行
        }
        if (pwd == null || pwd.trim().isEmpty()) {
            Map<String, Object> resp = new HashMap<>();
            resp.put("code", 400);
            resp.put("msg", "密码不能为空");
            response.getWriter().write(JsonUtil.toJson(resp));
            return;
        }

        try {
            // 2. 调用业务层处理登录
            String res = companyService.login(phone.trim(), pwd.trim());

            // 3. 处理登录结果
            Map<String, Object> resp = new HashMap<>();
            if ("success".equals(res)) {
                // 登录成功，存储企业手机号到Session
                HttpSession session = request.getSession();
                session.setAttribute("companyPhone", phone.trim());
                session.setMaxInactiveInterval(30 * 60); // Session 30分钟过期

                resp.put("code", 200);
                resp.put("msg", "登录成功");
            } else {
                // 登录失败（如密码错误、账号不存在）
                resp.put("code", 400);
                resp.put("msg", res);
            }
            // 响应JSON数据
            response.getWriter().write(JsonUtil.toJson(resp));

        } catch (SQLException e) {
            // 数据库异常处理
            e.printStackTrace(); // 控制台打印异常，便于调试
            Map<String, Object> resp = new HashMap<>();
            resp.put("code", 500);
            resp.put("msg", "服务器异常：" + e.getMessage());
            response.getWriter().write(JsonUtil.toJson(resp));
        } catch (Exception e) {
            // 新增：捕获其他未知异常，避免程序崩溃
            e.printStackTrace();
            Map<String, Object> resp = new HashMap<>();
            resp.put("code", 500);
            resp.put("msg", "系统异常，请稍后重试");
            response.getWriter().write(JsonUtil.toJson(resp));
        }
    }

    // 新增：处理GET请求（防止直接访问Servlet报错）
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        Map<String, Object> resp = new HashMap<>();
        resp.put("code", 405);
        resp.put("msg", "不支持GET请求，请使用POST请求");
        response.getWriter().write(JsonUtil.toJson(resp));
    }
}
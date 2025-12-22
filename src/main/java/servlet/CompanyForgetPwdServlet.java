package servlet;

import service.CompanyService;
import service.impl.CompanyServiceImpl;
import util.JsonUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * 企业忘记密码Servlet（发送验证码+重置密码）
 */
@WebServlet("/company/forgetPwd")
public class CompanyForgetPwdServlet extends HttpServlet {
    private final CompanyService companyService = new CompanyServiceImpl();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");

        // 1. 获取参数并做空值校验
        String action = request.getParameter("action");
        String phone = request.getParameter("phone");

        // 空值校验：操作类型、手机号
        if (action == null || action.trim().isEmpty()) {
            Map<String, Object> resp = new HashMap<>();
            resp.put("code", 400);
            resp.put("msg", "操作类型不能为空");
            response.getWriter().write(JsonUtil.toJson(resp));
            return;
        }
        if (phone == null || phone.trim().isEmpty()) {
            Map<String, Object> resp = new HashMap<>();
            resp.put("code", 400);
            resp.put("msg", "手机号不能为空");
            response.getWriter().write(JsonUtil.toJson(resp));
            return;
        }

        try {
            if ("sendCode".equals(action.trim())) {
                // 发送验证码
                String res = companyService.sendForgetPwdCode(phone.trim());
                Map<String, Object> resp = new HashMap<>();
                resp.put("code", res.equals("success") ? 200 : 400);
                resp.put("msg", res.equals("success") ? "验证码已发送（模拟）" : res);
                response.getWriter().write(JsonUtil.toJson(resp));

            } else if ("resetPwd".equals(action.trim())) {
                // 重置密码：补充code和newPwd的空值校验
                String code = request.getParameter("code");
                String newPwd = request.getParameter("newPwd");

                if (code == null || code.trim().isEmpty()) {
                    Map<String, Object> resp = new HashMap<>();
                    resp.put("code", 400);
                    resp.put("msg", "验证码不能为空");
                    response.getWriter().write(JsonUtil.toJson(resp));
                    return;
                }
                if (newPwd == null || newPwd.trim().isEmpty()) {
                    Map<String, Object> resp = new HashMap<>();
                    resp.put("code", 400);
                    resp.put("msg", "新密码不能为空");
                    response.getWriter().write(JsonUtil.toJson(resp));
                    return;
                }

                // 调用业务层重置密码
                String res = companyService.resetPwd(phone.trim(), code.trim(), newPwd.trim());
                Map<String, Object> resp = new HashMap<>();
                resp.put("code", res.equals("success") ? 200 : 400);
                resp.put("msg", res.equals("success") ? "密码重置成功" : res);
                response.getWriter().write(JsonUtil.toJson(resp));

            } else {
                // 无效操作类型
                Map<String, Object> resp = new HashMap<>();
                resp.put("code", 400);
                resp.put("msg", "无效的操作类型");
                response.getWriter().write(JsonUtil.toJson(resp));
            }

        } catch (SQLException e) {
            // 数据库异常
            e.printStackTrace();
            Map<String, Object> resp = new HashMap<>();
            resp.put("code", 500);
            resp.put("msg", "服务器异常：" + e.getMessage());
            response.getWriter().write(JsonUtil.toJson(resp));
        } catch (Exception e) {
            // 未知异常
            e.printStackTrace();
            Map<String, Object> resp = new HashMap<>();
            resp.put("code", 500);
            resp.put("msg", "系统异常，请稍后重试");
            response.getWriter().write(JsonUtil.toJson(resp));
        }
    }

    // 处理GET请求（避免直接访问报错）
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        Map<String, Object> resp = new HashMap<>();
        resp.put("code", 405);
        resp.put("msg", "不支持GET请求，请使用POST请求");
        response.getWriter().write(JsonUtil.toJson(resp));
    }
}
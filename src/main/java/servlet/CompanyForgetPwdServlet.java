package servlet;

import service.CompanyService;
import service.impl.CompanyServiceImpl;
import util.JsonUtil;
import util.MD5Util; // 新增：密码加密工具类
import entity.Company;

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
 * 企业忘记密码Servlet（适配SQL Server字段：company_name/credit_t_code/company_pwd）
 */
@WebServlet("/company/forgetPwd")
public class CompanyForgetPwdServlet extends HttpServlet {
    private final CompanyService companyService = new CompanyServiceImpl();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");

        // 1. 获取参数（适配前端jsp的name属性）
        String action = request.getParameter("action");
        String companyName = request.getParameter("companyName");
        String creditCode = request.getParameter("creditCode"); // 前端传的是信用代码，对应数据库credit_t_code

        // 2. 空值校验
        Map<String, Object> resp = new HashMap<>();
        if (action == null || action.trim().isEmpty()) {
            resp.put("code", 400);
            resp.put("msg", "操作类型不能为空");
            response.getWriter().write(JsonUtil.toJson(resp));
            return;
        }
        if (companyName == null || companyName.trim().isEmpty()) {
            resp.put("code", 400);
            resp.put("msg", "企业名称不能为空");
            response.getWriter().write(JsonUtil.toJson(resp));
            return;
        }
        if (creditCode == null || creditCode.trim().isEmpty()) {
            resp.put("code", 400);
            resp.put("msg", "社会信用代码不能为空");
            response.getWriter().write(JsonUtil.toJson(resp));
            return;
        }

        try {
            // 3. 核心校验：企业名称+社会信用代码（credit_t_code）是否匹配
            Company company = companyService.getCompanyByNameAndCreditCode(companyName.trim(), creditCode.trim());
            if (company == null) {
                resp.put("code", 400);
                resp.put("msg", "企业名称与社会信用代码不匹配");
                response.getWriter().write(JsonUtil.toJson(resp));
                return;
            }

            // 4. 重置密码逻辑（仅保留resetPwd操作，适配数据库company_pwd字段）
            if ("resetPwd".equals(action.trim())) {
                String newPwd = request.getParameter("newPassword");
                String confirmPwd = request.getParameter("confirmPassword");

                // 密码校验
                if (newPwd == null || newPwd.trim().isEmpty()) {
                    resp.put("code", 400);
                    resp.put("msg", "新密码不能为空");
                    response.getWriter().write(JsonUtil.toJson(resp));
                    return;
                }
                if (confirmPwd == null || confirmPwd.trim().isEmpty()) {
                    resp.put("code", 400);
                    resp.put("msg", "确认密码不能为空");
                    response.getWriter().write(JsonUtil.toJson(resp));
                    return;
                }
                if (!newPwd.trim().equals(confirmPwd.trim())) {
                    resp.put("code", 400);
                    resp.put("msg", "两次输入的密码不一致");
                    response.getWriter().write(JsonUtil.toJson(resp));
                    return;
                }
                // 密码长度校验（适配数据库密码规则）
                if (newPwd.trim().length() < 6) {
                    resp.put("code", 400);
                    resp.put("msg", "密码长度不能少于6位");
                    response.getWriter().write(JsonUtil.toJson(resp));
                    return;
                }

                // 5. 调用业务层重置密码（加密后更新company_pwd字段）
                String encryptPwd = MD5Util.md5(newPwd.trim()); // 密码MD5加密（和注册逻辑一致）
                int updateRows = companyService.resetPwdByCompanyName(companyName.trim(), encryptPwd);

                if (updateRows > 0) {
                    resp.put("code", 200);
                    resp.put("msg", "密码重置成功，请使用新密码登录");
                } else {
                    resp.put("code", 400);
                    resp.put("msg", "密码重置失败，请稍后重试");
                }
                response.getWriter().write(JsonUtil.toJson(resp));

            } else {
                resp.put("code", 400);
                resp.put("msg", "无效的操作类型，仅支持resetPwd");
                response.getWriter().write(JsonUtil.toJson(resp));
            }

        } catch (SQLException e) {
            e.printStackTrace();
            resp.put("code", 500);
            resp.put("msg", "数据库异常：" + e.getMessage());
            response.getWriter().write(JsonUtil.toJson(resp));
        } catch (Exception e) {
            e.printStackTrace();
            resp.put("code", 500);
            resp.put("msg", "系统异常，请稍后重试");
            response.getWriter().write(JsonUtil.toJson(resp));
        }
    }

    // 处理GET请求
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        Map<String, Object> resp = new HashMap<>();
        resp.put("code", 405);
        resp.put("msg", "不支持GET请求，请使用POST请求");
        response.getWriter().write(JsonUtil.toJson(resp));
    }
}
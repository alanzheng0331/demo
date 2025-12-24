package servlet;

import service.CompanyService;
import service.impl.CompanyServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;

/**
 * 企业登录Servlet
 * 适配说明：
 * 1. 完全匹配CompanyServiceImpl的登录返回值逻辑
 * 2. 统一页面跳转路径为companyIndex.jsp（与业务层提示逻辑对齐）
 * 3. 强化日志输出，便于排查登录问题
 */
@WebServlet("/company/login")
public class CompanyLoginServlet extends HttpServlet {
    private final CompanyService companyService = new CompanyServiceImpl();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 1. 统一编码设置，避免中文乱码（与业务层日志输出匹配）
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");

        // 2. 获取并预处理表单参数（严格匹配Service层的trim逻辑）
        String companyName = request.getParameter("adminUsername");
        String companyPwd = request.getParameter("adminPassword");
        String companyNameTrim = companyName == null ? "" : companyName.trim();
        String companyPwdTrim = companyPwd == null ? "" : companyPwd.trim();

        System.out.println("=== 企业登录Servlet接收参数 ===");
        System.out.println("原始企业名称：" + companyName + "，处理后：" + companyNameTrim);
        System.out.println("原始密码长度：" + (companyPwd == null ? 0 : companyPwd.length()) + "，处理后长度：" + companyPwdTrim.length());

        // 3. 空值校验（与Service层互补，保持提示语一致）
        if (companyNameTrim.isEmpty()) {
            handleLoginError(request, response, "企业名称不能为空！", companyNameTrim);
            return;
        }
        if (companyPwdTrim.isEmpty()) {
            handleLoginError(request, response, "企业密码不能为空！", companyNameTrim);
            return;
        }

        try {
            // 4. 调用Service层登录方法（参数直接传处理后的值，匹配Service层逻辑）
            String loginResult = companyService.login(companyNameTrim, companyPwdTrim);
            System.out.println("=== 企业[" + companyNameTrim + "]登录结果 ===" + loginResult);

            // 5. 严格匹配Service层返回值，分场景处理
            if ("success".equals(loginResult)) {
                // 5.1 登录成功：存储完整Session信息
                HttpSession session = request.getSession();
                session.setAttribute("company_name", companyNameTrim);
                session.setAttribute("login_status", "success");
                session.setMaxInactiveInterval(30 * 60); // 30分钟会话过期

                // 跳转企业首页（与原有逻辑一致）
                response.sendRedirect(request.getContextPath() + "/company/companyIndex.jsp");
            } else {
                // 5.2 登录失败：直接传递Service层的提示语（密码错误/审核中/禁用等）
                handleLoginError(request, response, loginResult, companyNameTrim);
            }

        } catch (SQLException e) {
            // 数据库异常：精准提示，便于定位问题
            e.printStackTrace();
            handleLoginError(request, response, "登录失败：数据库访问异常，请联系管理员！", companyNameTrim);
        } catch (Exception e) {
            // 通用异常：友好提示，隐藏底层错误
            e.printStackTrace();
            handleLoginError(request, response, "登录失败：系统暂不可用，请稍后重试！", companyNameTrim);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // GET请求处理：统一跳转到企业首页（与POST逻辑的跳转路径保持一致）
        // 移除原有"未登录跳登录页"逻辑，避免路径不一致问题
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("company_name") != null) {
            System.out.println("已登录状态，跳转企业首页");
            response.sendRedirect(request.getContextPath() + "/company/companyIndex.jsp");
        } else {
            System.out.println("未登录状态，跳转企业首页（展示登录表单）");
            response.sendRedirect(request.getContextPath() + "/company/companyIndex.jsp");
        }
    }

    /**
     * 统一处理登录失败逻辑（与Service层提示语完全兼容）
     * @param request 请求对象
     * @param response 响应对象
     * @param errorMsg 错误提示（直接使用Service层返回值）
     * @param companyName 企业名称（回显到页面）
     */
    private void handleLoginError(HttpServletRequest request, HttpServletResponse response, String errorMsg, String companyName) throws ServletException, IOException {
        // 设置错误信息和回显参数，与前端页面取值逻辑匹配
        request.setAttribute("errorMsg", errorMsg);
        request.setAttribute("companyName", companyName);

        // 转发到企业首页（保持与原有代码的页面路径一致）
        request.getRequestDispatcher("/company/companyIndex.jsp").forward(request, response);
    }
}
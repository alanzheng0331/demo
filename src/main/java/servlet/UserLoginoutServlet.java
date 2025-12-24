package servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class UserLoginoutServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 获取当前会话并销毁
        HttpSession session = request.getSession(false);
        if (session != null) {
            // 移除会话中的用户信息
            session.removeAttribute("loginUser");
            // 销毁整个会话
            session.invalidate();
        }
        // 重定向到登录页面
        response.sendRedirect(request.getContextPath() + "/user/index.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 支持POST请求退出（如果前端用POST方式）
        doGet(request, response);
    }
}
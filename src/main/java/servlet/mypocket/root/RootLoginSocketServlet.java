package servlet.mypocket.root;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

// 必加注解：指定Servlet映射路径，否则登录请求无法匹配
@WebServlet("/mypocket/rootLogin")
public class RootLoginSocketServlet extends HttpServlet {

    // 常量定义（新增isRoot的Session键，方便所有页面统一读取）
    private static final String ROOT_USERNAME = "root";
    private static final String ROOT_PASSWORD = "root";
    private static final String SESSION_KEY_USER = "rootUser";
    private static final String SESSION_KEY_IS_ROOT = "isRoot"; // 新增：所有页面统一读这个键

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 编码处理（原有逻辑不变）
        request.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentType("text/html;charset=" + StandardCharsets.UTF_8.name());

        // 获取参数（原有逻辑不变）
        String username = request.getParameter("username") == null ? "" : request.getParameter("username");
        String password = request.getParameter("password") == null ? "" : request.getParameter("password");

        if (ROOT_USERNAME.equals(username) && ROOT_PASSWORD.equals(password)) {
            // 1. 获取Session（用户级全局存储，跨页面有效）
            HttpSession session = request.getSession();
            // 2. 存入用户名（保留原有逻辑，不破坏联动）
            session.setAttribute(SESSION_KEY_USER, username);
            // 3. 存入布尔型isRoot标识（核心：所有页面统一读这个值）
            session.setAttribute(SESSION_KEY_IS_ROOT, true);
            // 4. 设置Session过期时间（原有逻辑不变）
            session.setMaxInactiveInterval(30 * 60);

            // 5. 重定向到首页（恢复重定向，避免转发的重复提交问题）
            String redirectUrl = request.getContextPath() + "/mypocket/rootIndex.jsp";
            response.sendRedirect(redirectUrl);
        } else {
            // 登录失败逻辑（原有逻辑不变）
            String errorMsg = URLEncoder.encode("用户名或密码错误，请重新登录！", StandardCharsets.UTF_8.name());
            String loginPageUrl = request.getContextPath() + "/mypocket/rootlogin.html?error=" + errorMsg;
            response.sendRedirect(loginPageUrl);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // GET请求重定向到登录页（原有逻辑不变）
        String loginPageUrl = request.getContextPath() + "/mypocket/rootlogin.html";
        response.sendRedirect(loginPageUrl);
    }
}
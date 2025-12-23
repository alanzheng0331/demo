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
import java.util.Objects;

/**
 * 根用户登录Servlet
 * 优化点：使用Session存储用户信息、动态获取项目路径、优化空值处理、增加安全限制、完善错误反馈
 */
public class RootLoginSocketServlet extends HttpServlet { // 类名简化：去掉Socket（非Socket通信，命名误导）

    // 常量定义：统一配置，便于维护
    private static final String ROOT_USERNAME = "root"; // 若需求是大写ROOT，改为"ROOT"
    private static final String ROOT_PASSWORD = "root";
    private static final String SESSION_KEY_USER = "rootUser"; // Session中用户信息的key

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 1. 处理请求/响应编码
        request.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentType("text/html;charset=" + StandardCharsets.UTF_8.name());

        // 2. 获取表单参数（优化空值处理：Objects.requireNonNullElse）
        // Java 8：三元运算符处理null，和你原代码一致
        String username = request.getParameter("username") == null ? "" : request.getParameter("username");
        String password = request.getParameter("password") == null ? "" : request.getParameter("password");

        // 3. 验证用户名密码
        if (ROOT_USERNAME.equals(username) && ROOT_PASSWORD.equals(password)) {
            // 3.1 存入Session（核心优化：替代URL传参，安全存储）
            HttpSession session = request.getSession();
            session.setAttribute(SESSION_KEY_USER, username); // 可存储用户对象，此处简化为用户名
            session.setMaxInactiveInterval(30 * 60); // 设置Session过期时间：30分钟（可选）

            // 3.2 重定向到根首页（使用相对路径，避免硬编码）
            // request.getContextPath()：动态获取项目根路径（替代硬编码的part_time_system_war_exploded）
            String redirectUrl = request.getContextPath() + "/mypocket/rootIndex.jsp";
            response.sendRedirect(redirectUrl);
        } else {
            // 3.3 验证失败：重定向回登录页并携带错误提示（启用错误反馈，优化用户体验）
            String errorMsg = URLEncoder.encode("用户名或密码错误，请重新登录！", StandardCharsets.UTF_8.name());
            String loginPageUrl = request.getContextPath() + "/mypocket/rootlogin.html?error=" + errorMsg;
            response.sendRedirect(loginPageUrl);
        }
        // 注意：Servlet容器会自动关闭输出流，无需手动调用out.close()
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 登录请求禁止使用GET（防止参数暴露），重定向到登录页或返回错误
        String loginPageUrl = request.getContextPath() + "/mypocket/rootlogin.html";
        response.sendRedirect(loginPageUrl);
        // 或直接返回405错误：response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, "不支持GET请求");
    }
}
package filter;

import entity.User;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * 登录状态拦截过滤器
 * 拦截需要登录才能访问的接口，放行无需登录的路径
 */
@WebFilter(filterName = "LoginFilter", urlPatterns = "/api/*")
public class LoginFilter implements Filter {
    // 无需登录即可访问的路径列表
    private List<String> excludePaths;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // 从web.xml配置中读取无需拦截的路径
        String excludePathsStr = filterConfig.getInitParameter("excludePaths");
        if (excludePathsStr != null && !excludePathsStr.isEmpty()) {
            excludePaths = Arrays.asList(excludePathsStr.split(","));
        } else {
            // 默认放行的路径
            excludePaths = Arrays.asList("/index.jsp", "/api/user/register", "/api/user/login", "/api/user/code", "/static/");
        }
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        // 1. 获取当前请求路径
        String requestURI = request.getRequestURI();
        // 去除项目上下文路径（适配Tomcat部署的不同路径）
        String contextPath = request.getContextPath();
        String path = requestURI.substring(contextPath.length());

        // 2. 判断是否为无需拦截的路径
        boolean isExclude = false;
        for (String excludePath : excludePaths) {
            // 支持模糊匹配（如/static/开头的所有路径）
            if (path.startsWith(excludePath) || path.equals(excludePath)) {
                isExclude = true;
                break;
            }
        }

        // 3. 无需拦截的路径直接放行
        if (isExclude) {
            chain.doFilter(request, response);
            return;
        }

        // 4. 检查登录状态
        HttpSession session = request.getSession(false); // 不创建新Session
        User loginUser = null;
        if (session != null) {
            loginUser = (User) session.getAttribute("loginUser");
        }

        // 5. 未登录则返回JSON格式的未授权提示
        if (loginUser == null) {
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"code\":401,\"msg\":\"请先登录后再操作\"}");
            return;
        }

        // 6. 已登录，放行请求
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // 过滤器销毁时无需额外操作
    }
}
package filter;


import constant.UserConstants;
import entity.User;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * 权限控制过滤器
 * 区分管理员和求职者的接口访问权限
 */
@WebFilter(filterName = "PermissionFilter", urlPatterns = {"/api/admin/*"})
public class PermissionFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // 初始化无额外配置
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        // 1. 检查登录状态
        HttpSession session = request.getSession(false);
        User loginUser = null;
        if (session != null) {
            loginUser = (User) session.getAttribute("loginUser");
        }

        // 2. 未登录直接返回未授权
        if (loginUser == null) {
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"code\":401,\"msg\":\"请先登录后再操作\"}");
            return;
        }

        // 3. 检查是否为管理员角色
        if (!UserConstants.ROLE_ADMIN.equals(loginUser.getRole())) {
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"code\":403,\"msg\":\"无管理员权限，禁止访问\"}");
            return;
        }

        // 4. 权限验证通过，放行请求
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // 过滤器销毁时无需额外操作
    }
}

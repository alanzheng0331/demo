package filter;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

/**
 * 字符编码过滤器
 */
@WebFilter(filterName = "CharacterEncodingFilter", urlPatterns = "/*")
public class CharacterEncodingFilter implements Filter {
    private String encoding = "UTF-8";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // 从配置获取编码格式
        String configEncoding = filterConfig.getInitParameter("encoding");
        if (configEncoding != null && !configEncoding.isEmpty()) {
            this.encoding = configEncoding;
        }
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        // 设置请求编码
        request.setCharacterEncoding(encoding);
        // 设置响应编码
        response.setCharacterEncoding(encoding);
        response.setContentType("text/html;charset=" + encoding);
        // 继续执行过滤器链
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // 销毁时释放资源
    }
}
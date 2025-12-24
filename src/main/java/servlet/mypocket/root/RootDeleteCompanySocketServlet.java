package servlet.mypocket.root;

import connection.mypocket.AllOfImpl;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

// 明确为删除公司的Servlet，所有文案/逻辑适配公司删除场景
public class RootDeleteCompanySocketServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 1. 统一编码处理，避免中文乱码
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        try {
            // 步骤1：获取前端传入的【公司】删除ID（核心参数）
            String companyDeleteId = request.getParameter("id");
            // 空值校验：明确提示是公司ID
            if (companyDeleteId == null || companyDeleteId.trim().isEmpty()) {
                out.println("RootDeleteCompanySocketServlet - 错误：删除的公司ID不能为空！");
                return;
            }

            // 步骤2：构建指定格式的Map（适配公司删除的参数格式）
            Map<String, Object> MY_WAY = new HashMap<>();
            MY_WAY.put("id", companyDeleteId.trim()); // 存入公司ID（字段名按业务要求，若需改可调整）

            // 步骤3：调用已实现的【删除公司】方法（核心适配点）
            String deleteResult = new AllOfImpl().RootDeleteCompany(MY_WAY);

            // 调试输出：全部改为公司相关文案，便于排查问题
            // 步骤4：重定向到指定的展示页面（保持你要求的路径，也可优化为动态路径）
            // 优化建议：response.sendRedirect(request.getContextPath() + "/RootShowSocketServlet");
            response.sendRedirect("http://localhost:8080/part_time_system_war_exploded/RootShowSocketServlet");
        } catch (Exception e) {
            // 异常提示：明确是公司删除失败
            e.printStackTrace(); // 后台打印异常栈，便于调试
            out.println("RootDeleteCompanySocketServlet - 公司删除失败：" + e.getMessage());
        } finally {
            out.close(); // 确保输出流关闭，避免资源泄漏
        }


    }
}
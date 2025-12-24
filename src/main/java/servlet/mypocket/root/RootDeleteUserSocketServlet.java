package servlet.mypocket.root;

import connection.mypocket.AllOfImpl;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

// 仅保留Servlet核心逻辑，不重复定义已实现的删除方法
public class RootDeleteUserSocketServlet extends HttpServlet {

    // 假设你已通过接口/父类拥有 RootDeleteUser/RootDeleteCompany 方法，此处不再重写

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 1. 处理编码，避免中文乱码
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        try {
            // 步骤1：获取前端传入的删除ID（核心参数）
            String deleteId = request.getParameter("id");
            if (deleteId == null || deleteId.trim().isEmpty()) {
                out.println("RootDeleteUserSocketServlet - 错误：删除ID不能为空！");
                return;
            }

            // 步骤2：构建指定格式的Map（key=字段名，value=字段值）
            Map<String, Object> MY_WAY = new HashMap<>();
            MY_WAY.put("id", deleteId.trim()); // 按要求存入删除所需的ID

            // 步骤3：直接调用已实现的删除方法（你已写好的 RootDeleteUser）
            // 注：此处直接调用你已实现的方法，无需重新定义
            String deleteResult = new AllOfImpl().RootDeleteUser(MY_WAY);

            // 临时输出调试信息（可根据需要删除）
            // 步骤4：重定向到指定页面（严格按你的要求）
            response.sendRedirect("http://localhost:8080/part_time_system_war_exploded/RootShowSocketServlet");
        } catch (Exception e) {
            // 异常处理：打印后台日志 + 给前端返回错误提示
            e.printStackTrace();
            out.println("RootDeleteUserSocketServlet - 删除失败：" + e.getMessage());
        } finally {
            out.close(); // 确保输出流关闭，避免资源泄漏
        }


    }
}
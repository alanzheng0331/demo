package servlet;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;

public class LoginServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 设置响应编码，避免中文乱码
        response.setContentType("text/html;charset=UTF-8");
        // 获取输出流，向前端页面输出内容
        PrintWriter out = response.getWriter();
        out.write("您好，这是 LoginServlet 返回到页面的内容！");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 设置响应编码，避免中文乱码
        response.setContentType("text/html;charset=UTF-8");
        // 获取输出流，向前端页面输出内容
        PrintWriter out = response.getWriter();
        out.write("您提交了登录表单，这是 Servlet 处理后的响应！");
    }
}
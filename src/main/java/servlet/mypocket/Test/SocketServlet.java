package servlet.mypocket.Test;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder; // 处理中文参数编码
import java.util.Scanner;

/**
 * 接收登录表单参数并传递到Test.html页面
 * 映射路径：/servletTest
 */
public class SocketServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 1. 处理编码，避免中文乱码
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        // 2. 获取表单参数（处理null值）
        String username = request.getParameter("username") == null ? "" : request.getParameter("username");
        String password = request.getParameter("password") == null ? "" : request.getParameter("password");
        String adminUsername = request.getParameter("adminUsername") == null ? "" : request.getParameter("adminUsername");
        String adminPassword = request.getParameter("adminPassword") == null ? "" : request.getParameter("adminPassword");

        // 3. 极简输出参数（可选，可删除，因为重定向后此输出会被覆盖）
        out.println("===== 表单提交的参数 =====<br>");
        out.println("用户登录 - 用户名：" + username + "<br>");
        out.println("用户登录 - 密码：" + password + "<br>");
        out.println("------------------------<br>");
        out.println("企业登录 - 企业名称：" + adminUsername + "<br>");
        out.println("企业登录 - 企业密码：" + adminPassword + "<br>");

        // 4. 拼接参数到重定向URL（使用URLEncoder处理中文，避免乱码）
        // 注意：密码等敏感数据不建议通过URL传递（会暴露在地址栏）
        String redirectUrl = "http://localhost:8080/part_time_system_war_exploded/mypocket/index.html"
                + "?username=" + URLEncoder.encode(username, "UTF-8")
                + "&password=" + URLEncoder.encode(password, "UTF-8")
                + "&adminUsername=" + URLEncoder.encode(adminUsername, "UTF-8")
                + "&adminPassword=" + URLEncoder.encode(adminPassword, "UTF-8");

        // 5. 重定向到Test.html并携带参数
        response.sendRedirect(redirectUrl);

        // 关闭输出流（规范操作，重定向后此语句实际已无影响）
        out.close();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
package servlet;

import util.JsonUtil;
import util.Result;
import util.MysqlDBUtil;

import javax.servlet.ServletException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 企业登录Servlet（适配Gson工具类）
 */

public class CompanyLoginServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        // 设置响应格式为JSON，防止中文乱码
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter writer = response.getWriter();

        // 1. 获取前端传递的参数
        String companyName = request.getParameter("companyName");
        String companyPwd = request.getParameter("companyPwd");

        // 2. 参数校验
        if (companyName == null || companyName.trim().isEmpty()) {
            String errorJson = JsonUtil.toJson(Result.error("企业名称不能为空"));
            writer.write(errorJson);
            writer.flush();
            writer.close();
            return;
        }
        if (companyPwd == null || companyPwd.trim().isEmpty()) {
            String errorJson = JsonUtil.toJson(Result.error("企业密码不能为空"));
            writer.write(errorJson);
            writer.flush();
            writer.close();
            return;
        }
        if (companyName.trim().length() < 2 || companyName.trim().length() > 200) {
            String errorJson = JsonUtil.toJson(Result.error("企业名称长度需在2-200个字符之间"));
            writer.write(errorJson);
            writer.flush();
            writer.close();
            return;
        }
        if (companyPwd.trim().length() < 6 || companyPwd.trim().length() > 32) {
            String errorJson = JsonUtil.toJson(Result.error("企业密码长度需在6-32个字符之间"));
            writer.write(errorJson);
            writer.flush();
            writer.close();
            return;
        }

        // 3. 连接数据库校验登录信息
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = MysqlDBUtil.getConnection();
            // 匹配t_company表查询
            String sql = "SELECT id, company_name, company_phone, status FROM t_company WHERE company_name = ? AND company_pwd = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, companyName.trim());
            pstmt.setString(2, companyPwd.trim());

            rs = pstmt.executeQuery();
            if (rs.next()) {
                // 校验企业账号状态
                Integer status = rs.getInt("status");
                if (status == null || status != 1) {
                    String errorJson = JsonUtil.toJson(Result.error("企业账号已被禁用或待审核，请等待管理员处理"));
                    writer.write(errorJson);
                    writer.flush();
                    writer.close();
                    return;
                }

                // 登录成功，存入Session
                HttpSession session = request.getSession();
                session.setAttribute("companyId", rs.getLong("id"));
                session.setAttribute("companyName", rs.getString("company_name"));
                session.setAttribute("companyPhone", rs.getString("company_phone"));

                // 返回成功响应
                String successJson = JsonUtil.toJson(Result.success("企业登录成功"));
                writer.write(successJson);
            } else {
                // 企业名称或密码错误
                String errorJson = JsonUtil.toJson(Result.error("企业名称或密码错误"));
                writer.write(errorJson);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            String errorJson = JsonUtil.toJson(Result.error("数据库异常，企业登录失败"));
            writer.write(errorJson);
        } finally {
            // 关闭资源
            MysqlDBUtil.close(conn, pstmt, rs);
            // 关闭输出流
            writer.flush();
            writer.close();
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // GET请求直接返回错误
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter writer = response.getWriter();
        String errorJson = JsonUtil.toJson(Result.error("不支持GET请求，请使用POST请求"));
        writer.write(errorJson);
        writer.flush();
        writer.close();
    }
}
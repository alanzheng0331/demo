package servlet;

import util.DBUtil;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 企业注册Servlet（带数据库存储）
 */
@WebServlet("/companyRegisterServlet")
public class CompanyRegisterServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        // 1. 获取表单参数
        String companyName = request.getParameter("companyName");
        String creditCode = request.getParameter("creditCode");
        String legalPersonName = request.getParameter("legalPersonName");
        String companyAddress = request.getParameter("companyAddress");
        String companyPhone = request.getParameter("companyPhone");
        String companyPassword = request.getParameter("companyPassword");
        String companyConfirmPassword = request.getParameter("companyConfirmPassword");
        String verifyCode = request.getParameter("companyVerifyCode");

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            // 2. 后端校验（原有逻辑不变）
            if (companyName == null || companyName.trim().isEmpty()) {
                throw new Exception("企业名称不能为空");
            }
            if (creditCode == null || creditCode.trim().isEmpty()) {
                throw new Exception("统一社会信用代码不能为空");
            }
            if (!companyPassword.equals(companyConfirmPassword)) {
                throw new Exception("两次输入的密码不一致");
            }
            // 验证码校验
            HttpSession session = request.getSession();
            String sessionVerifyCode = (String) session.getAttribute("verifyCode");
            if (sessionVerifyCode == null || !sessionVerifyCode.equalsIgnoreCase(verifyCode)) {
                throw new Exception("验证码输入错误");
            }

            // 3. 数据库操作：插入企业信息
            conn = DBUtil.getConnection();
            // 预编译SQL（防止SQL注入）
            String sql = "INSERT INTO CompanyInfo(companyName, creditCode, legalPersonName, companyAddress, companyPhone, companyPassword) VALUES(?, ?, ?, ?, ?, ?)";
            pstmt = conn.prepareStatement(sql);
            // 设置参数（注意：参数索引从1开始）
            pstmt.setString(1, companyName);
            pstmt.setString(2, creditCode);
            pstmt.setString(3, legalPersonName);
            pstmt.setString(4, companyAddress);
            pstmt.setString(5, companyPhone);
            pstmt.setString(6, companyPassword);  // 实际项目中建议加密存储（如MD5）

            // 执行插入
            int rows = pstmt.executeUpdate();
            if (rows <= 0) {
                throw new Exception("注册失败，请重试");
            }

            // 4. 注册成功
            request.setAttribute("msg", "企业注册成功！请等待管理员审核后登录");
            request.setAttribute("msgType", "success");
            request.getRequestDispatcher("/login.jsp").forward(request, response);

        } catch (SQLException e) {
            // 处理数据库异常（如社会信用代码重复）
            if (e.getMessage().contains("UNIQUE KEY") || e.getMessage().contains("唯一键")) {
                request.setAttribute("msg", "该统一社会信用代码已注册");
            } else {
                request.setAttribute("msg", "数据库错误：" + e.getMessage());
            }
            request.setAttribute("msgType", "error");
            // 回显表单数据
            request.setAttribute("companyName", companyName);
            request.setAttribute("creditCode", creditCode);
            request.setAttribute("legalPersonName", legalPersonName);
            request.setAttribute("companyAddress", companyAddress);
            request.setAttribute("companyPhone", companyPhone);
            request.getRequestDispatcher("/register.jsp").forward(request, response);
        } catch (Exception e) {
            // 其他异常
            request.setAttribute("msg", e.getMessage());
            request.setAttribute("msgType", "error");
            request.setAttribute("companyName", companyName);
            request.setAttribute("creditCode", creditCode);
            request.setAttribute("legalPersonName", legalPersonName);
            request.setAttribute("companyAddress", companyAddress);
            request.setAttribute("companyPhone", companyPhone);
            request.getRequestDispatcher("/register.jsp").forward(request, response);
        } finally {
            // 5. 关闭数据库连接（必须执行）
            DBUtil.close(conn, pstmt);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/register.jsp").forward(request, response);
    }
}
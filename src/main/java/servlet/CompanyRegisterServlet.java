package servlet;

import entity.Company;
import service.CompanyService;
import service.impl.CompanyServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/companyRegisterServlet")
public class CompanyRegisterServlet extends HttpServlet {

    private CompanyService companyService;

    @Override
    public void init() throws ServletException {
        super.init();
        companyService = new CompanyServiceImpl();
        System.out.println("=== CompanyRegisterServlet 初始化完成 ===");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("=== 开始处理企业注册POST请求 ===");

        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");

        String inputCode = request.getParameter("companyVerifyCode");
        String sessionCode = (String) request.getSession().getAttribute("COMPANY_VERIFY_CODE");

        System.out.println("前端输入验证码：" + inputCode);
        System.out.println("Session存储验证码：" + sessionCode);

        if (inputCode == null || sessionCode == null || !inputCode.equalsIgnoreCase(sessionCode)) {
            System.out.println("注册失败：验证码错误");
            request.setAttribute("msg", "验证码错误！");
            request.setAttribute("msgType", "error");
            echoRequestParams(request);
            request.getRequestDispatcher("/company/companyRegister.jsp").forward(request, response);
            return;
        }

        String companyName = getParameterWithTrim(request, "companyName");
        String creditCode = getParameterWithTrim(request, "creditCode");
        String legalPersonName = getParameterWithTrim(request, "legalPersonName");
        String companyAddress = getParameterWithTrim(request, "companyAddress");
        String companyPhone = getParameterWithTrim(request, "companyPhone");
        String companyPassword = getParameterWithTrim(request, "companyPassword");

        System.out.println("=== 前端提交参数 ===");
        System.out.println("企业名称：" + companyName);
        System.out.println("信用代码：" + creditCode);
        System.out.println("法人姓名：" + legalPersonName);
        System.out.println("公司地址：" + companyAddress);
        System.out.println("企业电话：" + companyPhone);
        System.out.println("密码长度：" + (companyPassword == null ? 0 : companyPassword.length()));

        Company company = new Company();
        company.setCompanyName(companyName);
        company.setCreditCode(creditCode);
        company.setLegalPersonName(legalPersonName);
        company.setCompanyAddress(companyAddress);
        company.setCompanyPhone(companyPhone);
        company.setCompanyPwd(companyPassword);

        try {
            String result = companyService.register(company);

            if ("success".equals(result)) {
                System.out.println("注册成功：企业名称=" + companyName);
                request.setAttribute("msg", "企业注册成功！请等待审核");
                request.setAttribute("msgType", "success");
                clearEchoParams(request);
            } else {
                System.out.println("注册失败：" + result);
                request.setAttribute("msg", "注册失败：" + result);
                request.setAttribute("msgType", "error");
                echoRequestParams(request);
            }
        } catch (SQLException e) {
            System.err.println("=== 注册数据库异常 ===");
            e.printStackTrace();
            request.setAttribute("msg", "注册失败：数据库异常！" + e.getMessage());
            request.setAttribute("msgType", "error");
            echoRequestParams(request);
        } catch (Exception e) {
            System.err.println("=== 注册未知异常 ===");
            e.printStackTrace();
            request.setAttribute("msg", "注册失败：系统异常！");
            request.setAttribute("msgType", "error");
            echoRequestParams(request);
        }

        String targetPath = "/company/companyRegister.jsp";
        System.out.println("转发到注册页：" + targetPath);
        request.getRequestDispatcher(targetPath).forward(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("接收到GET请求，重定向到注册页");
        response.sendRedirect(request.getContextPath() + "/company/companyRegister.jsp");
    }

    private String getParameterWithTrim(HttpServletRequest request, String paramName) {
        String value = request.getParameter(paramName);
        return value == null ? "" : value.trim();
    }

    private void echoRequestParams(HttpServletRequest request) {
        request.setAttribute("companyName", getParameterWithTrim(request, "companyName"));
        request.setAttribute("creditCode", getParameterWithTrim(request, "creditCode"));
        request.setAttribute("legalPersonName", getParameterWithTrim(request, "legalPersonName"));
        request.setAttribute("companyAddress", getParameterWithTrim(request, "companyAddress"));
        request.setAttribute("companyPhone", getParameterWithTrim(request, "companyPhone"));
    }

    private void clearEchoParams(HttpServletRequest request) {
        request.setAttribute("companyName", "");
        request.setAttribute("creditCode", "");
        request.setAttribute("legalPersonName", "");
        request.setAttribute("companyAddress", "");
        request.setAttribute("companyPhone", "");
    }
}
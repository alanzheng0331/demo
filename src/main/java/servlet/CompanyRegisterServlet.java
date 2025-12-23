package servlet;

import dao.CompanyDao;
import entity.Company;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

/**
 * 企业注册Servlet（无Service层，直接调用DAO）
 */
// 修改点1：添加注解，映射访问路径

public class CompanyRegisterServlet extends HttpServlet {

    private CompanyDao companyDao; // 直接注入DAO层

    @Override
    public void init() throws ServletException {
        super.init();
        companyDao = new CompanyDao(); // 实例化DAO
        System.out.println("=== CompanyRegisterServlet 初始化完成 ===");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("=== 开始处理企业注册POST请求 ===");

        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");

        // 1. 获取验证码参数
        String inputCode = request.getParameter("companyVerifyCode");
        String sessionCode = (String) request.getSession().getAttribute("verifyCode");

        System.out.println("前端输入验证码：" + inputCode);
        System.out.println("Session存储验证码：" + sessionCode);

        // 验证码校验
        if (inputCode == null || inputCode.trim().isEmpty() || sessionCode == null || !sessionCode.equals(inputCode.trim().toLowerCase())) {
            System.out.println("注册失败：验证码错误或为空/已过期");
            request.setAttribute("msg", "验证码错误或已过期！");
            request.setAttribute("msgType", "error");
            echoRequestParams(request);
            request.getRequestDispatcher("/company/companyRegister.jsp").forward(request, response);
            return;
        }

        // 移除Session中的验证码，防止重复使用
        request.getSession().removeAttribute("verifyCode");

        // 2. 获取表单参数
        String companyName = getParameterWithTrim(request, "companyName");
        String creditCode = getParameterWithTrim(request, "creditCode");
        String legalPersonName = getParameterWithTrim(request, "legalPersonName");
        String companyAddress = getParameterWithTrim(request, "companyAddress");
        String companyPhone = getParameterWithTrim(request, "companyPhone");
        String companyPassword = getParameterWithTrim(request, "companyPassword");
        String companyConfirmPassword = getParameterWithTrim(request, "companyConfirmPassword");

        // 3. 密码一致性校验
        if (companyPassword.isEmpty() || companyConfirmPassword.isEmpty()) {
            System.out.println("注册失败：密码或确认密码为空");
            request.setAttribute("msg", "密码和确认密码不能为空！");
            request.setAttribute("msgType", "error");
            echoRequestParams(request);
            request.getRequestDispatcher("/company/companyRegister.jsp").forward(request, response);
            return;
        }
        if (!companyPassword.equals(companyConfirmPassword)) {
            System.out.println("注册失败：两次密码输入不一致");
            request.setAttribute("msg", "两次密码输入不一致！");
            request.setAttribute("msgType", "error");
            echoRequestParams(request);
            request.getRequestDispatcher("/company/companyRegister.jsp").forward(request, response);
            return;
        }

        // 打印提交参数
        System.out.println("=== 前端提交参数 ===");
        System.out.println("企业名称：" + companyName);
        System.out.println("信用代码：" + creditCode);
        System.out.println("法人姓名：" + legalPersonName);
        System.out.println("公司地址：" + companyAddress);
        System.out.println("企业电话：" + companyPhone);
        System.out.println("密码长度：" + companyPassword.length());
        System.out.println("确认密码长度：" + companyConfirmPassword.length());

        // 4. 封装企业对象
        Company company = new Company();
        company.setCompanyName(companyName);
        company.setCreditCode(creditCode);
        company.setLegalPersonName(legalPersonName);
        company.setCompanyAddress(companyAddress);
        company.setCompanyPhone(companyPhone);
        company.setCompanyPwd(companyPassword); // 明文密码

        // 5. 执行注册逻辑（直接调用DAO，替代Service层）
        try {
            // 校验信用代码是否已存在
            if (companyDao.isCreditCodeExist(creditCode)) {
                request.setAttribute("msg", "注册失败：统一社会信用代码已被注册");
                request.setAttribute("msgType", "error");
                echoRequestParams(request);
                request.getRequestDispatcher("/company/companyRegister.jsp").forward(request, response);
                return;
            }

            // 校验手机号是否已存在
            if (companyDao.findByPhone(companyPhone) != null) {
                request.setAttribute("msg", "注册失败：企业联系电话已被注册");
                request.setAttribute("msgType", "error");
                echoRequestParams(request);
                request.getRequestDispatcher("/company/companyRegister.jsp").forward(request, response);
                return;
            }

            // 执行注册
            int rows = companyDao.register(company);
            if (rows > 0) {
                System.out.println("注册成功：企业名称=" + companyName);
                // 修改点2：注册成功，重定向到登录页（替换原有转发逻辑）
                String loginUrl = request.getContextPath() + "/login/login.jsp";
                response.sendRedirect(loginUrl);
                return; // 重定向后必须return，终止后续代码
            } else {
                request.setAttribute("msg", "注册失败，请稍后重试");
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

        // 只有注册失败时，才会执行到这里，转发回注册页
        String targetPath = "/company/companyRegister.jsp";
        System.out.println("转发到注册页：" + targetPath);
        request.getRequestDispatcher(targetPath).forward(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("接收到GET请求，重定向到注册页");
        response.sendRedirect(request.getContextPath() + "/company/companyRegister.jsp");
    }

    /**
     * 获取参数并去除前后空格
     */
    private String getParameterWithTrim(HttpServletRequest request, String paramName) {
        String value = request.getParameter(paramName);
        return value == null ? "" : value.trim();
    }

    /**
     * 回显表单参数
     */
    private void echoRequestParams(HttpServletRequest request) {
        request.setAttribute("companyName", getParameterWithTrim(request, "companyName"));
        request.setAttribute("creditCode", getParameterWithTrim(request, "creditCode"));
        request.setAttribute("legalPersonName", getParameterWithTrim(request, "legalPersonName"));
        request.setAttribute("companyAddress", getParameterWithTrim(request, "companyAddress"));
        request.setAttribute("companyPhone", getParameterWithTrim(request, "companyPhone"));
    }

    /**
     * 清空表单回显参数（注册成功后重定向，此处无需实际使用）
     */
    private void clearEchoParams(HttpServletRequest request) {
        request.setAttribute("companyName", "");
        request.setAttribute("creditCode", "");
        request.setAttribute("legalPersonName", "");
        request.setAttribute("companyAddress", "");
        request.setAttribute("companyPhone", "");
    }
}
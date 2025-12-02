// src/main/java/servlet/ApplicationServlet.java
package servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import entity.Application;
import service.ApplicationService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/api/applications")
public class ApplicationServlet extends HttpServlet {
    private ApplicationService applicationService = new ApplicationService();
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json;charset=UTF-8");

        // 解析前端提交的申请数据
        Integer studentId = Integer.parseInt(req.getParameter("studentId"));
        Integer jobId = Integer.parseInt(req.getParameter("jobId"));

        boolean success = applicationService.submitApplication(studentId, jobId);
        resp.getWriter().write(objectMapper.writeValueAsString(success ? "申请成功" : "申请失败"));
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json;charset=UTF-8");

        Integer studentId = Integer.parseInt(req.getParameter("studentId"));
        List<Application> applications = applicationService.getApplicationsByStudentId(studentId);
        objectMapper.writeValue(resp.getWriter(), applications);
    }
}
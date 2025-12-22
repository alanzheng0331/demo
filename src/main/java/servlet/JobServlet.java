// src/main/java/servlet/JobServlet.java
package servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import entity.PartTimeJob;
import service.JobService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/api/jobs")
public class JobServlet extends HttpServlet {
    private JobService jobService = new JobService();
    private ObjectMapper objectMapper = new ObjectMapper(); // Jackson库用于JSON转换

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 设置响应格式为JSON
        resp.setContentType("application/json;charset=UTF-8");

        // 1. 获取请求参数（如岗位ID）
        String jobIdStr = req.getParameter("id");

        if (jobIdStr == null || jobIdStr.isEmpty()) {
            // 查询所有可用岗位
            List<PartTimeJob> jobs = jobService.getAvailableJobs();
            objectMapper.writeValue(resp.getWriter(), jobs);
        } else {
            // 根据ID查询单个岗位
            try {
                Integer jobId = Integer.parseInt(jobIdStr);
                PartTimeJob job = jobService.getJobById(jobId);
                objectMapper.writeValue(resp.getWriter(), job);
            } catch (NumberFormatException e) {
                resp.sendError(400, "无效的岗位ID");
            }
        }
    }
}
package service;


import dao.JobDao;
import entity.Job;
import constant.JobConstants;
import util.JsonUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;

/**
 * 兼职模块核心控制器
 * 覆盖：兼职列表（分页/筛选/排序）、兼职详情、搜索
 */
public class JobServlet extends HttpServlet {
    private JobDao jobDao = new JobDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
        String path = request.getPathInfo();

        try {
            switch (path) {
                case "/list": // 兼职列表（分页+筛选+排序）
                    getJobList(request, out);
                    break;
                case "/detail": // 兼职详情
                    getJobDetail(request, out);
                    break;
                case "/search": // 关键词搜索（高级搜索）
                    searchJob(request, out);
                    break;
                default:
                    out.write("{\"code\":404,\"msg\":\"接口不存在\"}");
            }
        } catch (Exception e) {
            e.printStackTrace();
            out.write("{\"code\":500,\"msg\":\"服务器异常：" + e.getMessage() + "\"}");
        } finally {
            out.flush();
            out.close();
        }
    }

    // 1. 兼职列表（分页+筛选+排序）
    private void getJobList(HttpServletRequest request, PrintWriter out) throws SQLException {
        // 获取筛选参数
        String province = request.getParameter("province");
        String city = request.getParameter("city");
        String district = request.getParameter("district");
        String type = request.getParameter("type"); // 家教/促销/服务员/实习
        String salaryRange = request.getParameter("salaryRange"); // 0-100/100-200/200+
        String timeRange = request.getParameter("timeRange"); // today/3days/1week
        String sortType = request.getParameter("sortType"); // latest/highestSalary/nearest

        // 分页参数
        int pageNum = 1;
        int pageSize = 10;
        try {
            if (request.getParameter("pageNum") != null && !request.getParameter("pageNum").isEmpty()) {
                pageNum = Integer.parseInt(request.getParameter("pageNum"));
            }
            if (request.getParameter("pageSize") != null && !request.getParameter("pageSize").isEmpty()) {
                pageSize = Integer.parseInt(request.getParameter("pageSize"));
            }
        } catch (NumberFormatException e) {
            out.write("{\"code\":400,\"msg\":\"分页参数格式错误\"}");
            return;
        }

        // 校验分页参数
        if (pageNum < 1) pageNum = 1;
        if (pageSize < 1 || pageSize > 50) pageSize = 10; // 限制最大页大小

        // 校验排序类型
        if (sortType == null || sortType.isEmpty()) {
            sortType = JobConstants.SORT_TYPE_LATEST; // 默认最新发布
        } else if (!JobConstants.SORT_TYPE_LATEST.equals(sortType)
                && !JobConstants.SORT_TYPE_HIGHEST_SALARY.equals(sortType)
                && !JobConstants.SORT_TYPE_NEAREST.equals(sortType)) {
            sortType = JobConstants.SORT_TYPE_LATEST;
        }

        // 查询数据
        List<Job> jobList = jobDao.findJobList(
                province, city, district, type, salaryRange, timeRange, null, sortType, pageNum, pageSize
        );
        int total = jobDao.getJobCount(province, city, district, type, salaryRange, timeRange, null);

        // 计算总页数
        int totalPage = total % pageSize == 0 ? total / pageSize : total / pageSize + 1;

        // 返回结果
        out.write("{\"code\":200,\"msg\":\"查询成功\",\"data\":{\"list\":" + JsonUtil.toJson(jobList) +
                ",\"total\":" + total +
                ",\"pageNum\":" + pageNum +
                ",\"pageSize\":" + pageSize +
                ",\"totalPage\":" + totalPage + "}}");
    }

    // 2. 兼职详情
    private void getJobDetail(HttpServletRequest request, PrintWriter out) throws SQLException {
        String jobId = request.getParameter("jobId");
        if (jobId == null || jobId.isEmpty()) {
            out.write("{\"code\":400,\"msg\":\"兼职ID不能为空\"}");
            return;
        }

        // 校验ID格式
        Long id;
        try {
            id = Long.parseLong(jobId);
        } catch (NumberFormatException e) {
            out.write("{\"code\":400,\"msg\":\"兼职ID格式错误\"}");
            return;
        }

        // 查询详情
        Job job = jobDao.findById(id);
        if (job == null) {
            out.write("{\"code\":400,\"msg\":\"兼职不存在或已下架\"}");
            return;
        }

        out.write("{\"code\":200,\"msg\":\"查询成功\",\"data\":" + JsonUtil.toJson(job) + "}");
    }

    // 3. 高级搜索（关键词+多条件组合）
    private void searchJob(HttpServletRequest request, PrintWriter out) throws SQLException {
        // 获取搜索参数
        String keyword = request.getParameter("keyword"); // 标题/公司/工作内容
        String province = request.getParameter("province");
        String city = request.getParameter("city");
        String type = request.getParameter("type");
        String salaryRange = request.getParameter("salaryRange");
        String timeRange = request.getParameter("timeRange");
        String sortType = request.getParameter("sortType");

        // 分页参数
        int pageNum = 1;
        int pageSize = 10;
        try {
            pageNum = request.getParameter("pageNum") != null ? Integer.parseInt(request.getParameter("pageNum")) : 1;
            pageSize = request.getParameter("pageSize") != null ? Integer.parseInt(request.getParameter("pageSize")) : 10;
        } catch (NumberFormatException e) {
            out.write("{\"code\":400,\"msg\":\"分页参数格式错误\"}");
            return;
        }

        // 校验关键词
        if (keyword == null || keyword.isEmpty()) {
            out.write("{\"code\":400,\"msg\":\"搜索关键词不能为空\"}");
            return;
        }

        // 查询数据
        List<Job> jobList = jobDao.findJobList(
                province, city, null, type, salaryRange, timeRange, keyword, sortType, pageNum, pageSize
        );
        int total = jobDao.getJobCount(province, city, null, type, salaryRange, timeRange, keyword);
        int totalPage = total % pageSize == 0 ? total / pageSize : total / pageSize + 1;

        // 返回结果
        out.write("{\"code\":200,\"msg\":\"搜索成功\",\"data\":{\"list\":" + JsonUtil.toJson(jobList) +
                ",\"total\":" + total +
                ",\"pageNum\":" + pageNum +
                ",\"pageSize\":" + pageSize +
                ",\"totalPage\":" + totalPage + "}}");
    }
}

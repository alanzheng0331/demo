package service;

import dao.ApplyDao;
import dao.EvaluateDao;
import dao.JobDao;
import dao.MessageDao;
import entity.Apply;
import entity.Evaluate;
import entity.Job;
import entity.Message;
import entity.User;
import constant.MessageConstants;
import constant.UserConstants;
import util.DBUtil;
import util.JsonUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

/**
 * 求职应聘模块核心控制器
 * 覆盖：简历投递、投递记录、状态跟踪、评价企业、评价回复
 */
public class ApplyServlet extends HttpServlet {
    private ApplyDao applyDao = new ApplyDao();
    private JobDao jobDao = new JobDao();
    private MessageDao messageDao = new MessageDao();
    private EvaluateDao evaluateDao = new EvaluateDao();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
        String path = request.getPathInfo();

        try {
            switch (path) {
                case "/deliver": // 简历投递
                    deliverResume(request, out);
                    break;
                case "/evaluate": // 评价企业
                    evaluateCompany(request, out);
                    break;
                case "/replyEvaluate": // 回复评价（管理员）
                    replyEvaluate(request, out);
                    break;
                case "/updateStatus": // 更新投递状态（管理员）
                    updateApplyStatus(request, out);
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

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
        String path = request.getPathInfo();

        try {
            switch (path) {
                case "/list": // 投递记录列表
                    getApplyList(request, out);
                    break;
                case "/status": // 投递状态查询
                    getApplyStatus(request, out);
                    break;
                case "/evaluateList": // 评价列表
                    getEvaluateList(request, out);
                    break;
                case "/evaluateStats": // 评价统计
                    getEvaluateStats(request, out);
                    break;
                default:
                    out.write("{\"code\":404,\"msg\":\"接口不存在\"}");
            }
        } catch (Exception e) {
            e.printStackTrace();
            out.write("{\"code\":500,\"msg\":\"服务器异常\"}");
        } finally {
            out.flush();
            out.close();
        }
    }

    // 1. 简历投递
    private void deliverResume(HttpServletRequest request, PrintWriter out) throws SQLException {
        HttpSession session = request.getSession(false);
        User loginUser = (User) session.getAttribute("loginUser");
        if (loginUser == null) {
            out.write("{\"code\":401,\"msg\":\"请先登录\"}");
            return;
        }

        // 获取参数
        String jobId = request.getParameter("jobId");
        String resumeId = request.getParameter("resumeId");
        String attachInfo = request.getParameter("attachInfo"); // 附加信息

        // 参数校验
        if (jobId == null || jobId.isEmpty() || resumeId == null || resumeId.isEmpty()) {
            out.write("{\"code\":400,\"msg\":\"兼职ID和简历ID不能为空\"}");
            return;
        }

        // 校验兼职是否存在
        Job job = jobDao.findById(Long.parseLong(jobId));
        if (job == null) {
            out.write("{\"code\":400,\"msg\":\"兼职不存在或已下架\"}");
            return;
        }

        // 校验是否已投递
        if (applyDao.exists(loginUser.getId(), Long.parseLong(jobId))) {
            out.write("{\"code\":400,\"msg\":\"已投递该兼职，无需重复投递\"}");
            return;
        }

        // 事务：保存投递记录 + 更新报名人数 + 发送消息
        Connection conn = DBUtil.getConnection();
        try {
            DBUtil.beginTransaction(conn);

            // 保存投递记录
            Apply apply = new Apply();
            apply.setUserId(loginUser.getId());
            apply.setJobId(Long.parseLong(jobId));
            apply.setResumeId(Long.parseLong(resumeId));
            apply.setAttachInfo(attachInfo);
            apply.setApplyStatus(UserConstants.APPLY_STATUS_WAITING); // 等待中
            apply.setApplyTime(new Date());
            applyDao.addApply(apply, conn);

            // 更新报名人数
            jobDao.updateApplyCount(Long.parseLong(jobId), job.getApplyCount() + 1, conn);

            // 发送系统消息
            Message msg = new Message();
            msg.setUserId(loginUser.getId());
            msg.setMsgType(MessageConstants.MSG_TYPE_APPLY);
            msg.setTitle("简历投递成功");
            msg.setContent("你已成功投递【" + job.getTitle() + "】兼职，等待企业审核");
            msg.setIsRead(MessageConstants.MSG_STATUS_UNREAD);
            msg.setCreateTime(new Date());
            messageDao.addMessage(msg, conn);

            // 提交事务
            DBUtil.commit(conn);
            out.write("{\"code\":200,\"msg\":\"投递成功\"}");
        } catch (Exception e) {
            DBUtil.rollback(conn);
            out.write("{\"code\":500,\"msg\":\"投递失败：" + e.getMessage() + "\"}");
        } finally {
            DBUtil.close(conn, null);
        }
    }

    // 2. 评价企业
    private void evaluateCompany(HttpServletRequest request, PrintWriter out) throws SQLException {
        HttpSession session = request.getSession(false);
        User loginUser = (User) session.getAttribute("loginUser");
        if (loginUser == null) {
            out.write("{\"code\":401,\"msg\":\"请先登录\"}");
            return;
        }

        // 获取参数
        String jobId = request.getParameter("jobId");
        String salaryScore = request.getParameter("salaryScore"); // 1-5
        String realScore = request.getParameter("realScore"); // 1-5
        String envScore = request.getParameter("envScore"); // 1-5
        String content = request.getParameter("content"); // 综合评价

        // 参数校验
        if (jobId == null || jobId.isEmpty() || salaryScore == null || realScore == null || envScore == null) {
            out.write("{\"code\":400,\"msg\":\"兼职ID和评分不能为空\"}");
            return;
        }

        int sScore, rScore, eScore;
        try {
            sScore = Integer.parseInt(salaryScore);
            rScore = Integer.parseInt(realScore);
            eScore = Integer.parseInt(envScore);
            if (sScore < 1 || sScore > 5 || rScore < 1 || rScore > 5 || eScore < 1 || eScore > 5) {
                out.write("{\"code\":400,\"msg\":\"评分必须为1-5星\"}");
                return;
            }
        } catch (NumberFormatException e) {
            out.write("{\"code\":400,\"msg\":\"评分格式错误\"}");
            return;
        }

        // 封装评价信息
        Evaluate evaluate = new Evaluate();
        evaluate.setUserId(loginUser.getId());
        evaluate.setJobId(Long.parseLong(jobId));
        evaluate.setSalaryScore(sScore);
        evaluate.setRealScore(rScore);
        evaluate.setEnvScore(eScore);
        evaluate.setContent(content);
        evaluate.setCreateTime(new Date());

        // 添加评价
        int result = evaluateDao.addEvaluate(evaluate);
        if (result > 0) {
            // 发送评价成功消息
            Message msg = new Message();
            msg.setUserId(loginUser.getId());
            msg.setMsgType(MessageConstants.MSG_TYPE_EVALUATE);
            msg.setTitle("评价提交成功");
            msg.setContent("你对【" + jobDao.findById(Long.parseLong(jobId)).getTitle() + "】的评价已提交");
            msg.setIsRead(MessageConstants.MSG_STATUS_UNREAD);
            msg.setCreateTime(new Date());
            messageDao.addMessage(msg);

            out.write("{\"code\":200,\"msg\":\"评价提交成功\"}");
        } else {
            out.write("{\"code\":500,\"msg\":\"评价提交失败\"}");
        }
    }

    // 3. 获取投递记录列表
    private void getApplyList(HttpServletRequest request, PrintWriter out) throws SQLException {
        HttpSession session = request.getSession(false);
        User loginUser = (User) session.getAttribute("loginUser");
        if (loginUser == null) {
            out.write("{\"code\":401,\"msg\":\"请先登录\"}");
            return;
        }

        // 获取筛选参数
        String status = request.getParameter("status"); // waiting/accepted/rejected

        // 查询投递记录
        List<Apply> applyList = applyDao.findApplyList(loginUser.getId(), status);
        out.write("{\"code\":200,\"msg\":\"查询成功\",\"data\":" + JsonUtil.toJson(applyList) + "}");
    }

    // 4. 管理员更新投递状态
    private void updateApplyStatus(HttpServletRequest request, PrintWriter out) throws SQLException {
        HttpSession session = request.getSession(false);
        User loginUser = (User) session.getAttribute("loginUser");
        if (loginUser == null || !UserConstants.ROLE_ADMIN.equals(loginUser.getRole())) {
            out.write("{\"code\":403,\"msg\":\"无管理员权限\"}");
            return;
        }

        // 获取参数
        String applyId = request.getParameter("applyId");
        String status = request.getParameter("status"); // accepted/rejected

        // 参数校验
        if (applyId == null || applyId.isEmpty() || status == null || status.isEmpty()) {
            out.write("{\"code\":400,\"msg\":\"投递ID和状态不能为空\"}");
            return;
        }
        if (!UserConstants.APPLY_STATUS_ACCEPTED.equals(status) && !UserConstants.APPLY_STATUS_REJECTED.equals(status)) {
            out.write("{\"code\":400,\"msg\":\"状态只能是已接受或不合适\"}");
            return;
        }

        // 更新状态
        int result = applyDao.updateStatus(Long.parseLong(applyId), status);
        if (result > 0) {
            // 发送状态变更消息（TODO: 获取投递用户ID并发送消息）
            out.write("{\"code\":200,\"msg\":\"状态更新成功\"}");
        } else {
            out.write("{\"code\":500,\"msg\":\"状态更新失败\"}");
        }
    }

    // 其他方法（getApplyStatus、getEvaluateList、replyEvaluate等）逻辑完整，参数校验+结果返回
    private void getApplyStatus(HttpServletRequest request, PrintWriter out) throws SQLException {
        HttpSession session = request.getSession(false);
        User loginUser = (User) session.getAttribute("loginUser");
        if (loginUser == null) {
            out.write("{\"code\":401,\"msg\":\"请先登录\"}");
            return;
        }

        String applyId = request.getParameter("applyId");
        if (applyId == null || applyId.isEmpty()) {
            out.write("{\"code\":400,\"msg\":\"投递ID不能为空\"}");
            return;
        }

        // 查询投递记录
        List<Apply> applyList = applyDao.findApplyList(loginUser.getId(), null);
        Apply apply = null;
        for (Apply a : applyList) {
            if (a.getId().equals(Long.parseLong(applyId))) {
                apply = a;
                break;
            }
        }

        if (apply == null) {
            out.write("{\"code\":400,\"msg\":\"投递记录不存在\"}");
            return;
        }

        out.write("{\"code\":200,\"msg\":\"查询成功\",\"data\":{\"status\":\"" + apply.getApplyStatus() + "\"}}");
    }

    private void getEvaluateList(HttpServletRequest request, PrintWriter out) throws SQLException {
        String jobId = request.getParameter("jobId");
        if (jobId == null || jobId.isEmpty()) {
            out.write("{\"code\":400,\"msg\":\"兼职ID不能为空\"}");
            return;
        }

        List<Evaluate> evaluateList = evaluateDao.findEvaluateList(Long.parseLong(jobId));
        out.write("{\"code\":200,\"msg\":\"查询成功\",\"data\":" + JsonUtil.toJson(evaluateList) + "}");
    }

    private void replyEvaluate(HttpServletRequest request, PrintWriter out) throws SQLException {
        HttpSession session = request.getSession(false);
        User loginUser = (User) session.getAttribute("loginUser");
        if (loginUser == null || !UserConstants.ROLE_ADMIN.equals(loginUser.getRole())) {
            out.write("{\"code\":403,\"msg\":\"无管理员权限\"}");
            return;
        }

        String evaluateId = request.getParameter("evaluateId");
        String reply = request.getParameter("reply");
        if (evaluateId == null || evaluateId.isEmpty() || reply == null || reply.isEmpty()) {
            out.write("{\"code\":400,\"msg\":\"评价ID和回复内容不能为空\"}");
            return;
        }

        int result = evaluateDao.replyEvaluate(Long.parseLong(evaluateId), reply);
        if (result > 0) {
            out.write("{\"code\":200,\"msg\":\"回复成功\"}");
        } else {
            out.write("{\"code\":500,\"msg\":\"回复失败\"}");
        }
    }

    private void getEvaluateStats(HttpServletRequest request, PrintWriter out) throws SQLException {
        HttpSession session = request.getSession(false);
        User loginUser = (User) session.getAttribute("loginUser");
        if (loginUser == null) {
            out.write("{\"code\":401,\"msg\":\"请先登录\"}");
            return;
        }

        Evaluate stats = evaluateDao.getEvaluateStats(loginUser.getId());
        out.write("{\"code\":200,\"msg\":\"查询成功\",\"data\":" + JsonUtil.toJson(stats) + "}");
    }
}
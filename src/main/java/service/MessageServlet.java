package service;



import dao.MessageDao;
import entity.Message;
import entity.User;
import constant.MessageConstants;
import util.JsonUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;

/**
 * 消息模块核心控制器
 * 覆盖：消息列表、未读计数、标记已读、删除消息
 */
public class MessageServlet extends HttpServlet {
    private MessageDao messageDao = new MessageDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
        String path = request.getPathInfo();

        try {
            switch (path) {
                case "/list": // 消息列表（分类）
                    getMsgList(request, out);
                    break;
                case "/unreadCount": // 未读消息数
                    getUnreadCount(request, out);
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

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
        String path = request.getPathInfo();

        try {
            switch (path) {
                case "/read": // 标记已读（单条/批量）
                    markRead(request, out);
                    break;
                case "/delete": // 删除消息
                    deleteMsg(request, out);
                    break;
                case "/readAll": // 全部标记已读
                    markAllRead(request, out);
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

    // 1. 获取消息列表
    private void getMsgList(HttpServletRequest request, PrintWriter out) throws SQLException {
        HttpSession session = request.getSession(false);
        User loginUser = (User) session.getAttribute("loginUser");
        if (loginUser == null) {
            out.write("{\"code\":401,\"msg\":\"请先登录\"}");
            return;
        }

        // 获取消息类型
        String msgType = request.getParameter("msgType"); // system/apply/evaluate

        // 查询消息列表
        List<Message> msgList = messageDao.findMsgList(loginUser.getId(), msgType);
        out.write("{\"code\":200,\"msg\":\"查询成功\",\"data\":" + JsonUtil.toJson(msgList) + "}");
    }

    // 2. 获取未读消息数
    private void getUnreadCount(HttpServletRequest request, PrintWriter out) throws SQLException {
        HttpSession session = request.getSession(false);
        User loginUser = (User) session.getAttribute("loginUser");
        if (loginUser == null) {
            out.write("{\"code\":401,\"msg\":\"请先登录\"}");
            return;
        }

        int count = messageDao.getUnreadCount(loginUser.getId());
        out.write("{\"code\":200,\"msg\":\"查询成功\",\"data\":{\"unreadCount\":" + count + "}}");
    }

    // 3. 标记消息为已读
    private void markRead(HttpServletRequest request, PrintWriter out) throws SQLException {
        HttpSession session = request.getSession(false);
        User loginUser = (User) session.getAttribute("loginUser");
        if (loginUser == null) {
            out.write("{\"code\":401,\"msg\":\"请先登录\"}");
            return;
        }

        String msgIds = request.getParameter("msgIds"); // 逗号分隔的ID列表
        if (msgIds == null || msgIds.isEmpty()) {
            out.write("{\"code\":400,\"msg\":\"消息ID不能为空\"}");
            return;
        }

        int result = messageDao.markRead(loginUser.getId(), msgIds);
        out.write("{\"code\":200,\"msg\":\"标记成功\",\"data\":{\"count\":" + result + "}}");
    }

    // 4. 删除消息
    private void deleteMsg(HttpServletRequest request, PrintWriter out) throws SQLException {
        HttpSession session = request.getSession(false);
        User loginUser = (User) session.getAttribute("loginUser");
        if (loginUser == null) {
            out.write("{\"code\":401,\"msg\":\"请先登录\"}");
            return;
        }

        String msgId = request.getParameter("msgId");
        if (msgId == null || msgId.isEmpty()) {
            out.write("{\"code\":400,\"msg\":\"消息ID不能为空\"}");
            return;
        }

        int result = messageDao.deleteMsg(loginUser.getId(), Long.parseLong(msgId));
        if (result > 0) {
            out.write("{\"code\":200,\"msg\":\"删除成功\"}");
        } else {
            out.write("{\"code\":500,\"msg\":\"删除失败\"}");
        }
    }

    // 5. 全部标记已读
    private void markAllRead(HttpServletRequest request, PrintWriter out) throws SQLException {
        HttpSession session = request.getSession(false);
        User loginUser = (User) session.getAttribute("loginUser");
        if (loginUser == null) {
            out.write("{\"code\":401,\"msg\":\"请先登录\"}");
            return;
        }

        // 查询所有未读消息
        List<Message> unreadMsg = messageDao.findMsgList(loginUser.getId(), null);
        StringBuilder msgIds = new StringBuilder();
        for (Message msg : unreadMsg) {
            if (msg.getIsRead() == MessageConstants.MSG_STATUS_UNREAD) {
                if (msgIds.length() > 0) msgIds.append(",");
                msgIds.append(msg.getId());
            }
        }

        if (msgIds.length() == 0) {
            out.write("{\"code\":200,\"msg\":\"暂无未读消息\"}");
            return;
        }

        int result = messageDao.markRead(loginUser.getId(), msgIds.toString());
        out.write("{\"code\":200,\"msg\":\"全部标记已读成功\",\"data\":{\"count\":" + result + "}}");
    }
}

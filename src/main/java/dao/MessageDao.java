package dao;

import constant.MessageConstants;
import entity.Message;
import util.DBUtil;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

/**
 * 消息数据访问层
 */
public class MessageDao {
    /**
     * 添加消息
     */
    public int addMessage(Message msg, Connection conn) throws SQLException {
        PreparedStatement pstmt = null;
        String sql = "INSERT INTO t_message(user_id, msg_type, title, content, is_read, create_time) VALUES(?, ?, ?, ?, ?, ?)";
        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, msg.getUserId());
            pstmt.setString(2, msg.getMsgType());
            pstmt.setString(3, msg.getTitle());
            pstmt.setString(4, msg.getContent());
            pstmt.setInt(5, msg.getIsRead());
            pstmt.setDate(6, new java.sql.Date(new Date().getTime()));
            return pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        } finally {
            DBUtil.close(null, pstmt);
        }
    }

    /**
     * 添加消息（无事务）
     */
    public int addMessage(Message msg) throws SQLException {
        Connection conn = null;
        try {
            conn = DBUtil.getConnection();
            return addMessage(msg, conn);
        } finally {
            DBUtil.close(conn, null);
        }
    }

    /**
     * 获取用户消息列表
     */
    public List<Message> findMsgList(Long userId, String msgType) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        StringBuilder sql = new StringBuilder("SELECT * FROM t_message WHERE user_id = ?");
        if (msgType != null && !msgType.isEmpty()) {
            sql.append(" AND msg_type = ?");
        }
        sql.append(" ORDER BY create_time DESC");

        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql.toString());
            pstmt.setLong(1, userId);
            if (msgType != null && !msgType.isEmpty()) {
                pstmt.setString(2, msgType);
            }
            rs = pstmt.executeQuery();
            return new BeanListHandler<>(Message.class).handle(rs);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            DBUtil.close(conn, pstmt, rs);
        }
    }

    /**
     * 获取未读消息数
     */
    public int getUnreadCount(Long userId) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql = "SELECT COUNT(*) FROM t_message WHERE user_id = ? AND is_read = ?";
        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, userId);
            pstmt.setInt(2, MessageConstants.MSG_STATUS_UNREAD);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
            return 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        } finally {
            DBUtil.close(conn, pstmt, rs);
        }
    }

    /**
     * 标记消息为已读
     */
    public int markRead(Long userId, String msgIds) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        String sql = "UPDATE t_message SET is_read = ?, update_time = ? WHERE id IN (" + msgIds + ") AND user_id = ?";
        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, MessageConstants.MSG_STATUS_READ);
            pstmt.setDate(2, new java.sql.Date(new Date().getTime()));
            pstmt.setLong(3, userId);
            return pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        } finally {
            DBUtil.close(conn, pstmt);
        }
    }

    /**
     * 删除消息
     */
    public int deleteMsg(Long userId, Long msgId) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        String sql = "DELETE FROM t_message WHERE id = ? AND user_id = ?";
        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, msgId);
            pstmt.setLong(2, userId);
            return pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        } finally {
            DBUtil.close(conn, pstmt);
        }
    }
}
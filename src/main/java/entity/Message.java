package entity;
import java.io.Serializable;
import java.util.Date;

/**
 * 消息通知实体
 */
public class Message implements Serializable {
    private Long id;                    // 主键ID
    private Long userId;                // 接收用户ID
    private String msgType;             // 消息类型：system/apply/evaluate
    private String title;               // 消息标题
    private String content;             // 消息内容
    private Integer isRead;             // 是否已读：0-未读，1-已读
    private Date createTime;            // 创建时间

    // 空构造
    public Message() {}

    // Getter & Setter
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getMsgType() { return msgType; }
    public void setMsgType(String msgType) { this.msgType = msgType; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public Integer getIsRead() { return isRead; }
    public void setIsRead(Integer isRead) { this.isRead = isRead; }
    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }
}

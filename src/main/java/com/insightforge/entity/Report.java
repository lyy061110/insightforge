package com.insightforge.entity;

import java.time.LocalDateTime;

/**
 * 举报实体类
 * 对应数据表：reports
 * 用于记录用户对帖子的举报信息，包括举报原因和处理状态
 */
public class Report {
    /** 举报记录唯一标识ID */
    private Long id;
    /** 被举报的帖子ID */
    private Long postId;
    /** 举报用户ID */
    private Long userId;
    /** 举报原因类型 */
    private String reason;
    /** 举报详细描述 */
    private String description;
    /** 举报处理状态（如：pending、resolved等） */
    private String status;
    /** 严重程度等级 */
    private Integer severity;
    /** 举报时间 */
    private LocalDateTime createdAt;

    /** 举报用户名（非数据库字段，用于展示） */
    private String username;
    /** 被举报帖子标题（非数据库字段，用于展示） */
    private String postTitle;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getPostId() { return postId; }
    public void setPostId(Long postId) { this.postId = postId; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Integer getSeverity() { return severity; }
    public void setSeverity(Integer severity) { this.severity = severity; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPostTitle() { return postTitle; }
    public void setPostTitle(String postTitle) { this.postTitle = postTitle; }
}
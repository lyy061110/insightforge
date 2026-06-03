package com.insightforge.entity;

import java.time.LocalDateTime;

/**
 * 浏览历史实体类
 * 对应数据表：browse_history
 * 用于记录用户的帖子浏览历史，支持个性化推荐
 */
public class BrowseHistory {
    /** 浏览记录唯一标识ID */
    private Long id;
    /** 浏览用户ID */
    private Long userId;
    /** 被浏览的帖子ID */
    private Long postId;
    /** 浏览时间 */
    private LocalDateTime createdAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public Long getPostId() { return postId; }
    public void setPostId(Long postId) { this.postId = postId; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
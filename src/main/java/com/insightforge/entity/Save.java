package com.insightforge.entity;

import java.time.LocalDateTime;

/**
 * 收藏实体类
 * 对应数据表：saves
 * 用于记录用户对帖子的收藏关系
 */
public class Save {
    /** 收藏记录唯一标识ID */
    private Long id;
    /** 被收藏的帖子ID */
    private Long postId;
    /** 收藏用户ID */
    private Long userId;
    /** 收藏时间 */
    private LocalDateTime createdAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getPostId() { return postId; }
    public void setPostId(Long postId) { this.postId = postId; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
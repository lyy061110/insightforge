package com.insightforge.entity;

import java.time.LocalDateTime;

/**
 * 评论实体类
 * 对应数据表：comments
 * 用于存储用户对帖子的评论内容
 */
public class Comment {
    /** 评论唯一标识ID */
    private Long id;
    /** 所属帖子ID */
    private Long postId;
    /** 评论用户ID */
    private Long userId;
    /** 评论内容 */
    private String content;
    /** 评论创建时间 */
    private LocalDateTime createdAt;

    /** 评论用户名（非数据库字段，用于展示） */
    private String username;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getPostId() { return postId; }
    public void setPostId(Long postId) { this.postId = postId; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
}
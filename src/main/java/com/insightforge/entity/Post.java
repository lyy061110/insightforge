package com.insightforge.entity;

import java.time.LocalDateTime;

/**
 * 帖子实体类
 * 对应数据表：posts
 * 用于存储用户发布的帖子内容，包括标题、正文、分类及统计信息等
 */
public class Post {
    /** 帖子唯一标识ID */
    private Long id;
    /** 帖子标题 */
    private String title;
    /** 帖子正文内容 */
    private String content;
    /** 帖子分类 */
    private String category;
    /** 作者ID */
    private Long authorId;
    /** 帖子状态（如：published、draft等） */
    private String status;
    /** 点赞数 */
    private Integer likeCount;
    /** 评论数 */
    private Integer commentCount;
    /** 收藏数 */
    private Integer saveCount;
    /** 举报数 */
    private Integer reportCount;
    /** 帖子权重（用于排序推荐） */
    private Double weight;
    /** 创建时间 */
    private LocalDateTime createdAt;
    /** 更新时间 */
    private LocalDateTime updatedAt;

    /** 作者用户名（非数据库字段，用于展示） */
    private String authorName;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public Long getAuthorId() { return authorId; }
    public void setAuthorId(Long authorId) { this.authorId = authorId; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Integer getLikeCount() { return likeCount; }
    public void setLikeCount(Integer likeCount) { this.likeCount = likeCount; }
    public Integer getCommentCount() { return commentCount; }
    public void setCommentCount(Integer commentCount) { this.commentCount = commentCount; }
    public Integer getSaveCount() { return saveCount; }
    public void setSaveCount(Integer saveCount) { this.saveCount = saveCount; }
    public Integer getReportCount() { return reportCount; }
    public void setReportCount(Integer reportCount) { this.reportCount = reportCount; }
    public Double getWeight() { return weight; }
    public void setWeight(Double weight) { this.weight = weight; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    public String getAuthorName() { return authorName; }
    public void setAuthorName(String authorName) { this.authorName = authorName; }
}
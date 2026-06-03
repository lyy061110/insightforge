package com.insightforge.entity;

import java.time.LocalDateTime;

/**
 * 用户实体类
 * 对应数据表：users
 * 用于存储系统用户的基本信息，包括账户、声誉值和关注主题等
 */
public class User {
    /** 用户唯一标识ID */
    private Long id;
    /** 用户名 */
    private String username;
    /** 用户密码（加密存储） */
    private String password;
    /** 用户邮箱 */
    private String email;
    /** 用户声誉值 */
    private Integer reputation;
    /** 用户关注的主题列表（以逗号分隔） */
    private String followedTopics;
    /** 账户创建时间 */
    private LocalDateTime createdAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public Integer getReputation() { return reputation; }
    public void setReputation(Integer reputation) { this.reputation = reputation; }
    public String getFollowedTopics() { return followedTopics; }
    public void setFollowedTopics(String followedTopics) { this.followedTopics = followedTopics; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
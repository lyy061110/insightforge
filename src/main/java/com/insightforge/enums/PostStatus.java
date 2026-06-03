package com.insightforge.enums;

/**
 * 帖子状态枚举
 * 定义帖子在系统中的各种状态
 */
public enum PostStatus {
    /**
     * 待审核状态
     * 帖子已创建但尚未通过审核
     */
    PENDING,

    /**
     * 已发布状态
     * 帖子已通过审核并公开发布
     */
    PUBLISHED,

    /**
     * 已标记状态
     * 帖子被标记为可疑或需要关注
     */
    FLAGGED,

    /**
     * 已隐藏状态
     * 帖子被管理员隐藏，普通用户不可见
     */
    HIDDEN,

    /**
     * 已删除状态
     * 帖子已被删除，仅保留记录
     */
    DELETED
}
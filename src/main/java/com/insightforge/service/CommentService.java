package com.insightforge.service;

import com.insightforge.entity.Comment;

import java.util.List;

/**
 * 评论服务接口
 * <p>
 * 提供评论相关的核心业务功能，包括评论的查询、添加和删除等操作。
 * 该接口定义了评论管理的基本方法，支持用户对帖子进行评论互动。
 * </p>
 */
public interface CommentService {

    /**
     * 根据帖子ID获取评论列表
     *
     * @param postId 帖子ID
     * @return 该帖子下的所有评论列表
     */
    List<Comment> getCommentsByPostId(Long postId);

    /**
     * 添加评论
     * <p>
     * 为指定帖子添加评论，同时更新帖子的评论计数和热度权重
     * </p>
     *
     * @param postId  帖子ID
     * @param userId  评论用户ID
     * @param content 评论内容
     * @return 创建成功后的评论对象
     */
    Comment addComment(Long postId, Long userId, String content);

    /**
     * 删除评论
     *
     * @param commentId 评论ID
     */
    void deleteComment(Long commentId);
}
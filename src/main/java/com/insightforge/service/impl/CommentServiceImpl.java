package com.insightforge.service.impl;

import com.insightforge.engine.HashMapEngine;
import com.insightforge.engine.PriorityQueueEngine;
import com.insightforge.entity.Comment;
import com.insightforge.entity.Post;
import com.insightforge.mapper.CommentMapper;
import com.insightforge.mapper.PostMapper;
import com.insightforge.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 评论服务实现类
 * <p>
 * 实现评论相关的核心业务逻辑，包括评论的查询、添加和删除等功能。
 * 该类集成了HashMap缓存引擎和优先队列引擎：
 * - HashMapEngine: 用于缓存帖子数据
 * - PriorityQueueEngine: 用于更新帖子的热度权重
 * </p>
 */
@Service
public class CommentServiceImpl implements CommentService {

    /** 评论数据访问层 */
    @Autowired
    private CommentMapper commentMapper;

    /** 帖子数据访问层 */
    @Autowired
    private PostMapper postMapper;

    /** HashMap缓存引擎，用于缓存帖子数据 */
    @Autowired
    private HashMapEngine hashMapEngine;

    /** 优先队列引擎，用于更新热门帖子队列 */
    @Autowired
    private PriorityQueueEngine priorityQueueEngine;

    /**
     * 根据帖子ID获取评论列表
     *
     * @param postId 帖子ID
     * @return 该帖子下的所有评论列表
     */
    @Override
    public List<Comment> getCommentsByPostId(Long postId) {
        return commentMapper.findByPostId(postId);
    }

    /**
     * 添加评论
     * <p>
     * 创建评论记录，更新帖子的评论计数，并重新计算帖子的热度权重。
     * 权重计算公式：权重 = 点赞数 * 2.0 + 评论数 * 3.0 + 收藏数
     * </p>
     *
     * @param postId  帖子ID
     * @param userId  评论用户ID
     * @param content 评论内容
     * @return 创建成功后的评论对象
     */
    @Override
    public Comment addComment(Long postId, Long userId, String content) {
        // 创建评论对象
        Comment comment = new Comment();
        comment.setPostId(postId);
        comment.setUserId(userId);
        comment.setContent(content);
        // 保存评论到数据库
        commentMapper.insert(comment);
        // 更新帖子评论计数
        postMapper.incrementCommentCount(postId);
        // 获取帖子并更新权重
        Post post = hashMapEngine.getPost(postId);
        if (post != null) {
            // 计算新的权重值（评论权重较高，为3.0）
            int likeCount = post.getLikeCount() != null ? post.getLikeCount() : 0;
            int commentCount = post.getCommentCount() != null ? post.getCommentCount() : 0;
            int saveCount = post.getSaveCount() != null ? post.getSaveCount() : 0;
            double weight = likeCount * 2.0 + commentCount * 3.0 + saveCount;
            post.setWeight(weight);
            // 更新数据库中的权重
            postMapper.updateWeight(postId, weight);
            // 刷新缓存
            hashMapEngine.refreshPostCache(post);
            // 更新热门帖子队列
            priorityQueueEngine.addToTrending(post);
        }
        return comment;
    }

    /**
     * 删除评论
     *
     * @param commentId 评论ID
     */
    @Override
    public void deleteComment(Long commentId) {
        commentMapper.deleteById(commentId);
    }
}
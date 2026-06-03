package com.insightforge.mapper;

import com.insightforge.entity.Comment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 评论数据访问层接口
 * 提供评论相关的数据库操作，包括评论的查询、插入、删除等功能
 */
@Mapper
public interface CommentMapper {
    /**
     * 根据帖子ID查询评论列表
     *
     * @param postId 帖子ID
     * @return 评论实体列表
     */
    List<Comment> findByPostId(@Param("postId") Long postId);

    /**
     * 插入新评论
     *
     * @param comment 评论实体对象
     * @return 影响的行数
     */
    int insert(Comment comment);

    /**
     * 根据ID删除评论
     *
     * @param id 评论ID
     * @return 影响的行数
     */
    int deleteById(@Param("id") Long id);
}
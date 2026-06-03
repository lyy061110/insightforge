package com.insightforge.mapper;

import com.insightforge.entity.Like;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 点赞数据访问层接口
 * 提供点赞相关的数据库操作，包括点赞记录的查询、插入、删除和统计等功能
 */
@Mapper
public interface LikeMapper {
    /**
     * 根据帖子ID和用户ID查询点赞记录
     *
     * @param postId 帖子ID
     * @param userId 用户ID
     * @return 点赞实体对象，如果不存在则返回null
     */
    Like findByPostAndUser(@Param("postId") Long postId, @Param("userId") Long userId);

    /**
     * 插入新点赞记录
     *
     * @param like 点赞实体对象
     * @return 影响的行数
     */
    int insert(Like like);

    /**
     * 删除点赞记录（取消点赞）
     *
     * @param postId 帖子ID
     * @param userId 用户ID
     * @return 影响的行数
     */
    int delete(@Param("postId") Long postId, @Param("userId") Long userId);

    /**
     * 统计帖子的点赞数量
     *
     * @param postId 帖子ID
     * @return 点赞数量
     */
    int countByPostId(@Param("postId") Long postId);
}
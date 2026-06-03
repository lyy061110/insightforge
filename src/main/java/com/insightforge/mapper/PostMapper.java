package com.insightforge.mapper;

import com.insightforge.entity.Post;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 帖子数据访问层接口
 * 提供帖子相关的数据库操作，包括帖子的增删改查、状态管理、计数更新等功能
 */
@Mapper
public interface PostMapper {
    /**
     * 根据帖子ID查询帖子详情
     *
     * @param id 帖子ID
     * @return 帖子实体对象，如果不存在则返回null
     */
    Post findById(@Param("id") Long id);

    /**
     * 查询所有帖子列表
     *
     * @return 帖子实体列表
     */
    List<Post> findAll();

    /**
     * 查询所有帖子列表（管理员视角）
     *
     * @return 帖子实体列表
     */
    List<Post> findAllAdmin();

    /**
     * 根据分类查询帖子列表
     *
     * @param category 分类名称
     * @return 帖子实体列表
     */
    List<Post> findByCategory(@Param("category") String category);

    /**
     * 根据作者ID查询帖子列表
     *
     * @param authorId 作者ID
     * @return 帖子实体列表
     */
    List<Post> findByAuthorId(@Param("authorId") Long authorId);

    /**
     * 根据标题关键词搜索帖子
     *
     * @param keyword 搜索关键词
     * @return 匹配的帖子实体列表
     */
    List<Post> searchByTitle(@Param("keyword") String keyword);

    /**
     * 按权重排序查询所有帖子
     *
     * @return 按权重排序的帖子实体列表
     */
    List<Post> findAllOrderByWeight();

    /**
     * 查询最新发布的帖子
     *
     * @param limit 返回数量限制
     * @return 最新的帖子实体列表
     */
    List<Post> findLatest(@Param("limit") int limit);

    /**
     * 插入新帖子
     *
     * @param post 帖子实体对象
     * @return 影响的行数
     */
    int insert(Post post);

    /**
     * 更新帖子信息
     *
     * @param post 帖子实体对象
     * @return 影响的行数
     */
    int update(Post post);

    /**
     * 更新帖子状态
     *
     * @param id     帖子ID
     * @param status 新状态
     * @return 影响的行数
     */
    int updateStatus(@Param("id") Long id, @Param("status") String status);

    /**
     * 增加帖子点赞数
     *
     * @param id 帖子ID
     * @return 影响的行数
     */
    int incrementLikeCount(@Param("id") Long id);

    /**
     * 减少帖子点赞数
     *
     * @param id 帖子ID
     * @return 影响的行数
     */
    int decrementLikeCount(@Param("id") Long id);

    /**
     * 增加帖子评论数
     *
     * @param id 帖子ID
     * @return 影响的行数
     */
    int incrementCommentCount(@Param("id") Long id);

    /**
     * 增加帖子收藏数
     *
     * @param id 帖子ID
     * @return 影响的行数
     */
    int incrementSaveCount(@Param("id") Long id);

    /**
     * 减少帖子收藏数
     *
     * @param id 帖子ID
     * @return 影响的行数
     */
    int decrementSaveCount(@Param("id") Long id);

    /**
     * 增加帖子举报数
     *
     * @param id 帖子ID
     * @return 影响的行数
     */
    int incrementReportCount(@Param("id") Long id);

    /**
     * 更新帖子权重
     *
     * @param id     帖子ID
     * @param weight 新权重值
     * @return 影响的行数
     */
    int updateWeight(@Param("id") Long id, @Param("weight") double weight);

    /**
     * 根据ID删除帖子
     *
     * @param id 帖子ID
     * @return 影响的行数
     */
    int deleteById(@Param("id") Long id);
}
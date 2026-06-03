package com.insightforge.mapper;

import com.insightforge.entity.Save;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 收藏数据访问层接口
 * 提供收藏相关的数据库操作，包括收藏记录的查询、插入、删除等功能
 */
@Mapper
public interface SaveMapper {
    /**
     * 根据用户ID查询收藏列表
     *
     * @param userId 用户ID
     * @return 收藏实体列表
     */
    List<Save> findByUserId(@Param("userId") Long userId);

    /**
     * 根据帖子ID和用户ID查询收藏记录
     *
     * @param postId 帖子ID
     * @param userId 用户ID
     * @return 收藏实体对象，如果不存在则返回null
     */
    Save findByPostAndUser(@Param("postId") Long postId, @Param("userId") Long userId);

    /**
     * 插入新收藏记录
     *
     * @param save 收藏实体对象
     * @return 影响的行数
     */
    int insert(Save save);

    /**
     * 删除收藏记录（取消收藏）
     *
     * @param postId 帖子ID
     * @param userId 用户ID
     * @return 影响的行数
     */
    int delete(@Param("postId") Long postId, @Param("userId") Long userId);
}
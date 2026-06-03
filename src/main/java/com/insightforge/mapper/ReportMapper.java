package com.insightforge.mapper;

import com.insightforge.entity.Report;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 举报数据访问层接口
 * 提供举报相关的数据库操作，包括举报记录的查询、插入、状态更新和统计等功能
 */
@Mapper
public interface ReportMapper {
    /**
     * 查询所有举报记录
     *
     * @return 举报实体列表
     */
    List<Report> findAll();

    /**
     * 根据举报ID查询举报详情
     *
     * @param id 举报ID
     * @return 举报实体对象，如果不存在则返回null
     */
    Report findById(@Param("id") Long id);

    /**
     * 根据状态查询举报列表
     *
     * @param status 举报状态
     * @return 举报实体列表
     */
    List<Report> findByStatus(@Param("status") String status);

    /**
     * 根据帖子ID查询举报列表
     *
     * @param postId 帖子ID
     * @return 举报实体列表
     */
    List<Report> findByPostId(@Param("postId") Long postId);

    /**
     * 根据帖子ID和用户ID查询举报记录
     *
     * @param postId 帖子ID
     * @param userId 用户ID
     * @return 举报实体对象，如果不存在则返回null
     */
    Report findByPostAndUser(@Param("postId") Long postId, @Param("userId") Long userId);

    /**
     * 插入新举报记录
     *
     * @param report 举报实体对象
     * @return 影响的行数
     */
    int insert(Report report);

    /**
     * 更新举报状态
     *
     * @param id     举报ID
     * @param status 新状态
     * @return 影响的行数
     */
    int updateStatus(@Param("id") Long id, @Param("status") String status);

    /**
     * 统计帖子的举报数量
     *
     * @param postId 帖子ID
     * @return 举报数量
     */
    int countByPostId(@Param("postId") Long postId);
}
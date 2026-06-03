package com.insightforge.mapper;

import com.insightforge.entity.BrowseHistory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 浏览历史数据访问层接口
 * 提供浏览历史相关的数据库操作，包括浏览记录的查询和插入等功能
 */
@Mapper
public interface BrowseHistoryMapper {
    /**
     * 根据用户ID查询浏览历史列表
     *
     * @param userId 用户ID
     * @return 浏览历史实体列表
     */
    List<BrowseHistory> findByUserId(@Param("userId") Long userId);

    /**
     * 插入新浏览历史记录
     *
     * @param history 浏览历史实体对象
     * @return 影响的行数
     */
    int insert(BrowseHistory history);
}
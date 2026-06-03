package com.insightforge.service;

import com.insightforge.entity.Report;

import java.util.List;

/**
 * 举报服务接口
 * <p>
 * 提供举报相关的核心业务功能，包括举报记录的查询和处理等操作。
 * 该接口定义了举报管理的基本方法，支持管理员对用户举报进行审核和处理。
 * </p>
 */
public interface ReportService {

    /**
     * 获取所有举报记录
     *
     * @return 所有举报记录列表
     */
    List<Report> getAllReports();

    /**
     * 获取待处理的举报记录
     * <p>
     * 返回状态为PENDING的举报记录，供管理员审核
     * </p>
     *
     * @return 待处理的举报记录列表
     */
    List<Report> getPendingReports();

    /**
     * 处理举报
     * <p>
     * 根据处理动作对举报进行相应处理：
     * - REMOVE: 删除被举报的帖子，将举报状态标记为已解决
     * - DISMISS: 驳回举报，将举报状态标记为已驳回
     * </p>
     *
     * @param id     举报记录ID
     * @param action 处理动作（REMOVE/DISMISS）
     */
    void resolveReport(Long id, String action);
}
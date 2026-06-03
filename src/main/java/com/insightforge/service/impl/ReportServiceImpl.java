package com.insightforge.service.impl;

import com.insightforge.entity.Report;
import com.insightforge.mapper.ReportMapper;
import com.insightforge.service.PostService;
import com.insightforge.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 举报服务实现类
 * <p>
 * 实现举报相关的核心业务逻辑，包括举报记录的查询和处理等功能。
 * 该类依赖PostService来处理举报相关的帖子操作（如删除帖子）。
 * </p>
 */
@Service
public class ReportServiceImpl implements ReportService {

    /** 举报数据访问层 */
    @Autowired
    private ReportMapper reportMapper;

    /** 帖子服务，用于处理举报相关的帖子操作 */
    @Autowired
    private PostService postService;

    /**
     * 获取所有举报记录
     *
     * @return 所有举报记录列表
     */
    @Override
    public List<Report> getAllReports() {
        return reportMapper.findAll();
    }

    /**
     * 获取待处理的举报记录
     * <p>
     * 查询状态为PENDING的举报记录，供管理员审核
     * </p>
     *
     * @return 待处理的举报记录列表
     */
    @Override
    public List<Report> getPendingReports() {
        return reportMapper.findByStatus("PENDING");
    }

    /**
     * 处理举报
     * <p>
     * 根据处理动作执行相应操作：
     * - REMOVE: 删除被举报的帖子，将举报状态标记为已解决(RESOLVED)
     * - DISMISS: 驳回举报，将举报状态标记为已驳回(DISMISSED)
     * </p>
     *
     * @param id     举报记录ID
     * @param action 处理动作（REMOVE/DISMISS）
     */
    @Override
    public void resolveReport(Long id, String action) {
        // 处理动作：删除帖子
        if ("REMOVE".equalsIgnoreCase(action)) {
            // 获取举报记录
            Report report = reportMapper.findById(id);
            if (report != null) {
                // 删除被举报的帖子
                postService.deletePost(report.getPostId());
            }
            // 更新举报状态为已解决
            reportMapper.updateStatus(id, "RESOLVED");
        } 
        // 处理动作：驳回举报
        else if ("DISMISS".equalsIgnoreCase(action)) {
            // 更新举报状态为已驳回
            reportMapper.updateStatus(id, "DISMISSED");
        }
    }
}
package com.insightforge.enums;

/**
 * 举报状态枚举
 * 定义举报记录在系统中的各种处理状态
 */
public enum ReportStatus {
    /**
     * 待处理状态
     * 举报已提交，等待管理员审核
     */
    PENDING,

    /**
     * 审核中状态
     * 管理员正在查看和处理该举报
     */
    REVIEWING,

    /**
     * 已解决状态
     * 举报已处理，确认违规并采取相应措施
     */
    RESOLVED,

    /**
     * 已驳回状态
     * 举报经审核后被驳回，确认无违规行为
     */
    DISMISSED
}
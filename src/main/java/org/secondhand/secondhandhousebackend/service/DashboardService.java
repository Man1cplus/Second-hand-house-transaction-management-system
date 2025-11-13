package org.secondhand.secondhandhousebackend.service;

import org.secondhand.secondhandhousebackend.DTO.DashboardStatsDTO;

/**
 * Dashboard统计服务接口
 */
public interface DashboardService {
    /**
     * 获取总体统计数据
     */
    DashboardStatsDTO getDashboardStats();
}


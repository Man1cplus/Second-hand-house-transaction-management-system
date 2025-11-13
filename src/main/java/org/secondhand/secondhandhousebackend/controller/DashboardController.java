package org.secondhand.secondhandhousebackend.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.secondhand.secondhandhousebackend.DTO.DashboardStatsDTO;
import org.secondhand.secondhandhousebackend.DTO.Result;
import org.secondhand.secondhandhousebackend.entity.Users;
import org.secondhand.secondhandhousebackend.service.DashboardService;
import org.secondhand.secondhandhousebackend.utils.IPUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Dashboard控制器
 * 提供数据概览统计接口
 */
@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    /**
     * 获取总体统计数据
     * 仅管理员可访问
     */
    @GetMapping("/stats")
    public Result getDashboardStats(HttpServletRequest request) {
        // 检查用户是否登录
        Users user = (Users) request.getAttribute("user");
        if (user == null) {
            return Result.fail("用户未登录");
        }

        // 检查用户角色是否为管理员
        if (user.getRole() == null || !user.getRole().toString().equals("管理员")) {
            return Result.fail("权限不足，仅管理员可访问");
        }

        try {
            DashboardStatsDTO stats = dashboardService.getDashboardStats();
            // 获取客户端真实IP地址
            String clientIP = IPUtil.getClientIP(request);
            stats.setClientIP(clientIP);
            return Result.ok(stats);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail("获取统计数据失败: " + e.getMessage());
        }
    }
}


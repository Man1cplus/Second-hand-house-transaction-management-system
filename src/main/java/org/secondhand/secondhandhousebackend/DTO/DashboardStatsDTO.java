package org.secondhand.secondhandhousebackend.DTO;

import lombok.Data;
import java.util.Map;
import java.util.List;

/**
 * Dashboard统计数据DTO
 */
@Data
public class DashboardStatsDTO {
    // 总体统计
    private Long totalUsers;
    private Long totalProperties;
    private Long totalAppointments;
    private Long totalContracts;
    private Long totalFavorites;
    private Long totalBills;
    
    // 客户端IP地址
    private String clientIP;

    // 用户统计
    private Map<String, Long> userStats;
    
    // 活跃用户统计
    private ActiveUserStats activeUserStats;

    // 房源统计
    private PropertyStats propertyStats;

    // 预约统计
    private AppointmentStats appointmentStats;

    // 合同统计
    private ContractStats contractStats;

    // 收藏统计
    private FavoriteStats favoriteStats;

    // 账单统计
    private BillStats billStats;

    // 月度统计数据（用于图表）
    private MonthlyStats monthlyStats;

    @Data
    public static class PropertyStats {
        private Long total;
        private Long available; // 在售
        private Long sold; // 已售出
        private Long inactive; // 下架
        private Long pending; // 待审核
        private Long rejected; // 审核拒绝
        private Long thisMonth; // 本月新增
    }

    @Data
    public static class AppointmentStats {
        private Long total;
        private Long pending; // 待审批
        private Long approved; // 已预约
        private Long cancelled; // 已取消
        private Long thisMonth; // 本月新增
    }

    @Data
    public static class ContractStats {
        private Long total;
        private Long pending; // 待审核
        private Long signed; // 已签订
        private Long cancelled; // 已取消
        private Long thisMonth; // 本月新增
    }

    @Data
    public static class FavoriteStats {
        private Long total;
        private Long today; // 今日新增
        private Long thisWeek; // 本周新增
        private Long thisMonth; // 本月新增
        private String hotProperty; // 热门房源（收藏数最多的房源标题）
    }

    @Data
    public static class BillStats {
        private Long total;
        private Long pending; // 待支付
        private Long paid; // 已支付
        private Long cancelled; // 已取消
        private Long refunded; // 已退款
        private Long thisMonth; // 本月新增
    }

    @Data
    public static class MonthlyStats {
        private List<String> days; // 日期列表
        private List<Long> currentMonth; // 本月数据（按天统计）
        private List<Long> lastMonth; // 上月数据（按天统计）
    }

    @Data
    public static class ActiveUserStats {
        private Long totalActiveUsers; // 总活跃用户数（最近30天内有任何操作的用户）
        private Long todayActiveUsers; // 今日活跃用户数
        private Long thisWeekActiveUsers; // 本周活跃用户数
        private Long thisMonthActiveUsers; // 本月活跃用户数
    }
}


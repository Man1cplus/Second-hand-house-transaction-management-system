package org.secondhand.secondhandhousebackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.secondhand.secondhandhousebackend.DTO.DashboardStatsDTO;
import org.secondhand.secondhandhousebackend.entity.Bills;
import org.secondhand.secondhandhousebackend.entity.Contracts;
import org.secondhand.secondhandhousebackend.entity.Properties;
import org.secondhand.secondhandhousebackend.entity.Userfavorites;
import org.secondhand.secondhandhousebackend.entity.Users;
import org.secondhand.secondhandhousebackend.entity.Viewingappointments;
import org.secondhand.secondhandhousebackend.enums.ViewStatus;
import org.secondhand.secondhandhousebackend.mapper.BillsMapper;
import org.secondhand.secondhandhousebackend.mapper.ContractsMapper;
import org.secondhand.secondhandhousebackend.mapper.PropertiesMapper;
import org.secondhand.secondhandhousebackend.mapper.UserfavoritesMapper;
import org.secondhand.secondhandhousebackend.mapper.UsersMapper;
import org.secondhand.secondhandhousebackend.mapper.ViewingappointmentsMapper;
import org.secondhand.secondhandhousebackend.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Dashboard统计服务实现类
 */
@Service
public class DashboardServiceImpl implements DashboardService {

    @Autowired
    private UsersMapper usersMapper;

    @Autowired
    private PropertiesMapper propertiesMapper;

    @Autowired
    private ViewingappointmentsMapper viewingappointmentsMapper;

    @Autowired
    private ContractsMapper contractsMapper;

    @Autowired
    private UserfavoritesMapper userfavoritesMapper;

    @Autowired
    private BillsMapper billsMapper;

    @Override
    public DashboardStatsDTO getDashboardStats() {
        DashboardStatsDTO stats = new DashboardStatsDTO();

        // 获取当前时间和月初时间
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startOfMonth = now.withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime startOfToday = now.withHour(0).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime startOfWeek = now.minusDays(now.getDayOfWeek().getValue() - 1).withHour(0).withMinute(0).withSecond(0).withNano(0);

        // 转换为Date类型
        Date startOfMonthDate = Date.from(startOfMonth.atZone(ZoneId.systemDefault()).toInstant());
        Date startOfTodayDate = Date.from(startOfToday.atZone(ZoneId.systemDefault()).toInstant());
        Date startOfWeekDate = Date.from(startOfWeek.atZone(ZoneId.systemDefault()).toInstant());

        // 1. 总体统计
        stats.setTotalUsers(usersMapper.selectCount(null));
        stats.setTotalProperties(propertiesMapper.selectCount(null));
        stats.setTotalAppointments(viewingappointmentsMapper.selectCount(null));
        stats.setTotalContracts(contractsMapper.selectCount(null));
        stats.setTotalFavorites(userfavoritesMapper.selectCount(null));
        stats.setTotalBills(billsMapper.selectCount(null));

        // 2. 用户统计（按角色）
        List<Users> allUsers = usersMapper.selectList(null);
        Map<String, Long> userStats = allUsers.stream()
                .collect(Collectors.groupingBy(
                        user -> user.getRole() != null ? user.getRole().toString() : "未知",
                        Collectors.counting()
                ));
        stats.setUserStats(userStats);

        // 2.1 活跃用户统计（基于用户操作：预约、收藏、合同）
        // 注意：由于预约表没有创建时间字段，我们使用预约时间来判断
        // 预约时间可能是未来时间，所以我们统计所有预约时间在合理范围内的预约
        DashboardStatsDTO.ActiveUserStats activeUserStats = new DashboardStatsDTO.ActiveUserStats();
        
        // 计算时间范围（最近30天到今天+7天，覆盖预约时间可能是未来的情况）
        LocalDateTime thirtyDaysAgo = now.minusDays(30);
        LocalDateTime sevenDaysLater = now.plusDays(7);
        Date thirtyDaysAgoDate = Date.from(thirtyDaysAgo.atZone(ZoneId.systemDefault()).toInstant());
        Date sevenDaysLaterDate = Date.from(sevenDaysLater.atZone(ZoneId.systemDefault()).toInstant());
        
        // 收集活跃用户ID（最近30天内有任何操作的用户）
        Set<Integer> activeUserIds = new HashSet<>();
        
        // 从预约记录中获取活跃用户（买家）
        // 统计预约时间在最近30天到未来7天范围内的预约（因为预约时间可能是未来）
        LambdaQueryWrapper<Viewingappointments> appointmentWrapper = new LambdaQueryWrapper<>();
        appointmentWrapper.ge(Viewingappointments::getAppointmenttime, thirtyDaysAgoDate)
                         .le(Viewingappointments::getAppointmenttime, sevenDaysLaterDate);
        List<Viewingappointments> recentAppointments = viewingappointmentsMapper.selectList(appointmentWrapper);
        for (Viewingappointments appointment : recentAppointments) {
            if (appointment.getBuyerid() != null) {
                activeUserIds.add(appointment.getBuyerid());
            }
        }
        
        // 从收藏记录中获取活跃用户
        LambdaQueryWrapper<Userfavorites> favoriteWrapper = new LambdaQueryWrapper<>();
        favoriteWrapper.ge(Userfavorites::getCollectiontime, thirtyDaysAgoDate);
        List<Userfavorites> recentFavorites = userfavoritesMapper.selectList(favoriteWrapper);
        for (Userfavorites favorite : recentFavorites) {
            if (favorite.getUserId() != null) {
                activeUserIds.add(favorite.getUserId());
            }
        }
        
        // 从合同记录中获取活跃用户（买家和卖家）
        LambdaQueryWrapper<Contracts> contractWrapper = new LambdaQueryWrapper<>();
        contractWrapper.ge(Contracts::getSigningdate, thirtyDaysAgoDate);
        List<Contracts> recentContracts = contractsMapper.selectList(contractWrapper);
        for (Contracts contract : recentContracts) {
            if (contract.getBuyerid() != null) {
                activeUserIds.add(contract.getBuyerid());
            }
            if (contract.getSellerid() != null) {
                activeUserIds.add(contract.getSellerid());
            }
        }
        
        activeUserStats.setTotalActiveUsers((long) activeUserIds.size());
        
        // 今日活跃用户
        // 由于没有创建时间字段，我们统计预约时间在今天及未来7天内的预约（考虑到预约时间可能是未来）
        // 同时统计今天收藏的用户和今天签订的合同
        LocalDateTime todayEnd = now.plusDays(7);
        Date todayEndDate = Date.from(todayEnd.atZone(ZoneId.systemDefault()).toInstant());
        Set<Integer> todayActiveUserIds = new HashSet<>();
        LambdaQueryWrapper<Viewingappointments> todayAppointmentWrapper = new LambdaQueryWrapper<>();
        // 预约时间在今天及未来7天内（考虑到预约时间可能是未来，这些预约很可能是今天或最近创建的）
        todayAppointmentWrapper.ge(Viewingappointments::getAppointmenttime, startOfTodayDate)
                              .le(Viewingappointments::getAppointmenttime, todayEndDate);
        List<Viewingappointments> todayAppointments = viewingappointmentsMapper.selectList(todayAppointmentWrapper);
        for (Viewingappointments appointment : todayAppointments) {
            if (appointment.getBuyerid() != null) {
                todayActiveUserIds.add(appointment.getBuyerid());
            }
        }
        // 今天收藏的用户
        LambdaQueryWrapper<Userfavorites> todayFavoriteWrapper = new LambdaQueryWrapper<>();
        todayFavoriteWrapper.ge(Userfavorites::getCollectiontime, startOfTodayDate);
        List<Userfavorites> todayFavorites = userfavoritesMapper.selectList(todayFavoriteWrapper);
        for (Userfavorites favorite : todayFavorites) {
            if (favorite.getUserId() != null) {
                todayActiveUserIds.add(favorite.getUserId());
            }
        }
        // 今天签订的合同
        LambdaQueryWrapper<Contracts> todayContractWrapper = new LambdaQueryWrapper<>();
        todayContractWrapper.ge(Contracts::getSigningdate, startOfTodayDate);
        List<Contracts> todayContracts = contractsMapper.selectList(todayContractWrapper);
        for (Contracts contract : todayContracts) {
            if (contract.getBuyerid() != null) {
                todayActiveUserIds.add(contract.getBuyerid());
            }
            if (contract.getSellerid() != null) {
                todayActiveUserIds.add(contract.getSellerid());
            }
        }
        activeUserStats.setTodayActiveUsers((long) todayActiveUserIds.size());
        
        // 本周活跃用户（预约时间在本周及未来7天内）
        LocalDateTime weekEnd = now.plusDays(7);
        Date weekEndDate = Date.from(weekEnd.atZone(ZoneId.systemDefault()).toInstant());
        Set<Integer> weekActiveUserIds = new HashSet<>();
        LambdaQueryWrapper<Viewingappointments> weekAppointmentWrapper = new LambdaQueryWrapper<>();
        weekAppointmentWrapper.ge(Viewingappointments::getAppointmenttime, startOfWeekDate)
                             .le(Viewingappointments::getAppointmenttime, weekEndDate);
        List<Viewingappointments> weekAppointments = viewingappointmentsMapper.selectList(weekAppointmentWrapper);
        for (Viewingappointments appointment : weekAppointments) {
            if (appointment.getBuyerid() != null) {
                weekActiveUserIds.add(appointment.getBuyerid());
            }
        }
        LambdaQueryWrapper<Userfavorites> weekFavoriteWrapper = new LambdaQueryWrapper<>();
        weekFavoriteWrapper.ge(Userfavorites::getCollectiontime, startOfWeekDate);
        List<Userfavorites> weekFavorites = userfavoritesMapper.selectList(weekFavoriteWrapper);
        for (Userfavorites favorite : weekFavorites) {
            if (favorite.getUserId() != null) {
                weekActiveUserIds.add(favorite.getUserId());
            }
        }
        activeUserStats.setThisWeekActiveUsers((long) weekActiveUserIds.size());
        
        // 本月活跃用户（预约时间在本月及未来7天内）
        LocalDateTime monthEnd = now.plusDays(7);
        Date monthEndDate = Date.from(monthEnd.atZone(ZoneId.systemDefault()).toInstant());
        Set<Integer> monthActiveUserIds = new HashSet<>();
        LambdaQueryWrapper<Viewingappointments> monthAppointmentWrapper = new LambdaQueryWrapper<>();
        monthAppointmentWrapper.ge(Viewingappointments::getAppointmenttime, startOfMonthDate)
                              .le(Viewingappointments::getAppointmenttime, monthEndDate);
        List<Viewingappointments> monthAppointments = viewingappointmentsMapper.selectList(monthAppointmentWrapper);
        for (Viewingappointments appointment : monthAppointments) {
            if (appointment.getBuyerid() != null) {
                monthActiveUserIds.add(appointment.getBuyerid());
            }
        }
        LambdaQueryWrapper<Userfavorites> monthFavoriteWrapper = new LambdaQueryWrapper<>();
        monthFavoriteWrapper.ge(Userfavorites::getCollectiontime, startOfMonthDate);
        List<Userfavorites> monthFavorites = userfavoritesMapper.selectList(monthFavoriteWrapper);
        for (Userfavorites favorite : monthFavorites) {
            if (favorite.getUserId() != null) {
                monthActiveUserIds.add(favorite.getUserId());
            }
        }
        LambdaQueryWrapper<Contracts> monthContractWrapper = new LambdaQueryWrapper<>();
        monthContractWrapper.ge(Contracts::getSigningdate, startOfMonthDate);
        List<Contracts> monthContracts = contractsMapper.selectList(monthContractWrapper);
        for (Contracts contract : monthContracts) {
            if (contract.getBuyerid() != null) {
                monthActiveUserIds.add(contract.getBuyerid());
            }
            if (contract.getSellerid() != null) {
                monthActiveUserIds.add(contract.getSellerid());
            }
        }
        activeUserStats.setThisMonthActiveUsers((long) monthActiveUserIds.size());
        
        stats.setActiveUserStats(activeUserStats);

        // 3. 房源统计
        List<Properties> allProperties = propertiesMapper.selectList(null);
        DashboardStatsDTO.PropertyStats propertyStats = new DashboardStatsDTO.PropertyStats();
        propertyStats.setTotal((long) allProperties.size());

        // 统计各状态的房源
        long available = 0, sold = 0, inactive = 0, pending = 0, rejected = 0, thisMonth = 0;
        for (Properties property : allProperties) {
            String status = property.getStatus() != null ? property.getStatus().toString() : "";
            if (status.contains("在售")) {
                available++;
            } else if (status.contains("已售出")) {
                sold++;
            } else if (status.contains("下架")) {
                inactive++;
            } else if (status.contains("待审核")) {
                pending++;
            } else if (status.contains("审核拒绝")) {
                rejected++;
            }

            // 本月新增
            if (property.getPublishdate() != null) {
                Date publishDate = property.getPublishdate();
                if (publishDate.after(startOfMonthDate) || publishDate.equals(startOfMonthDate)) {
                    thisMonth++;
                }
            }
        }
        propertyStats.setAvailable(available);
        propertyStats.setSold(sold);
        propertyStats.setInactive(inactive);
        propertyStats.setPending(pending);
        propertyStats.setRejected(rejected);
        propertyStats.setThisMonth(thisMonth);
        stats.setPropertyStats(propertyStats);

        // 4. 预约统计
        List<Viewingappointments> allAppointments = viewingappointmentsMapper.selectList(null);
        DashboardStatsDTO.AppointmentStats appointmentStats = new DashboardStatsDTO.AppointmentStats();
        appointmentStats.setTotal((long) allAppointments.size());

        long appointmentPending = 0, appointmentApproved = 0, appointmentCancelled = 0, appointmentThisMonth = 0;
        for (Viewingappointments appointment : allAppointments) {
            if (appointment.getStatus() == ViewStatus.待审批) {
                appointmentPending++;
            } else if (appointment.getStatus() == ViewStatus.已预约) {
                appointmentApproved++;
            } else if (appointment.getStatus() == ViewStatus.已取消) {
                appointmentCancelled++;
            }

            // 本月新增
            if (appointment.getAppointmenttime() != null) {
                Date appointmentDate = appointment.getAppointmenttime();
                if (appointmentDate.after(startOfMonthDate) || appointmentDate.equals(startOfMonthDate)) {
                    appointmentThisMonth++;
                }
            }
        }
        appointmentStats.setPending(appointmentPending);
        appointmentStats.setApproved(appointmentApproved);
        appointmentStats.setCancelled(appointmentCancelled);
        appointmentStats.setThisMonth(appointmentThisMonth);
        stats.setAppointmentStats(appointmentStats);

        // 5. 合同统计
        List<Contracts> allContracts = contractsMapper.selectList(null);
        DashboardStatsDTO.ContractStats contractStats = new DashboardStatsDTO.ContractStats();
        contractStats.setTotal((long) allContracts.size());

        long contractPending = 0, contractSigned = 0, contractCancelled = 0, contractThisMonth = 0;
        for (Contracts contract : allContracts) {
            String contractStatus = contract.getContractstatus() != null ? contract.getContractstatus() : "";
            if (contractStatus.contains("待审核")) {
                contractPending++;
            } else if (contractStatus.contains("已签订")) {
                contractSigned++;
            } else if (contractStatus.contains("已取消")) {
                contractCancelled++;
            }

            // 本月新增
            if (contract.getSigningdate() != null) {
                Date signingDate = contract.getSigningdate();
                if (signingDate.after(startOfMonthDate) || signingDate.equals(startOfMonthDate)) {
                    contractThisMonth++;
                }
            }
        }
        contractStats.setPending(contractPending);
        contractStats.setSigned(contractSigned);
        contractStats.setCancelled(contractCancelled);
        contractStats.setThisMonth(contractThisMonth);
        stats.setContractStats(contractStats);

        // 6. 收藏统计
        List<Userfavorites> allFavorites = userfavoritesMapper.selectList(null);
        DashboardStatsDTO.FavoriteStats favoriteStats = new DashboardStatsDTO.FavoriteStats();
        favoriteStats.setTotal((long) allFavorites.size());

        long favoritesToday = 0, favoritesThisWeek = 0, favoritesThisMonth = 0;
        Map<Integer, Long> propertyFavoriteCount = new HashMap<>();

        for (Userfavorites favorite : allFavorites) {
            // 统计收藏数最多的房源
            Integer propertyId = favorite.getPropertyId();
            propertyFavoriteCount.put(propertyId, propertyFavoriteCount.getOrDefault(propertyId, 0L) + 1);

            // 时间统计
            if (favorite.getCollectiontime() != null) {
                Date collectionDate = favorite.getCollectiontime();
                if (collectionDate.after(startOfTodayDate) || collectionDate.equals(startOfTodayDate)) {
                    favoritesToday++;
                }
                if (collectionDate.after(startOfWeekDate) || collectionDate.equals(startOfWeekDate)) {
                    favoritesThisWeek++;
                }
                if (collectionDate.after(startOfMonthDate) || collectionDate.equals(startOfMonthDate)) {
                    favoritesThisMonth++;
                }
            }
        }

        favoriteStats.setToday(favoritesToday);
        favoriteStats.setThisWeek(favoritesThisWeek);
        favoriteStats.setThisMonth(favoritesThisMonth);

        // 找出收藏数最多的房源
        String hotProperty = "暂无";
        if (!propertyFavoriteCount.isEmpty()) {
            Integer hotPropertyId = propertyFavoriteCount.entrySet().stream()
                    .max(Map.Entry.comparingByValue())
                    .map(Map.Entry::getKey)
                    .orElse(null);
            if (hotPropertyId != null) {
                Properties hotPropertyObj = propertiesMapper.selectById(hotPropertyId);
                if (hotPropertyObj != null && hotPropertyObj.getTitle() != null) {
                    hotProperty = hotPropertyObj.getTitle();
                }
            }
        }
        favoriteStats.setHotProperty(hotProperty);
        stats.setFavoriteStats(favoriteStats);

        // 7. 账单统计
        List<Bills> allBills = billsMapper.selectList(null);
        DashboardStatsDTO.BillStats billStats = new DashboardStatsDTO.BillStats();
        billStats.setTotal((long) allBills.size());

        long billPending = 0, billPaid = 0, billCancelled = 0, billRefunded = 0, billThisMonth = 0;
        for (Bills bill : allBills) {
            String billStatus = bill.getBillstatus() != null ? bill.getBillstatus() : "";
            if (billStatus.contains("待支付")) {
                billPending++;
            } else if (billStatus.contains("已支付")) {
                billPaid++;
            } else if (billStatus.contains("已取消")) {
                billCancelled++;
            } else if (billStatus.contains("已退款")) {
                billRefunded++;
            }

            // 本月新增
            if (bill.getCreatedate() != null) {
                Date createDate = bill.getCreatedate();
                if (createDate.after(startOfMonthDate) || createDate.equals(startOfMonthDate)) {
                    billThisMonth++;
                }
            }
        }
        billStats.setPending(billPending);
        billStats.setPaid(billPaid);
        billStats.setCancelled(billCancelled);
        billStats.setRefunded(billRefunded);
        billStats.setThisMonth(billThisMonth);
        stats.setBillStats(billStats);

        // 8. 月度统计数据（最近7天）
        DashboardStatsDTO.MonthlyStats monthlyStats = new DashboardStatsDTO.MonthlyStats();
        List<String> days = new ArrayList<>();
        List<Long> currentMonthData = new ArrayList<>();
        List<Long> lastMonthData = new ArrayList<>();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd");
        LocalDate today = LocalDate.now();
        
        // 生成最近7天的日期
        for (int i = 6; i >= 0; i--) {
            LocalDate date = today.minusDays(i);
            days.add(date.format(formatter));

            // 获取当天的预约数量
            LocalDateTime dayStart = date.atStartOfDay();
            LocalDateTime dayEnd = date.plusDays(1).atStartOfDay();
            Date dayStartDate = Date.from(dayStart.atZone(ZoneId.systemDefault()).toInstant());
            Date dayEndDate = Date.from(dayEnd.atZone(ZoneId.systemDefault()).toInstant());

            LambdaQueryWrapper<Viewingappointments> wrapper = new LambdaQueryWrapper<>();
            wrapper.between(Viewingappointments::getAppointmenttime, dayStartDate, dayEndDate);
            long count = viewingappointmentsMapper.selectCount(wrapper);
            currentMonthData.add(count);

            // 获取上个月同一天的数据
            LocalDate lastMonthDate = date.minusMonths(1);
            LocalDateTime lastMonthDayStart = lastMonthDate.atStartOfDay();
            LocalDateTime lastMonthDayEnd = lastMonthDate.plusDays(1).atStartOfDay();
            Date lastMonthDayStartDate = Date.from(lastMonthDayStart.atZone(ZoneId.systemDefault()).toInstant());
            Date lastMonthDayEndDate = Date.from(lastMonthDayEnd.atZone(ZoneId.systemDefault()).toInstant());

            LambdaQueryWrapper<Viewingappointments> lastMonthWrapper = new LambdaQueryWrapper<>();
            lastMonthWrapper.between(Viewingappointments::getAppointmenttime, lastMonthDayStartDate, lastMonthDayEndDate);
            long lastMonthCount = viewingappointmentsMapper.selectCount(lastMonthWrapper);
            lastMonthData.add(lastMonthCount);
        }

        monthlyStats.setDays(days);
        monthlyStats.setCurrentMonth(currentMonthData);
        monthlyStats.setLastMonth(lastMonthData);
        stats.setMonthlyStats(monthlyStats);

        return stats;
    }
}


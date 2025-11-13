package org.secondhand.secondhandhousebackend.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import jakarta.servlet.http.HttpServletRequest;
import org.secondhand.secondhandhousebackend.DTO.Result;
import org.secondhand.secondhandhousebackend.entity.Properties;
import org.secondhand.secondhandhousebackend.entity.Users;
import org.secondhand.secondhandhousebackend.entity.Viewingappointments;
import org.secondhand.secondhandhousebackend.enums.UserRole;
import org.secondhand.secondhandhousebackend.enums.ViewStatus;
import org.secondhand.secondhandhousebackend.service.PropertiesService;
import org.secondhand.secondhandhousebackend.service.ViewingappointmentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/viewingappointments")
public class ViewingappointmentsController {

    @Autowired
    private ViewingappointmentsService viewingappointmentsService;

    @Autowired
    private PropertiesService propertiesService;


    // 用户申请预约看房
    @PostMapping("/apply")
    public Result applyViewingAppointment(@RequestBody Viewingappointments appointment, HttpServletRequest request) {
        // 从request attribute中获取用户信息（由拦截器设置）
        Users user = (Users) request.getAttribute("user");
        if (user == null) {
            return Result.fail("User not logged in");
        }
        Integer buyerId = user.getUserid(); // 从request中获取当前登录用户的ID
        appointment.setBuyerid(buyerId); // 设置买家ID
        appointment.setStatus(ViewStatus.待审批); // 设置预约状态为"待审批"
        boolean success = viewingappointmentsService.save(appointment);
        if (success) {
            return Result.ok("Appointment applied successfully");
        } else {
            return Result.fail("Failed to apply viewing appointment");
        }
    }

    // 审批预约（卖家和管理员可以审批自己房源的预约）
    @PutMapping("/approve/{appointmentid}")
    public Result approveViewingAppointment(@PathVariable Integer appointmentid, @RequestParam String statusStr, HttpServletRequest request) {
        // 从request attribute中获取用户信息（由拦截器设置）
        Users user = (Users) request.getAttribute("user");
        if (user == null) {
            return Result.fail("用户未登录");
        }

        Viewingappointments appointment = viewingappointmentsService.getById(appointmentid);
        if (appointment == null) {
            return Result.fail("Appointment not found");
        }

        // 权限检查：只有卖家和管理员可以审批预约
        if (user.getRole() == UserRole.管理员) {
            // 管理员可以审批所有预约
        } else if (user.getRole() == UserRole.卖家) {
            // 卖家只能审批自己房源的预约
            Properties property = propertiesService.getById(appointment.getPropertyid());
            if (property == null || !property.getSellerid().equals(user.getUserid())) {
                return Result.fail("无权审批此预约");
            }
        } else {
            // 买家无权审批预约
            return Result.fail("无权审批预约");
        }

        ViewStatus viewStatus = ViewStatus.fromDescription(statusStr);
        if (viewStatus == null) {
            return Result.fail("Invalid status");
        }

        appointment.setStatus(viewStatus); // 设置新的预约状态
        boolean success = viewingappointmentsService.updateById(appointment);
        if (success) {
            return Result.ok("Appointment approved successfully");
        } else {
            return Result.fail("Failed to approve viewing appointment");
        }
    }

    // 更新预约看房信息（买家只能更新自己的预约，卖家和管理员可以更新自己房源的预约）
    @PutMapping("/{appointmentid}")
    public Result updateViewingAppointment(@PathVariable Integer appointmentid, @RequestBody Viewingappointments appointment, HttpServletRequest request) {
        // 从request attribute中获取用户信息（由拦截器设置）
        Users user = (Users) request.getAttribute("user");
        if (user == null) {
            return Result.fail("用户未登录");
        }

        // 先查询原预约信息
        Viewingappointments existingAppointment = viewingappointmentsService.getById(appointmentid);
        if (existingAppointment == null) {
            return Result.fail("预约不存在");
        }

        // 权限检查
        if (user.getRole() == UserRole.管理员) {
            // 管理员可以更新所有预约
        } else if (user.getRole() == UserRole.卖家) {
            // 卖家只能更新自己房源的预约
            Properties property = propertiesService.getById(existingAppointment.getPropertyid());
            if (property == null || !property.getSellerid().equals(user.getUserid())) {
                return Result.fail("无权更新此预约");
            }
        } else {
            // 买家只能更新自己申请的预约
            if (!existingAppointment.getBuyerid().equals(user.getUserid())) {
                return Result.fail("无权更新此预约");
            }
        }

        appointment.setAppointmentid(appointmentid);
        boolean success = viewingappointmentsService.updateById(appointment);
        if (success) {
            return Result.ok();
        } else {
            return Result.fail("Failed to update viewing appointment");
        }
    }

    // 删除预约看房（买家只能删除自己的预约，卖家和管理员可以删除自己房源的预约）
    @DeleteMapping("/{appointmentid}")
    public Result deleteViewingAppointment(@PathVariable Integer appointmentid, HttpServletRequest request) {
        // 从request attribute中获取用户信息（由拦截器设置）
        Users user = (Users) request.getAttribute("user");
        if (user == null) {
            return Result.fail("用户未登录");
        }

        Viewingappointments appointment = viewingappointmentsService.getById(appointmentid);
        if (appointment == null) {
            return Result.fail("预约不存在");
        }

        // 权限检查
        if (user.getRole() == UserRole.管理员) {
            // 管理员可以删除所有预约
        } else if (user.getRole() == UserRole.卖家) {
            // 卖家只能删除自己房源的预约
            Properties property = propertiesService.getById(appointment.getPropertyid());
            if (property == null || !property.getSellerid().equals(user.getUserid())) {
                return Result.fail("无权删除此预约");
            }
        } else {
            // 买家只能删除自己申请的预约
            if (!appointment.getBuyerid().equals(user.getUserid())) {
                return Result.fail("无权删除此预约");
            }
        }

        boolean success = viewingappointmentsService.removeById(appointmentid);
        if (success) {
            return Result.ok();
        } else {
            return Result.fail("Failed to delete viewing appointment");
        }
    }

    // 获取单个预约看房详情（根据用户角色进行权限控制）
    @GetMapping("/{appointmentid}")
    public Result getViewingAppointment(@PathVariable Integer appointmentid, HttpServletRequest request) {
        // 从request attribute中获取用户信息（由拦截器设置）
        Users user = (Users) request.getAttribute("user");
        if (user == null) {
            return Result.fail("用户未登录");
        }

        Viewingappointments appointment = viewingappointmentsService.getById(appointmentid);
        if (appointment == null) {
            return Result.fail("Viewing appointment not found");
        }

        // 根据用户角色进行权限检查
        if (user.getRole() == UserRole.管理员) {
            // 管理员可以查看所有预约
            return Result.ok(appointment);
        } else if (user.getRole() == UserRole.卖家) {
            // 卖家只能查看自己房源的预约
            Properties property = propertiesService.getById(appointment.getPropertyid());
            if (property != null && property.getSellerid().equals(user.getUserid())) {
                return Result.ok(appointment);
            } else {
                return Result.fail("无权访问此预约");
            }
        } else {
            // 买家只能查看自己申请的预约
            if (appointment.getBuyerid().equals(user.getUserid())) {
                return Result.ok(appointment);
            } else {
                return Result.fail("无权访问此预约");
            }
        }
    }

    // 获取所有预约看房（根据用户角色过滤：买家只能看到自己申请的预约，卖家只能看到自己房源的预约，管理员可以看到所有预约）
    @GetMapping
    public Result getAllViewingAppointments(HttpServletRequest request) {
        // 从request attribute中获取用户信息（由拦截器设置）
        Users user = (Users) request.getAttribute("user");
        if (user == null) {
            return Result.fail("用户未登录");
        }

        List<Viewingappointments> appointmentsList;

        // 根据用户角色过滤预约
        if (user.getRole() == UserRole.管理员) {
            // 管理员可以查看所有预约
            appointmentsList = viewingappointmentsService.list();
        } else if (user.getRole() == UserRole.卖家) {
            // 卖家只能查看自己房源的预约
            // 先查询该卖家的所有房源ID
            LambdaQueryWrapper<Properties> propertiesQueryWrapper = new LambdaQueryWrapper<>();
            propertiesQueryWrapper.eq(Properties::getSellerid, user.getUserid());
            List<Properties> sellerProperties = propertiesService.list(propertiesQueryWrapper);
            
            // 提取房源ID列表
            List<Integer> propertyIds = sellerProperties.stream()
                    .map(Properties::getPropertyid)
                    .collect(Collectors.toList());
            
            if (propertyIds.isEmpty()) {
                // 如果卖家没有房源，返回空列表
                appointmentsList = List.of();
            } else {
                // 查询这些房源的预约
                LambdaQueryWrapper<Viewingappointments> appointmentsQueryWrapper = new LambdaQueryWrapper<>();
                appointmentsQueryWrapper.in(Viewingappointments::getPropertyid, propertyIds);
                appointmentsList = viewingappointmentsService.list(appointmentsQueryWrapper);
            }
        } else {
            // 买家只能查看自己申请的预约
            LambdaQueryWrapper<Viewingappointments> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Viewingappointments::getBuyerid, user.getUserid());
            appointmentsList = viewingappointmentsService.list(queryWrapper);
        }

        return Result.ok(appointmentsList);
    }
}
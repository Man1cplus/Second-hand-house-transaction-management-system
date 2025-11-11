package org.secondhand.secondhandhousebackend.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.secondhand.secondhandhousebackend.DTO.Result;
import org.secondhand.secondhandhousebackend.entity.Users;
import org.secondhand.secondhandhousebackend.entity.Viewingappointments;
import org.secondhand.secondhandhousebackend.enums.ViewStatus;
import org.secondhand.secondhandhousebackend.service.ViewingappointmentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/viewingappointments")
public class ViewingappointmentsController {

    @Autowired
    private ViewingappointmentsService viewingappointmentsService;


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

    // 管理员审批预约
    @PutMapping("/approve/{appointmentid}")
    public Result approveViewingAppointment(@PathVariable Integer appointmentid, @RequestParam String statusStr) {
        Viewingappointments appointment = viewingappointmentsService.getById(appointmentid);
        if (appointment == null) {
            return Result.fail("Appointment not found");
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

    // 更新预约看房信息
    @PutMapping("/{appointmentid}")
    public Result updateViewingAppointment(@PathVariable Integer appointmentid, @RequestBody Viewingappointments appointment) {
        appointment.setAppointmentid(appointmentid);
        boolean success = viewingappointmentsService.updateById(appointment);
        if (success) {
            return Result.ok();
        } else {
            return Result.fail("Failed to update viewing appointment");
        }
    }

    // 删除预约看房
    @DeleteMapping("/{appointmentid}")
    public Result deleteViewingAppointment(@PathVariable Integer appointmentid) {
        boolean success = viewingappointmentsService.removeById(appointmentid);
        if (success) {
            return Result.ok();
        } else {
            return Result.fail("Failed to delete viewing appointment");
        }
    }

    // 获取单个预约看房详情
    @GetMapping("/{appointmentid}")
    public Result getViewingAppointment(@PathVariable Integer appointmentid) {
        Viewingappointments appointment = viewingappointmentsService.getById(appointmentid);
        if (appointment != null) {
            return Result.ok(appointment);
        } else {
            return Result.fail("Viewing appointment not found");
        }
    }

    // 获取所有预约看房
    @GetMapping
    public Result getAllViewingAppointments() {
        List<Viewingappointments> appointments = viewingappointmentsService.list();
        return Result.ok(appointments);
    }
}
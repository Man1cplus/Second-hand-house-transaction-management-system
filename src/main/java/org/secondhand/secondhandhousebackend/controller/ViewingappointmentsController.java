package org.secondhand.secondhandhousebackend.controller;

import org.secondhand.secondhandhousebackend.DTO.Result;
import org.secondhand.secondhandhousebackend.entity.Viewingappointments;
import org.secondhand.secondhandhousebackend.service.ViewingappointmentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/viewingappointments")
public class ViewingappointmentsController {

    @Autowired
    private ViewingappointmentsService viewingappointmentsService;

    // 创建新的预约看房
    @PostMapping
    public Result createViewingAppointment(@RequestBody Viewingappointments appointment) {
        boolean success = viewingappointmentsService.save(appointment);
        if (success) {
            return Result.ok();
        } else {
            return Result.fail("Failed to create viewing appointment");
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
package org.secondhand.secondhandhousebackend.controller;

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
    public boolean createViewingAppointment(@RequestBody Viewingappointments appointment) {
        return viewingappointmentsService.save(appointment);
    }

    // 更新预约看房信息
    @PutMapping("/{appointmentid}")
    public boolean updateViewingAppointment(@PathVariable Integer appointmentid, @RequestBody Viewingappointments appointment) {
        appointment.setAppointmentid(appointmentid);
        return viewingappointmentsService.updateById(appointment);
    }

    // 删除预约看房
    @DeleteMapping("/{appointmentid}")
    public boolean deleteViewingAppointment(@PathVariable Integer appointmentid) {
        return viewingappointmentsService.removeById(appointmentid);
    }

    // 获取单个预约看房详情
    @GetMapping("/{appointmentid}")
    public Viewingappointments getViewingAppointment(@PathVariable Integer appointmentid) {
        return viewingappointmentsService.getById(appointmentid);
    }

    // 获取所有预约看房
    @GetMapping
    public List<Viewingappointments> getAllViewingAppointments() {
        return viewingappointmentsService.list();
    }
}
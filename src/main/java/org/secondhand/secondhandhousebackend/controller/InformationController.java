package org.secondhand.secondhandhousebackend.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.secondhand.secondhandhousebackend.DTO.Result;
import org.secondhand.secondhandhousebackend.entity.Information;
import org.secondhand.secondhandhousebackend.entity.Users;
import org.secondhand.secondhandhousebackend.enums.UserRole;
import org.secondhand.secondhandhousebackend.service.InformationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/info")
public class InformationController {

    @Autowired
    private InformationService informationService;

    // 创建新的资讯 - 只有管理员可以创建
    @PostMapping
    public Result createInformation(@RequestBody Information information, HttpServletRequest request) {
        // 从request attribute中获取用户信息（由拦截器设置）
        Users user = (Users) request.getAttribute("user");
        
        // 检查用户是否为管理员
        if (user == null || user.getRole() != UserRole.管理员) {
            return Result.fail("只有管理员可以创建资讯");
        }
        
        boolean success = informationService.save(information);
        if (success) {
            // 获取刚刚插入的资讯 ID
            Integer infoid = information.getInfoid();
            // 保存资讯与标签的关系
            informationService.saveInfoTags(infoid, information.getTagIds());
            return Result.ok();
        } else {
            return Result.fail("创建资讯失败!");
        }
    }

    // 更新资讯信息 - 已禁用，资讯不能修改
    @PutMapping("/{infoid}")
    public Result updateInformation(@PathVariable Integer infoid, @RequestBody Information information) {
        return Result.fail("资讯创建后不能修改，只能删除");
    }

    // 删除资讯 - 只有管理员可以删除
    @DeleteMapping("/{infoid}")
    public Result deleteInformation(@PathVariable Integer infoid, HttpServletRequest request) {
        // 从request attribute中获取用户信息（由拦截器设置）
        Users user = (Users) request.getAttribute("user");
        
        // 检查用户是否为管理员
        if (user == null || user.getRole() != UserRole.管理员) {
            return Result.fail("只有管理员可以删除资讯");
        }
        
        boolean success = informationService.deleteInfoWithTags(infoid);
        if (success) {
            return Result.ok();
        } else {
            return Result.fail("删除资讯失败!");
        }
    }

    // 获取单个资讯详情
    @GetMapping("/{infoid}")
    public Result getInformation(@PathVariable Integer infoid) {
        Information information = informationService.getById(infoid);
        if (information != null) {
            // 获取资讯的标签信息
            information.setTagIds(informationService.getInfoTagIds(infoid));
            return Result.ok(information);
        } else {
            return Result.fail("Information not found");
        }
    }

    // 获取所有资讯
    @GetMapping
    public Result getAllInformation() {
        List<Information> informationList = informationService.list();
        // 获取每个资讯的标签信息
        for (Information information : informationList) {
            information.setTagIds(informationService.getInfoTagIds(information.getInfoid()));
        }
        return Result.ok(informationList);
    }
}
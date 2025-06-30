//package org.secondhand.secondhandhousebackend.controller;
//
//import org.secondhand.secondhandhousebackend.DTO.Result;
//import org.secondhand.secondhandhousebackend.entity.Informationcategories;
//import org.secondhand.secondhandhousebackend.service.InformationcategoriesService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
///**
// * 咨询分类管理控制器
// */
//@RestController
//@RequestMapping("/infocategories")
//public class InformationcategoriesController {
//
//    @Autowired
//    private InformationcategoriesService informationcategoriesService;
//
//    // 创建新的资讯分类
//    @PostMapping
//    public Result createInfoCategory(@RequestBody Informationcategories infoCategory) {
//        boolean success = informationcategoriesService.save(infoCategory);
//        if (success) {
//            return Result.ok();
//        } else {
//            return Result.fail("Failed to create info category");
//        }
//    }
//
//    // 更新资讯分类信息
//    @PutMapping("/{infocategoryid}")
//    public Result updateInfoCategory(@PathVariable Integer infocategoryid, @RequestBody Informationcategories infoCategory) {
//        infoCategory.setInfocategoryid(infocategoryid);
//        boolean success = informationcategoriesService.updateById(infoCategory);
//        if (success) {
//            return Result.ok();
//        } else {
//            return Result.fail("Failed to update info category");
//        }
//    }
//
//    // 删除资讯分类
//    @DeleteMapping("/{infocategoryid}")
//    public Result deleteInfoCategory(@PathVariable Integer infocategoryid) {
//        boolean success = informationcategoriesService.removeById(infocategoryid);
//        if (success) {
//            return Result.ok();
//        } else {
//            return Result.fail("Failed to delete info category");
//        }
//    }
//
//    // 获取单个资讯分类详情
//    @GetMapping("/{infocategoryid}")
//    public Result getInfoCategory(@PathVariable Integer infocategoryid) {
//        Informationcategories infoCategory = informationcategoriesService.getById(infocategoryid);
//        if (infoCategory != null) {
//            return Result.ok(infoCategory);
//        } else {
//            return Result.fail("Info category not found");
//        }
//    }
//
//    // 获取所有资讯分类
//    @GetMapping
//    public Result getAllInfoCategories() {
//        List<Informationcategories> infoCategories = informationcategoriesService.list();
//        return Result.ok(infoCategories);
//    }
//}
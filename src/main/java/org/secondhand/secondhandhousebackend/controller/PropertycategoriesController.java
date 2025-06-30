//package org.secondhand.secondhandhousebackend.controller;
//
//import org.secondhand.secondhandhousebackend.DTO.Result;
//import org.secondhand.secondhandhousebackend.entity.Propertycategories;
//import org.secondhand.secondhandhousebackend.service.PropertycategoriesService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/propertycategories")
//public class PropertycategoriesController {
//
//    @Autowired
//    private PropertycategoriesService propertycategoriesService;
//
//    // 创建新的房源分类
//    @PostMapping
//    public Result createPropertyCategory(@RequestBody Propertycategories propertyCategory) {
//        boolean success = propertycategoriesService.save(propertyCategory);
//        if (success) {
//            return Result.ok();
//        } else {
//            return Result.fail("Failed to create property category");
//        }
//    }
//
//    // 更新房源分类信息
//    @PutMapping("/{categoryid}")
//    public Result updatePropertyCategory(@PathVariable Integer categoryid, @RequestBody Propertycategories propertyCategory) {
//        propertyCategory.setCategoryid(categoryid);
//        boolean success = propertycategoriesService.updateById(propertyCategory);
//        if (success) {
//            return Result.ok();
//        } else {
//            return Result.fail("Failed to update property category");
//        }
//    }
//
//    // 删除房源分类
//    @DeleteMapping("/{categoryid}")
//    public Result deletePropertyCategory(@PathVariable Integer categoryid) {
//        boolean success = propertycategoriesService.removeById(categoryid);
//        if (success) {
//            return Result.ok();
//        } else {
//            return Result.fail("Failed to delete property category");
//        }
//    }
//
//    // 获取单个房源分类详情
//    @GetMapping("/{categoryid}")
//    public Result getPropertyCategory(@PathVariable Integer categoryid) {
//        Propertycategories propertyCategory = propertycategoriesService.getById(categoryid);
//        if (propertyCategory != null) {
//            return Result.ok(propertyCategory);
//        } else {
//            return Result.fail("Property category not found");
//        }
//    }
//
//    // 获取所有房源分类
//    @GetMapping
//    public Result getAllPropertyCategories() {
//        List<Propertycategories> propertyCategories = propertycategoriesService.list();
//        return Result.ok(propertyCategories);
//    }
//}
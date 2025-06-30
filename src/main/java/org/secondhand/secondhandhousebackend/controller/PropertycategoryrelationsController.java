//package org.secondhand.secondhandhousebackend.controller;
//
//import org.secondhand.secondhandhousebackend.DTO.Result;
//import org.secondhand.secondhandhousebackend.entity.Propertycategoryrelations;
//import org.secondhand.secondhandhousebackend.service.PropertycategoryrelationsService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/propertycategoryrelations")
//public class PropertycategoryrelationsController {
//
//    @Autowired
//    private PropertycategoryrelationsService propertycategoryrelationsService;
//
//    // 创建新的房源分类关系
//    @PostMapping
//    public Result createPropertyCategoryRelation(@RequestBody Propertycategoryrelations relation) {
//        boolean success = propertycategoryrelationsService.save(relation);
//        if (success) {
//            return Result.ok();
//        } else {
//            return Result.fail("Failed to create property category relation");
//        }
//    }
//
//    // 更新房源分类关系信息
//    @PutMapping("/{relationid}")
//    public Result updatePropertyCategoryRelation(@PathVariable Integer relationid, @RequestBody Propertycategoryrelations relation) {
//        relation.setRelationid(relationid);
//        boolean success = propertycategoryrelationsService.updateById(relation);
//        if (success) {
//            return Result.ok();
//        } else {
//            return Result.fail("Failed to update property category relation");
//        }
//    }
//
//    // 删除房源分类关系
//    @DeleteMapping("/{relationid}")
//    public Result deletePropertyCategoryRelation(@PathVariable Integer relationid) {
//        boolean success = propertycategoryrelationsService.removeById(relationid);
//        if (success) {
//            return Result.ok();
//        } else {
//            return Result.fail("Failed to delete property category relation");
//        }
//    }
//
//    // 获取单个房源分类关系详情
//    @GetMapping("/{relationid}")
//    public Result getPropertyCategoryRelation(@PathVariable Integer relationid) {
//        Propertycategoryrelations relation = propertycategoryrelationsService.getById(relationid);
//        if (relation != null) {
//            return Result.ok(relation);
//        } else {
//            return Result.fail("Property category relation not found");
//        }
//    }
//
//    // 获取所有房源分类关系
//    @GetMapping
//    public Result getAllPropertyCategoryRelations() {
//        List<Propertycategoryrelations> relations = propertycategoryrelationsService.list();
//        return Result.ok(relations);
//    }
//}
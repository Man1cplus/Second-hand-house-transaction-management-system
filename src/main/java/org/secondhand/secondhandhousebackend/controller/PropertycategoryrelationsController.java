package org.secondhand.secondhandhousebackend.controller;

import org.secondhand.secondhandhousebackend.entity.Propertycategoryrelations;
import org.secondhand.secondhandhousebackend.service.PropertycategoryrelationsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/propertycategoryrelations")
public class PropertycategoryrelationsController {

    @Autowired
    private PropertycategoryrelationsService propertycategoryrelationsService;

    // 创建新的房源分类关系
    @PostMapping
    public boolean createPropertyCategoryRelation(@RequestBody Propertycategoryrelations relation) {
        return propertycategoryrelationsService.save(relation);
    }

    // 更新房源分类关系信息
    @PutMapping("/{relationid}")
    public boolean updatePropertyCategoryRelation(@PathVariable Integer relationid, @RequestBody Propertycategoryrelations relation) {
        relation.setRelationid(relationid);
        return propertycategoryrelationsService.updateById(relation);
    }

    // 删除房源分类关系
    @DeleteMapping("/{relationid}")
    public boolean deletePropertyCategoryRelation(@PathVariable Integer relationid) {
        return propertycategoryrelationsService.removeById(relationid);
    }

    // 获取单个房源分类关系详情
    @GetMapping("/{relationid}")
    public Propertycategoryrelations getPropertyCategoryRelation(@PathVariable Integer relationid) {
        return propertycategoryrelationsService.getById(relationid);
    }

    // 获取所有房源分类关系
    @GetMapping
    public List<Propertycategoryrelations> getAllPropertyCategoryRelations() {
        return propertycategoryrelationsService.list();
    }
}
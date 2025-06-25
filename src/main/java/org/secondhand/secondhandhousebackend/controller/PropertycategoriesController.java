package org.secondhand.secondhandhousebackend.controller;

import org.secondhand.secondhandhousebackend.entity.Propertycategories;
import org.secondhand.secondhandhousebackend.service.PropertycategoriesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/propertycategories")
public class PropertycategoriesController {

    @Autowired
    private PropertycategoriesService propertycategoriesService;

    // 创建新的房源分类
    @PostMapping
    public boolean createPropertyCategory(@RequestBody Propertycategories propertyCategory) {
        return propertycategoriesService.save(propertyCategory);
    }

    // 更新房源分类信息
    @PutMapping("/{categoryid}")
    public boolean updatePropertyCategory(@PathVariable Integer categoryid, @RequestBody Propertycategories propertyCategory) {
        propertyCategory.setCategoryid(categoryid);
        return propertycategoriesService.updateById(propertyCategory);
    }

    // 删除房源分类
    @DeleteMapping("/{categoryid}")
    public boolean deletePropertyCategory(@PathVariable Integer categoryid) {
        return propertycategoriesService.removeById(categoryid);
    }

    // 获取单个房源分类详情
    @GetMapping("/{categoryid}")
    public Propertycategories getPropertyCategory(@PathVariable Integer categoryid) {
        return propertycategoriesService.getById(categoryid);
    }

    // 获取所有房源分类
    @GetMapping
    public List<Propertycategories> getAllPropertyCategories() {
        return propertycategoriesService.list();
    }
}
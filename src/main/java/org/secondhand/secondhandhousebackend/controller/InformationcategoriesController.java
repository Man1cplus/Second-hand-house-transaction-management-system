package org.secondhand.secondhandhousebackend.controller;

import org.secondhand.secondhandhousebackend.entity.Informationcategories;
import org.secondhand.secondhandhousebackend.service.InformationcategoriesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 咨询分类管理控制器
 */
@RestController
@RequestMapping("/infocategories")
public class InformationcategoriesController {

    @Autowired
    private InformationcategoriesService informationcategoriesService;

    // 创建新的资讯分类
    @PostMapping
    public boolean createInfoCategory(@RequestBody Informationcategories infoCategory) {
        return informationcategoriesService.save(infoCategory);
    }

    // 更新资讯分类信息
    @PutMapping("/{infocategoryid}")
    public boolean updateInfoCategory(@PathVariable Integer infocategoryid, @RequestBody Informationcategories infoCategory) {
        infoCategory.setInfocategoryid(infocategoryid);
        return informationcategoriesService.updateById(infoCategory);
    }

    // 删除资讯分类
    @DeleteMapping("/{infocategoryid}")
    public boolean deleteInfoCategory(@PathVariable Integer infocategoryid) {
        return informationcategoriesService.removeById(infocategoryid);
    }

    // 获取单个资讯分类详情
    @GetMapping("/{infocategoryid}")
    public Informationcategories getInfoCategory(@PathVariable Integer infocategoryid) {
        return informationcategoriesService.getById(infocategoryid);
    }

    // 获取所有资讯分类
    @GetMapping
    public List<Informationcategories> getAllInfoCategories() {
        return informationcategoriesService.list();
    }
}
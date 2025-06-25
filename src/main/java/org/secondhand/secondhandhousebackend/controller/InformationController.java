package org.secondhand.secondhandhousebackend.controller;

import org.secondhand.secondhandhousebackend.entity.Information;
import org.secondhand.secondhandhousebackend.service.InformationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/info")
public class InformationController {

    @Autowired
    private InformationService informationService;

    // 创建新的资讯
    @PostMapping
    public boolean createInformation(@RequestBody Information information) {
        return informationService.save(information);
    }

    // 更新资讯信息
    @PutMapping("/{infoid}")
    public boolean updateInformation(@PathVariable Integer infoid, @RequestBody Information information) {
        information.setInfoid(infoid);
        return informationService.updateById(information);
    }

    // 删除资讯
    @DeleteMapping("/{infoid}")
    public boolean deleteInformation(@PathVariable Integer infoid) {
        return informationService.removeById(infoid);
    }

    // 获取单个资讯详情
    @GetMapping("/{infoid}")
    public Information getInformation(@PathVariable Integer infoid) {
        return informationService.getById(infoid);
    }

    // 获取所有资讯
    @GetMapping
    public List<Information> getAllInformation() {
        return informationService.list();
    }
}
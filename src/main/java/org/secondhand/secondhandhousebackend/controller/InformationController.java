package org.secondhand.secondhandhousebackend.controller;

import org.secondhand.secondhandhousebackend.DTO.Result;
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
    public Result createInformation(@RequestBody Information information) {
        boolean success = informationService.save(information);
        if (success) {
            return Result.ok();
        } else {
            return Result.fail("Failed to create information");
        }
    }

    // 更新资讯信息
    @PutMapping("/{infoid}")
    public Result updateInformation(@PathVariable Integer infoid, @RequestBody Information information) {
        information.setInfoid(infoid);
        boolean success = informationService.updateById(information);
        if (success) {
            return Result.ok();
        } else {
            return Result.fail("Failed to update information");
        }
    }

    // 删除资讯
    @DeleteMapping("/{infoid}")
    public Result deleteInformation(@PathVariable Integer infoid) {
        boolean success = informationService.removeById(infoid);
        if (success) {
            return Result.ok();
        } else {
            return Result.fail("Failed to delete information");
        }
    }

    // 获取单个资讯详情
    @GetMapping("/{infoid}")
    public Result getInformation(@PathVariable Integer infoid) {
        Information information = informationService.getById(infoid);
        if (information != null) {
            return Result.ok(information);
        } else {
            return Result.fail("Information not found");
        }
    }

    // 获取所有资讯
    @GetMapping
    public Result getAllInformation() {
        List<Information> informationList = informationService.list();
        return Result.ok(informationList);
    }
}
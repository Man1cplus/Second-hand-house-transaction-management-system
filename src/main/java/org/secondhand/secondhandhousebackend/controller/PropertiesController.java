package org.secondhand.secondhandhousebackend.controller;

import org.secondhand.secondhandhousebackend.DTO.Result;
import org.secondhand.secondhandhousebackend.entity.Properties;
import org.secondhand.secondhandhousebackend.service.PropertiesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/properties")
public class PropertiesController {

    @Autowired
    private PropertiesService propertiesService;

    // 创建新的房源
    @PostMapping
    public Result createProperty(@RequestBody Properties property) {
        boolean success = propertiesService.save(property);
        if (success) {
            propertiesService.savePropertyTags(property.getPropertyid(), property.getTagIds());
            return Result.ok();
        } else {
            return Result.fail("Failed to create property");
        }
    }

    // 更新房源信息
    @PutMapping("/{propertyid}")
    public Result updateProperty(@PathVariable Integer propertyid, @RequestBody Properties property) {
        property.setPropertyid(propertyid);
        boolean success = propertiesService.updateById(property);
        if (success) {
            propertiesService.updatePropertyTags(propertyid, property.getTagIds());
            return Result.ok();
        } else {
            return Result.fail("Failed to update property");
        }
    }

    // 删除房源
    @DeleteMapping("/{propertyid}")
    public Result deleteProperty(@PathVariable Integer propertyid) {
        boolean success = propertiesService.deletePropertyWithTagsAndFavorites(propertyid);
        if (success) {
            return Result.ok();
        } else {
            return Result.fail("Failed to delete property");
        }
    }

    // 获取单个房源详情
    @GetMapping("/{propertyid}")
    public Result getProperty(@PathVariable Integer propertyid) {
        Properties property = propertiesService.getById(propertyid);
        if (property != null) {
            property.setTagIds(propertiesService.getPropertyTagIds(propertyid));
            return Result.ok(property);
        } else {
            return Result.fail("Property not found");
        }
    }

    // 获取所有房源
    @GetMapping
    public Result getAllProperties() {
        List<Properties> propertiesList = propertiesService.list();
        for (Properties property : propertiesList) {
            property.setTagIds(propertiesService.getPropertyTagIds(property.getPropertyid()));
        }
        return Result.ok(propertiesList);
    }
}
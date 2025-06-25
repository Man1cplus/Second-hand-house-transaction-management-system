package org.secondhand.secondhandhousebackend.controller;

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
    public boolean createProperty(@RequestBody Properties property) {
        return propertiesService.save(property);
    }

    // 更新房源信息
    @PutMapping("/{propertyid}")
    public boolean updateProperty(@PathVariable Integer propertyid, @RequestBody Properties property) {
        property.setPropertyid(propertyid);
        return propertiesService.updateById(property);
    }

    // 删除房源
    @DeleteMapping("/{propertyid}")
    public boolean deleteProperty(@PathVariable Integer propertyid) {
        return propertiesService.removeById(propertyid);
    }

    // 获取单个房源详情
    @GetMapping("/{propertyid}")
    public Properties getProperty(@PathVariable Integer propertyid) {
        return propertiesService.getById(propertyid);
    }

    // 获取所有房源
    @GetMapping
    public List<Properties> getAllProperties() {
        return propertiesService.list();
    }
}
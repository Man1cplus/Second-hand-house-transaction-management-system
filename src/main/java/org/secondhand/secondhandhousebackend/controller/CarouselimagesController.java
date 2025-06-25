package org.secondhand.secondhandhousebackend.controller;

import org.secondhand.secondhandhousebackend.entity.Carouselimages;
import org.secondhand.secondhandhousebackend.service.CarouselimagesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/carouselimages")
public class CarouselimagesController {

    @Autowired
    private CarouselimagesService carouselimagesService;

    // 创建新的轮播图
    @PostMapping
    public boolean createCarouselimage(@RequestBody Carouselimages carouselimage) {
        return carouselimagesService.save(carouselimage);
    }

    // 更新轮播图信息
    @PutMapping("/{imageid}")
    public boolean updateCarouselimage(@PathVariable Integer imageid, @RequestBody Carouselimages carouselimage) {
        carouselimage.setImageid(imageid);
        return carouselimagesService.updateById(carouselimage);
    }

    // 删除轮播图
    @DeleteMapping("/{imageid}")
    public boolean deleteCarouselimage(@PathVariable Integer imageid) {
        return carouselimagesService.removeById(imageid);
    }

    // 获取单个轮播图详情
    @GetMapping("/{imageid}")
    public Carouselimages getCarouselimage(@PathVariable Integer imageid) {
        return carouselimagesService.getById(imageid);
    }

    // 获取所有轮播图
    @GetMapping
    public List<Carouselimages> getAllCarouselimages() {
        return carouselimagesService.list();
    }
}
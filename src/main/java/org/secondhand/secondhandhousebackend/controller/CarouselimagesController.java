//package org.secondhand.secondhandhousebackend.controller;
//
//import org.secondhand.secondhandhousebackend.DTO.Result;
//import org.secondhand.secondhandhousebackend.entity.Carouselimages;
//import org.secondhand.secondhandhousebackend.service.CarouselimagesService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/carouselimages")
//public class CarouselimagesController {
//
//    @Autowired
//    private CarouselimagesService carouselimagesService;
//
//    // 创建新的轮播图
//    @PostMapping
//    public Result createCarouselimage(@RequestBody Carouselimages carouselimage) {
//        boolean success = carouselimagesService.save(carouselimage);
//        if (success) {
//            return Result.ok();
//        } else {
//            return Result.fail("Failed to create carousel image");
//        }
//    }
//
//    // 更新轮播图信息
//    @PutMapping("/{imageid}")
//    public Result updateCarouselimage(@PathVariable Integer imageid, @RequestBody Carouselimages carouselimage) {
//        carouselimage.setImageid(imageid);
//        boolean success = carouselimagesService.updateById(carouselimage);
//        if (success) {
//            return Result.ok();
//        } else {
//            return Result.fail("Failed to update carousel image");
//        }
//    }
//
//    // 删除轮播图
//    @DeleteMapping("/{imageid}")
//    public Result deleteCarouselimage(@PathVariable Integer imageid) {
//        boolean success = carouselimagesService.removeById(imageid);
//        if (success) {
//            return Result.ok();
//        } else {
//            return Result.fail("Failed to delete carousel image");
//        }
//    }
//
//    // 获取单个轮播图详情
//    @GetMapping("/{imageid}")
//    public Result getCarouselimage(@PathVariable Integer imageid) {
//        Carouselimages carouselimage = carouselimagesService.getById(imageid);
//        if (carouselimage != null) {
//            return Result.ok(carouselimage);
//        } else {
//            return Result.fail("Carousel image not found");
//        }
//    }
//
//    // 获取所有轮播图
//    @GetMapping
//    public Result getAllCarouselimages() {
//        List<Carouselimages> carouselimages = carouselimagesService.list();
//        return Result.ok(carouselimages);
//    }
//}
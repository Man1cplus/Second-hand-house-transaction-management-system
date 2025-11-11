package org.secondhand.secondhandhousebackend.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.secondhand.secondhandhousebackend.DTO.Result;
import org.secondhand.secondhandhousebackend.entity.Users;
import org.secondhand.secondhandhousebackend.service.UserfavoritesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("users/favorites")
public class UserFavoritesController {
    @Autowired
    private UserfavoritesService userFavoritesService;

    @GetMapping
    public Result getUserFavorites(@RequestParam Integer page, @RequestParam Integer size, HttpServletRequest request) {
        if (page == null || size == null) {
            return Result.fail("分页参数不能为空");
        }
        // 从request attribute中获取用户信息（由拦截器设置）
        Users user = (Users) request.getAttribute("user");
        if (user == null) {
            return Result.fail("用户未登录");
        }
        // 创建临时session用于兼容service方法
        HttpSession session = request.getSession();
        session.setAttribute("user", user);
        return Result.ok(userFavoritesService.getUserFavorites(page, size, session));
    }

    @PostMapping("/{propertyId}")
    public Result collectProperty(HttpServletRequest request, @PathVariable Integer propertyId) {
        Users user = (Users) request.getAttribute("user");
        if (user == null) {
            return Result.fail("用户未登录");
        }
        int userId = user.getUserid();
        boolean result = userFavoritesService.collectProperty(userId, propertyId);
        if (result) {
            return Result.ok("收藏成功");
        } else {
            return Result.fail("房源已收藏");
        }
    }

    @DeleteMapping("/{propertyId}")
    public Result unCollectProperty(@PathVariable Integer propertyId, HttpServletRequest request) {
        Users user = (Users) request.getAttribute("user");
        if (user == null) {
            return Result.fail("用户未登录");
        }
        int userId = user.getUserid();
        boolean result = userFavoritesService.unCollectProperty(userId, propertyId);
        if (result) {
            return Result.ok("取消收藏成功");
        } else {
            return Result.fail("取消收藏失败");
        }
    }
    //检查是否收藏房源
    @GetMapping("/check/{propertyId}")
    public Result checkFavorite(HttpServletRequest request, @PathVariable Integer propertyId) {
        Users user = (Users) request.getAttribute("user");
        if (user == null) {
            return Result.fail("用户未登录");
        }
        int userId = user.getUserid();
        return Result.ok(userFavoritesService.isFavorite(userId, propertyId));
    }
}

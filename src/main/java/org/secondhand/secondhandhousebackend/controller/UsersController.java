package org.secondhand.secondhandhousebackend.controller;

import org.secondhand.secondhandhousebackend.entity.Users;
import org.secondhand.secondhandhousebackend.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UsersController {

    @Autowired
    private UsersService usersService;

    // 创建新用户
    @PostMapping
    public boolean createUser(@RequestBody Users user) {
        return usersService.save(user);
    }

    // 更新用户信息
    @PutMapping("/{userid}")
    public boolean updateUser(@PathVariable Integer userid, @RequestBody Users user) {
        user.setUserid(userid);
        return usersService.updateById(user);
    }

    // 删除用户
    @DeleteMapping("/{userid}")
    public boolean deleteUser(@PathVariable Integer userid) {
        return usersService.removeById(userid);
    }

    // 获取单个用户详情
    @GetMapping("/{userid}")
    public Users getUser(@PathVariable Integer userid) {
        return usersService.getById(userid);
    }

    // 获取所有用户
    @GetMapping
    public List<Users> getAllUsers() {
        return usersService.list();
    }
}
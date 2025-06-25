package org.secondhand.secondhandhousebackend.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.secondhand.secondhandhousebackend.DTO.LoginFormDTO;
import org.secondhand.secondhandhousebackend.DTO.Result;
import org.secondhand.secondhandhousebackend.VO.UserVO;
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

    @PostMapping("/login")
    public Result login(@RequestBody LoginFormDTO loginForm, HttpSession session) {
        boolean success = usersService.login(loginForm, session);
        if (success) {
            Users user = (Users) session.getAttribute("user");
            return Result.ok(UserVO.fromUser(user));
        }
        return Result.fail("用户名或密码错误");
    }

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
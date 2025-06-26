package org.secondhand.secondhandhousebackend.controller;

import jakarta.servlet.http.HttpSession;
import org.secondhand.secondhandhousebackend.DTO.LoginFormDTO;
import org.secondhand.secondhandhousebackend.DTO.Result;
import org.secondhand.secondhandhousebackend.DTO.UsersDTO;
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

    // 用户登录
    @PostMapping("/login")
    public Result login(@RequestBody LoginFormDTO loginForm, HttpSession session) {
        boolean success = usersService.login(loginForm, session);
        if (success) {
            Users user = (Users) session.getAttribute("user");
            return Result.ok(UserVO.fromUser(user));
        }
        return Result.fail("用户名或密码错误");
    }

    @PostMapping("/register")
    public Result registerUser(@RequestBody Users user, HttpSession session) {
        //用户名，密码
        Result result = usersService.register(user, session);
        return result;
    }

    // 创建新用户
    @PostMapping
    public Result createUser(@RequestBody Users user) {
        boolean success = usersService.save(user);
        if (success) {
            return Result.ok();
        } else {
            return Result.fail("Failed to create user");
        }
    }

    // 更新用户信息
    @PutMapping("/{userid}")
    public Result updateUser(@PathVariable Integer userid, @RequestBody Users user) {
        user.setUserid(userid);
        boolean success = usersService.updateById(user);
        if (success) {
            return Result.ok();
        } else {
            return Result.fail("Failed to update user");
        }
    }

    // 删除用户
    @DeleteMapping("/{userid}")
    public Result deleteUser(@PathVariable Integer userid) {
        boolean success = usersService.removeById(userid);
        if (success) {
            return Result.ok();
        } else {
            return Result.fail("Failed to delete user");
        }
    }

    // 获取单个用户详情
    @GetMapping("/{userid}")
    public Result getUser(@PathVariable Integer userid) {
        Users user = usersService.getById(userid);
        if (user != null) {
            return Result.ok(user);
        } else {
            return Result.fail("User not found");
        }
    }

    // 获取所有用户
    @GetMapping
    public Result getAllUsers() {
        List<Users> users = usersService.list();
        return Result.ok(users);
    }

    // 个人信息修改

    @PutMapping("/profile/username")
    public Result changeUser(@RequestBody UsersDTO UserDto,HttpSession session) {
        return usersService.changeUser(UserDto, session);
    }
}
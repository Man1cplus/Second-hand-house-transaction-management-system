package org.secondhand.secondhandhousebackend.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.secondhand.secondhandhousebackend.DTO.LoginFormDTO;
import org.secondhand.secondhandhousebackend.DTO.Result;
import org.secondhand.secondhandhousebackend.DTO.UsersDTO;
import org.secondhand.secondhandhousebackend.VO.UserVO;
import org.secondhand.secondhandhousebackend.entity.Users;
import org.secondhand.secondhandhousebackend.service.UsersService;
import org.secondhand.secondhandhousebackend.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UsersController {

    @Autowired
    private UsersService usersService;

    @Autowired
    private JwtUtil jwtUtil;

    // 用户登录
    @PostMapping("/login")
    public Result login(@RequestBody LoginFormDTO loginForm, HttpSession session) {
        boolean success = usersService.login(loginForm, session);
        if (success) {
            Users user = (Users) session.getAttribute("user");
            // 生成JWT token（包含角色信息）
            String role = user.getRole() != null ? user.getRole().toString() : null;
            String token = jwtUtil.generateToken(user.getUserid(), user.getUsername(), role);
            
            // 返回用户信息和token
            Map<String, Object> data = new HashMap<>();
            data.put("user", UserVO.fromUser(user));
            data.put("token", token);
            
            return Result.ok(data);
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
    public Result changeUser(@RequestBody UsersDTO UserDto, HttpServletRequest request) {
        // 从request attribute中获取用户信息（由拦截器设置）
        Users user = (Users) request.getAttribute("user");
        if (user == null) {
            return Result.fail("用户还没有登录！");
        }
        // 创建临时session用于兼容service方法
        HttpSession session = request.getSession();
        session.setAttribute("user", user);
        return usersService.changeUser(UserDto, session);
    }

    @PostMapping("/logout")
    public Result logout(HttpServletRequest request) {
        // JWT是无状态的，客户端只需要删除token即可
        // 这里可以添加token黑名单机制（可选）
        return Result.ok("登出成功");
    }

    // 更新用户头像
    @PutMapping("/avatar")
    public Result updateAvatar(@RequestBody Map<String, String> requestBody, HttpServletRequest request) {
        // 从request attribute中获取用户信息（由拦截器设置）
        Users user = (Users) request.getAttribute("user");
        if (user == null) {
            return Result.fail("用户还没有登录！");
        }

        String avatarUri = requestBody.get("avatar");
        if (avatarUri == null || avatarUri.isEmpty()) {
            return Result.fail("头像URI不能为空");
        }

        // 更新用户头像
        user.setAvatar(avatarUri);
        boolean success = usersService.updateById(user);
        if (success) {
            return Result.ok(user);
        } else {
            return Result.fail("更新头像失败");
        }
    }
}
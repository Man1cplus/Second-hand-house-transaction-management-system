package org.secondhand.secondhandhousebackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.servlet.http.HttpSession;
import org.secondhand.secondhandhousebackend.DTO.LoginFormDTO;
import org.secondhand.secondhandhousebackend.DTO.Result;
import org.secondhand.secondhandhousebackend.DTO.UsersDTO;
import org.secondhand.secondhandhousebackend.entity.Users;
import org.secondhand.secondhandhousebackend.mapper.UsersMapper;
import org.secondhand.secondhandhousebackend.service.UsersService;
import org.secondhand.secondhandhousebackend.utils.PasswordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Date;

/**
* @author 86198
* @description 针对表【users】的数据库操作Service实现
* @createDate 2025-06-24 17:39:39
*/
@Service
public class UsersServiceImpl extends ServiceImpl<UsersMapper, Users>
    implements UsersService{

    @Autowired
    private UsersMapper usersMapper;

    @Autowired
    private PasswordUtil passwordUtil;
    /**
     * 通过用户名来得到用户
     * @param username
     * @return
     */
    @Override
    public Users getByName(String username) {
        LambdaQueryWrapper<Users> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Users::getUsername, username);
        Users user = this.getOne(queryWrapper);
        return user;
    }

    @Override
    public boolean login(LoginFormDTO loginForm, HttpSession session) {
        // 参数校验
        if (StringUtils.isEmpty(loginForm.getUsername()) ||
                StringUtils.isEmpty(loginForm.getPassword())) {
            throw new IllegalArgumentException("用户名和密码不能为空");
        }

        Users user = getByName(loginForm.getUsername());
        if (user == null) {
            return false;
        }

        // 使用BCrypt验证密码
        if (!passwordUtil.matches(loginForm.getPassword(), user.getPassword())) {
            return false;
        }

        // 注意：JWT认证不再使用Session，这里保留session是为了兼容性，实际不会使用
        session.setAttribute("user", user);
        return true;
    }

    public Result register(Users users, HttpSession session) {
        if (StringUtils.isEmpty(users.getUsername())) {
            return Result.fail("用户名不能为空");
        }
        if (StringUtils.isEmpty(users.getPassword())) {
            return Result.fail("密码不能为空");
        }
        if (StringUtils.isEmpty(users.getEmail())) {
            return Result.fail("邮箱不能为空");
        }
        if (StringUtils.isEmpty(users.getPhonenumber())){
            return Result.fail("手机号不能为空");
        }
        
        // 检查用户名是否已存在
        if (getByName(users.getUsername()) != null) {
            return Result.fail("用户名已存在");
        }
        
        // 加密密码
        String encodedPassword = passwordUtil.encode(users.getPassword());
        users.setPassword(encodedPassword);

        users.setRegistrationtime(new Date());
        
        // 如果没有设置role，默认设置为买家
        if (users.getRole() == null) {
            users.setRole(org.secondhand.secondhandhousebackend.enums.UserRole.买家);
        }
        
        // 设置默认头像
        if (StringUtils.isEmpty(users.getAvatar())) {
            users.setAvatar("/files/download/Default.png");
        }
        
        save(users);
        return Result.ok();
    }

    @Override
    public Result changeUser(UsersDTO usersDTO,HttpSession session) {
        Users user = (Users) session.getAttribute("user");

        if (user == null) {
            return Result.fail("用户还没有登录！");
        }
        // 校验用户名是否已存在（排除当前用户）
        Users existingUser = getByName(usersDTO.getUsername());
        if (existingUser != null && !existingUser.getUserid().equals(user.getUserid())) {
            return Result.fail("用户名已被占用");
        }

        // 校验邮箱是否已存在（排除当前用户）
        if (usersMapper.existsByEmail(usersDTO.getEmail()) > 0) {
            Users userByEmail = usersMapper.selectByEmail(usersDTO.getEmail());
            if (userByEmail != null && !userByEmail.getUserid().equals(user.getUserid())) {
                return Result.fail("邮箱已被占用");
            }
        }
        
        user.setUsername(usersDTO.getUsername());
        user.setEmail(usersDTO.getEmail());
        
        // 如果提供了新密码，则加密后更新
        if (StringUtils.isNotEmpty(usersDTO.getPassword())) {
            String encodedPassword = passwordUtil.encode(usersDTO.getPassword());
            user.setPassword(encodedPassword);
        }
        
        // 如果提供了新手机号，则更新
        if (StringUtils.isNotEmpty(usersDTO.getPhonenumber())) {
            user.setPhonenumber(usersDTO.getPhonenumber());
        }
        if (!updateById(user)) {
            return Result.fail("用户信息更新失败");
        }

        return Result.ok("用户信息更新成功");
    }


}





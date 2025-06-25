package org.secondhand.secondhandhousebackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.servlet.http.HttpSession;
import org.secondhand.secondhandhousebackend.DTO.LoginFormDTO;
import org.secondhand.secondhandhousebackend.entity.Users;
import org.secondhand.secondhandhousebackend.mapper.UsersMapper;
import org.secondhand.secondhandhousebackend.service.UsersService;
import org.springframework.stereotype.Service;

/**
* @author 86198
* @description 针对表【users】的数据库操作Service实现
* @createDate 2025-06-24 17:39:39
*/
@Service
public class UsersServiceImpl extends ServiceImpl<UsersMapper, Users>
    implements UsersService{

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
        if (user == null || !user.getPassword().equals(loginForm.getPassword())) {
            return false;
        }

        session.setAttribute("user", user);
        return true;
    }
}





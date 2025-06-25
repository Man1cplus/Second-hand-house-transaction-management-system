package org.secondhand.secondhandhousebackend.service;


import com.baomidou.mybatisplus.extension.service.IService;
import jakarta.servlet.http.HttpSession;
import org.secondhand.secondhandhousebackend.DTO.LoginFormDTO;
import org.secondhand.secondhandhousebackend.entity.Users;

/**
* @author 86198
* @description 针对表【users】的数据库操作Service
* @createDate 2025-06-24 17:39:39
*/
public interface UsersService extends IService<Users> {

    boolean login(LoginFormDTO loginForm, HttpSession session);

    Users getByName(String username);
}

package org.secondhand.secondhandhousebackend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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

}





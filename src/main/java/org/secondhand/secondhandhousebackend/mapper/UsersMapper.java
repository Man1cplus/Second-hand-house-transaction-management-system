package org.secondhand.secondhandhousebackend.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.secondhand.secondhandhousebackend.entity.Users;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author 86198
* @description 针对表【users】的数据库操作Mapper
* @createDate 2025-06-24 17:36:52
* @Entity org.secondhand.secondhandhousebackend.service.entity.Users
*/
@Mapper
public interface UsersMapper extends BaseMapper<Users> {
    @Select("SELECT COUNT(*) FROM users WHERE username = #{username}")
    int existsByUsername(@Param("username") String username);

    @Select("SELECT COUNT(*) FROM users WHERE email = #{email}")
    int existsByEmail(@Param("email") String email);
}






package org.secondhand.secondhandhousebackend.VO;

import lombok.Data;
import org.secondhand.secondhandhousebackend.entity.Users;

@Data
public class UserVO {
    private Integer userid;
    private String username;
    private String role;          // 角色标识


    public static UserVO fromUser(Users user) {
        UserVO vo = new UserVO();
        vo.setUserid(user.getUserid());
        vo.setUsername(user.getUsername());
        vo.setRole(user.getRole().name());      // 获取枚举名称
        return vo;
    }
}

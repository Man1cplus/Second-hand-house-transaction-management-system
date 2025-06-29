package org.secondhand.secondhandhousebackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.servlet.http.HttpSession;
import org.secondhand.secondhandhousebackend.DTO.Result;
import org.secondhand.secondhandhousebackend.entity.Userfavorites;
import org.secondhand.secondhandhousebackend.entity.Users;
import org.secondhand.secondhandhousebackend.service.UserfavoritesService;
import org.secondhand.secondhandhousebackend.mapper.UserfavoritesMapper;
import org.springframework.stereotype.Service;

import static com.baomidou.mybatisplus.extension.toolkit.Db.count;

/**
* @author 86198
* @description 针对表【userfavorites】的数据库操作Service实现
* @createDate 2025-06-28 09:10:56
*/
@Service
public class UserfavoritesServiceImpl extends ServiceImpl<UserfavoritesMapper, Userfavorites>
    implements UserfavoritesService{

    //得到用户收藏的房源
    @Override
    public Object getUserFavorites(Integer page, Integer size, HttpSession session) {
        if (page == null || size == null) {
            return Result.fail("分页参数不能为空");
        }
        // 从Session中获取当前登录用户ID
        Users user = (Users) session.getAttribute("user");
        int userid = user.getUserid();
        System.out.println("Current user ID: " + userid);
        // 创建查询条件
        LambdaQueryWrapper<Userfavorites> queryWrapper = new LambdaQueryWrapper<>();
        // 根据用户ID查询收藏记录
        queryWrapper.eq(Userfavorites::getUserId, userid);
        // 使用MyBatisPlus的分页查询功能
        Page<Userfavorites> userFavoritesPage = new Page<>(page, size);
        Page<Userfavorites> result = this.page(userFavoritesPage, queryWrapper);
        if (result == null || result.getRecords().isEmpty()) {
            return Result.ok("暂无收藏房源");
        }
        // 如果需要返回更多房源信息，可以在这里关联查询Properties表
        return Result.ok(result.getRecords());
    }

    //收藏房源
    @Override
    public boolean collectProperty(Integer userId, Integer propertyId) {
        // 检查是否已收藏
        LambdaQueryWrapper<Userfavorites> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Userfavorites::getUserId, userId)
                .eq(Userfavorites::getPropertyId, propertyId);
        Userfavorites exist = this.getOne(queryWrapper);
        if (exist != null) {
            return false; // 已收藏，返回false
        }
        // 未收藏，执行收藏操作
        Userfavorites userFavorites = new Userfavorites();
        userFavorites.setUserId(userId);
        userFavorites.setPropertyId(propertyId);
        return this.save(userFavorites);
    }

    //取消收藏房源
    @Override
    public boolean unCollectProperty(Integer userId, Integer propertyId) {
        LambdaQueryWrapper<Userfavorites> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Userfavorites::getUserId, userId)
                .eq(Userfavorites::getPropertyId, propertyId);
        return this.remove(queryWrapper);
    }
    //判断是否收藏
    @Override
    public Object isFavorite(Integer userId, Integer propertyId) {
        if (userId == null || propertyId == null) {
            return false;
        }
        // 创建查询条件
        LambdaQueryWrapper<Userfavorites> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Userfavorites::getUserId, userId)
                .eq(Userfavorites::getPropertyId, propertyId);
        // 判断记录是否存在
        return count(queryWrapper) > 0;
    }

}






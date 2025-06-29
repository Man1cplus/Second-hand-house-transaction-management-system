package org.secondhand.secondhandhousebackend.service;

import jakarta.servlet.http.HttpSession;
import org.secondhand.secondhandhousebackend.entity.Userfavorites;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author 86198
* @description 针对表【userfavorites】的数据库操作Service
* @createDate 2025-06-28 09:10:56
*/
public interface UserfavoritesService extends IService<Userfavorites> {

    Object getUserFavorites(Integer page, Integer size, HttpSession session);

    boolean collectProperty(Integer userId, Integer propertyId);

    boolean unCollectProperty(Integer userId, Integer propertyId);

    Object isFavorite(Integer userId, Integer propertyId);
}

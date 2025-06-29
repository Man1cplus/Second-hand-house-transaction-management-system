package org.secondhand.secondhandhousebackend.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.secondhand.secondhandhousebackend.entity.Userfavorites;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author 86198
* @description 针对表【userfavorites】的数据库操作Mapper
* @createDate 2025-06-28 09:10:56
* @Entity org.secondhand.secondhandhousebackend.service.entity.Userfavorites
*/
public interface UserfavoritesMapper extends BaseMapper<Userfavorites> {
    // 删除与房源相关的所有收藏记录
    @Delete("DELETE FROM UserFavorites WHERE PropertyID = #{propertyid}")
    void deleteByPropertyId(@Param("propertyid") Integer propertyid);
}





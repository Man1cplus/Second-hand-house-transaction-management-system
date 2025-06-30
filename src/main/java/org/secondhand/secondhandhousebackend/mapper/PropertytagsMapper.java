package org.secondhand.secondhandhousebackend.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.secondhand.secondhandhousebackend.entity.Propertytags;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
* @author 86198
* @description 针对表【propertytags】的数据库操作Mapper
* @createDate 2025-06-29 16:13:49
* @Entity org.secondhand.secondhandhousebackend.entity.Propertytags
*/
@Mapper
public interface PropertytagsMapper extends BaseMapper<Propertytags> {
    // 根据房源 ID 获取标签 ID 列表
    @Select("SELECT tagid FROM PropertyTagRelations WHERE propertyid = #{propertyid}")
    List<Integer> getTagIdsByPropertyId(@Param("propertyid") Integer propertyid);
}





package org.secondhand.secondhandhousebackend.mapper;

import org.apache.ibatis.annotations.*;
import org.secondhand.secondhandhousebackend.entity.Propertytagrelations;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
* @author 86198
* @description 针对表【propertytagrelations】的数据库操作Mapper
* @createDate 2025-06-29 16:13:46
* @Entity org.secondhand.secondhandhousebackend.entity.Propertytagrelations
*/
@Mapper
public interface PropertytagrelationsMapper extends BaseMapper<Propertytagrelations> {
    // 删除房源的所有标签关系
    @Delete("DELETE FROM PropertyTagRelations WHERE propertyid = #{propertyid}")
    void deleteByPropertyId(@Param("propertyid") Integer propertyid);

    // 插入新的房源标签关系
    @Insert("INSERT INTO PropertyTagRelations (propertyid, tagid) VALUES (#{propertyid}, #{tagid})")
    void insertPropertyTagRelation(@Param("propertyid") Integer propertyid, @Param("tagid") Integer tagid);

    // 根据房源 ID 获取标签 ID 列表
    @Select("SELECT tagid FROM PropertyTagRelations WHERE propertyid = #{propertyid}")
    List<Integer> getTagIdsByPropertyId(@Param("propertyid") Integer propertyid);
}





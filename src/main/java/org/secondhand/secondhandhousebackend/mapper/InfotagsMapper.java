package org.secondhand.secondhandhousebackend.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.secondhand.secondhandhousebackend.entity.Infotags;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
* @author 86198
* @description 针对表【infotags】的数据库操作Mapper
* @createDate 2025-06-29 15:12:14
* @Entity org.secondhand.secondhandhousebackend.service.entity.Infotags
*/
public interface InfotagsMapper extends BaseMapper<Infotags> {
    // 根据资讯 ID 获取标签 ID 列表
    @Select("SELECT tagid FROM InfoTagRelations WHERE infoid = #{infoid}")
    List<Integer> getTagIdsByInfoId(@Param("infoid") Integer infoid);
}





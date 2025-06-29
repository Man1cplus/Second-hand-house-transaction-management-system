package org.secondhand.secondhandhousebackend.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.secondhand.secondhandhousebackend.entity.Infotagrelations;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author 86198
* @description 针对表【infotagrelations】的数据库操作Mapper
* @createDate 2025-06-29 15:12:30
* @Entity org.secondhand.secondhandhousebackend.service.entity.Infotagrelations
*/
public interface InfotagrelationsMapper extends BaseMapper<Infotagrelations> {
    // 删除资讯的所有标签关系
    @Delete("DELETE FROM InfoTagRelations WHERE infoid = #{infoid}")
    void deleteByInfoId(@Param("infoid") Integer infoid);

    // 插入新的资讯标签关系
    @Insert("INSERT INTO InfoTagRelations (infoid, tagid) VALUES (#{infoid}, #{tagid})")
    void insertInfoTagRelation(@Param("infoid") Integer infoid, @Param("tagid") Integer tagid);
}





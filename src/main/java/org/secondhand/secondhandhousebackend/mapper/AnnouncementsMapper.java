package org.secondhand.secondhandhousebackend.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.secondhand.secondhandhousebackend.entity.Announcements;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author 86198
* @description 针对表【announcements】的数据库操作Mapper
* @createDate 2025-06-24 17:34:11
* @Entity org.secondhand.secondhandhousebackend.entity.Announcements
*/
@Mapper
public interface AnnouncementsMapper extends BaseMapper<Announcements> {

}





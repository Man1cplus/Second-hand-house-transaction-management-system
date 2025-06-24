package org.secondhand.secondhandhousebackend.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.secondhand.secondhandhousebackend.entity.Information;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author 86198
* @description 针对表【information】的数据库操作Mapper
* @createDate 2025-06-24 17:36:39
* @Entity org.secondhand.secondhandhousebackend.entity.Information
*/
@Mapper
public interface InformationMapper extends BaseMapper<Information> {

}





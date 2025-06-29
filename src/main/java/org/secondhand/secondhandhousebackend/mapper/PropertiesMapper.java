package org.secondhand.secondhandhousebackend.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.secondhand.secondhandhousebackend.entity.Properties;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author 86198
* @description 针对表【properties】的数据库操作Mapper
* @createDate 2025-06-24 17:36:44
* @Entity org.secondhand.secondhandhousebackend.service.entity.Properties
*/
@Mapper
public interface PropertiesMapper extends BaseMapper<Properties> {

}





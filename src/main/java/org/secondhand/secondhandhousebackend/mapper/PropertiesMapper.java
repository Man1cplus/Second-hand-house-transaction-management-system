package org.secondhand.secondhandhousebackend.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.secondhand.secondhandhousebackend.entity.Properties;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
* @author 86198
* @description 针对表【properties】的数据库操作Mapper
* @createDate 2025-06-24 17:36:44
* @Entity org.secondhand.secondhandhousebackend.service.entity.Properties
*/
@Mapper
public interface PropertiesMapper extends BaseMapper<Properties> {
    
    /**
     * 查询所有房源（使用 resultMap 确保字段正确映射）
     * @return 房源列表
     */
    List<Properties> selectAllWithResultMap();
    
    /**
     * 根据状态查询房源
     * @param status 状态
     * @return 房源列表
     */
    List<Properties> selectByStatus(@Param("status") String status);
    
    /**
     * 根据卖家ID查询房源
     * @param sellerid 卖家ID
     * @return 房源列表
     */
    List<Properties> selectBySellerId(@Param("sellerid") Integer sellerid);
    
    /**
     * 根据ID查询单个房源（使用 resultMap 确保字段正确映射）
     * @param propertyid 房源ID
     * @return 房源对象
     */
    Properties selectById(@Param("propertyid") Integer propertyid);
}





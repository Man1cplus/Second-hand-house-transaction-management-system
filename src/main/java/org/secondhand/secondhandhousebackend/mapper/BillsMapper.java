package org.secondhand.secondhandhousebackend.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.secondhand.secondhandhousebackend.entity.Bills;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * 账单Mapper接口
 * @author system
 * @description 针对表【bills】的数据库操作Mapper
 */
@Mapper
public interface BillsMapper extends BaseMapper<Bills> {

}


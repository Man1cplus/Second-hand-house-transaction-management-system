package org.secondhand.secondhandhousebackend.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.secondhand.secondhandhousebackend.entity.Contracts;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author 86198
* @description 针对表【contracts】的数据库操作Mapper
* @createDate 2025-06-24 17:36:37
* @Entity org.secondhand.secondhandhousebackend.entity.Contracts
*/
@Mapper
public interface ContractsMapper extends BaseMapper<Contracts> {

}





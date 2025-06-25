package org.secondhand.secondhandhousebackend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.secondhand.secondhandhousebackend.entity.Information;
import org.secondhand.secondhandhousebackend.mapper.InformationMapper;
import org.secondhand.secondhandhousebackend.service.InformationService;
import org.springframework.stereotype.Service;

/**
* @author 86198
* @description 针对表【information】的数据库操作Service实现
* @createDate 2025-06-24 17:39:28
*/
@Service
public class InformationServiceImpl extends ServiceImpl<InformationMapper, Information>
    implements InformationService{

}





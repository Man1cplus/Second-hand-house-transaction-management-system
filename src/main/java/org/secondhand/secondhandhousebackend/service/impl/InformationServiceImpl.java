package org.secondhand.secondhandhousebackend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.secondhand.secondhandhousebackend.entity.Information;
import org.secondhand.secondhandhousebackend.mapper.InformationMapper;
import org.secondhand.secondhandhousebackend.mapper.InfotagrelationsMapper;
import org.secondhand.secondhandhousebackend.mapper.InfotagsMapper;
import org.secondhand.secondhandhousebackend.service.InformationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author 86198
* @description 针对表【information】的数据库操作Service实现
* @createDate 2025-06-24 17:39:28
*/
@Service
public class InformationServiceImpl extends ServiceImpl<InformationMapper, Information>
    implements InformationService{

    @Autowired
    private InfotagsMapper infoTagMapper;

    @Autowired
    private InfotagrelationsMapper infoTagRelationsMapper;

    @Override
    public void saveInfoTags(Integer infoid, List<Integer> tagIds) {
        // 删除原有的标签关系
        infoTagRelationsMapper.deleteByInfoId(infoid);
        // 插入新的标签关系
        for (Integer tagId : tagIds) {
            infoTagRelationsMapper.insertInfoTagRelation(infoid, tagId);
        }
    }

    @Override
    public void updateInfoTags(Integer infoid, List<Integer> tagIds) {
        saveInfoTags(infoid, tagIds);
    }

    @Override
    public List<Integer> getInfoTagIds(Integer infoid) {
        return infoTagMapper.getTagIdsByInfoId(infoid);
    }

    @Override
    public boolean deleteInfoWithTags(Integer infoid) {
        // 删除与资讯相关的所有标签关系
        infoTagRelationsMapper.deleteByInfoId(infoid);
        // 删除资讯记录
        return removeById(infoid);
    }
}





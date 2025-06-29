package org.secondhand.secondhandhousebackend.service.impl;

import org.secondhand.secondhandhousebackend.entity.Properties;
import org.secondhand.secondhandhousebackend.mapper.PropertytagrelationsMapper;
import org.secondhand.secondhandhousebackend.mapper.PropertiesMapper;
import org.secondhand.secondhandhousebackend.mapper.UserfavoritesMapper;
import org.secondhand.secondhandhousebackend.service.PropertiesService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PropertiesServiceImpl extends ServiceImpl<PropertiesMapper, Properties> implements PropertiesService {

    @Autowired
    private PropertytagrelationsMapper propertyTagRelationsMapper;

    @Autowired
    private UserfavoritesMapper userFavoritesMapper;

    @Override
    public void savePropertyTags(Integer propertyid, List<Integer> tagIds) {
        propertyTagRelationsMapper.deleteByPropertyId(propertyid);
        for (Integer tagId : tagIds) {
            propertyTagRelationsMapper.insertPropertyTagRelation(propertyid, tagId);
        }
    }

    @Override
    public void updatePropertyTags(Integer propertyid, List<Integer> tagIds) {
        savePropertyTags(propertyid, tagIds);
    }

    @Override
    public List<Integer> getPropertyTagIds(Integer propertyid) {
        return propertyTagRelationsMapper.getTagIdsByPropertyId(propertyid);
    }

    @Override
    public boolean deletePropertyWithTagsAndFavorites(Integer propertyid) {
        // 删除与房源相关的所有标签关系
        propertyTagRelationsMapper.deleteByPropertyId(propertyid);
        // 删除与房源相关的所有收藏记录
        userFavoritesMapper.deleteByPropertyId(propertyid);
        // 删除房源记录
        return removeById(propertyid);
    }
}
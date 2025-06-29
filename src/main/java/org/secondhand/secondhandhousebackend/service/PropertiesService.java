package org.secondhand.secondhandhousebackend.service;


import com.baomidou.mybatisplus.extension.service.IService;
import org.secondhand.secondhandhousebackend.entity.Properties;

import java.util.List;

/**
* @author 86198
* @description 针对表【properties】的数据库操作Service
* @createDate 2025-06-24 17:39:32
*/
public interface PropertiesService extends IService<Properties> {
    // 保存房源与标签的关系
    void savePropertyTags(Integer propertyid, List<Integer> tagIds);

    // 更新房源与标签的关系
    void updatePropertyTags(Integer propertyid, List<Integer> tagIds);

    // 根据房源 ID 获取标签 ID 列表
    List<Integer> getPropertyTagIds(Integer propertyid);

    // 删除房源及其关联的标签关系和收藏记录
    boolean deletePropertyWithTagsAndFavorites(Integer propertyid);
}

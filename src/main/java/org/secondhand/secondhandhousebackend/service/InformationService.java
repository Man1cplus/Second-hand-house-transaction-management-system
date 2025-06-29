package org.secondhand.secondhandhousebackend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.secondhand.secondhandhousebackend.entity.Information;

import java.util.List;

/**
* @author 86198
* @description 针对表【information】的数据库操作Service
* @createDate 2025-06-24 17:39:28
*/
public interface InformationService extends IService<Information> {

    // 保存资讯与标签的关系
    void saveInfoTags(Integer infoid, List<Integer> tagIds);

    // 更新资讯与标签的关系
    void updateInfoTags(Integer infoid, List<Integer> tagIds);

    // 根据资讯 ID 获取标签 ID 列表
    List<Integer> getInfoTagIds(Integer infoid);

    // 删除资讯及其关联的标签关系
    boolean deleteInfoWithTags(Integer infoid);
}

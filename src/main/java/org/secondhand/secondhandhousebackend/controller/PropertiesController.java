package org.secondhand.secondhandhousebackend.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.secondhand.secondhandhousebackend.DTO.Result;
import org.secondhand.secondhandhousebackend.entity.Properties;
import org.secondhand.secondhandhousebackend.entity.Users;
import org.secondhand.secondhandhousebackend.enums.UserRole;
import org.secondhand.secondhandhousebackend.mapper.PropertiesMapper;
import org.secondhand.secondhandhousebackend.service.PropertiesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/properties")
public class PropertiesController {

    @Autowired
    private PropertiesService propertiesService;
    
    @Autowired
    private PropertiesMapper propertiesMapper;

    private final ObjectMapper objectMapper = new ObjectMapper();

    // 创建新的房源
    @PostMapping
    public Result createProperty(@RequestBody Properties property) {
        try {
            // 验证面积值（数据库 Area 字段为 DECIMAL(5,2)，最大值为 999.99）
            if (property.getArea() != null) {
                if (property.getArea().compareTo(java.math.BigDecimal.ZERO) < 0) {
                    return Result.fail("面积不能为负数");
                }
                if (property.getArea().compareTo(new java.math.BigDecimal("999.99")) > 0) {
                    return Result.fail("面积值过大，最大支持 999.99 平方米");
                }
            }
            
            // 验证价格值
            if (property.getPrice() != null) {
                if (property.getPrice().compareTo(java.math.BigDecimal.ZERO) < 0) {
                    return Result.fail("价格不能为负数");
                }
            }
            
            // 验证并规范化状态值
            if (property.getStatus() == null || property.getStatus().toString().trim().isEmpty()) {
                // 如果状态为空或未设置，默认设置为"待审核"
                property.setStatus("待审核");
            } else {
                String statusStr = property.getStatus().toString().trim();
                // 确保状态值不超过50个字符（数据库字段长度）
                if (statusStr.length() > 50) {
                    return Result.fail("状态值过长，最大支持50个字符");
                }
                property.setStatus(statusStr);
            }
            
            // 将图片列表序列化为JSON字符串
            if (property.getPhotoList() != null && !property.getPhotoList().isEmpty()) {
                String photosJson = objectMapper.writeValueAsString(property.getPhotoList());
                property.setPhotos(photosJson);
            }
            boolean success = propertiesService.save(property);
            if (success) {
                // 只有当 tagIds 不为 null 时才保存标签关系
                if (property.getTagIds() != null) {
                    propertiesService.savePropertyTags(property.getPropertyid(), property.getTagIds());
                }
                return Result.ok();
            } else {
                return Result.fail("Failed to create property");
            }
        } catch (Exception e) {
            return Result.fail("Failed to create property: " + e.getMessage());
        }
    }

    // 更新房源信息
    @PutMapping("/{propertyid}")
    public Result updateProperty(@PathVariable Integer propertyid, @RequestBody Properties property) {
        try {
            property.setPropertyid(propertyid);
            
            // 验证面积值（数据库 Area 字段为 DECIMAL(5,2)，最大值为 999.99）
            if (property.getArea() != null) {
                if (property.getArea().compareTo(java.math.BigDecimal.ZERO) < 0) {
                    return Result.fail("面积不能为负数");
                }
                if (property.getArea().compareTo(new java.math.BigDecimal("999.99")) > 0) {
                    return Result.fail("面积值过大，最大支持 999.99 平方米");
                }
            }
            
            // 验证价格值
            if (property.getPrice() != null) {
                if (property.getPrice().compareTo(java.math.BigDecimal.ZERO) < 0) {
                    return Result.fail("价格不能为负数");
                }
            }
            
            // 验证并规范化状态值
            if (property.getStatus() != null) {
                String statusStr = property.getStatus().toString().trim();
                // 确保状态值不超过50个字符（数据库字段长度）
                if (statusStr.length() > 50) {
                    return Result.fail("状态值过长，最大支持50个字符");
                }
                // 规范化状态值：确保是有效的状态
                if (!statusStr.isEmpty()) {
                    property.setStatus(statusStr);
                }
            }
            
            // 将图片列表序列化为JSON字符串
            if (property.getPhotoList() != null && !property.getPhotoList().isEmpty()) {
                String photosJson = objectMapper.writeValueAsString(property.getPhotoList());
                property.setPhotos(photosJson);
            } else if (property.getPhotoList() != null && property.getPhotoList().isEmpty()) {
                // 如果传入空列表，清空图片
                property.setPhotos(null);
            }
            boolean success = propertiesService.updateById(property);
            if (success) {
                // 只有当 tagIds 不为 null 时才更新标签关系
                if (property.getTagIds() != null) {
                    propertiesService.updatePropertyTags(propertyid, property.getTagIds());
                }
                return Result.ok();
            } else {
                return Result.fail("Failed to update property");
            }
        } catch (Exception e) {
            return Result.fail("Failed to update property: " + e.getMessage());
        }
    }

    // 删除房源
    @DeleteMapping("/{propertyid}")
    public Result deleteProperty(@PathVariable Integer propertyid) {
        boolean success = propertiesService.deletePropertyWithTagsAndFavorites(propertyid);
        if (success) {
            return Result.ok();
        } else {
            return Result.fail("Failed to delete property");
        }
    }

    // 获取单个房源详情
    @GetMapping("/{propertyid}")
    public Result getProperty(@PathVariable Integer propertyid) {
        try {
            // 使用自定义查询方法，确保字段正确映射（特别是经纬度字段）
            Properties property = propertiesMapper.selectById(propertyid);
            if (property != null) {
                property.setTagIds(propertiesService.getPropertyTagIds(propertyid));
                // 将JSON字符串反序列化为图片列表
                if (property.getPhotos() != null && !property.getPhotos().isEmpty()) {
                    List<String> photoList = objectMapper.readValue(property.getPhotos(), 
                        new TypeReference<List<String>>() {});
                    property.setPhotoList(photoList);
                } else {
                    property.setPhotoList(new ArrayList<>());
                }
                return Result.ok(property);
            } else {
                return Result.fail("Property not found");
            }
        } catch (Exception e) {
            return Result.fail("Failed to get property: " + e.getMessage());
        }
    }

    // 获取所有房源（根据用户角色过滤：卖家只能看到自己的房源，管理员可以看到所有房源）
    @GetMapping
    public Result getAllProperties(HttpServletRequest request) {
        try {
            // 从request attribute中获取用户信息（由拦截器设置）
            Users user = (Users) request.getAttribute("user");
            
            List<Properties> propertiesList;
            
            // 如果用户未登录，返回空列表（实际上拦截器会阻止这种情况）
            if (user == null) {
                return Result.fail("用户未登录");
            }
            
            // 根据用户角色过滤房源
            // 使用自定义查询方法，确保字段正确映射（特别是经纬度字段）
            if (user.getRole() == UserRole.管理员) {
                // 管理员可以查看所有房源
                propertiesList = propertiesMapper.selectAllWithResultMap();
            } else if (user.getRole() == UserRole.卖家) {
                // 卖家只能查看自己的房源
                propertiesList = propertiesMapper.selectBySellerId(user.getUserid());
            } else {
                // 买家、经纪人等只能查看"在售"状态的房源（审核通过的房源）
                propertiesList = propertiesMapper.selectByStatus("在售");
            }
            
            // 处理每个房源的标签和图片
            for (Properties property : propertiesList) {
                property.setTagIds(propertiesService.getPropertyTagIds(property.getPropertyid()));
                // 将JSON字符串反序列化为图片列表，并设置第一张图片
                if (property.getPhotos() != null && !property.getPhotos().isEmpty()) {
                    List<String> photoList = objectMapper.readValue(property.getPhotos(), 
                        new TypeReference<List<String>>() {});
                    property.setPhotoList(photoList);
                } else {
                    property.setPhotoList(new ArrayList<>());
                }
            }
            return Result.ok(propertiesList);
        } catch (Exception e) {
            return Result.fail("Failed to get properties: " + e.getMessage());
        }
    }

    // 管理员审核房源
    @PutMapping("/approve/{propertyid}")
    public Result approveProperty(@PathVariable Integer propertyid, @RequestParam String status, HttpServletRequest request) {
        try {
            // 从request attribute中获取用户信息（由拦截器设置）
            Users user = (Users) request.getAttribute("user");
            
            // 检查用户是否为管理员
            if (user == null || user.getRole() != UserRole.管理员) {
                return Result.fail("只有管理员可以审核房源");
            }
            
            Properties property = propertiesService.getById(propertyid);
            if (property == null) {
                return Result.fail("房源不存在");
            }
            
            // 验证状态值
            if (!status.equals("在售") && !status.equals("审核拒绝")) {
                return Result.fail("无效的状态值，只能设置为'在售'或'审核拒绝'");
            }
            
            // 确保状态值不超过50个字符（数据库字段长度）
            if (status.length() > 50) {
                return Result.fail("状态值过长，最大支持50个字符");
            }
            
            // 更新房源状态
            property.setStatus(status);
            boolean success = propertiesService.updateById(property);
            
            if (success) {
                return Result.ok("审核成功");
            } else {
                return Result.fail("审核失败");
            }
        } catch (Exception e) {
            return Result.fail("审核失败: " + e.getMessage());
        }
    }
}
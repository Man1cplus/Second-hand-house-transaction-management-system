package org.secondhand.secondhandhousebackend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.servlet.http.HttpSession;
import org.secondhand.secondhandhousebackend.DTO.PurchaseRequest;
import org.secondhand.secondhandhousebackend.DTO.Result;
import org.secondhand.secondhandhousebackend.entity.Contracts;
import org.secondhand.secondhandhousebackend.entity.Properties;
import org.secondhand.secondhandhousebackend.entity.Users;
import org.secondhand.secondhandhousebackend.mapper.ContractsMapper;
import org.secondhand.secondhandhousebackend.mapper.PropertiesMapper;
import org.secondhand.secondhandhousebackend.service.ContractsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
* @author 86198
* @description 针对表【contracts】的数据库操作Service实现
* @createDate 2025-06-24 17:39:26
*/
@Service
public class ContractsServiceImpl extends ServiceImpl<ContractsMapper, Contracts>
    implements ContractsService{

    @Autowired
    private PropertiesMapper propertiesMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result purchaseProperty(PurchaseRequest request, HttpSession session) {
        Integer propertyId = request.getPropertyId();
        Users user = (Users) session.getAttribute("user");
        Integer buyerId = user.getUserid();

        // 检查房源是否存在
        Properties property = propertiesMapper.selectById(propertyId);
        if (property == null) {
            return Result.fail("Property not found");
        }

        // 检查房源是否已售出
        if ("已售出".equals(property.getStatus())) {
            return Result.fail("Property already sold");
        }

        // 创建合同
        Contracts contract = new Contracts();
        contract.setPropertyid(propertyId);
        contract.setBuyerid(buyerId);
        contract.setSigningdate(new Date());
        contract.setContractstatus("已签订");

        // 保存合同
        boolean contractSaved = this.save(contract);
        if (!contractSaved) {
            return Result.fail("Failed to create contract");
        }

        // 更新房源状态为“已售出”
        property.setStatus("已售出");
        int updatedRows = propertiesMapper.updateById(property);
        if (updatedRows <= 0) {
            return Result.fail("Failed to update property status");
        }

        return Result.ok("Purchase successful");
    }
}





package org.secondhand.secondhandhousebackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.servlet.http.HttpSession;
import org.secondhand.secondhandhousebackend.DTO.ContractApplicationRequest;
import org.secondhand.secondhandhousebackend.DTO.PurchaseRequest;
import org.secondhand.secondhandhousebackend.DTO.Result;
import org.secondhand.secondhandhousebackend.entity.Contracts;
import org.secondhand.secondhandhousebackend.entity.Properties;
import org.secondhand.secondhandhousebackend.entity.Users;
import org.secondhand.secondhandhousebackend.mapper.ContractsMapper;
import org.secondhand.secondhandhousebackend.mapper.PropertiesMapper;
import org.secondhand.secondhandhousebackend.mapper.UsersMapper;
import org.secondhand.secondhandhousebackend.service.BillsService;
import org.secondhand.secondhandhousebackend.service.ContractsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

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

    @Autowired
    private UsersMapper usersMapper;

    @Autowired
    private BillsService billsService;

    /**
     * 验证用户是否存在（代码层外键验证）
     * @param userId 用户ID
     * @return 用户是否存在
     */
    private boolean validateUserExists(Integer userId) {
        if (userId == null) {
            return false;
        }
        Users user = usersMapper.selectById(userId);
        return user != null;
    }

    /**
     * 验证房源是否存在（代码层外键验证）
     * @param propertyId 房源ID
     * @return 房源是否存在
     */
    private boolean validatePropertyExists(Integer propertyId) {
        if (propertyId == null) {
            return false;
        }
        Properties property = propertiesMapper.selectById(propertyId);
        return property != null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result purchaseProperty(PurchaseRequest request, HttpSession session) {
        Integer propertyId = request.getPropertyId();
        Users user = (Users) session.getAttribute("user");
        Integer buyerId = user.getUserid();

        // 代码层外键验证：检查房源是否存在
        Properties property = propertiesMapper.selectById(propertyId);
        if (property == null) {
            return Result.fail("房源不存在");
        }

        // 代码层外键验证：检查买家是否存在
        if (!validateUserExists(buyerId)) {
            return Result.fail("买家不存在");
        }

        // 代码层外键验证：检查卖家是否存在
        Integer sellerId = property.getSellerid();
        if (!validateUserExists(sellerId)) {
            return Result.fail("卖家不存在");
        }

        // 检查房源是否已售出
        if ("已售出".equals(property.getStatus())) {
            return Result.fail("Property already sold");
        }

        // 创建合同
        Contracts contract = new Contracts();
        contract.setPropertyid(propertyId);
        contract.setBuyerid(buyerId);
        contract.setSellerid(sellerId);
        contract.setSigningdate(new Date());
        contract.setContractstatus("已签订");
        // 注意：这个方法不涉及合同文件上传，contractfile可以为null

        // 保存合同（重写的save方法会自动更新房源状态为"已售出"）
        boolean contractSaved = this.save(contract);
        if (!contractSaved) {
            return Result.fail("Failed to create contract");
        }

        return Result.ok("Purchase successful");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result applyContract(ContractApplicationRequest request, HttpSession session) {
        Users user = (Users) session.getAttribute("user");
        if (user == null) {
            return Result.fail("用户未登录");
        }

        Integer buyerId = user.getUserid();
        Integer propertyId = request.getPropertyId();
        String contractFileUri = request.getContractFileUri();

        // 检查参数
        if (propertyId == null) {
            return Result.fail("房源ID不能为空");
        }
        if (contractFileUri == null || contractFileUri.isEmpty()) {
            return Result.fail("合同文件不能为空");
        }

        // 代码层外键验证：检查房源是否存在
        Properties property = propertiesMapper.selectById(propertyId);
        if (property == null) {
            return Result.fail("房源不存在");
        }

        // 代码层外键验证：检查买家是否存在
        if (!validateUserExists(buyerId)) {
            return Result.fail("买家不存在");
        }

        // 代码层外键验证：检查卖家是否存在
        Integer sellerId = property.getSellerid();
        if (sellerId == null) {
            return Result.fail("房源没有关联卖家");
        }
        if (!validateUserExists(sellerId)) {
            return Result.fail("卖家不存在");
        }

        // 检查房源是否已售出
        if ("已售出".equals(property.getStatus())) {
            return Result.fail("房源已售出，无法申请合同");
        }

        // 检查是否已经存在待审核的合同
        LambdaQueryWrapper<Contracts> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Contracts::getPropertyid, propertyId)
                .eq(Contracts::getBuyerid, buyerId)
                .eq(Contracts::getContractstatus, "待审核");
        long count = this.count(queryWrapper);
        if (count > 0) {
            return Result.fail("该房源已存在待审核的合同申请");
        }

        // 买家申请合同时，必须先支付服务费才能创建合同
        // 检查是否已支付服务费
        Boolean paidServiceFee = request.getPaidServiceFee();
        String paymentMethod = request.getPaymentMethod();
        String paymentTransactionId = request.getPaymentTransactionId();
        
        // 必须支付服务费才能创建合同
        if (paidServiceFee == null || !paidServiceFee) {
            return Result.fail("必须支付服务费才能申请合同");
        }
        
        // 支付信息验证
        if (paymentMethod == null || paymentMethod.isEmpty()) {
            return Result.fail("支付方式不能为空");
        }
        if (paymentTransactionId == null || paymentTransactionId.isEmpty()) {
            return Result.fail("支付交易号不能为空");
        }

        // 创建合同申请
        Contracts contract = new Contracts();
        contract.setPropertyid(propertyId);
        contract.setBuyerid(buyerId);
        contract.setSellerid(sellerId);
        contract.setContractfile(contractFileUri);
        contract.setContractstatus("待审核");
        // 申请时不需要签订日期
        contract.setSigningdate(null);

        // 保存合同
        boolean contractSaved = this.save(contract);
        if (!contractSaved) {
            return Result.fail("合同申请创建失败");
        }

        // 买家申请合同时，创建买家的服务费账单（账单金额 = 服务费，房源价格的0.5%）
        // 注意：服务费（0.5%）必须在申请时支付，账单状态为"已支付"
        Result billResult = billsService.createServiceFeeBill(
            contract.getContractid(), 
            buyerId, 
            "buyer",
            true,  // 已支付
            paymentMethod,
            paymentTransactionId
        );
        
        if (!billResult.getSuccess()) {
            // 如果账单创建失败，回滚合同申请
            this.removeById(contract.getContractid());
            return Result.fail("服务费账单创建失败: " + billResult.getErrorMsg());
        }

        return Result.ok("合同申请提交成功");
    }

    @Override
    public Result getPendingContractsBySeller(HttpSession session) {
        Users user = (Users) session.getAttribute("user");
        if (user == null) {
            return Result.fail("用户未登录");
        }

        Integer sellerId = user.getUserid();

        // 查询该卖家的所有待审核合同
        LambdaQueryWrapper<Contracts> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Contracts::getSellerid, sellerId)
                .eq(Contracts::getContractstatus, "待审核")
                .orderByDesc(Contracts::getContractid);

        List<Contracts> contracts = this.list(queryWrapper);

        return Result.ok(contracts);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result signContract(Integer contractId, HttpSession session) {
        Users user = (Users) session.getAttribute("user");
        if (user == null) {
            return Result.fail("用户未登录");
        }

        Integer sellerId = user.getUserid();

        // 查询合同
        Contracts contract = this.getById(contractId);
        if (contract == null) {
            return Result.fail("合同不存在");
        }

        // 检查是否是该卖家的合同
        if (!sellerId.equals(contract.getSellerid())) {
            return Result.fail("无权操作此合同");
        }

        // 检查合同状态
        if (!"待审核".equals(contract.getContractstatus())) {
            return Result.fail("只能签订待审核状态的合同");
        }

        // 代码层外键验证：检查房源是否存在
        Properties property = propertiesMapper.selectById(contract.getPropertyid());
        if (property == null) {
            return Result.fail("房源不存在");
        }

        // 代码层外键验证：检查卖家是否存在
        if (!validateUserExists(contract.getSellerid())) {
            return Result.fail("卖家不存在");
        }

        // 代码层外键验证：检查买家是否存在
        if (!validateUserExists(contract.getBuyerid())) {
            return Result.fail("买家不存在");
        }

        // 检查房源是否已售出
        if ("已售出".equals(property.getStatus())) {
            return Result.fail("房源已售出");
        }

        // 更新合同状态为"已签订"
        // 注意：创建一个新的对象用于更新，以避免 MyBatis 一级缓存导致 updateById 中的状态比较失效
        Contracts updateContract = new Contracts();
        updateContract.setContractid(contractId);
        updateContract.setContractstatus("已签订");
        updateContract.setSigningdate(new Date());
        updateContract.setPropertyid(contract.getPropertyid()); // 传入房源ID以便后续更新状态

        boolean contractUpdated = this.updateById(updateContract);
        if (!contractUpdated) {
            return Result.fail("合同签订失败");
        }

        // 卖家签订合同时，创建卖家的服务费账单（0.5%房价）
        // 注意：这里创建的是卖家的服务费账单，买家在申请时已经创建了买家的服务费账单
        Result sellerServiceFeeResult = billsService.createServiceFeeBill(contractId, sellerId, "seller");
        if (!sellerServiceFeeResult.getSuccess()) {
            System.err.println("合同签订成功，但卖家服务费账单创建失败: " + sellerServiceFeeResult.getErrorMsg());
        }

        // 卖家签订完合同后，创建买家的房款账单（完整房价）
        Result propertyPriceBillResult = billsService.createPropertyPriceBill(contractId);
        if (!propertyPriceBillResult.getSuccess()) {
            System.err.println("合同签订成功，但买家房款账单创建失败: " + propertyPriceBillResult.getErrorMsg());
        }

        return Result.ok("合同签订成功");
    }

    @Override
    public String validateContractForeignKeys(Contracts contract) {
        // 验证房源是否存在
        if (contract.getPropertyid() != null) {
            if (!validatePropertyExists(contract.getPropertyid())) {
                return "房源不存在";
            }
        }

        // 验证买家是否存在
        if (contract.getBuyerid() != null) {
            if (!validateUserExists(contract.getBuyerid())) {
                return "买家不存在";
            }
        }

        // 验证卖家是否存在
        if (contract.getSellerid() != null) {
            if (!validateUserExists(contract.getSellerid())) {
                return "卖家不存在";
            }
        }

        // 如果合同关联了房源，验证卖家是否与房源一致
        if (contract.getPropertyid() != null && contract.getSellerid() != null) {
            Properties property = propertiesMapper.selectById(contract.getPropertyid());
            if (property != null && !contract.getSellerid().equals(property.getSellerid())) {
                return "卖家ID与房源不匹配";
            }
        }

        return null; // 验证通过
    }

    /**
     * 重写save方法：当合同状态为"已签订"时，自动更新房源状态为"已售出"
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean save(Contracts contract) {
        // 先保存合同
        boolean saved = super.save(contract);
        
        // 如果保存成功且合同状态为"已签订"，更新房源状态
        if (saved && contract.getPropertyid() != null && "已签订".equals(contract.getContractstatus())) {
            updatePropertyStatusToSold(contract.getPropertyid());
        }
        
        return saved;
    }

    /**
     * 重写updateById方法：当合同状态更新为"已签订"时，自动更新房源状态为"已售出"
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateById(Contracts contract) {
        // 如果合同ID为空，无法更新
        if (contract.getContractid() == null) {
            return false;
        }
        
        // 查询原合同状态
        Contracts existingContract = this.getById(contract.getContractid());
        if (existingContract == null) {
            return false;
        }
        
        String oldStatus = existingContract.getContractstatus();
        String newStatus = contract.getContractstatus();
        // 使用原合同的房源ID，因为更新时可能不包含propertyid字段
        Integer propertyId = contract.getPropertyid() != null 
                ? contract.getPropertyid() 
                : existingContract.getPropertyid();
        
        // 更新合同
        boolean updated = super.updateById(contract);
        
        // 如果更新成功，且状态从非"已签订"变为"已签订"，更新房源状态
        if (updated && propertyId != null 
                && !"已签订".equals(oldStatus) && "已签订".equals(newStatus)) {
            updatePropertyStatusToSold(propertyId);
        }
        
        return updated;
    }

    /**
     * 更新房源状态为"已售出"的辅助方法
     * @param propertyId 房源ID
     */
    private void updatePropertyStatusToSold(Integer propertyId) {
        if (propertyId == null) {
            return;
        }
        
        Properties property = propertiesMapper.selectById(propertyId);
        if (property != null && !"已售出".equals(property.getStatus())) {
            property.setStatus("已售出");
            propertiesMapper.updateById(property);
        }
    }
}





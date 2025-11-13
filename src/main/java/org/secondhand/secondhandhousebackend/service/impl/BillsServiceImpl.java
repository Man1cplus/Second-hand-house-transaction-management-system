package org.secondhand.secondhandhousebackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.secondhand.secondhandhousebackend.DTO.Result;
import org.secondhand.secondhandhousebackend.entity.Bills;
import org.secondhand.secondhandhousebackend.entity.Contracts;
import org.secondhand.secondhandhousebackend.entity.Properties;
import org.secondhand.secondhandhousebackend.entity.Users;
import org.secondhand.secondhandhousebackend.mapper.BillsMapper;
import org.secondhand.secondhandhousebackend.mapper.ContractsMapper;
import org.secondhand.secondhandhousebackend.mapper.PropertiesMapper;
import org.secondhand.secondhandhousebackend.mapper.UsersMapper;
import org.secondhand.secondhandhousebackend.service.BillsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;

/**
 * 账单服务实现类
 */
@Service
public class BillsServiceImpl extends ServiceImpl<BillsMapper, Bills> implements BillsService {

    @Autowired
    private ContractsMapper contractsMapper;

    @Autowired
    private PropertiesMapper propertiesMapper;

    @Autowired
    private UsersMapper usersMapper;

    /**
     * 验证用户是否存在（代码层外键验证）
     */
    private boolean validateUserExists(Integer userId) {
        if (userId == null) {
            return false;
        }
        Users user = usersMapper.selectById(userId);
        return user != null;
    }

    /**
     * 验证合同是否存在（代码层外键验证）
     */
    private boolean validateContractExists(Integer contractId) {
        if (contractId == null) {
            return false;
        }
        Contracts contract = contractsMapper.selectById(contractId);
        return contract != null;
    }

    /**
     * 验证房源是否存在（代码层外键验证）
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
    public Result createBill(Integer contractId) {
        // 兼容旧方法，调用创建服务费账单（默认买家支付）
        Contracts contract = contractsMapper.selectById(contractId);
        if (contract == null) {
            return Result.fail("合同不存在");
        }
        return createServiceFeeBill(contractId, contract.getBuyerid(), "buyer");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result createServiceFeeBill(Integer contractId, Integer payerId, String payerType) {
        // 验证合同是否存在
        Contracts contract = contractsMapper.selectById(contractId);
        if (contract == null) {
            return Result.fail("合同不存在");
        }

        // 检查是否已经存在该合同的服务费账单
        List<Bills> existingBills = this.list(new LambdaQueryWrapper<Bills>()
                .eq(Bills::getContractid, contractId));
        
        // 获取房源信息
        Properties property = propertiesMapper.selectById(contract.getPropertyid());
        if (property == null) {
            return Result.fail("房源不存在");
        }

        // 获取房源价格（参考信息）
        BigDecimal propertyPrice = property.getPrice();
        if (propertyPrice == null || propertyPrice.compareTo(BigDecimal.ZERO) <= 0) {
            return Result.fail("房源价格无效");
        }

        // 计算服务费（房源价格的0.5%）
        BigDecimal serviceFee = propertyPrice.multiply(new BigDecimal("0.005"))
                .setScale(2, RoundingMode.HALF_UP);

        // 检查是否已存在相同支付者的服务费账单
        // 如果买家已经支付了服务费（在申请合同时），卖家签订合同时不应该再创建买家的服务费账单
        for (Bills existingBill : existingBills) {
            if (existingBill.getServicefee() != null && 
                existingBill.getServicefee().compareTo(serviceFee) == 0) {
                // 检查备注字段，判断是否为相同支付者的账单
                String remark = existingBill.getRemark();
                if (remark != null) {
                    // 如果买家已经存在服务费账单，且当前要创建的是买家账单，则返回已存在的账单
                    if ("buyer".equals(payerType) && remark.contains("买家服务费账单")) {
                        return Result.ok(existingBill);
                    }
                    // 如果卖家已经存在服务费账单，且当前要创建的是卖家账单，则返回已存在的账单
                    if ("seller".equals(payerType) && remark.contains("卖家服务费账单")) {
                        return Result.ok(existingBill);
                    }
                }
            }
        }

        // 创建服务费账单（账单金额 = 服务费，0.5%房价）
        Bills bill = new Bills();
        bill.setContractid(contractId);
        bill.setBuyerid(contract.getBuyerid());
        bill.setSellerid(contract.getSellerid());
        bill.setPropertyid(contract.getPropertyid());
        bill.setPropertyprice(propertyPrice); // 房源价格（参考信息）
        bill.setServicefee(serviceFee); // 服务费（账单金额，0.5%房价）
        bill.setBillstatus("待支付");
        bill.setCreatedate(new Date());
        bill.setCreatedat(new Date());
        bill.setUpdatedat(new Date());
        // 使用备注字段标识账单类型和支付者
        if ("buyer".equals(payerType)) {
            bill.setRemark("买家服务费账单（0.5%房价）");
        } else if ("seller".equals(payerType)) {
            bill.setRemark("卖家服务费账单（0.5%房价）");
        } else {
            bill.setRemark("服务费账单（0.5%房价）");
        }

        // 保存账单
        boolean saved = this.save(bill);
        if (!saved) {
            return Result.fail("服务费账单创建失败");
        }

        return Result.ok(bill);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result createServiceFeeBill(Integer contractId, Integer payerId, String payerType, 
                                       Boolean paid, String paymentMethod, String paymentTransactionId) {
        // 验证合同是否存在
        Contracts contract = contractsMapper.selectById(contractId);
        if (contract == null) {
            return Result.fail("合同不存在");
        }

        // 检查是否已经存在该合同的服务费账单
        List<Bills> existingBills = this.list(new LambdaQueryWrapper<Bills>()
                .eq(Bills::getContractid, contractId));
        
        // 获取房源信息
        Properties property = propertiesMapper.selectById(contract.getPropertyid());
        if (property == null) {
            return Result.fail("房源不存在");
        }

        // 获取房源价格（参考信息）
        BigDecimal propertyPrice = property.getPrice();
        if (propertyPrice == null || propertyPrice.compareTo(BigDecimal.ZERO) <= 0) {
            return Result.fail("房源价格无效");
        }

        // 计算服务费（房源价格的0.5%）
        BigDecimal serviceFee = propertyPrice.multiply(new BigDecimal("0.005"))
                .setScale(2, RoundingMode.HALF_UP);

        // 检查是否已存在相同支付者的服务费账单
        for (Bills existingBill : existingBills) {
            if (existingBill.getServicefee() != null && 
                existingBill.getServicefee().compareTo(serviceFee) == 0) {
                // 检查备注字段，判断是否为相同支付者的账单
                String remark = existingBill.getRemark();
                if (remark != null) {
                    // 如果买家已经存在服务费账单，且当前要创建的是买家账单，则返回已存在的账单
                    if ("buyer".equals(payerType) && remark.contains("买家服务费账单")) {
                        return Result.ok(existingBill);
                    }
                    // 如果卖家已经存在服务费账单，且当前要创建的是卖家账单，则返回已存在的账单
                    if ("seller".equals(payerType) && remark.contains("卖家服务费账单")) {
                        return Result.ok(existingBill);
                    }
                }
            }
        }

        // 创建服务费账单（账单金额 = 服务费，0.5%房价）
        Bills bill = new Bills();
        bill.setContractid(contractId);
        bill.setBuyerid(contract.getBuyerid());
        bill.setSellerid(contract.getSellerid());
        bill.setPropertyid(contract.getPropertyid());
        bill.setPropertyprice(propertyPrice); // 房源价格（参考信息）
        bill.setServicefee(serviceFee); // 服务费（账单金额，0.5%房价）
        
        // 根据支付状态设置账单状态和支付信息
        Date now = new Date();
        if (paid != null && paid) {
            // 如果已支付，设置账单状态为"已支付"
            bill.setBillstatus("已支付");
            bill.setPaydate(now);
            if (paymentMethod != null && !paymentMethod.isEmpty()) {
                bill.setPaymentmethod(paymentMethod);
            }
            if (paymentTransactionId != null && !paymentTransactionId.isEmpty()) {
                bill.setPaymenttransactionid(paymentTransactionId);
            }
        } else {
            // 如果未支付，设置账单状态为"待支付"
            bill.setBillstatus("待支付");
        }
        
        bill.setCreatedate(now);
        bill.setCreatedat(now);
        bill.setUpdatedat(now);
        
        // 使用备注字段标识账单类型和支付者
        if ("buyer".equals(payerType)) {
            bill.setRemark("买家服务费账单（0.5%房价）");
        } else if ("seller".equals(payerType)) {
            bill.setRemark("卖家服务费账单（0.5%房价）");
        } else {
            bill.setRemark("服务费账单（0.5%房价）");
        }

        // 保存账单
        boolean saved = this.save(bill);
        if (!saved) {
            return Result.fail("服务费账单创建失败");
        }

        return Result.ok(bill);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result createPropertyPriceBill(Integer contractId) {
        // 验证合同是否存在
        Contracts contract = contractsMapper.selectById(contractId);
        if (contract == null) {
            return Result.fail("合同不存在");
        }

        // 检查是否已经存在该合同的房款账单
        LambdaQueryWrapper<Bills> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Bills::getContractid, contractId);
        List<Bills> existingBills = this.list(queryWrapper);
        
        // 获取房源信息
        Properties property = propertiesMapper.selectById(contract.getPropertyid());
        if (property == null) {
            return Result.fail("房源不存在");
        }

        // 获取房源价格（完整房价）
        BigDecimal propertyPrice = property.getPrice();
        if (propertyPrice == null || propertyPrice.compareTo(BigDecimal.ZERO) <= 0) {
            return Result.fail("房源价格无效");
        }

        // 检查是否已存在房款账单（通过比较servicefee是否等于propertyprice）
        for (Bills existingBill : existingBills) {
            if (existingBill.getServicefee() != null && 
                existingBill.getServicefee().compareTo(propertyPrice) == 0) {
                // 已存在房款账单
                return Result.ok(existingBill);
            }
        }

        // 创建房款账单（账单金额 = 完整房价）
        Bills bill = new Bills();
        bill.setContractid(contractId);
        bill.setBuyerid(contract.getBuyerid());
        bill.setSellerid(contract.getSellerid());
        bill.setPropertyid(contract.getPropertyid());
        bill.setPropertyprice(propertyPrice); // 房源价格（参考信息）
        bill.setServicefee(propertyPrice); // 账单金额 = 完整房价（使用servicefee字段存储账单金额）
        bill.setBillstatus("待支付");
        bill.setCreatedate(new Date());
        bill.setCreatedat(new Date());
        bill.setUpdatedat(new Date());
        bill.setRemark("房款账单（完整房价）"); // 使用备注字段标识账单类型

        // 保存账单
        boolean saved = this.save(bill);
        if (!saved) {
            return Result.fail("房款账单创建失败");
        }

        return Result.ok(bill);
    }

    @Override
    public Result getPendingBillsByBuyer(Integer buyerId) {
        // 验证买家是否存在
        if (!validateUserExists(buyerId)) {
            return Result.fail("买家不存在");
        }

        // 查询该买家的所有待支付账单
        // 只查询买家需要支付的账单：买家服务费账单和房款账单
        LambdaQueryWrapper<Bills> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Bills::getBuyerid, buyerId)
                .eq(Bills::getBillstatus, "待支付")
                .and(wrapper -> wrapper
                    .like(Bills::getRemark, "买家服务费账单")
                    .or()
                    .like(Bills::getRemark, "房款账单"))
                .orderByDesc(Bills::getCreatedate);

        List<Bills> bills = this.list(queryWrapper);
        return Result.ok(bills);
    }

    @Override
    public Result getAllBillsByBuyer(Integer buyerId) {
        // 验证买家是否存在
        if (!validateUserExists(buyerId)) {
            return Result.fail("买家不存在");
        }

        // 查询该买家的所有账单
        // 只查询买家需要支付的账单：买家服务费账单和房款账单
        LambdaQueryWrapper<Bills> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Bills::getBuyerid, buyerId)
                .and(wrapper -> wrapper
                    .like(Bills::getRemark, "买家服务费账单")
                    .or()
                    .like(Bills::getRemark, "房款账单"))
                .orderByDesc(Bills::getCreatedate);

        List<Bills> bills = this.list(queryWrapper);
        return Result.ok(bills);
    }

    @Override
    public Result getBillById(Integer billId) {
        Bills bill = this.getById(billId);
        if (bill == null) {
            return Result.fail("账单不存在");
        }
        return Result.ok(bill);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result payBill(Integer billId, String paymentMethod, String paymentTransactionId) {
        // 查询账单
        Bills bill = this.getById(billId);
        if (bill == null) {
            return Result.fail("账单不存在");
        }

        // 检查账单状态
        if (!"待支付".equals(bill.getBillstatus())) {
            return Result.fail("只能支付待支付状态的账单");
        }

        // 更新账单状态
        bill.setBillstatus("已支付");
        bill.setPaydate(new Date());
        bill.setPaymentmethod(paymentMethod);
        bill.setPaymenttransactionid(paymentTransactionId);
        bill.setUpdatedat(new Date());

        boolean updated = this.updateById(bill);
        if (!updated) {
            return Result.fail("账单支付失败");
        }

        return Result.ok("账单支付成功");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result cancelBill(Integer billId) {
        // 查询账单
        Bills bill = this.getById(billId);
        if (bill == null) {
            return Result.fail("账单不存在");
        }

        // 检查账单状态
        if (!"待支付".equals(bill.getBillstatus())) {
            return Result.fail("只能取消待支付状态的账单");
        }

        // 更新账单状态
        bill.setBillstatus("已取消");
        bill.setUpdatedat(new Date());

        boolean updated = this.updateById(bill);
        if (!updated) {
            return Result.fail("账单取消失败");
        }

        return Result.ok("账单取消成功");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result withdrawBill(Integer billId, Integer buyerId) {
        // 验证买家是否存在
        if (!validateUserExists(buyerId)) {
            return Result.fail("买家不存在");
        }

        // 查询账单
        Bills bill = this.getById(billId);
        if (bill == null) {
            return Result.fail("账单不存在");
        }

        // 验证账单是否属于该买家
        if (!buyerId.equals(bill.getBuyerid())) {
            return Result.fail("无权操作此账单");
        }

        // 检查账单类型：只能撤回服务费账单，不能撤回房价账单
        String remark = bill.getRemark();
        if (remark == null) {
            return Result.fail("账单类型不明确，无法撤回");
        }

        // 不能撤回房价账单
        if (remark.contains("房款账单")) {
            return Result.fail("不能撤回房价账单");
        }

        // 只能撤回买家的服务费账单
        if (!remark.contains("买家服务费账单")) {
            return Result.fail("只能撤回买家的服务费账单");
        }

        // 检查账单状态：只能撤回"已支付"或"待支付"状态的服务费账单
        String billStatus = bill.getBillstatus();
        if (!"已支付".equals(billStatus) && !"待支付".equals(billStatus)) {
            return Result.fail("只能撤回已支付或待支付状态的服务费账单");
        }

        // 检查合同是否存在
        Integer contractId = bill.getContractid();
        if (contractId == null) {
            return Result.fail("账单未关联合同，无法撤回");
        }

        Contracts contract = contractsMapper.selectById(contractId);
        if (contract == null) {
            return Result.fail("合同不存在，无法撤回");
        }

        // 检查合同状态：只能撤回"待审核"状态的合同对应的账单
        if (!"待审核".equals(contract.getContractstatus())) {
            return Result.fail("只能撤回待审核状态合同的服务费账单");
        }

        // 验证合同是否属于该买家
        if (!buyerId.equals(contract.getBuyerid())) {
            return Result.fail("无权撤回此账单对应的合同");
        }

        // 删除账单
        boolean billDeleted = this.removeById(billId);
        if (!billDeleted) {
            return Result.fail("账单删除失败");
        }

        // 删除对应的合同记录
        boolean contractDeleted = contractsMapper.deleteById(contractId) > 0;
        if (!contractDeleted) {
            // 如果合同删除失败，回滚账单删除（但MyBatis-Plus的removeById不支持回滚，这里只能记录错误）
            // 实际上在事务中，如果后续操作失败，整个事务会回滚
            return Result.fail("合同删除失败，账单已删除");
        }

        return Result.ok("服务费账单撤回成功，合同已删除");
    }

    @Override
    public String validateBillForeignKeys(Bills bill) {
        // 验证合同是否存在
        if (bill.getContractid() != null) {
            if (!validateContractExists(bill.getContractid())) {
                return "合同不存在";
            }
        }

        // 验证买家是否存在
        if (bill.getBuyerid() != null) {
            if (!validateUserExists(bill.getBuyerid())) {
                return "买家不存在";
            }
        }

        // 验证卖家是否存在
        if (bill.getSellerid() != null) {
            if (!validateUserExists(bill.getSellerid())) {
                return "卖家不存在";
            }
        }

        // 验证房源是否存在
        if (bill.getPropertyid() != null) {
            if (!validatePropertyExists(bill.getPropertyid())) {
                return "房源不存在";
            }
        }

        return null; // 验证通过
    }
}


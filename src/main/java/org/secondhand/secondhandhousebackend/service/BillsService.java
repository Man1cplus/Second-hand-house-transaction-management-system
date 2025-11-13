package org.secondhand.secondhandhousebackend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.secondhand.secondhandhousebackend.DTO.Result;
import org.secondhand.secondhandhousebackend.entity.Bills;

/**
 * 账单服务接口
 */
public interface BillsService extends IService<Bills> {
    /**
     * 创建账单（卖家签订合同时自动调用）
     * @param contractId 合同ID
     * @return 创建结果
     */
    Result createBill(Integer contractId);

    /**
     * 创建服务费账单（0.5%房价）
     * @param contractId 合同ID
     * @param payerId 支付者ID（买家ID或卖家ID）
     * @param payerType 支付者类型（"buyer"或"seller"）
     * @return 创建结果
     */
    Result createServiceFeeBill(Integer contractId, Integer payerId, String payerType);

    /**
     * 创建服务费账单（0.5%房价），支持指定支付状态和支付信息
     * @param contractId 合同ID
     * @param payerId 支付者ID（买家ID或卖家ID）
     * @param payerType 支付者类型（"buyer"或"seller"）
     * @param paid 是否已支付
     * @param paymentMethod 支付方式（如果已支付）
     * @param paymentTransactionId 支付交易号（如果已支付）
     * @return 创建结果
     */
    Result createServiceFeeBill(Integer contractId, Integer payerId, String payerType, 
                                 Boolean paid, String paymentMethod, String paymentTransactionId);

    /**
     * 创建房款账单（完整房价）
     * @param contractId 合同ID
     * @return 创建结果
     */
    Result createPropertyPriceBill(Integer contractId);

    /**
     * 获取买家的待支付账单列表
     * @param buyerId 买家ID
     * @return 账单列表
     */
    Result getPendingBillsByBuyer(Integer buyerId);

    /**
     * 获取买家的所有账单列表
     * @param buyerId 买家ID
     * @return 账单列表
     */
    Result getAllBillsByBuyer(Integer buyerId);

    /**
     * 获取账单详情
     * @param billId 账单ID
     * @return 账单详情
     */
    Result getBillById(Integer billId);

    /**
     * 支付账单
     * @param billId 账单ID
     * @param paymentMethod 支付方式
     * @param paymentTransactionId 支付交易号
     * @return 支付结果
     */
    Result payBill(Integer billId, String paymentMethod, String paymentTransactionId);

    /**
     * 取消账单
     * @param billId 账单ID
     * @return 取消结果
     */
    Result cancelBill(Integer billId);

    /**
     * 撤回账单（买家可以撤回服务费账单，同时删除对应的合同记录，但不能撤回房价账单）
     * @param billId 账单ID
     * @param buyerId 买家ID
     * @return 撤回结果
     */
    Result withdrawBill(Integer billId, Integer buyerId);

    /**
     * 验证账单外键（代码层外键验证）
     * @param bill 账单对象
     * @return 验证错误信息，如果为null则表示验证通过
     */
    String validateBillForeignKeys(Bills bill);
}


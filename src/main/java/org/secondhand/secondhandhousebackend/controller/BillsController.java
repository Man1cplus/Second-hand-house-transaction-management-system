package org.secondhand.secondhandhousebackend.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.secondhand.secondhandhousebackend.DTO.Result;
import org.secondhand.secondhandhousebackend.entity.Bills;
import org.secondhand.secondhandhousebackend.entity.Users;
import org.secondhand.secondhandhousebackend.service.BillsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bills")
public class BillsController {

    @Autowired
    private BillsService billsService;

    /**
     * 创建账单（通常由系统自动调用，卖家签订合同时触发）
     * @param contractId 合同ID
     * @return 创建结果
     */
    @PostMapping("/create/{contractId}")
    public Result createBill(@PathVariable Integer contractId) {
        return billsService.createBill(contractId);
    }

    /**
     * 获取买家的待支付账单列表
     * @param httpRequest HTTP请求
     * @return 账单列表
     */
    @GetMapping("/buyer/pending")
    public Result getPendingBillsByBuyer(HttpServletRequest httpRequest) {
        // 从request attribute中获取用户信息（由拦截器设置）
        Users user = (Users) httpRequest.getAttribute("user");
        if (user == null) {
            return Result.fail("用户未登录");
        }
        return billsService.getPendingBillsByBuyer(user.getUserid());
    }

    /**
     * 获取买家的所有账单列表
     * @param httpRequest HTTP请求
     * @return 账单列表
     */
    @GetMapping("/buyer/all")
    public Result getAllBillsByBuyer(HttpServletRequest httpRequest) {
        // 从request attribute中获取用户信息（由拦截器设置）
        Users user = (Users) httpRequest.getAttribute("user");
        if (user == null) {
            return Result.fail("用户未登录");
        }
        return billsService.getAllBillsByBuyer(user.getUserid());
    }

    /**
     * 获取账单详情
     * @param billId 账单ID
     * @return 账单详情
     */
    @GetMapping("/{billId}")
    public Result getBillById(@PathVariable Integer billId) {
        return billsService.getBillById(billId);
    }

    /**
     * 支付账单
     * @param billId 账单ID
     * @param request 支付请求（包含支付方式和交易号）
     * @param httpRequest HTTP请求
     * @return 支付结果
     */
    @PostMapping("/{billId}/pay")
    public Result payBill(
            @PathVariable Integer billId,
            @RequestBody PayBillRequest request,
            HttpServletRequest httpRequest) {
        // 从request attribute中获取用户信息（由拦截器设置）
        Users user = (Users) httpRequest.getAttribute("user");
        if (user == null) {
            return Result.fail("用户未登录");
        }

        // 验证账单是否属于当前用户
        Bills bill = billsService.getById(billId);
        if (bill == null) {
            return Result.fail("账单不存在");
        }
        if (!user.getUserid().equals(bill.getBuyerid())) {
            return Result.fail("无权操作此账单");
        }

        return billsService.payBill(billId, request.getPaymentMethod(), request.getPaymentTransactionId());
    }

    /**
     * 取消账单（禁止买家取消账单）
     * @param billId 账单ID
     * @param httpRequest HTTP请求
     * @return 取消结果
     */
    @PostMapping("/{billId}/cancel")
    public Result cancelBill(@PathVariable Integer billId, HttpServletRequest httpRequest) {
        // 从request attribute中获取用户信息（由拦截器设置）
        Users user = (Users) httpRequest.getAttribute("user");
        if (user == null) {
            return Result.fail("用户未登录");
        }

        // 禁止买家取消账单
        if (user.getRole() == org.secondhand.secondhandhousebackend.enums.UserRole.买家) {
            return Result.fail("买家无权取消账单");
        }

        // 验证账单是否存在
        Bills bill = billsService.getById(billId);
        if (bill == null) {
            return Result.fail("账单不存在");
        }

        // 如果不是管理员，检查是否有权限取消（卖家只能取消自己相关的账单）
        if (user.getRole() != org.secondhand.secondhandhousebackend.enums.UserRole.管理员) {
            // 卖家只能取消自己房源的账单
            if (!user.getUserid().equals(bill.getSellerid())) {
                return Result.fail("无权操作此账单");
            }
        }

        return billsService.cancelBill(billId);
    }

    /**
     * 撤回账单（买家可以撤回服务费账单，同时删除对应的合同记录）
     * @param billId 账单ID
     * @param httpRequest HTTP请求
     * @return 撤回结果
     */
    @PostMapping("/{billId}/withdraw")
    public Result withdrawBill(@PathVariable Integer billId, HttpServletRequest httpRequest) {
        // 从request attribute中获取用户信息（由拦截器设置）
        Users user = (Users) httpRequest.getAttribute("user");
        if (user == null) {
            return Result.fail("用户未登录");
        }

        // 只有买家可以撤回账单
        if (user.getRole() != org.secondhand.secondhandhousebackend.enums.UserRole.买家) {
            return Result.fail("只有买家可以撤回服务费账单");
        }

        return billsService.withdrawBill(billId, user.getUserid());
    }

    /**
     * 支付账单请求DTO
     */
    public static class PayBillRequest {
        private String paymentMethod;
        private String paymentTransactionId;

        public String getPaymentMethod() {
            return paymentMethod;
        }

        public void setPaymentMethod(String paymentMethod) {
            this.paymentMethod = paymentMethod;
        }

        public String getPaymentTransactionId() {
            return paymentTransactionId;
        }

        public void setPaymentTransactionId(String paymentTransactionId) {
            this.paymentTransactionId = paymentTransactionId;
        }
    }
}


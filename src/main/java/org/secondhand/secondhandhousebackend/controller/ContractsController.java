package org.secondhand.secondhandhousebackend.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.secondhand.secondhandhousebackend.DTO.ContractApplicationRequest;
import org.secondhand.secondhandhousebackend.DTO.PurchaseRequest;
import org.secondhand.secondhandhousebackend.DTO.Result;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.secondhand.secondhandhousebackend.entity.Contracts;
import org.secondhand.secondhandhousebackend.entity.Users;
import org.secondhand.secondhandhousebackend.enums.UserRole;
import org.secondhand.secondhandhousebackend.service.ContractsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/contracts")
public class ContractsController {

    @Autowired
    private ContractsService contractsService;


    // 创建新的合同
    @PostMapping
    public Result createContract(@RequestBody Contracts contract) {
        // 代码层外键验证
        String validationError = contractsService.validateContractForeignKeys(contract);
        if (validationError != null) {
            return Result.fail(validationError);
        }

        boolean success = contractsService.save(contract);
        if (success) {
            return Result.ok();
        } else {
            return Result.fail("Failed to create contract");
        }
    }

    // 更新合同信息（禁止已签订合同被修改）
    @PutMapping("/{contractid}")
    public Result updateContract(@PathVariable Integer contractid, @RequestBody Contracts contract, HttpServletRequest httpRequest) {
        // 检查合同是否存在
        Contracts existingContract = contractsService.getById(contractid);
        if (existingContract == null) {
            return Result.fail("合同不存在");
        }

        // 如果合同已签订，禁止修改
        if ("已签订".equals(existingContract.getContractstatus())) {
            return Result.fail("已签订的合同不能修改");
        }

        contract.setContractid(contractid);
        
        // 代码层外键验证
        String validationError = contractsService.validateContractForeignKeys(contract);
        if (validationError != null) {
            return Result.fail(validationError);
        }

        boolean success = contractsService.updateById(contract);
        if (success) {
            return Result.ok();
        } else {
            return Result.fail("Failed to update contract");
        }
    }

    // 删除合同（禁止已签订合同被删除，禁止买家删除合同）
    @DeleteMapping("/{contractid}")
    public Result deleteContract(@PathVariable Integer contractid, HttpServletRequest httpRequest) {
        // 从request attribute中获取用户信息（由拦截器设置）
        Users user = (Users) httpRequest.getAttribute("user");
        if (user == null) {
            return Result.fail("用户未登录");
        }

        // 检查合同是否存在
        Contracts existingContract = contractsService.getById(contractid);
        if (existingContract == null) {
            return Result.fail("合同不存在");
        }

        // 如果合同已签订，禁止删除
        if ("已签订".equals(existingContract.getContractstatus())) {
            return Result.fail("已签订的合同不能删除");
        }

        // 禁止买家删除合同
        if (user.getRole() == UserRole.买家) {
            return Result.fail("买家无权删除合同");
        }

        // 如果不是管理员，检查是否有权限删除（卖家只能删除自己的合同）
        if (user.getRole() != UserRole.管理员) {
            // 卖家只能删除自己房源的合同
            if (!user.getUserid().equals(existingContract.getSellerid())) {
                return Result.fail("无权删除此合同");
            }
        }

        boolean success = contractsService.removeById(contractid);
        if (success) {
            return Result.ok();
        } else {
            return Result.fail("Failed to delete contract");
        }
    }

    // 获取单个合同详情
    @GetMapping("/{contractid}")
    public Result getContract(@PathVariable Integer contractid) {
        Contracts contract = contractsService.getById(contractid);
        if (contract != null) {
            return Result.ok(contract);
        } else {
            return Result.fail("Contract not found");
        }
    }

    // 获取所有合同（根据用户角色过滤：管理员可以看到所有合同，买家只能看到自己的合同，卖家只能看到自己相关的合同）
    @GetMapping
    public Result getAllContracts(HttpServletRequest httpRequest) {
        // 从request attribute中获取用户信息（由拦截器设置）
        Users user = (Users) httpRequest.getAttribute("user");
        if (user == null) {
            return Result.fail("用户未登录");
        }

        List<Contracts> contracts;
        
        // 根据用户角色过滤合同
        if (user.getRole() == UserRole.管理员) {
            // 管理员可以查看所有合同
            contracts = contractsService.list();
        } else if (user.getRole() == UserRole.买家) {
            // 买家只能查看自己申请的合同
            LambdaQueryWrapper<Contracts> buyerQuery = new LambdaQueryWrapper<>();
            buyerQuery.eq(Contracts::getBuyerid, user.getUserid());
            contracts = contractsService.list(buyerQuery);
        } else if (user.getRole() == UserRole.卖家) {
            // 卖家只能查看自己房源的合同
            LambdaQueryWrapper<Contracts> sellerQuery = new LambdaQueryWrapper<>();
            sellerQuery.eq(Contracts::getSellerid, user.getUserid());
            contracts = contractsService.list(sellerQuery);
        } else {
            // 其他角色（如经纪人）暂时返回空列表
            contracts = List.of();
        }
        
        return Result.ok(contracts);
    }

    // 用户购买房源
    @PostMapping("/purchase")
    public Result purchaseProperty(@RequestBody PurchaseRequest request, HttpServletRequest httpRequest) {
        // 从request attribute中获取用户信息（由拦截器设置）
        org.secondhand.secondhandhousebackend.entity.Users user = 
            (org.secondhand.secondhandhousebackend.entity.Users) httpRequest.getAttribute("user");
        if (user == null) {
            return Result.fail("用户未登录");
        }
        // 创建临时session用于兼容service方法
        HttpSession session = httpRequest.getSession();
        session.setAttribute("user", user);
        return contractsService.purchaseProperty(request, session);
    }

    // 买家提出合同签订申请
    @PostMapping("/apply")
    public Result applyContract(@RequestBody ContractApplicationRequest request, HttpServletRequest httpRequest) {
        // 从request attribute中获取用户信息（由拦截器设置）
        org.secondhand.secondhandhousebackend.entity.Users user = 
            (org.secondhand.secondhandhousebackend.entity.Users) httpRequest.getAttribute("user");
        if (user == null) {
            return Result.fail("用户未登录");
        }
        // 创建临时session用于兼容service方法
        HttpSession session = httpRequest.getSession();
        session.setAttribute("user", user);
        return contractsService.applyContract(request, session);
    }

    // 卖家查看待审核的合同列表
    @GetMapping("/seller/pending")
    public Result getPendingContractsBySeller(HttpServletRequest httpRequest) {
        // 从request attribute中获取用户信息（由拦截器设置）
        org.secondhand.secondhandhousebackend.entity.Users user = 
            (org.secondhand.secondhandhousebackend.entity.Users) httpRequest.getAttribute("user");
        if (user == null) {
            return Result.fail("用户未登录");
        }
        // 创建临时session用于兼容service方法
        HttpSession session = httpRequest.getSession();
        session.setAttribute("user", user);
        return contractsService.getPendingContractsBySeller(session);
    }

    // 卖家签订合同
    @PostMapping("/{contractid}/sign")
    public Result signContract(@PathVariable Integer contractid, HttpServletRequest httpRequest) {
        // 从request attribute中获取用户信息（由拦截器设置）
        org.secondhand.secondhandhousebackend.entity.Users user = 
            (org.secondhand.secondhandhousebackend.entity.Users) httpRequest.getAttribute("user");
        if (user == null) {
            return Result.fail("用户未登录");
        }
        // 创建临时session用于兼容service方法
        HttpSession session = httpRequest.getSession();
        session.setAttribute("user", user);
        return contractsService.signContract(contractid, session);
    }

}
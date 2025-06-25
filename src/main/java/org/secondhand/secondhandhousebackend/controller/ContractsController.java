package org.secondhand.secondhandhousebackend.controller;

import org.secondhand.secondhandhousebackend.DTO.Result;
import org.secondhand.secondhandhousebackend.entity.Contracts;
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
        boolean success = contractsService.save(contract);
        if (success) {
            return Result.ok();
        } else {
            return Result.fail("Failed to create contract");
        }
    }

    // 更新合同信息
    @PutMapping("/{contractid}")
    public Result updateContract(@PathVariable Integer contractid, @RequestBody Contracts contract) {
        contract.setContractid(contractid);
        boolean success = contractsService.updateById(contract);
        if (success) {
            return Result.ok();
        } else {
            return Result.fail("Failed to update contract");
        }
    }

    // 删除合同
    @DeleteMapping("/{contractid}")
    public Result deleteContract(@PathVariable Integer contractid) {
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

    // 获取所有合同
    @GetMapping
    public Result getAllContracts() {
        List<Contracts> contracts = contractsService.list();
        return Result.ok(contracts);
    }
}
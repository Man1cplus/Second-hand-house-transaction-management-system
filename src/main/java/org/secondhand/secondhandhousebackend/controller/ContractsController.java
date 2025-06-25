package org.secondhand.secondhandhousebackend.controller;

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
    public boolean createContract(@RequestBody Contracts contract) {
        return contractsService.save(contract);
    }

    // 更新合同信息
    @PutMapping("/{contractid}")
    public boolean updateContract(@PathVariable Integer contractid, @RequestBody Contracts contract) {
        contract.setContractid(contractid);
        return contractsService.updateById(contract);
    }

    // 删除合同
    @DeleteMapping("/{contractid}")
    public boolean deleteContract(@PathVariable Integer contractid) {
        return contractsService.removeById(contractid);
    }

    // 获取单个合同详情
    @GetMapping("/{contractid}")
    public Contracts getContract(@PathVariable Integer contractid) {
        return contractsService.getById(contractid);
    }

    // 获取所有合同
    @GetMapping
    public List<Contracts> getAllContracts() {
        return contractsService.list();
    }
}
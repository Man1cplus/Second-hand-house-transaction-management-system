package org.secondhand.secondhandhousebackend.service;


import com.baomidou.mybatisplus.extension.service.IService;
import jakarta.servlet.http.HttpSession;
import org.secondhand.secondhandhousebackend.DTO.ContractApplicationRequest;
import org.secondhand.secondhandhousebackend.DTO.PurchaseRequest;
import org.secondhand.secondhandhousebackend.DTO.Result;
import org.secondhand.secondhandhousebackend.entity.Contracts;

/**
* @author 86198
* @description 针对表【contracts】的数据库操作Service
* @createDate 2025-06-24 17:39:26
*/
public interface ContractsService extends IService<Contracts> {

    Result purchaseProperty(PurchaseRequest request,HttpSession session);

    /**
     * 买家提出合同签订申请
     * @param request 合同申请请求
     * @param session HTTP会话
     * @return 操作结果
     */
    Result applyContract(ContractApplicationRequest request, HttpSession session);

    /**
     * 卖家查看待审核的合同列表
     * @param session HTTP会话
     * @return 合同列表
     */
    Result getPendingContractsBySeller(HttpSession session);

    /**
     * 卖家签订合同
     * @param contractId 合同ID
     * @param session HTTP会话
     * @return 操作结果
     */
    Result signContract(Integer contractId, HttpSession session);

    /**
     * 验证合同外键（代码层外键验证）
     * @param contract 合同实体
     * @return 验证结果，如果验证失败返回错误信息，成功返回null
     */
    String validateContractForeignKeys(Contracts contract);
}

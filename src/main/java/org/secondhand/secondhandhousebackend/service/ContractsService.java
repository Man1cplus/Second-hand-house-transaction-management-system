package org.secondhand.secondhandhousebackend.service;


import com.baomidou.mybatisplus.extension.service.IService;
import jakarta.servlet.http.HttpSession;
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
}

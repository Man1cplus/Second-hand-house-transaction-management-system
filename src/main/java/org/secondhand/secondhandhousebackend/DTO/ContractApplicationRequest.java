package org.secondhand.secondhandhousebackend.DTO;

import lombok.Data;

/**
 * 合同申请请求DTO
 */
@Data
public class ContractApplicationRequest {
    /**
     * 房源ID
     */
    private Integer propertyId;

    /**
     * 合同文件URI（上传后返回的URI）
     */
    private String contractFileUri;

    /**
     * 是否已支付服务费（申请合同时，如果已支付服务费，则账单状态应为"已支付"）
     */
    private Boolean paidServiceFee;

    /**
     * 支付方式（如果已支付）：支付宝、微信支付、银行卡等
     */
    private String paymentMethod;

    /**
     * 支付交易号（如果已支付）：第三方支付平台的交易号
     */
    private String paymentTransactionId;
}


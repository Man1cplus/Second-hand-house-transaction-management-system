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
}


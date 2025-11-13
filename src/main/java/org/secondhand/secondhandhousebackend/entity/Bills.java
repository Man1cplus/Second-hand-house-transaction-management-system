package org.secondhand.secondhandhousebackend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

/**
 * 账单实体类
 * @TableName bills
 */
@TableName(value = "bills")
@Data
public class Bills {
    /**
     * 账单ID，主键，自增
     */
    @TableId(type = IdType.AUTO)
    private Integer billid;

    /**
     * 合同ID，外键，关联contracts表
     */
    private Integer contractid;

    /**
     * 买家ID，外键，关联用户表
     */
    private Integer buyerid;

    /**
     * 卖家ID，外键，关联用户表
     */
    private Integer sellerid;

    /**
     * 房源ID，外键，关联properties表
     */
    private Integer propertyid;

    /**
     * 房源价格（单位：元）
     */
    private BigDecimal propertyprice;

    /**
     * 服务费金额（房源价格的1%，单位：元）
     */
    private BigDecimal servicefee;

    /**
     * 账单状态：待支付、已支付、已取消、已退款
     */
    private String billstatus;

    /**
     * 账单创建时间
     */
    private Date createdate;

    /**
     * 支付时间（支付后更新）
     */
    private Date paydate;

    /**
     * 支付方式：支付宝、微信支付、银行卡等
     */
    private String paymentmethod;

    /**
     * 支付交易号（第三方支付平台的交易号）
     */
    private String paymenttransactionid;

    /**
     * 备注信息
     */
    private String remark;

    /**
     * 创建时间戳
     */
    private Date createdat;

    /**
     * 更新时间戳
     */
    private Date updatedat;
}


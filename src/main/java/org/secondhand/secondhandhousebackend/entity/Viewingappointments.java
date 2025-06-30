package org.secondhand.secondhandhousebackend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;
import org.secondhand.secondhandhousebackend.enums.ViewStatus;

/**
 * 
 * @TableName viewingappointments
 */
@TableName(value ="viewingappointments")
@Data
public class Viewingappointments {
    /**
     * 预约ID，主键，自增
     */
    @TableId(type = IdType.AUTO)
    private Integer appointmentid;

    /**
     * 房源ID，外键，关联房源信息表
     */
    private Integer propertyid;

    /**
     * 买家ID，外键，关联用户表
     */
    private Integer buyerid;

    /**
     * 预约时间
     */
    private Date appointmenttime;

    /**
     * 状态，如待审批、已预约、已取消
     */
    public ViewStatus status;

}
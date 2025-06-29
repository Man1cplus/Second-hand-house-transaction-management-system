package org.secondhand.secondhandhousebackend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName userfavorites
 */
@TableName(value ="userfavorites")
@Data
public class Userfavorites {
    /**
     * 收藏ID，主键，自增
     */
    @TableId(type = IdType.AUTO)
    private Integer favoriteid;

    /**
     * 用户ID，关联Users表
     */
    @TableField("UserID")
    private Integer userId;

    /**
     * 房源ID，关联Properties表
     */
    @TableField("PropertyId")
    private Integer propertyId;

    /**
     * 收藏时间
     */
    private Date collectiontime;

}
package org.secondhand.secondhandhousebackend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 
 * @TableName propertycategoryrelations
 */
@TableName(value ="propertycategoryrelations")
@Data
public class Propertycategoryrelations {
    /**
     * 关系ID，主键，自增
     */
    @TableId(type = IdType.AUTO)
    private Integer relationid;

    /**
     * 房源ID，外键，关联房源信息表
     */
    private Integer propertyid;

    /**
     * 分类ID，外键，关联房源分类表
     */
    private Integer categoryid;

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        Propertycategoryrelations other = (Propertycategoryrelations) that;
        return (this.getRelationid() == null ? other.getRelationid() == null : this.getRelationid().equals(other.getRelationid()))
            && (this.getPropertyid() == null ? other.getPropertyid() == null : this.getPropertyid().equals(other.getPropertyid()))
            && (this.getCategoryid() == null ? other.getCategoryid() == null : this.getCategoryid().equals(other.getCategoryid()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getRelationid() == null) ? 0 : getRelationid().hashCode());
        result = prime * result + ((getPropertyid() == null) ? 0 : getPropertyid().hashCode());
        result = prime * result + ((getCategoryid() == null) ? 0 : getCategoryid().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", relationid=").append(relationid);
        sb.append(", propertyid=").append(propertyid);
        sb.append(", categoryid=").append(categoryid);
        sb.append("]");
        return sb.toString();
    }
}
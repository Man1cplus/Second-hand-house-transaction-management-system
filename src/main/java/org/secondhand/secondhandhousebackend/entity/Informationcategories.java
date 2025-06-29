package org.secondhand.secondhandhousebackend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 
 * @TableName informationcategories
 */
@TableName(value ="informationcategories")
@Data
public class Informationcategories {
    /**
     * 资讯分类ID，主键，自增
     */
    @TableId(type = IdType.AUTO)
    private Integer infocategoryid;

    /**
     * 资讯分类名称，不为空
     */
    private String infocategoryname;

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
        Informationcategories other = (Informationcategories) that;
        return (this.getInfocategoryid() == null ? other.getInfocategoryid() == null : this.getInfocategoryid().equals(other.getInfocategoryid()))
            && (this.getInfocategoryname() == null ? other.getInfocategoryname() == null : this.getInfocategoryname().equals(other.getInfocategoryname()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getInfocategoryid() == null) ? 0 : getInfocategoryid().hashCode());
        result = prime * result + ((getInfocategoryname() == null) ? 0 : getInfocategoryname().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", infocategoryid=").append(infocategoryid);
        sb.append(", infocategoryname=").append(infocategoryname);
        sb.append("]");
        return sb.toString();
    }
}
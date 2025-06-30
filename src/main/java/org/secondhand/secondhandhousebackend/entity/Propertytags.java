package org.secondhand.secondhandhousebackend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 
 * @TableName propertytags
 */
@TableName(value ="propertytags")
@Data
public class Propertytags {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer tagid;

    /**
     * 
     */
    private String tagname;

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
        Propertytags other = (Propertytags) that;
        return (this.getTagid() == null ? other.getTagid() == null : this.getTagid().equals(other.getTagid()))
            && (this.getTagname() == null ? other.getTagname() == null : this.getTagname().equals(other.getTagname()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getTagid() == null) ? 0 : getTagid().hashCode());
        result = prime * result + ((getTagname() == null) ? 0 : getTagname().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", tagid=").append(tagid);
        sb.append(", tagname=").append(tagname);
        sb.append("]");
        return sb.toString();
    }
}
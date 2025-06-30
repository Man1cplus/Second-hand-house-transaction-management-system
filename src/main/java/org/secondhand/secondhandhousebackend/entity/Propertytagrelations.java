package org.secondhand.secondhandhousebackend.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 
 * @TableName propertytagrelations
 */
@TableName(value ="propertytagrelations")
@Data
public class Propertytagrelations {
    /**
     * 
     */
    @TableId
    private Integer propertyid;

    /**
     * 
     */
    private Integer tagid;

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
        Propertytagrelations other = (Propertytagrelations) that;
        return (this.getPropertyid() == null ? other.getPropertyid() == null : this.getPropertyid().equals(other.getPropertyid()))
            && (this.getTagid() == null ? other.getTagid() == null : this.getTagid().equals(other.getTagid()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getPropertyid() == null) ? 0 : getPropertyid().hashCode());
        result = prime * result + ((getTagid() == null) ? 0 : getTagid().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", propertyid=").append(propertyid);
        sb.append(", tagid=").append(tagid);
        sb.append("]");
        return sb.toString();
    }
}
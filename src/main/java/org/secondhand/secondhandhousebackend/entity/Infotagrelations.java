package org.secondhand.secondhandhousebackend.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 
 * @TableName infotagrelations
 */
@TableName(value ="infotagrelations")
@Data
public class Infotagrelations {
    /**
     * 
     */
    @TableId
    private Integer infoid;

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
        Infotagrelations other = (Infotagrelations) that;
        return (this.getInfoid() == null ? other.getInfoid() == null : this.getInfoid().equals(other.getInfoid()))
            && (this.getTagid() == null ? other.getTagid() == null : this.getTagid().equals(other.getTagid()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getInfoid() == null) ? 0 : getInfoid().hashCode());
        result = prime * result + ((getTagid() == null) ? 0 : getTagid().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", infoid=").append(infoid);
        sb.append(", tagid=").append(tagid);
        sb.append("]");
        return sb.toString();
    }
}
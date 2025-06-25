package org.secondhand.secondhandhousebackend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 
 * @TableName carouselimages
 */
@TableName(value ="carouselimages")
@Data
public class Carouselimages {
    /**
     * 图片ID，主键，自增
     */
    @TableId(type = IdType.AUTO)
    private Integer imageid;

    /**
     * 图片路径，不为空
     */
    private String imagepath;

    /**
     * 排序，用于控制显示顺序
     */
    private Integer sortorder;

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
        Carouselimages other = (Carouselimages) that;
        return (this.getImageid() == null ? other.getImageid() == null : this.getImageid().equals(other.getImageid()))
            && (this.getImagepath() == null ? other.getImagepath() == null : this.getImagepath().equals(other.getImagepath()))
            && (this.getSortorder() == null ? other.getSortorder() == null : this.getSortorder().equals(other.getSortorder()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getImageid() == null) ? 0 : getImageid().hashCode());
        result = prime * result + ((getImagepath() == null) ? 0 : getImagepath().hashCode());
        result = prime * result + ((getSortorder() == null) ? 0 : getSortorder().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", imageid=").append(imageid);
        sb.append(", imagepath=").append(imagepath);
        sb.append(", sortorder=").append(sortorder);
        sb.append("]");
        return sb.toString();
    }
}
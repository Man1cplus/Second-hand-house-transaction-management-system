package org.secondhand.secondhandhousebackend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import java.util.List;

import lombok.Data;

/**
 * 
 * @TableName information
 */
@TableName(value ="information")
@Data
public class Information {
    /**
     * 资讯ID，主键，自增
     */
    @TableId(type = IdType.AUTO)
    private Integer infoid;

    /**
     * 资讯分类ID，外键，关联资讯分类表
     */
    private Integer infocategoryid;

    /**
     * 标题，不为空
     */
    private String title;

    /**
     * 内容，文本
     */
    private String content;

    /**
     * 发布日期
     */
    private Date publishdate;

    /**
     * 发布者ID，外键，关联用户表
     */
    private Integer publisherid;

    // 用于存储标签 ID 列表
    @TableField(exist = false) // 防止 MyBatis Plus 尝试将此字段映射到数据库表
    private List<Integer> tagIds;

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
        Information other = (Information) that;
        return (this.getInfoid() == null ? other.getInfoid() == null : this.getInfoid().equals(other.getInfoid()))
            && (this.getInfocategoryid() == null ? other.getInfocategoryid() == null : this.getInfocategoryid().equals(other.getInfocategoryid()))
            && (this.getTitle() == null ? other.getTitle() == null : this.getTitle().equals(other.getTitle()))
            && (this.getContent() == null ? other.getContent() == null : this.getContent().equals(other.getContent()))
            && (this.getPublishdate() == null ? other.getPublishdate() == null : this.getPublishdate().equals(other.getPublishdate()))
            && (this.getPublisherid() == null ? other.getPublisherid() == null : this.getPublisherid().equals(other.getPublisherid()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getInfoid() == null) ? 0 : getInfoid().hashCode());
        result = prime * result + ((getInfocategoryid() == null) ? 0 : getInfocategoryid().hashCode());
        result = prime * result + ((getTitle() == null) ? 0 : getTitle().hashCode());
        result = prime * result + ((getContent() == null) ? 0 : getContent().hashCode());
        result = prime * result + ((getPublishdate() == null) ? 0 : getPublishdate().hashCode());
        result = prime * result + ((getPublisherid() == null) ? 0 : getPublisherid().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", infoid=").append(infoid);
        sb.append(", infocategoryid=").append(infocategoryid);
        sb.append(", title=").append(title);
        sb.append(", content=").append(content);
        sb.append(", publishdate=").append(publishdate);
        sb.append(", publisherid=").append(publisherid);
        sb.append("]");
        return sb.toString();
    }
}
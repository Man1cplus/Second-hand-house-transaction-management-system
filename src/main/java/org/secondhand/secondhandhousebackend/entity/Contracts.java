package org.secondhand.secondhandhousebackend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName contracts
 */
@TableName(value ="contracts")
@Data
public class Contracts {
    /**
     * 合同ID，主键，自增
     */
    @TableId(type = IdType.AUTO)
    private Integer contractid;

    /**
     * 房源ID，外键，关联房源信息表
     */
    private Integer propertyid;

    /**
     * 买家ID，外键，关联用户表
     */
    private Integer buyerid;

    /**
     * 签订日期
     */
    private Date signingdate;

    /**
     * 合同状态，如待审核、已签订、已取消
     */
    private String contractstatus;

    /**
     * 卖家ID，外键，关联用户表
     */
    private Integer sellerid;

    /**
     * 合同文件路径
     */
    private String contractfile;

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
        Contracts other = (Contracts) that;
        return (this.getContractid() == null ? other.getContractid() == null : this.getContractid().equals(other.getContractid()))
            && (this.getPropertyid() == null ? other.getPropertyid() == null : this.getPropertyid().equals(other.getPropertyid()))
            && (this.getBuyerid() == null ? other.getBuyerid() == null : this.getBuyerid().equals(other.getBuyerid()))
            && (this.getSigningdate() == null ? other.getSigningdate() == null : this.getSigningdate().equals(other.getSigningdate()))
            && (this.getContractstatus() == null ? other.getContractstatus() == null : this.getContractstatus().equals(other.getContractstatus()))
            && (this.getSellerid() == null ? other.getSellerid() == null : this.getSellerid().equals(other.getSellerid()))
            && (this.getContractfile() == null ? other.getContractfile() == null : this.getContractfile().equals(other.getContractfile()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getContractid() == null) ? 0 : getContractid().hashCode());
        result = prime * result + ((getPropertyid() == null) ? 0 : getPropertyid().hashCode());
        result = prime * result + ((getBuyerid() == null) ? 0 : getBuyerid().hashCode());
        result = prime * result + ((getSigningdate() == null) ? 0 : getSigningdate().hashCode());
        result = prime * result + ((getContractstatus() == null) ? 0 : getContractstatus().hashCode());
        result = prime * result + ((getSellerid() == null) ? 0 : getSellerid().hashCode());
        result = prime * result + ((getContractfile() == null) ? 0 : getContractfile().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", contractid=").append(contractid);
        sb.append(", propertyid=").append(propertyid);
        sb.append(", buyerid=").append(buyerid);
        sb.append(", signingdate=").append(signingdate);
        sb.append(", contractstatus=").append(contractstatus);
        sb.append(", sellerid=").append(sellerid);
        sb.append(", contractfile=").append(contractfile);
        sb.append("]");
        return sb.toString();
    }
}
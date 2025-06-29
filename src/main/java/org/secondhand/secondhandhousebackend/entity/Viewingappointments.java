package org.secondhand.secondhandhousebackend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

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
     * 状态，如已预约、已取消
     */
    private Object status;

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
        Viewingappointments other = (Viewingappointments) that;
        return (this.getAppointmentid() == null ? other.getAppointmentid() == null : this.getAppointmentid().equals(other.getAppointmentid()))
            && (this.getPropertyid() == null ? other.getPropertyid() == null : this.getPropertyid().equals(other.getPropertyid()))
            && (this.getBuyerid() == null ? other.getBuyerid() == null : this.getBuyerid().equals(other.getBuyerid()))
            && (this.getAppointmenttime() == null ? other.getAppointmenttime() == null : this.getAppointmenttime().equals(other.getAppointmenttime()))
            && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getAppointmentid() == null) ? 0 : getAppointmentid().hashCode());
        result = prime * result + ((getPropertyid() == null) ? 0 : getPropertyid().hashCode());
        result = prime * result + ((getBuyerid() == null) ? 0 : getBuyerid().hashCode());
        result = prime * result + ((getAppointmenttime() == null) ? 0 : getAppointmenttime().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", appointmentid=").append(appointmentid);
        sb.append(", propertyid=").append(propertyid);
        sb.append(", buyerid=").append(buyerid);
        sb.append(", appointmenttime=").append(appointmenttime);
        sb.append(", status=").append(status);
        sb.append("]");
        return sb.toString();
    }
}
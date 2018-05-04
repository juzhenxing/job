package com.zxkj.job.common.bean;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public abstract class BasePo<T extends Model> extends Model<T> {

    /**
     * 不用metaObject，只能控制单个操作更改数据
     * 乐观锁 @Version
     */
    @TableId(type = IdType.INPUT)
    @TableField(value = "id")
    protected Long id;

    /**
     * 创建时间
     */
    protected Date gmtCreate;

    /**
     * 修改时间
     */
    protected Date gmtModified;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}

package com.zxkj.job.bean.po;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.zxkj.job.common.bean.BasePo;
import com.zxkj.job.enums.CheckStateType;
import com.zxkj.job.enums.EducationBackgroundType;
import lombok.Data;

import java.util.Date;

/**
 * 管理员类
 */
@Data
@TableName("t_job_administrator")
public class AdministratorPo extends BasePo<AdministratorPo> {

    private String phone;

    private String email;

    private String userName;

    private String password;

    private CheckStateType checkState;

    /**
     * 是否激活
     */
    @TableField("is_activate")
    private Boolean activate;

}

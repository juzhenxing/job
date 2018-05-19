package com.zxkj.job.bean.vo;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.zxkj.job.common.bean.BasePo;
import com.zxkj.job.common.bean.BaseVo;
import com.zxkj.job.enums.CheckStateType;
import lombok.Data;

/**
 * 管理员类
 */
@Data
public class AdministratorVo extends BaseVo {

    private String phone;

    private String email;

    private String userName;

    private String password;

    private CheckStateType checkState;

    /**
     * 是否激活
     */
    private Boolean activate;

}

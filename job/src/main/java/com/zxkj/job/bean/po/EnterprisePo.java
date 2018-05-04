package com.zxkj.job.bean.po;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.zxkj.job.common.bean.BasePo;
import com.zxkj.job.enums.*;
import lombok.Data;

@Data
@TableName("t_job_enterprise")
public class EnterprisePo extends BasePo<EnterprisePo> {

    private String fullname;

    private String shortname;

    private String orgCode;

    private String recommend;

    private NatureType nature;

    private IndustryInvolvedType industryInvolved;

    private WorkerNumberType workerNumber;

    private String name;

    private String phone;

    private String telephone;

    private String homepageUrl;

    private ProvinceType province;

    private String city;

    private String address;

    private String postcode;

    private String licenseFileName;

    private String email;

    private String userName;

    private String password;

    private CheckStateType checkState;

    @TableField("is_legal")
    private Boolean legal;

    /**
     * 是否激活
     */
    @TableField("is_activate")
    private Boolean activate;

}

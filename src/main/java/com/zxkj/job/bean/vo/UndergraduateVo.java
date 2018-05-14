package com.zxkj.job.bean.vo;

import com.baomidou.mybatisplus.annotations.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.zxkj.job.common.bean.BasePo;
import com.zxkj.job.common.bean.BaseVo;
import com.zxkj.job.enums.EducationBackgroundType;
import lombok.Data;

import java.util.Date;

/**
 * 学生类
 */
@Data
public class UndergraduateVo extends BaseVo {

    private String headUrl;

    private String name;

    private Integer sex;

    private String school;

    private EducationBackgroundType educationBackground;

    private String profession;

    @JsonFormat(pattern = "yyyy-MM")
    private Date graduateYear;

    private String phone;

    private String email;

}

package com.zxkj.job.bean.po;

import com.baomidou.mybatisplus.annotations.TableName;
import com.zxkj.job.common.bean.BasePo;
import com.zxkj.job.enums.EducationBackgroundType;
import lombok.Data;

import java.util.Date;

/**
 * 学生类
 */
@Data
@TableName("t_job_undergraduate")
public class UndergraduatePo extends BasePo<UndergraduatePo> {

    private static final long serialVersionUID = 2015229907224556910L;

    private String headUrl;

    private String name;

    private Integer sex;

    private String school;

    private EducationBackgroundType educationBackground;

    private String profession;

    private Date graduateYear;

    private String phone;

    private String email;

    private String password;

    private Long acquiescenceResumeId;

}

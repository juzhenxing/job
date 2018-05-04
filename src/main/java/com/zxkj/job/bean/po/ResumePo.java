package com.zxkj.job.bean.po;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.zxkj.job.common.bean.BasePo;
import com.zxkj.job.enums.EducationBackgroundType;
import lombok.Data;

import java.util.Date;

/**
 * 简历类
 */
@Data
@TableName("t_job_resume")
public class ResumePo extends BasePo<ResumePo> {

    private String name;

    @TableField("is_acquiescence")
    private Boolean acquiescence;

    private Integer type;

    private Date createTime;

    private Integer completeness;

    private String undergraduateName;

    private Integer undergraduateSex;

    private String undergraduatePhone;

    private String undergraduateEmail;

    private String undergraduateSchool;

    private EducationBackgroundType undergraduateEducationBackground;

    private String undergraduateProfession;

    private Date undergraduateGraduateYear;

    private Date birthDate;

    private Integer politicsStatus;

    private String nativePlace;

    private String url;

    private Long undergraduateId;

}

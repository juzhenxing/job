package com.zxkj.job.bean.po;

import com.baomidou.mybatisplus.annotations.TableName;
import com.zxkj.job.common.bean.BasePo;
import com.zxkj.job.enums.EducationBackgroundType;
import com.zxkj.job.enums.JobCategoryType;
import lombok.Data;

/**
 * 职位类
 */
@Data
@TableName("t_job_professional")
public class ProfessionalPo extends BasePo<ProfessionalPo> {

    private Long enterpriseId;

    private String name;

    private Integer hiring;

    private EducationBackgroundType educationBackground;

    private String majorField;

    private String sketch;

    private String  jobRequirement;

    private String workplace;

    private JobCategoryType type;

}

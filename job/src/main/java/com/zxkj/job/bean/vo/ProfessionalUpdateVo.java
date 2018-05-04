package com.zxkj.job.bean.vo;

import com.zxkj.job.common.bean.BaseVo;
import com.zxkj.job.enums.EducationBackgroundType;
import com.zxkj.job.enums.JobCategoryType;
import lombok.Data;

/**
 * 职位类
 */
@Data
public class ProfessionalUpdateVo extends BaseVo {

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

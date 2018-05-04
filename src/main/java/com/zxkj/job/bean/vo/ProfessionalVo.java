package com.zxkj.job.bean.vo;

import com.baomidou.mybatisplus.annotations.TableName;
import com.zxkj.job.common.bean.BasePo;
import com.zxkj.job.common.bean.BaseVo;
import com.zxkj.job.enums.EducationBackgroundType;
import com.zxkj.job.enums.JobCategoryType;
import lombok.Data;

/**
 * 职位类
 */
@Data
public class ProfessionalVo extends BaseVo {

    private Long enterpriseId;

    private String name;

    private Integer hiring;

    private String educationBackground;

    private String majorField;

    private String sketch;

    private String  jobRequirement;

    private String workplace;

    private String type;

}

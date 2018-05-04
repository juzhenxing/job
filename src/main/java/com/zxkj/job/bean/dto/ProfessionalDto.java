package com.zxkj.job.bean.dto;

import com.baomidou.mybatisplus.annotations.TableName;
import com.zxkj.job.common.bean.BasePo;
import com.zxkj.job.enums.EducationBackgroundType;
import com.zxkj.job.enums.JobCategoryType;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 职位类
 */
@Data
public class ProfessionalDto {

    @NotNull(message = "职位id不能为空")
    private Long id;

    @NotBlank(message = "招聘职位不能为空")
    private String name;

    @NotNull(message = "招聘人数不能为空")
    private Integer hiring;

    @NotNull(message = "学历要求不能为空")
    private EducationBackgroundType educationBackground;

    @NotBlank(message = "专业要求不能为空")
    private String majorField;

    /**
     * 岗位职责
     */
    @NotBlank(message = "岗位职责不能为空")
    private String sketch;

    @NotBlank(message = "职位要求不能为空")
    private String  jobRequirement;

    /**
     * 工作地点
     */
    @NotBlank(message = "工作地点不能为空")
    private String workplace;

    @NotNull(message = "职位类别不能为空")
    private JobCategoryType type;

}

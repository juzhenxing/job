package com.zxkj.job.bean.dto;

import com.zxkj.job.enums.EducationBackgroundType;
import com.zxkj.job.enums.JobCategoryType;
import com.zxkj.job.enums.ProvinceType;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 职位类
 */
@Data
public class QueryProfessionalDto {

    private JobCategoryType jobCategoryType;

    private ProvinceType province;

}

package com.zxkj.job.bean.dto;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.zxkj.job.common.bean.BasePo;
import com.zxkj.job.enums.EducationBackgroundType;
import com.zxkj.job.enums.PoliticsStatusType;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 简历类
 */
@Data
public class ResumeDto extends BasePo<ResumeDto> {

    private Long id;

    @NotBlank(message = "名称不能为空")
    private String name;

    @NotNull(message = "设为默认不能为空")
    private Boolean acquiescence;

    @NotBlank(message = "出生年月不能为空")
    private String birthDate;

    @NotNull(message = "政治面貌不能为空")
    private PoliticsStatusType politicsStatus;

    @NotBlank(message = "籍贯不能为空")
    private String province;

    @NotBlank(message = "籍贯不能为空")
    private String city;

    private String url;

}

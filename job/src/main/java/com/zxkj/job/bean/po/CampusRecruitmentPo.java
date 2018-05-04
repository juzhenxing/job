package com.zxkj.job.bean.po;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.zxkj.job.common.bean.BasePo;
import com.zxkj.job.enums.JobCategoryType;
import lombok.Data;

import java.util.Date;

/**
 * 校招类
 */
@Data
@TableName("t_job_campus_recruitment")
public class CampusRecruitmentPo extends BasePo<CampusRecruitmentPo> {

    private String title;

    private String tags;

    private String generalRegulationFileName;

    private Long enterpriseId;

    private String workplace;

    @TableField("is_hot")
    private Boolean hot;

    @TableField("is_recommend")
    private Boolean recommend;

    @TableField("is_official")
    private Boolean official;

}

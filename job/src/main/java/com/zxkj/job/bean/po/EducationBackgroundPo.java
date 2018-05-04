package com.zxkj.job.bean.po;

import com.baomidou.mybatisplus.annotations.TableName;
import com.zxkj.job.common.bean.BasePo;
import com.zxkj.job.enums.EducationBackgroundType;
import com.zxkj.job.enums.RankType;
import lombok.Data;

import java.util.Date;

/**
 * 教育背景类
 */
@Data
@TableName("t_job_education_background")
public class EducationBackgroundPo extends BasePo<EducationBackgroundPo> {

    private String school;

    private EducationBackgroundType educationBackground;

    private Date startTime;

    private Date endTime;

    private String profession;

    private RankType ranking;

    private String description;

    private Long resumeId;

}

package com.zxkj.job.bean.po;

import com.baomidou.mybatisplus.annotations.TableName;
import com.zxkj.job.common.bean.BasePo;
import lombok.Data;

import java.util.Date;

@Data
@TableName("t_job_project_competition")
public class ProjectCompetitionPo extends BasePo<ProjectCompetitionPo> {

    private String name;

    private Integer scale;

    private String source;

    private String duty;

    private Date startTime;

    private Date endTime;

    private String briefIntroduction;

    private String performance;

    private Long resumeId;

}

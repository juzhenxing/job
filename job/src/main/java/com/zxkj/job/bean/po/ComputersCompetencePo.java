package com.zxkj.job.bean.po;

import com.baomidou.mybatisplus.annotations.TableName;
import com.zxkj.job.common.bean.BasePo;
import com.zxkj.job.enums.LevelType;
import lombok.Data;

/**
 * 计算机能力类
 */
@Data
@TableName("t_job_computers_competence")
public class ComputersCompetencePo extends BasePo<ComputersCompetencePo> {

    private String name;

    private LevelType levelType;

    private Long resumeId;
}

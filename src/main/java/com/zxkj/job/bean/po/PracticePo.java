package com.zxkj.job.bean.po;

import com.baomidou.mybatisplus.annotations.TableName;
import com.zxkj.job.common.bean.BasePo;
import lombok.Data;

import java.util.Date;

/**
 * 实习经历表
 */
@Data
@TableName("t_job_practice")
public class PracticePo extends BasePo<PracticePo> {

    /**
     * 全职为1，兼职为2，实习为3
     */
    private Integer type;

    private String enterpriseName;

    /**
     * 公司规模:50人以内为1,50-100人为2,100-300人为3，300-1000人为4,1000-2000人为5
     */
    private Integer scale;

    private String jobName;

    private Date startTime;

    private Date endTime;

    private String description;

    private Long resumeId;

}

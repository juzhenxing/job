package com.zxkj.job.bean.po;

import com.baomidou.mybatisplus.annotations.TableName;
import com.zxkj.job.common.bean.BasePo;
import com.zxkj.job.enums.HonorsAwardsType;
import lombok.Data;

/**
 * 荣誉奖励类
 */
@Data
@TableName("t_job_honors_awards")
public class HonorsAwardsPo extends BasePo<HonorsAwardsPo> {

    private String name;

    private HonorsAwardsType honorsAwardsType;

    private Long resumeId;
}

package com.zxkj.job.bean.po;

import com.baomidou.mybatisplus.annotations.TableName;
import com.zxkj.job.common.bean.BasePo;
import com.zxkj.job.enums.CollectType;
import lombok.Data;

/**
 * 收藏类
 */
@Data
@TableName("t_job_collect")
public class CollectPo extends BasePo<CollectPo> {

    private CollectType type;

    private Long undergraduateId;

    private Long careerTalkId;

    private Long campusRecruitmentId;

}

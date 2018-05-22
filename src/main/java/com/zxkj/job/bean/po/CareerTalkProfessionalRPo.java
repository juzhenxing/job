package com.zxkj.job.bean.po;

import com.baomidou.mybatisplus.annotations.TableName;
import com.zxkj.job.common.bean.BasePo;
import lombok.Data;

/**
 * 宣讲会职位关系类
 */
@Data
@TableName("t_job_career_talk_professional_r")
public class CareerTalkProfessionalRPo extends BasePo<CareerTalkProfessionalRPo> {

    private Long careerTalkId;

    private Long professionalId;

}

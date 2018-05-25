package com.zxkj.job.bean.po;

import com.baomidou.mybatisplus.annotations.TableName;
import com.zxkj.job.common.bean.BasePo;
import com.zxkj.job.enums.StatusType;
import lombok.Data;

import java.util.Date;

/**
 * 投递信息类
 */
@Data
@TableName("t_job_delivery_information")
public class DeliveryInformationPo extends BasePo<DeliveryInformationPo> {

    private Long professionalId;

    private Long resumeId;

    private Long undergraduateId;

    private Long enterpriseId;

    private Date time;

    private StatusType status;

    private Long careerTalkOrCampusRecruitmentId;

}

package com.zxkj.job.bean.vo;

import com.baomidou.mybatisplus.annotations.TableName;
import com.zxkj.job.common.bean.BasePo;
import com.zxkj.job.common.bean.BaseVo;
import com.zxkj.job.enums.StatusType;
import lombok.Data;

import java.util.Date;

/**
 * 投递信息类
 */
@Data
public class DeliveryInformationVo extends BaseVo {

    private Long professionalId;

    private String professionalName;

    private Long resumeId;

    private Long undergraduateId;

    private String undergraduateName;

    private Long enterpriseId;

    private String enterpriseName;

    private Date time;

    private String status;

    private Date deliveryTime;

}

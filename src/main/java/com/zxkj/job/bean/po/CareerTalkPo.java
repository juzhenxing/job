package com.zxkj.job.bean.po;

import com.baomidou.mybatisplus.annotations.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.zxkj.job.common.bean.BasePo;
import com.zxkj.job.enums.OperationType;
import com.zxkj.job.enums.ProvinceType;
import lombok.Data;

import javax.naming.OperationNotSupportedException;
import java.util.Date;

/**
 * 宣讲会类
 */
@Data
@TableName("t_job_career_talk")
public class CareerTalkPo extends BasePo<CareerTalkPo> {

    private Long enterpriseId;

    private Date startTime;

    private Date endTime;

    private ProvinceType province;

    private String city;

    private String location;

    private String school;

    private String name;

    private Long hits;

    private OperationType operationType;

    private String preachingTextFileName;

    private String applyUrl;

}

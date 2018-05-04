package com.zxkj.job.bean.vo;

import com.baomidou.mybatisplus.annotations.TableName;
import com.zxkj.job.common.bean.BasePo;
import com.zxkj.job.common.bean.BaseVo;
import com.zxkj.job.enums.OperationType;
import com.zxkj.job.enums.ProvinceType;
import lombok.Data;

import java.util.Date;

/**
 * 宣讲会类
 */
@Data
public class CareerTalkVo extends BaseVo {

    private Long enterpriseId;

    private Date startTime;

    private Date endTime;

    private String province;

    private String city;

    private String location;

    private String school;

    private String name;

    private Long hits;

    private String operationType;

    private Date createTime;
}

package com.zxkj.job.bean.vo;

import com.baomidou.mybatisplus.annotations.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.zxkj.job.common.bean.BasePo;
import com.zxkj.job.common.bean.BaseVo;
import com.zxkj.job.enums.OperationType;
import com.zxkj.job.enums.ProvinceType;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * 宣讲会类
 */
@Data
public class CareerTalkVo extends BaseVo {

    private Long enterpriseId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone="GMT+8")
    private Date startTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone="GMT+8")
    private Date endTime;

    private String province;

    private String city;

    private String location;

    private String school;

    private String name;

    private Long hits;

    private String operationType;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone="GMT+8")
    private Date createTime;

    private String preachingTextFileName;

    private List<ProfessionalVo> professionalVoList;

    private String applyUrl;
}

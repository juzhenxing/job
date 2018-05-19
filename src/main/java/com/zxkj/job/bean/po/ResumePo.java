package com.zxkj.job.bean.po;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.zxkj.job.common.bean.BasePo;
import com.zxkj.job.enums.EducationBackgroundType;
import com.zxkj.job.enums.PoliticsStatusType;
import lombok.Data;

import java.util.Date;


/**
 * 简历类
 */
@Data
@TableName("t_job_resume")
public class ResumePo extends BasePo<ResumePo> {

    private String name;

    /**
     * 0表示在线简历，1表示附件简历
     */
    private Integer type;

    private Float completeness;

    private Date birthDate;

    private PoliticsStatusType politicsStatus;

    private String province;

    private String city;

    private String url;

    private Long undergraduateId;

}

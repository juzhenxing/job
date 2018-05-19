package com.zxkj.job.bean.vo;

import com.zxkj.job.common.bean.BaseVo;
import com.zxkj.job.enums.PoliticsStatusType;
import lombok.Data;

import java.util.Date;

/**
 * 简历类
 */
@Data
public class ResumeInfoVo extends BaseVo {

    private String name;

    private Integer type;

    private Float completeness;

    private Date birthDate;

    private Integer age;

    private String politicsStatus;

    private String province;

    private String city;

    private String url;

    private Boolean acquiescence;

    private Date createTime;

}

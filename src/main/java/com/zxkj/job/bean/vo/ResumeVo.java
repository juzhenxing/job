package com.zxkj.job.bean.vo;

import com.baomidou.mybatisplus.annotations.TableName;
import com.zxkj.job.common.bean.BasePo;
import com.zxkj.job.common.bean.BaseVo;
import com.zxkj.job.enums.PoliticsStatusType;
import lombok.Data;

import java.util.Date;

/**
 * 简历类
 */
@Data
public class ResumeVo extends BaseVo {

    private String name;

    private Boolean type;

    private Float completeness;

    private Date birthDate;

    private PoliticsStatusType politicsStatus;

    private String province;

    private String city;

    private String url;

    private Boolean acquiescence;

    private Date createTime;

}

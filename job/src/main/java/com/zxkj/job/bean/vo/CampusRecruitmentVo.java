package com.zxkj.job.bean.vo;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.zxkj.job.common.bean.BasePo;
import com.zxkj.job.common.bean.BaseVo;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 校招类
 */
@Data
public class CampusRecruitmentVo extends BaseVo {

    private String title;

    private String tags;

    private String generalRegulationFileName;

    private Long enterpriseId;

    private String workplace;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date releaseTime;

    private List<ProfessionalVo> professionalVoList;



}

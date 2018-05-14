package com.zxkj.job.bean.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zxkj.job.common.bean.BaseVo;
import com.zxkj.job.enums.EducationBackgroundType;
import com.zxkj.job.enums.ProvinceType;
import com.zxkj.job.enums.RankType;
import lombok.Data;

import java.util.Date;

/**
 * 教育背景类
 */
@Data
public class EducationBackgroundVo extends BaseVo {

    private String province;

    private String city;

    private String school;

    private String educationBackground;

    private String startTime;

    private String endTime;

    private String profession;

    private String ranking;

    private String description;

    private Long resumeId;

}

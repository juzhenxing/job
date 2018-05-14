package com.zxkj.job.bean.dto;

import com.baomidou.mybatisplus.annotations.TableName;
import com.zxkj.job.common.bean.BasePo;
import com.zxkj.job.enums.EducationBackgroundType;
import com.zxkj.job.enums.ProvinceType;
import com.zxkj.job.enums.RankType;
import lombok.Data;

import java.util.Date;

/**
 * 教育背景类
 */
@Data
public class EducationBackgroundDto {

    private Long id;

    private ProvinceType province;

    private String city;

    private String school;

    private EducationBackgroundType educationBackground;

    private String startTime;

    private String endTime;

    private String profession;

    private RankType ranking;

    private String description;

    private Long resumeId;

}

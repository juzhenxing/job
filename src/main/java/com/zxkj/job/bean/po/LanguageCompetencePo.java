package com.zxkj.job.bean.po;

import com.baomidou.mybatisplus.annotations.TableName;
import com.zxkj.job.common.bean.BasePo;
import com.zxkj.job.enums.LanguageType;
import com.zxkj.job.enums.LevelType;
import lombok.Data;

@Data
@TableName("t_job_language_competence")
public class LanguageCompetencePo extends BasePo<LanguageCompetencePo> {


    private LanguageType name;

    private LevelType hearAbout;

    private LevelType readDepict;

    private String otherDescription;

    private Long resumeId;

}

package com.zxkj.job.bean.po;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.zxkj.job.common.bean.BasePo;
import lombok.Data;

/**
 * 校招职位关系类
 */
@Data
@TableName("t_job_campus_recruitment_professional_r")
public class CampusRecruitmentProfessionalRPo extends BasePo<CampusRecruitmentProfessionalRPo> {

    private Long campusRecruitmentId;

    private Long professionalId;

}

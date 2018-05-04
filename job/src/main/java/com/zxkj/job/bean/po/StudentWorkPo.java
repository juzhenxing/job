package com.zxkj.job.bean.po;

import com.baomidou.mybatisplus.annotations.TableName;
import com.zxkj.job.common.bean.BasePo;
import lombok.Data;

import java.util.Date;

/**
 * 学生工作类
 */
@Data
@TableName("t_job_student_work")
public class StudentWorkPo extends BasePo<StudentWorkPo> {

    private String school;

    private String organizationName;

    private String jobTitle;

    private Date startTime;

    private Date endTime;

    private String jobDescription;

    private Long resumeId;

}

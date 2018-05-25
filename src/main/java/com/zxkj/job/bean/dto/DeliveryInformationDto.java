package com.zxkj.job.bean.dto;

import com.baomidou.mybatisplus.annotations.TableName;
import com.zxkj.job.common.bean.BasePo;
import com.zxkj.job.enums.StatusType;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 投递信息类
 */
@Data
public class DeliveryInformationDto {

    private Long id;

    @NotNull(message = "职位id不能为空")
    private Long professionalId;

    @NotNull(message = "简历id不能为空")
    private Long resumeId;

    @NotNull(message = "宣讲会id或校园招聘id不能为空")
    private Long careerTalkOrCampusRecruitmentId;

}

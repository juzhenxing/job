package com.zxkj.job.bean.dto;

import com.baomidou.mybatisplus.annotations.TableName;
import com.zxkj.job.common.bean.BasePo;
import com.zxkj.job.enums.CollectType;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 收藏类
 */
@Data
public class CollectDto {

    @NotNull(message = "收藏类型不能为空")
    private CollectType type;

    private Long careerTalkId;

    private Long campusRecruitmentId;

}

package com.zxkj.job.bean.vo;

import com.baomidou.mybatisplus.annotations.TableName;
import com.zxkj.job.common.bean.BasePo;
import com.zxkj.job.common.bean.BaseVo;
import com.zxkj.job.enums.CollectType;
import lombok.Data;

/**
 * 收藏类
 */
@Data
public class CollectVo extends BaseVo {

    private String type;

    private String enterpriseName;

    private String collectTime;

    private Long undergraduateId;

    private Long careerTalkId;

    private Long campusRecruitmentId;
}

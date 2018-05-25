package com.zxkj.job.bean.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zxkj.job.common.bean.BaseVo;
import com.zxkj.job.enums.OperationType;
import com.zxkj.job.enums.ProvinceType;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * 宣讲会类
 */
@Data
public class CareerTalkUpdateVo extends BaseVo {

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private String startTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private String endTime;

    private Integer province;

    private String city;

    private String location;

    private String school;

    private Integer operationType;

    private String preachingTextFileName;

    private List<ProfessionalVo> professionalVoList;

    private String applyUrl;

}

package com.zxkj.job.bean.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zxkj.job.enums.OperationType;
import com.zxkj.job.enums.ProvinceType;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 宣讲会类
 */
@Data
public class QueryCareerTalkDto {

    private ProvinceType province;

    private String key;

}

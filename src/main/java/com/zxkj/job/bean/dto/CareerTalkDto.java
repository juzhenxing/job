package com.zxkj.job.bean.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zxkj.job.enums.*;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Date;
import java.util.List;

/**
 * 宣讲会类
 */
@Data
public class CareerTalkDto {

    private Long id;

    @NotNull(message = "宣讲开始时间不能为空")
    private String startTime;

    @NotNull(message = "宣讲结束时间不能为空")
    private String endTime;

    @NotNull(message = "省份不能为空")
    private ProvinceType province;

    @NotBlank(message = "城市不能为空")
    private String city;

    @NotBlank(message = "举办地点不能为空")
    private String location;

    @NotBlank(message = "所在高校不能为空")
    private String school;

    @NotNull(message = "申请方式不能为空")
    private OperationType operationType;

    @NotNull(message = "宣讲正文不能为空")
    private MultipartFile preachingText;

    private List<Long> professionalIds;

    private String applyUrl;

}

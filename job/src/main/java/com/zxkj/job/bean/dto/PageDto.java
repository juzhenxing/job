package com.zxkj.job.bean.dto;

import com.zxkj.job.enums.IndustryInvolvedType;
import com.zxkj.job.enums.NatureType;
import com.zxkj.job.enums.ProvinceType;
import com.zxkj.job.enums.WorkerNumberType;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * 查询企业类
 */
@Data
public class PageDto {

    @NotNull(message = "页码不能为空")
    private Integer page;

    @NotNull(message = "页面大小不能为空")
    private Integer limit;
}

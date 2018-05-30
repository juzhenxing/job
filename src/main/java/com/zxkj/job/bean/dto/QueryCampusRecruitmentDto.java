package com.zxkj.job.bean.dto;

import com.zxkj.job.enums.JobCategoryType;
import com.zxkj.job.enums.ProvinceType;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 校招类
 */
@Data
public class QueryCampusRecruitmentDto {

    private ProvinceType province;

    private JobCategoryType jobCategoryType;

    private String key;

}

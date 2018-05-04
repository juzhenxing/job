package com.zxkj.job.bean.dto;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.zxkj.job.common.bean.BasePo;
import com.zxkj.job.enums.JobCategoryType;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * 校招类
 */
@Data
public class CampusRecruitmentDto {

    private Long id;

    @NotBlank(message = "标题不能为空")
    private String title;

    @NotBlank(message = "文章标签不能为空")
    private String tags;

    @NotNull(message = "简章不能为空")
    private MultipartFile generalRegulation;

    @NotBlank(message = "涉及城市不能为空")
    private String workplace;

    @NotNull(message = "职位不能为空")
    private List<Long> professionalIds;
}

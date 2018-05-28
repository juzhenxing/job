package com.zxkj.job.bean.dto;

import com.zxkj.job.enums.EducationBackgroundType;
import lombok.Data;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.Date;

/**
 * 简单学生类
 */
@Data
public class SimpleUndergraduateDto {

    @NotBlank(message = "email不能为空")
    @Email(message = "email格式不正确")
    private String email;

    @NotBlank(message = "密码不能为空")
    private String password;

    private String code;

}

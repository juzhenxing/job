package com.zxkj.job.bean.dto;

import com.zxkj.job.enums.EducationBackgroundType;
import lombok.Data;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

/**
 * 学生类
 */
@Data
public class UndergraduateDto {

    private MultipartFile headFile;

    private String name;

    private Integer sex;

    private String school;

    private EducationBackgroundType educationBackground;

    private String profession;

    private Date graduateYear;

    private String phone;

    private String email;

    private String password;

}

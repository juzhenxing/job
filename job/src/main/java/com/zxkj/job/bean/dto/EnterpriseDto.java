package com.zxkj.job.bean.dto;

import com.zxkj.job.enums.IndustryInvolvedType;
import com.zxkj.job.enums.NatureType;
import com.zxkj.job.enums.ProvinceType;
import com.zxkj.job.enums.WorkerNumberType;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.File;

/**
 * 企业类
 */
@Data
public class EnterpriseDto {

    @NotBlank(message = "公司名称不能为空")
    private String fullname;

    private String shortname;

    @NotBlank(message = "组织机构代码不能为空")
    @Pattern(regexp = "^[1-9]\\d+$", message = "请正确填写您的组织机构代码")
    private String orgCode;

    @NotBlank(message = "公司简介不能为空")
    private String recommend;

    private NatureType nature;

    private IndustryInvolvedType industryInvolved;

    private WorkerNumberType workerNumber;

    @NotBlank(message = "公司联系人必填")
    private String name;

    @NotBlank(message = "公司联系电话必填")
    private String phone;

    @NotBlank(message = "公司座机不能为空")
    private String telephone;

    private String homepageUrl;

    private ProvinceType province;

    @NotBlank(message = "市区必填")
    private String city;

    @NotBlank(message = "地址必填")
    private String address;

    @NotBlank(message = "邮编必填")
    @Pattern(regexp = "^[1-9]\\d+$", message = "请正确填写您的公司邮编")
    private String postcode;

    @NotNull(message = "营业执照必须上传")
    private MultipartFile license;

}

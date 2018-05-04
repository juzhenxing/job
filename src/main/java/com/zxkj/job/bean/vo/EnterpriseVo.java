package com.zxkj.job.bean.vo;

import com.zxkj.job.common.bean.BaseVo;
import com.zxkj.job.enums.*;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * 企业类
 */
@Data
public class EnterpriseVo extends BaseVo{

    private String fullname;

    private String shortname;

    private String orgCode;

    private String recommend;

    private String nature;

    private String industryInvolved;

    private String workerNumber;

    private String name;

    private String phone;

    private String telephone;

    private String homepageUrl;

    private String province;

    private String city;

    private String address;

    private String postcode;

    private String license;

    private String email;

    private String userName;

    private String checkStateType;
}

package com.zxkj.job.bean.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

/**
 * 简单企业类
 */
@Data
public class SimpleEnterpriseDto {

    @NotBlank(message = "用户名不能为空")
    private String userName;

    @NotBlank(message = "密码不能为空")
    private String password;

    @NotBlank(message = "email不能为空")
    @Email(message = "email格式不正确")
    private String email;

}

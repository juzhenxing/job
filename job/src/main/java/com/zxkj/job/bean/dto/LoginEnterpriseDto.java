package com.zxkj.job.bean.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

/**
 * 登录企业类
 */
@Data
public class LoginEnterpriseDto {

    @NotBlank(message = "用户名或邮箱不能为空")
    private String usernameOrEmail;

    @NotBlank(message = "密码不能为空")
    private String password;

}

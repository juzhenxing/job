package com.zxkj.job.bean.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 登录管理员类
 */
@Data
public class LoginAdministratorDto {

    @NotBlank(message = "用户名或邮箱不能为空")
    private String usernameOrEmail;

    @NotBlank(message = "密码不能为空")
    private String password;

}

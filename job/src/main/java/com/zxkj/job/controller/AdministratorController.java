package com.zxkj.job.controller;

import com.zxkj.job.bean.dto.*;
import com.zxkj.job.common.bean.PagedResult;
import com.zxkj.job.enums.CheckStateType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public interface AdministratorController {

    @GetMapping("pre-register")
    String preRegister();

    @GetMapping("login")
    String login();

    @GetMapping("index")
    String index();

    /**
     * 注册
     *
     * @param administratorDto
     * @return
     */
    @PostMapping("register")
    ModelAndView register(@Valid AdministratorDto administratorDto);

    @GetMapping("register")
    ModelAndView register(
            @NotBlank(message = "email不能为空") @Email(message = "email格式不正确")String email,
            @NotBlank(message = "验证码不能为空") String code,
            HttpSession httpSession);

    @PostMapping("login")
    ModelAndView login(
            @Valid LoginAdministratorDto loginAdministratorDto,
            HttpSession httpSession);

    @GetMapping("logout")
    String logout(HttpSession httpSession);

    @GetMapping("find-password")
    String findPassword();

    @PostMapping("check-identity")
    ModelAndView checkIdentity(
            @NotNull(message = "邮箱不能为空") @NotBlank(message = "邮箱不能为空") @Email(message = "邮箱格式不正确") String email,
            HttpSession httpSession);

    @PostMapping("reset-password")
    ModelAndView resetPassword(
            @NotNull(message = "验证码不能为空") @NotBlank(message = "验证码不能为空") String code,
            HttpSession httpSession);

    @PostMapping("set-success")
    ModelAndView setSuccess(
            @NotNull(message = "密码不能为空")@NotBlank(message = "密码不能为空") String password,
            HttpSession httpSession);

    @GetMapping("pre-check-application")
    String checkApplication();

    @GetMapping("check-application")
    @ResponseBody
    PagedResult checkApplication(PageDto pageDto);

    @PostMapping("check-application")
    ModelAndView checkApplication(Long enterpriseId, CheckStateType checkStateType);

    @GetMapping("get-enterprise-by-id")
    ModelAndView getEnterpriseById(Long enterpriseId);
}

package com.zxkj.job.controller;

import com.zxkj.job.bean.dto.*;
import com.zxkj.job.common.bean.PagedResult;
import com.zxkj.job.enums.JobCategoryType;
import com.zxkj.job.enums.ProvinceType;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public interface VisitorController {

    /**
     * 注册
     *
     * @param simpleUndergraduateDto
     * @return
     */
    @PostMapping("add")
    ModelAndView add(@Valid SimpleUndergraduateDto simpleUndergraduateDto, HttpSession httpSession);

    @GetMapping("index")
    ModelAndView index(@RequestParam(required = false) ProvinceType province, ModelAndView modelAndView);

    @GetMapping("campus-recruitment")
    ModelAndView campusRecruitment(@RequestParam(required = false) ProvinceType province, @RequestParam(required = false) JobCategoryType jobCategoryType, ModelAndView modelAndView);

    @GetMapping("career-talk")
    ModelAndView careerTalk(@RequestParam(required = false) ProvinceType province, @RequestParam(required = false) String school, ModelAndView modelAndView);

    /**
     * 学生登录
     * @param simpleUndergraduateDto
     * @return
     */
    @PostMapping("login")
    ModelAndView login(@Valid SimpleUndergraduateDto simpleUndergraduateDto, HttpSession httpSession);

    @GetMapping("register")
    String register();

    @GetMapping("login")
    String login();

    @PostMapping("entry")
    ModelAndView entry(@Valid SimpleUndergraduateDto simpleUndergraduateDto, HttpSession httpSession);

    @GetMapping("find-password")
    String findPassword();

    @PostMapping("check-identity")
    ModelAndView checkIdentity(@NotNull(message = "邮箱不能为空")@NotBlank(message = "邮箱不能为空") @Email(message = "邮箱格式不正确") String email, HttpSession httpSession);

    @PostMapping("re-check-identity")
    @ResponseBody
    ModelMap reCheckIdentity(@NotNull(message = "邮箱不能为空")@NotBlank(message = "邮箱不能为空") @Email(message = "邮箱格式不正确") String email, HttpSession httpSession);

    @PostMapping("reset-password")
    ModelAndView resetPassword(@NotNull(message = "验证码不能为空")@NotBlank(message = "验证码不能为空") String code, HttpSession httpSession);

    @PostMapping("set-success")
    ModelAndView setSuccess(@NotNull(message = "密码不能为空")@NotBlank(message = "密码不能为空") String password, HttpSession httpSession);

    @GetMapping("list-career-talk")
    @ResponseBody
    PagedResult listCareerTalk(PageDto pageDto);

    @GetMapping("list-career-talk-by-dto")
    @ResponseBody
    PagedResult listCareerTalkByQueryDto(QueryCareerTalkDto queryCareerTalkDto, PageDto pageDto);

    @GetMapping("list-campus-recruitment")
    @ResponseBody
    PagedResult listCampusRecruitment(PageDto pageDto);

    @GetMapping("list-campus-recruitment-by-dto")
    @ResponseBody
    PagedResult listCampusRecruitmentByQueryDto(QueryCampusRecruitmentDto queryCampusRecruitmentDto, PageDto pageDto);

    @GetMapping("get-campus-recruitment-by-id")
    ModelAndView getCampusRecruitmentById(Long campusRecruitmentId);

    @GetMapping("list-professional-by-campus-recruitment-id")
    @ResponseBody
    PagedResult listProfessionalByCampusRecruitmentId(PageDto pageDto, Long campusRecruitmentId);

    @GetMapping("check-get-campus-recruitment-by-id")
    @ResponseBody
    ModelMap checkGetCampusRecruitmentById(Long campusRecruitmentId);

    @GetMapping("get-professional-by-id")
    ModelAndView getProfessionalById(Long professionalId);

    @GetMapping("check-get-professional-by-id")
    @ResponseBody
    ModelMap checkGetProfessionalById(Long professionalId);

    @PostMapping("search")
    ModelAndView search(@NotBlank(message = "关键字不能为空") String key, ModelAndView modelAndView);

    @GetMapping("get-career-talk-by-id")
    ModelAndView getCareerTalkById(Long careerTalkId, ModelAndView modelAndView, HttpSession httpSession);

    @GetMapping("list-professional-by-career-talk-id")
    @ResponseBody
    PagedResult listProfessionalByCareerTalkId(PageDto pageDto, Long careerTalkId);

    @PostMapping("register-acquire-code")
    @ResponseBody
    ModelMap registerAcquireCode(@NotNull(message = "邮箱不能为空")@NotBlank(message = "邮箱不能为空") @Email(message = "邮箱格式不正确") String email, HttpSession httpSession);


}

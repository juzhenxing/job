package com.zxkj.job.controller;

import com.zxkj.job.bean.dto.PageDto;
import com.zxkj.job.bean.dto.ResumeDto;
import com.zxkj.job.bean.dto.SimpleUndergraduateDto;
import com.zxkj.job.bean.dto.UndergraduateDto;
import com.zxkj.job.common.bean.PagedResult;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcProperties;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

public interface UndergraduateController {

    /**
     * 注册
     *
     * @param simpleUndergraduateDto
     * @return
     */
    @PostMapping("add")
    ModelAndView add(@Valid SimpleUndergraduateDto simpleUndergraduateDto, HttpSession httpSession);

    @GetMapping("index")
    String index();

    @GetMapping("campus-recruitment")
    ModelAndView campusRecruitment();

    @GetMapping("career-talk")
    String careerTalk();

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

    @PostMapping("reset-password")
    ModelAndView resetPassword(@NotNull(message = "验证码不能为空")@NotBlank(message = "验证码不能为空") String code, HttpSession httpSession);

    @PostMapping("set-success")
    ModelAndView setSuccess(@NotNull(message = "密码不能为空")@NotBlank(message = "密码不能为空") String password, HttpSession httpSession);

    @GetMapping("list-career-talk")
    @ResponseBody
    PagedResult listCareerTalk(PageDto pageDto);

    @GetMapping("list-campus-recruitment")
    @ResponseBody
    PagedResult listCampusRecruitment(PageDto pageDto);

    @GetMapping("logout")
    String logout(HttpSession httpSession);

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

    @GetMapping("personal-center")
    ModelAndView personalCenter(HttpSession httpSession);

    @GetMapping("info-update")
    ModelAndView infoUpdate(HttpSession httpSession);

    @PostMapping("info-update")
    ModelAndView infoUpdate(UndergraduateDto undergraduateDto, HttpSession httpSession);

    @GetMapping("resume-add")
    ModelAndView resumeAdd();

    @PostMapping("resume-add")
    ModelAndView resumeAdd(@Valid ResumeDto resumeDto, HttpSession httpSession);

    @GetMapping("list-resume")
    @ResponseBody
    PagedResult listResume(PageDto pageDto, HttpSession httpSession);

    @PostMapping("delete-resume")
    @ResponseBody
    ModelMap deleteResume(Long resumeId, HttpSession httpSession);
}

package com.zxkj.job.controller;

import com.zxkj.job.bean.dto.PageDto;
import com.zxkj.job.bean.dto.SimpleUndergraduateDto;
import com.zxkj.job.common.bean.PagedResult;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

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

    @GetMapping("list-career-talk")
    @ResponseBody
    PagedResult listCareerTalk(PageDto pageDto);

    @GetMapping("list-campus-recruitment")
    @ResponseBody
    PagedResult listCampusRecruitment(PageDto pageDto);

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
}

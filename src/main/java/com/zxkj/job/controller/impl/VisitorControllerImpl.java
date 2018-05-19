package com.zxkj.job.controller.impl;

import com.zxkj.job.bean.dto.*;
import com.zxkj.job.bean.po.UndergraduatePo;
import com.zxkj.job.bean.vo.ResumeVo;
import com.zxkj.job.common.BaseControllerImpl;
import com.zxkj.job.common.bean.PagedResult;
import com.zxkj.job.controller.UndergraduateController;
import com.zxkj.job.controller.VisitorController;
import com.zxkj.job.enums.*;
import com.zxkj.job.exp.JobException;
import com.zxkj.job.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
@RequestMapping("visitor")
public class VisitorControllerImpl implements VisitorController {

    @Autowired
    UndergraduateService undergraduateService;

    @Autowired
    CareerTalkService careerTalkService;

    @Autowired
    CampusRecruitmentService campusRecruitmentService;

    @Autowired
    ProfessionalService professionalService;

    @Autowired
    ResumeService resumeService;

    @Autowired
    EducationBackgroundService educationBackgroundService;

    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public ModelAndView add(SimpleUndergraduateDto simpleUndergraduateDto, HttpSession httpSession) {
        ModelAndView modelAndView = new ModelAndView();
        try{
            String email = undergraduateService.add(simpleUndergraduateDto);
            httpSession.setAttribute("email", email);
            modelAndView.setViewName("index");
        }catch (JobException jobException){
            jobException.printStackTrace();
            modelAndView.addObject("errorMessage", jobException.getMessage());
            modelAndView.setViewName("undergraduate_register");
        }
        return modelAndView;
    }

    @Override
    public String index() {
        return "index";
    }

    @Override
    public ModelAndView campusRecruitment() {
        ModelAndView modelAndView = new ModelAndView("campus_recruitment");
        modelAndView.addObject("jobCategoryType", JobCategoryType.values());
        modelAndView.addObject("provinceType", ProvinceType.values());
        return modelAndView;
    }

    @Override
    public String careerTalk() {
        return "career_talk";
    }

    @Override
    public ModelAndView login(@Valid SimpleUndergraduateDto simpleUndergraduateDto, HttpSession httpSession) {
        ModelAndView modelAndView = new ModelAndView();
        try{
            undergraduateService.login(simpleUndergraduateDto, httpSession);
        }catch (JobException jobException){
            jobException.printStackTrace();
            modelAndView.addObject("errorMessage", jobException.getMessage());
        }
        modelAndView.setViewName("index");
        return modelAndView;
    }

    @Override
    public String register() {
        return "undergraduate_register";
    }

    @Override
    public String login() {
        return "undergraduate_login";
    }

    @Override
    public ModelAndView entry(@Valid SimpleUndergraduateDto simpleUndergraduateDto, HttpSession httpSession) {
        ModelAndView modelAndView = new ModelAndView();
        try{
            undergraduateService.login(simpleUndergraduateDto, httpSession);
            modelAndView.setViewName("index");
        }catch (JobException jobException){
            jobException.printStackTrace();
            modelAndView.addObject("errorMessage", jobException.getMessage());
            modelAndView.setViewName("undergraduate_login");
        }
        return modelAndView;
    }

    @Override
    public String findPassword() {
        return "find_password";
    }

    @Override
    public PagedResult listCareerTalk(PageDto pageDto) {
        return careerTalkService.list(pageDto);
    }

    @Override
    public PagedResult listCampusRecruitment(PageDto pageDto) {
        return campusRecruitmentService.list(pageDto);
    }

    @Override
    public ModelAndView getCampusRecruitmentById(Long campusRecruitmentId) {
        ModelAndView modelAndView = new ModelAndView();
        try {
            modelAndView.addObject("jobCategoryType", JobCategoryType.values());
            modelAndView.addObject("campusRecruitmentVo", campusRecruitmentService.getById(campusRecruitmentId));
        } catch (Exception e) {
            logger.error(e.getMessage());
            modelAndView.addObject("errorMessage", e.getMessage());
        }
        modelAndView.setViewName("campus_recruitment_info");
        return modelAndView;
    }

    @Override
    public PagedResult listProfessionalByCampusRecruitmentId(PageDto pageDto, Long campusRecruitmentId) {
        return professionalService.listProfessionalByCampusRecruitmentId(pageDto, campusRecruitmentId);
    }

    @Override
    public ModelMap checkGetCampusRecruitmentById(Long campusRecruitmentId) {
        ModelMap modelMap = new ModelMap();
        try {
            campusRecruitmentService.getById(campusRecruitmentId);
        } catch (Exception e) {
            logger.error(e.getMessage());
            modelMap.addAttribute("errorMessage", e.getMessage());
        }
        return modelMap;
    }
    @Override
    public ModelAndView getProfessionalById(Long professionalId) {
        ModelAndView modelAndView = new ModelAndView();
        try {
            modelAndView.addObject("professionalVo", professionalService.getById(professionalId));
        } catch (Exception e) {
            logger.error(e.getMessage());
            modelAndView.addObject("errorMessage", e.getMessage());
        }
        modelAndView.setViewName("enterprise_professional_info");
        return modelAndView;
    }

    @Override
    public ModelMap checkGetProfessionalById(Long professionalId) {
        ModelMap modelMap = new ModelMap();
        try {
            professionalService.getById(professionalId);
        } catch (Exception e) {
            logger.error(e.getMessage());
            modelMap.addAttribute("errorMessage", e.getMessage());
        }
        return modelMap;
    }

    private String checkLogin(ModelAndView modelAndView, HttpSession httpSession){
        Object emailObj = httpSession.getAttribute("email");
        if (org.springframework.util.StringUtils.isEmpty(emailObj)) {
//            throw JobException.NOT_LOGGED_IN_EXCEPTION;
            modelAndView.addObject("errorMessage", "您还未登录");
            modelAndView.setViewName("undergraduate_login");
            return null;
        }
        return emailObj.toString();
    }
}

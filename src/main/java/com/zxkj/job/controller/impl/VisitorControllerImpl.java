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
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

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

    @Autowired
    CampusRecruitmentProfessionalRService campusRecruitmentProfessionalRService;

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
    public ModelAndView index(ProvinceType province, ModelAndView modelAndView) {
        modelAndView.setViewName("index");
        if(province == null){
            province = ProvinceType.BEI_JING;
        }
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        HttpSession httpSession = request.getSession();
        httpSession.setAttribute("province", province);
        httpSession.setAttribute("provinces", ProvinceType.values());
        return modelAndView;
    }

    @Override
    public ModelAndView campusRecruitment(JobCategoryType jobCategoryType, ModelAndView modelAndView) {
        if(jobCategoryType != null){
            logger.error(jobCategoryType.getName());
            modelAndView.addObject("jobCategoryType", jobCategoryType);
        }else{
            modelAndView.addObject("jobCategoryType", 0);
        }
        modelAndView.addObject("jobCategoryTypes", JobCategoryType.values());
        modelAndView.addObject("provinceType", ProvinceType.values());
        modelAndView.setViewName("campus_recruitment");
        return modelAndView;
    }

    @Override
    public ModelAndView careerTalk(ModelAndView modelAndView) {
        modelAndView.setViewName("career_talk");
        modelAndView.addObject("jobCategoryType", JobCategoryType.values());
        modelAndView.addObject("provinceType", ProvinceType.values());
        return modelAndView;
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
        return index((ProvinceType)httpSession.getAttribute("province"), modelAndView);
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
            return index((ProvinceType)httpSession.getAttribute("province"), modelAndView);
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
    public PagedResult listCareerTalkByQueryDto(QueryCareerTalkDto queryCareerTalkDto, PageDto pageDto) {
        return careerTalkService.listByQueryDto(queryCareerTalkDto, pageDto);
    }

    @Override
    public PagedResult listCampusRecruitment(PageDto pageDto) {
        return campusRecruitmentService.list(pageDto);
    }

    @Override
    public PagedResult listCampusRecruitmentByQueryDto(QueryCampusRecruitmentDto queryCampusRecruitmentDto, PageDto pageDto) {
        return campusRecruitmentService.listByQueryCampusRecruitmentDto(queryCampusRecruitmentDto, pageDto);
    }

    @Override
    public ModelAndView getCampusRecruitmentById(Long campusRecruitmentId) {
        ModelAndView modelAndView = new ModelAndView();
        try {
            modelAndView.addObject("jobCategoryType", 0);
            modelAndView.addObject("jobCategoryTypes", JobCategoryType.values());
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

    @Override
    public ModelAndView search(@NotBlank(message = "关键字不能为空") String key, ModelAndView modelAndView) {
        modelAndView.setViewName("search");
        modelAndView.addObject("key", key);
        return modelAndView;
    }

    @Override
    public PagedResult listCampusRecruitmentByQueryProfessionalDto(QueryProfessionalDto queryProfessionalDto, PageDto pageDto) {
        return campusRecruitmentProfessionalRService.listCampusRecruitmentByQueryProfessionalDto(queryProfessionalDto, pageDto);
    }

    public String checkLogin(ModelAndView modelAndView, HttpSession httpSession){
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

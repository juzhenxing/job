package com.zxkj.job.controller.impl;

import com.zxkj.job.bean.dto.PageDto;
import com.zxkj.job.bean.dto.ResumeDto;
import com.zxkj.job.bean.dto.SimpleUndergraduateDto;
import com.zxkj.job.bean.dto.UndergraduateDto;
import com.zxkj.job.bean.po.UndergraduatePo;
import com.zxkj.job.common.BaseControllerImpl;
import com.zxkj.job.common.bean.PagedResult;
import com.zxkj.job.controller.UndergraduateController;
import com.zxkj.job.enums.EducationBackgroundType;
import com.zxkj.job.enums.JobCategoryType;
import com.zxkj.job.enums.PoliticsStatusType;
import com.zxkj.job.enums.ProvinceType;
import com.zxkj.job.exp.JobException;
import com.zxkj.job.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;
import org.thymeleaf.util.StringUtils;

import javax.mail.MessagingException;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
@RequestMapping("undergraduate")
public class UndergraduateControllerImpl extends BaseControllerImpl<UndergraduateService, UndergraduatePo> implements UndergraduateController {

    @Autowired
    CareerTalkService careerTalkService;

    @Autowired
    CampusRecruitmentService campusRecruitmentService;

    @Autowired
    ProfessionalService professionalService;

    @Autowired
    ResumeService resumeService;

    @Override
    public ModelAndView add(SimpleUndergraduateDto simpleUndergraduateDto, HttpSession httpSession) {
        ModelAndView modelAndView = new ModelAndView();
        try{
            String email = service.add(simpleUndergraduateDto);
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
            service.login(simpleUndergraduateDto, httpSession);
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
            service.login(simpleUndergraduateDto, httpSession);
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
    public ModelAndView checkIdentity(String email, HttpSession httpSession) {
        ModelAndView modelAndView = new ModelAndView();
        try {
            service.checkIdentity(email);
            httpSession.setAttribute("emailKey", email);
            modelAndView.setViewName("check_identity");
        } catch (Exception e) {
            e.printStackTrace();
            modelAndView.addObject("errorMessage", e.getMessage());
            modelAndView.setViewName("find_password");
        }
        return modelAndView;
    }

    @Override
    public ModelAndView resetPassword(String code, HttpSession httpSession) {
        ModelAndView modelAndView = new ModelAndView();
        try {
            service.resetPassword(code, httpSession);
            modelAndView.setViewName("reset_password");
        } catch (Exception e) {
            e.printStackTrace();
            modelAndView.addObject("errorMessage", e.getMessage());
            modelAndView.setViewName("check_identity");
        }
        return modelAndView;
    }

    @Override
    public ModelAndView setSuccess(String password, HttpSession httpSession) {
        ModelAndView modelAndView = new ModelAndView();
        try {
            if(service.setSuccess(password, httpSession)){
                modelAndView.setViewName("finish");
            }else{
                modelAndView.setViewName("reset_password");
                modelAndView.addObject("errorMessage", "重置密码失败");
            }
        }catch (Exception e){
            e.printStackTrace();
            modelAndView.setViewName("reset_password");
            modelAndView.addObject("errorMessage", e.getMessage());
        }
        return modelAndView;
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
    public String logout(HttpSession httpSession) {
        httpSession.invalidate();
        return "index";
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

    @Override
    public ModelAndView personalCenter(HttpSession httpSession) {
        ModelAndView modelAndView = new ModelAndView();
        String email = checkLogin(modelAndView, httpSession);
        if(org.springframework.util.StringUtils.isEmpty(email)){
            return modelAndView;
        }
        try{
            modelAndView.addObject("undergraduateVo", service.getByEmail(email));
        }catch (Exception e){
            modelAndView.addObject("errorMessage", e.getMessage());
        }
        modelAndView.setViewName("undergraduate_personal_center");
        return modelAndView;
    }

    @Override
    public ModelAndView infoUpdate(HttpSession httpSession) {
        ModelAndView modelAndView = new ModelAndView();
        String email = checkLogin(modelAndView, httpSession);
        if(org.springframework.util.StringUtils.isEmpty(email)){
            return modelAndView;
        }
        try{
            modelAndView.addObject("undergraduateVo", service.getByEmail(email));
            modelAndView.addObject("educationBackgroundType", EducationBackgroundType.values());
            modelAndView.setViewName("undergraduate_info_update");
            return modelAndView;
        }catch (Exception e){
            modelAndView.addObject("errorMessage", e.getMessage());
        }
        modelAndView.setViewName("undergraduate_personal_center");
        return modelAndView;
    }

    @Override
    public ModelAndView infoUpdate(UndergraduateDto undergraduateDto, HttpSession httpSession) {
        ModelAndView modelAndView = new ModelAndView();
        String email = checkLogin(modelAndView, httpSession);
        if(org.springframework.util.StringUtils.isEmpty(email)){
            return modelAndView;
        }
        try {
            if (service.infoUpdate(undergraduateDto, httpSession)) {
                modelAndView.addObject("message", "更新成功");
                modelAndView.addObject("undergraduateVo", service.getByEmail(email));
                modelAndView.setViewName("undergraduate_personal_center");
                return modelAndView;
            } else {
                modelAndView.addObject("errorMessage", "更新失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.debug("e: ", e);
            modelAndView.addObject("errorMessage", e.getMessage());
        }
        modelAndView.addObject("undergraduateVo", service.getByEmail(email));
        modelAndView.addObject("educationBackgroundType", EducationBackgroundType.values());
        modelAndView.setViewName("undergraduate_info_update");
        return modelAndView;
    }

    @Override
    public ModelAndView resumeAdd() {
        ModelAndView modelAndView = new ModelAndView("undergraduate_resume_add");
        modelAndView.addObject("politicsStatusType", PoliticsStatusType.values());
        return modelAndView;
    }

    @Override
    public ModelAndView resumeAdd(ResumeDto resumeDto, HttpSession httpSession) {
        ModelAndView modelAndView = new ModelAndView();
        String email = checkLogin(modelAndView, httpSession);
        if(org.springframework.util.StringUtils.isEmpty(email)){
            return modelAndView;
        }
        try {
            if (resumeService.add(resumeDto, httpSession)) {
                modelAndView.addObject("message", "添加成功");
                modelAndView.addObject("undergraduateVo", service.getByEmail(email));
                modelAndView.setViewName("undergraduate_personal_center");
                return modelAndView;
            } else {
                modelAndView.addObject("errorMessage", "添加失败");
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            modelAndView.addObject("errorMessage", e.getMessage());
        }
        modelAndView.addObject("politicsStatusType", PoliticsStatusType.values());
        modelAndView.setViewName("undergraduate_resume_add");
        return modelAndView;
    }

    @Override
    public PagedResult listResume(PageDto pageDto, HttpSession httpSession) {
        ModelAndView modelAndView = new ModelAndView();
        String email = checkLogin(modelAndView, httpSession);
        if(org.springframework.util.StringUtils.isEmpty(email)){
            throw JobException.NOT_LOGGED_IN_EXCEPTION;
        }
        return resumeService.list(pageDto, httpSession);
    }

    @Override
    public ModelMap deleteResume(Long resumeId, HttpSession httpSession) {
        ModelAndView modelAndView = new ModelAndView();
        String email = checkLogin(modelAndView, httpSession);
        ModelMap modelMap = new ModelMap();
        if(org.springframework.util.StringUtils.isEmpty(email)){
            modelMap.addAttribute("errorMessage", "您还未登录");
            return modelMap;
        }
        try {
            if (resumeService.deleteByResumeId(resumeId, httpSession)) {
                modelMap.addAttribute("message", "删除成功");
            } else {
                modelMap.addAttribute("errorMessage", "删除失败");
            }
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

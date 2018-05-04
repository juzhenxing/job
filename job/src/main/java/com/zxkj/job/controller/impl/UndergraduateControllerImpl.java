package com.zxkj.job.controller.impl;

import com.zxkj.job.bean.dto.PageDto;
import com.zxkj.job.bean.dto.SimpleUndergraduateDto;
import com.zxkj.job.bean.po.UndergraduatePo;
import com.zxkj.job.common.BaseControllerImpl;
import com.zxkj.job.common.bean.PagedResult;
import com.zxkj.job.controller.UndergraduateController;
import com.zxkj.job.exp.JobException;
import com.zxkj.job.service.CareerTalkService;
import com.zxkj.job.service.UndergraduateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
    public String campusRecruitment() {
        return "campus_recruitment";
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
}

package com.zxkj.job.controller.impl;

import com.zxkj.job.bean.dto.*;
import com.zxkj.job.bean.po.AdministratorPo;
import com.zxkj.job.common.BaseControllerImpl;
import com.zxkj.job.common.bean.PagedResult;
import com.zxkj.job.controller.AdministratorController;
import com.zxkj.job.enums.CheckStateType;
import com.zxkj.job.exp.JobException;
import com.zxkj.job.service.AdministratorService;
import com.zxkj.job.service.EnterpriseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
@RequestMapping("administrator")
public class AdministratorControllerImpl extends BaseControllerImpl<AdministratorService, AdministratorPo> implements AdministratorController {

    @Autowired
    EnterpriseService enterpriseService;

    @Override
    public String preRegister() {
        return "administrator_register";
    }

    @Override
    public String login() {
        return "administrator_login";
    }

    @Override
    public String index() {
        return "administrator_index";
    }

    @Override
    public ModelAndView register(@Valid AdministratorDto administratorDto) {
        ModelAndView modelAndView = new ModelAndView();
        try{
            String email = service.register(administratorDto);
            modelAndView.addObject("message", "我们已向您的邮箱"+email+"发送了一封激活邮件,请点击邮箱中的链接来激活您的账户！");
        }catch (Exception e){
            e.printStackTrace();
            modelAndView.addObject("errorMessage", e.getMessage());
        }
        modelAndView.setViewName("administrator_register");
        return modelAndView;
    }

    @Override
    public ModelAndView register(String email, String code, HttpSession httpSession) {
        ModelAndView modelAndView = new ModelAndView();
        try{
            String userName = service.register(email, code);
            httpSession.setAttribute("userName", userName);
            modelAndView.addObject("message", "您的账户已激活成功！");
        }catch (JobException e){
            e.printStackTrace();
            modelAndView.addObject("errorMessage", e.getMessage());
        }
        modelAndView.setViewName("administrator_login");
        return modelAndView;
    }

    @Override
    public ModelAndView login(LoginAdministratorDto loginAdministratorDto, HttpSession httpSession) {
        ModelAndView modelAndView = new ModelAndView();
        try{
            String userName = service.login(loginAdministratorDto);
            httpSession.setAttribute("userName", userName);
            modelAndView.setViewName("administrator_homepage");
        }catch (Exception e){
            e.printStackTrace();
            modelAndView.addObject("errorMessage", e.getMessage());
            modelAndView.setViewName("administrator_login");
        }
        return modelAndView;
    }

    @Override
    public String logout(HttpSession httpSession) {
        httpSession.invalidate();
        return "administrator_login";
    }

    @Override
    public String findPassword() {
        return "administrator_find_password";
    }

    @Override
    public ModelAndView checkIdentity(String email, HttpSession httpSession) {
        ModelAndView modelAndView = new ModelAndView();
        try {
            service.checkIdentity(email);
            httpSession.setAttribute("emailKey", email);
            modelAndView.setViewName("administrator_check_identity");
        } catch (Exception e) {
            e.printStackTrace();
            modelAndView.addObject("errorMessage", e.getMessage());
            modelAndView.setViewName("administrator_find_password");
        }
        return modelAndView;
    }

    @Override
    public ModelAndView resetPassword(String code, HttpSession httpSession) {
        ModelAndView modelAndView = new ModelAndView();
        try {
            service.resetPassword(code, httpSession);
            modelAndView.setViewName("administrator_reset_password");
        } catch (Exception e) {
            e.printStackTrace();
            modelAndView.addObject("errorMessage", e.getMessage());
            modelAndView.setViewName("administrator_check_identity");
        }
        return modelAndView;
    }

    @Override
    public ModelAndView setSuccess(String password, HttpSession httpSession) {
        ModelAndView modelAndView = new ModelAndView();
        try {
            if(service.setSuccess(password, httpSession)){
                modelAndView.setViewName("administrator_finish");
            }else{
                modelAndView.setViewName("administrator_reset_password");
                modelAndView.addObject("errorMessage", "重置密码失败");
            }
        }catch (Exception e){
            e.printStackTrace();
            modelAndView.setViewName("administrator_reset_password");
            modelAndView.addObject("errorMessage", e.getMessage());
        }
        return modelAndView;
    }

    @Override
    public String checkApplication() {
        return "administrator_check_application";
    }

    @Override
    public PagedResult checkApplication(PageDto pageDto) {
        PagedResult pagedResult = service.checkApplication(pageDto);
        return pagedResult;
    }

    @Override
    public ModelAndView checkApplication(Long enterpriseId, CheckStateType checkStateType) {
        ModelAndView modelAndView = new ModelAndView();
        try{
            service.checkApplication(enterpriseId, checkStateType);
        }catch (Exception e){
            e.printStackTrace();
            modelAndView.addObject("errorMessage", e.getMessage());
        }
        modelAndView.setViewName("administrator_check_application");
        return modelAndView;
    }

    @Override
    public ModelAndView getEnterpriseById(Long enterpriseId) {
        ModelAndView modelAndView = new ModelAndView();
        try{
            modelAndView.addObject("enterpriseVo", enterpriseService.getById(enterpriseId));
        }catch (Exception e){
            e.printStackTrace();
            modelAndView.addObject("errorMessage", e.getMessage());
        }
        modelAndView.setViewName("enterprise_info");
        return modelAndView;
    }

}

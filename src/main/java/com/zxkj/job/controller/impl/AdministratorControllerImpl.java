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
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

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
            AdministratorPo administratorPo = service.register(administratorDto);
            modelAndView.addObject("message", "我们已向您的邮箱"+administratorPo.getEmail()+"发送了一封激活邮件,请点击邮箱中的链接来激活您的账户！");
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
            AdministratorPo administratorPo = service.register(email, code);
            httpSession.setAttribute("administratorPo", administratorPo);
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
            AdministratorPo administratorPo = service.login(loginAdministratorDto);
            httpSession.setAttribute("administratorPo", administratorPo);
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
        httpSession.removeAttribute("administratorPo");
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
    public ModelMap reCheckIdentity(String email, HttpSession httpSession) {
        ModelMap modelMap = new ModelMap();
        try {
            service.checkIdentity(email);
            httpSession.setAttribute("emailKey", email);
            modelMap.addAttribute("message", "验证码重新获取成功");
        } catch (Exception e) {
            e.printStackTrace();
            modelMap.addAttribute("errorMessage", "验证码重新获取失败");
        }
        return modelMap;
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
    public ModelMap checkApplication(Long enterpriseId, CheckStateType checkStateType) {
        ModelMap modelMap = new ModelMap();
        try {
            if (service.checkApplication(enterpriseId, checkStateType)) {
                modelMap.addAttribute("message", "审核成功");
            } else {
                modelMap.addAttribute("errorMessage", "审核失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            modelMap.addAttribute("errorMessage", e.getMessage());
        }
        return modelMap;
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

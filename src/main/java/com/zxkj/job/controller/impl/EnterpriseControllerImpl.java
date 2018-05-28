package com.zxkj.job.controller.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.tools.javac.comp.Enter;
import com.zxkj.job.bean.dto.*;
import com.zxkj.job.bean.po.*;
import com.zxkj.job.bean.vo.*;
import com.zxkj.job.common.BaseControllerImpl;
import com.zxkj.job.common.bean.PagedResult;
import com.zxkj.job.controller.EnterpriseController;
import com.zxkj.job.enums.CheckStateType;
import com.zxkj.job.enums.EducationBackgroundType;
import com.zxkj.job.enums.JobCategoryType;
import com.zxkj.job.enums.StatusType;
import com.zxkj.job.exp.JobException;
import com.zxkj.job.service.*;
import com.zxkj.job.util.BeanUtil;
import com.zxkj.job.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("enterprise")
public class EnterpriseControllerImpl extends BaseControllerImpl<EnterpriseService, EnterprisePo> implements EnterpriseController, HandlerExceptionResolver {

    @Autowired
    CareerTalkService careerTalkService;

    @Autowired
    ProfessionalService professionalService;

    @Autowired
    CampusRecruitmentService campusRecruitmentService;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    DeliveryInformationService deliveryInformationService;

    @Autowired
    UndergraduateService undergraduateService;

    @Autowired
    ResumeService resumeService;

    @Autowired
    EducationBackgroundService educationBackgroundService;

    @Autowired
    FileUtil fileUtil;

    @Autowired
    CareerTalkProfessionalRService careerTalkProfessionalRService;

    @Autowired
    CampusRecruitmentProfessionalRService campusRecruitmentProfessionalRService;

    @Override
    public String preRegister() {
        return "enterprise_register";
    }

    @Override
    public String login() {
        return "enterprise_login";
    }

    @Override
    public String index() {
        return "enterprise_index";
    }

    @Override
    public ModelAndView add(SimpleEnterpriseDto simpleEnterpriseDto) {
        ModelAndView modelAndView = new ModelAndView();
        try {
            String email = service.add(simpleEnterpriseDto);
            modelAndView.addObject("message", "我们已向您的邮箱" + email + "发送了一封激活邮件,请点击邮箱中的链接来激活您的账户！");
            modelAndView.setViewName("enterprise_index");
        } catch (Exception e) {
            e.printStackTrace();
            modelAndView.addObject("errorMessage", e.getMessage());
            modelAndView.setViewName("enterprise_register");
        }
        return modelAndView;
    }

    @Override
    public ModelAndView hrInfoComplete(HttpSession httpSession) {
        ModelAndView modelAndView = new ModelAndView();
        Object userName = httpSession.getAttribute("userName");
        if (StringUtils.isEmpty(userName)) {
            modelAndView.addObject("errorMessage", "用户未登录");
            modelAndView.setViewName("enterprise_login");
            return modelAndView;
        }
        EnterprisePo enterprisePo = service.selectOneByUserName(userName.toString());
        if (enterprisePo == null) {
            modelAndView.addObject("errorMessage", "该企业账户不存在");
            modelAndView.setViewName("enterprise_login");
            return modelAndView;
        }
        if (enterprisePo.getCheckState() == null) {
            modelAndView.setViewName("hr_info_complete");
        } else {
            modelAndView.addObject("errorMessage", "您已完善过企业信息！");
            modelAndView.setViewName("enterprise_index");
        }
        return modelAndView;
    }

    @Override
    public ModelAndView register(String email, String code, HttpSession httpSession) {
        ModelAndView modelAndView = new ModelAndView();
        try {
            String userName = service.register(email, code);
            httpSession.setAttribute("userName", userName);
            modelAndView.addObject("message", "您的账户已激活成功！");
        } catch (JobException e) {
            e.printStackTrace();
            modelAndView.addObject("errorMessage", e.getMessage());
        }
        modelAndView.setViewName("enterprise_index");
        return modelAndView;
    }

    @Override
    public String logout(HttpSession httpSession) {
        httpSession.removeAttribute("enterpriseVo");
        return "enterprise_index";
    }

    @Override
    public String requestPasswordReset() {
        return "request_password_reset";
    }

    @Override
    public ModelAndView login(LoginEnterpriseDto loginEnterpriseDto, HttpSession httpSession) {
        ModelAndView modelAndView = new ModelAndView();
        try {
            EnterprisePo enterprisePo = service.login(loginEnterpriseDto);
            httpSession.setAttribute("enterpriseVo", service.getById(enterprisePo.getId()));
            if (StringUtils.isEmpty(enterprisePo.getCheckState())) {
                modelAndView.setViewName("hr_info_complete");
            } else {
                if (enterprisePo.getCheckState() == CheckStateType.UNCHECKED) {
                    modelAndView.addObject("errorMessage", "您的企业账户还未审核");
                } else if (enterprisePo.getCheckState() == CheckStateType.NO_PASS) {
                    modelAndView.addObject("errorMessage", "您的企业账户未审核通过，请重新完善企业信息！");
                } else {
                    modelAndView.setViewName("enterprise_homepage");
                    return modelAndView;
                }
                modelAndView.setViewName("enterprise_login");
            }
        } catch (Exception e) {
            e.printStackTrace();
            modelAndView.addObject("errorMessage", e.getMessage());
            modelAndView.setViewName("enterprise_login");
        }
        return modelAndView;
    }

    @Override
    public ModelAndView requestPasswordReset(String email) {
        ModelAndView modelAndView = new ModelAndView();
        try {
            service.requestPasswordReset(email);
            modelAndView.addObject("message", "发送成功！请到您的邮箱点击链接找回密码");
            modelAndView.setViewName("enterprise_index");
        } catch (Exception e) {
            e.printStackTrace();
            modelAndView.addObject("errorMessage", e.getMessage());
            modelAndView.setViewName("request_password_reset");
        }
        return modelAndView;
    }

    @Override
    public ModelAndView resetPassword(String email, String code, HttpSession httpSession) {
        ModelAndView modelAndView = new ModelAndView();
        try {
            String userName = service.resetPassword(email, code);
            httpSession.setAttribute("userName", userName);
            modelAndView.setViewName("enterprise_reset_password");
        } catch (Exception e) {
            e.printStackTrace();
            modelAndView.addObject("errorMessage", e.getMessage());
            modelAndView.setViewName("enterprise_index");
        }
        return modelAndView;
    }

    @Override
    public ModelAndView resetPasswordNext(String password, HttpSession httpSession) {
        ModelAndView modelAndView = new ModelAndView();
        try {
            if (service.resetPasswordNext(password, httpSession)) {
                modelAndView.setViewName("enterprise_login");
                modelAndView.addObject("message", "密码找回成功");
            } else {
                modelAndView.setViewName("enterprise_reset_password");
                modelAndView.addObject("errorMessage", "密码找回失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            modelAndView.setViewName("enterprise_reset_password");
            modelAndView.addObject("errorMessage", e.getMessage());
        }
        return modelAndView;
    }

    @Override
    public ModelAndView hrInfoComplete(EnterpriseDto enterpriseDto, HttpSession httpSession) {
        ModelAndView modelAndView = new ModelAndView();
        try {
            if (service.hrInfoComplete(enterpriseDto, httpSession)) {
                modelAndView.setViewName("enterprise_index");
                modelAndView.addObject("message", "审核结果将在三个工作日内，以邮件的形式通知您！");
            } else {
                modelAndView.setViewName("hr_info_complete");
                modelAndView.addObject("errorMessage", "完善企业信息失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            modelAndView.setViewName("hr_info_complete");
            modelAndView.addObject("errorMessage", "完善企业信息失败");
        }
        return modelAndView;
    }

    @Override
    public ModelAndView careerTalkIndex(ModelAndView modelAndView, HttpSession httpSession) {
        EnterpriseVo enterpriseVo = (EnterpriseVo) httpSession.getAttribute("enterpriseVo");
        modelAndView.addObject("professionalVoList", professionalService.list(enterpriseVo.getId()));
        modelAndView.setViewName("enterprise_career_talk_index");
        return modelAndView;
    }

    @Override
    public ModelMap addCareerTalk(CareerTalkDto careerTalkDto, HttpSession httpSession) {
//        logger.error(careerTalkDto.getSchool());
//        logger.error("职位数量：" + careerTalkDto.getProfessionalIds().size());
        ModelMap modelMap = new ModelMap();
        try {
            if (careerTalkService.add(careerTalkDto, httpSession)) {
                modelMap.addAttribute("message", "添加成功");
            } else {
                modelMap.addAttribute("errorMessage", "添加失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            modelMap.addAttribute("errorMessage", e.getMessage());
        }
        return modelMap;
    }

    @Override
    public PagedResult listCareerTalk(PageDto pageDto, HttpSession httpSession) {
        EnterpriseVo enterpriseVo = (EnterpriseVo)httpSession.getAttribute("enterpriseVo");
        PagedResult pagedResult = careerTalkService.list(pageDto, enterpriseVo.getId());
        return pagedResult;
    }

    @Override
    public ModelMap deleteCareerTalk(Long careerTalkId, HttpSession httpSession) {
        ModelMap modelMap = new ModelMap();
        try {
            if (careerTalkService.deleteByCareerTalkId(careerTalkId, httpSession)) {
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

    @Override
    public ModelAndView updateCareerTalk(Long careerTalkId, HttpSession httpSession) {
        ModelAndView modelAndView = new ModelAndView();
        try {
            CareerTalkUpdateVo careerTalkUpdateVo = careerTalkService.selectOneById(careerTalkId, httpSession);
//            modelAndView.addObject("careerTalkUpdateVo", objectMapper.writeValueAsString(careerTalkUpdateVo));
            logger.error(objectMapper.writeValueAsString(careerTalkUpdateVo));
            modelAndView.addObject("careerTalkUpdateVo", careerTalkUpdateVo);
            EnterpriseVo enterpriseVo = (EnterpriseVo)httpSession.getAttribute("enterpriseVo");
            modelAndView.addObject("professionalVoList", professionalService.list(enterpriseVo.getId()));
            modelAndView.setViewName("enterprise_career_talk_update");
        } catch (Exception e) {
            logger.error(e.getMessage());
            modelAndView.addObject("errorMessage", e.getMessage());
            modelAndView.setViewName("enterprise_career_talk_index");
        }
        return modelAndView;
    }

    @Override
    public ModelMap updateCareerTalk(CareerTalkDto careerTalkDto, HttpSession httpSession) {
        ModelMap modelMap = new ModelMap();
        try {
            if (careerTalkService.updateById(careerTalkDto, httpSession)) {
                modelMap.addAttribute("message", "更新成功");
            } else {
                modelMap.addAttribute("errorMessage", "更新失败");
            }
        } catch (Exception e) {
            logger.error("e", e);
            modelMap.addAttribute("errorMessage", e.getMessage());
        }
        return modelMap;
    }

    @Override
    public ModelAndView addProfessional() {
        ModelAndView modelAndView = new ModelAndView("enterprise_professional_add");
        modelAndView.addObject("jobCategoryType", JobCategoryType.values());
        modelAndView.addObject("educationBackgroundType", EducationBackgroundType.values());
        return modelAndView;
    }

    @Override
    public ModelAndView addProfessional(ProfessionalDto professionalDto, HttpSession httpSession) {
        ModelAndView modelAndView = new ModelAndView();
        try {
            if (professionalService.add(professionalDto, httpSession)) {
                modelAndView.addObject("message", "添加成功");
                modelAndView.setViewName("enterprise_professional_index");
                return modelAndView;
            } else {
                modelAndView.addObject("errorMessage", "添加失败");
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            modelAndView.addObject("errorMessage", e.getMessage());
        }
        modelAndView.addObject("jobCategoryType", JobCategoryType.values());
        modelAndView.addObject("educationBackgroundType", EducationBackgroundType.values());
        modelAndView.setViewName("enterprise_professional_add");
        return modelAndView;
    }

    @Override
    public PagedResult listProfessional(PageDto pageDto, HttpSession httpSession) {
        Object enterpriseObj = httpSession.getAttribute("enterpriseVo");
        if (StringUtils.isEmpty(enterpriseObj)) {
//            throw JobException.NOT_LOGGED_IN_EXCEPTION;
            logger.error("您还未登录");
        }
        EnterpriseVo enterpriseVo = (EnterpriseVo) enterpriseObj;
        PagedResult pagedResult = professionalService.list(pageDto, enterpriseVo.getId());
        return pagedResult;
    }

    @Override
    public String enterpriseProfessionalIndex() {
        return "enterprise_professional_index";
    }

    @Override
    public ModelMap deleteProfessional(Long professionalId, HttpSession httpSession) {
        ModelMap modelMap = new ModelMap();
        try {
            if (professionalService.deleteByProfessionalId(professionalId, httpSession)) {
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

    @Override
    public ModelAndView getProfessionalById(Long professionalId, HttpSession httpSession) {
        ModelAndView modelAndView = new ModelAndView();
        try {
            modelAndView.addObject("professionalVo", professionalService.getById(professionalId, httpSession));
        } catch (Exception e) {
            logger.error(e.getMessage());
            modelAndView.addObject("errorMessage", e.getMessage());
        }
        modelAndView.setViewName("enterprise_professional_info");
        return modelAndView;
    }

    @Override
    public ModelMap checkGetProfessionalById(Long professionalId, HttpSession httpSession) {
        ModelMap modelMap = new ModelMap();
        try {
            professionalService.getById(professionalId, httpSession);
        } catch (Exception e) {
            logger.error(e.getMessage());
            modelMap.addAttribute("errorMessage", e.getMessage());
        }
        return modelMap;
    }

    @Override
    public ModelAndView updateProfessional(Long professionalId, HttpSession httpSession) {
        ModelAndView modelAndView = new ModelAndView();
        try {
            ProfessionalUpdateVo professionalUpdateVo = professionalService.getProfessionalUpdateVoById(professionalId, httpSession);
            List<CareerTalkProfessionalRPo> careerTalkProfessionalRPos = careerTalkProfessionalRService.listByProfessionalId(professionalId);
            if(careerTalkProfessionalRPos != null && careerTalkProfessionalRPos.size() > 0){
                throw JobException.PROFESSIONAL_UPDATE_CAREERTALK_EXCEPTION;
            }
            List<CampusRecruitmentProfessionalRPo> campusRecruitmentProfessionalRPos = campusRecruitmentProfessionalRService.listByProfessionalId(professionalId);
            if(campusRecruitmentProfessionalRPos != null && campusRecruitmentProfessionalRPos.size() > 0){
                throw JobException.PROFESSIONAL_UPDATE_CAMPUSRECRUITMENT_EXCEPTION;
            }
            modelAndView.addObject("professionalUpdateVo", professionalUpdateVo);
            modelAndView.addObject("jobCategoryType", JobCategoryType.values());
            modelAndView.addObject("educationBackgroundType", EducationBackgroundType.values());
            modelAndView.setViewName("enterprise_professional_update");
            return modelAndView;
        } catch (Exception e) {
            logger.error(e.getMessage());
            modelAndView.addObject("errorMessage", e.getMessage());
        }
        modelAndView.setViewName("enterprise_professional_index");
        return modelAndView;
    }

    @Override
    public ModelAndView updateProfessional(ProfessionalDto professionalDto, HttpSession httpSession) {
        ModelAndView modelAndView = new ModelAndView();
        try {
            if (professionalService.updateById(professionalDto, httpSession)) {
                modelAndView.addObject("message", "更新成功");
                modelAndView.setViewName("enterprise_professional_index");
                return modelAndView;
            } else {
                modelAndView.addObject("errorMessage", "更新失败");
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            modelAndView.addObject("errorMessage", e.getMessage());
        }
        modelAndView.addObject("professionalUpdateVo", professionalService.getProfessionalUpdateVoById(professionalDto.getId(), httpSession));
        modelAndView.addObject("jobCategoryType", JobCategoryType.values());
        modelAndView.addObject("educationBackgroundType", EducationBackgroundType.values());
        modelAndView.setViewName("enterprise_professional_update");
        return modelAndView;
    }

    @Override
    public ModelAndView addCampusRecruitment(HttpSession httpSession, ModelAndView modelAndView) {
        Object enterpriseObj = httpSession.getAttribute("enterpriseVo");
        EnterpriseVo enterpriseVo = (EnterpriseVo) enterpriseObj;
        modelAndView.setViewName("enterprise_campus_recruitment_add");
        modelAndView.addObject("professionalVoList", professionalService.list(enterpriseVo.getId()));
        return modelAndView;
    }

    @Override
    public ModelAndView addCampusRecruitment(CampusRecruitmentDto campusRecruitmentDto, HttpSession httpSession, ModelAndView modelAndView) {
        try {
            if (campusRecruitmentService.add(campusRecruitmentDto, httpSession)) {
                modelAndView.addObject("message", "添加成功");
                modelAndView.setViewName("enterprise_campus_recruitment_index");
                return modelAndView;
            } else {
                modelAndView.addObject("errorMessage", "添加失败");
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            modelAndView.addObject("errorMessage", e.getMessage());
        }
        return addCampusRecruitment(httpSession, modelAndView);
    }

    @Override
    public PagedResult listCampusRecruitment(PageDto pageDto, HttpSession httpSession) {
        Object enterpriseObj = httpSession.getAttribute("enterpriseVo");
        EnterpriseVo enterpriseVo = (EnterpriseVo) enterpriseObj;
        PagedResult pagedResult = campusRecruitmentService.list(pageDto, enterpriseVo.getId());
        return pagedResult;
    }

    @Override
    public String enterpriseCampusRecruitmentIndex() {
        return "enterprise_campus_recruitment_index";
    }

    @Override
    public ModelMap deleteCampusRecruitment(Long campusRecruitmentId, HttpSession httpSession) {
        ModelMap modelMap = new ModelMap();
        try {
            if (campusRecruitmentService.deleteByCampusRecruitmentId(campusRecruitmentId, httpSession)) {
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

    @Override
    public ModelMap checkGetCampusRecruitmentById(Long campusRecruitmentId, HttpSession httpSession) {
        ModelMap modelMap = new ModelMap();
        try {
            campusRecruitmentService.getById(campusRecruitmentId, httpSession);
        } catch (Exception e) {
            logger.error(e.getMessage());
            modelMap.addAttribute("errorMessage", e.getMessage());
        }
        return modelMap;
    }

    @Override
    public ModelAndView getCampusRecruitmentById(Long campusRecruitmentId, HttpSession httpSession) {
        ModelAndView modelAndView = new ModelAndView();
        try {
            modelAndView.addObject("campusRecruitmentVo", campusRecruitmentService.getById(campusRecruitmentId, httpSession));
        } catch (Exception e) {
            logger.error(e.getMessage());
            modelAndView.addObject("errorMessage", e.getMessage());
        }
        modelAndView.setViewName("enterprise_campus_recruitment_info");
        return modelAndView;
    }

    @Override
    public ResponseEntity<InputStreamResource> downloadGeneralRegulation(String generalRegulationFileName) {
        try {
            return fileUtil.downloadFile(generalRegulationFileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ModelAndView updateCampusRecruitment(Long campusRecruitmentId, HttpSession httpSession) {
        ModelAndView modelAndView = new ModelAndView();
        try {
            campusRecruitmentService.checkUpdateById(campusRecruitmentId);
            EnterpriseVo enterpriseVo = (EnterpriseVo)httpSession.getAttribute("enterpriseVo");
            modelAndView.addObject("professionalVoList", professionalService.list(enterpriseVo.getId()));
            CampusRecruitmentVo campusRecruitmentVo = campusRecruitmentService.getById(campusRecruitmentId, httpSession);
            modelAndView.addObject("campusRecruitmentVo", campusRecruitmentVo);
            modelAndView.setViewName("enterprise_campus_recruitment_update");
            return modelAndView;
        } catch (Exception e) {
            logger.error(e.getMessage());
            modelAndView.addObject("errorMessage", e.getMessage());
        }
        modelAndView.setViewName("enterprise_campus_recruitment_index");
        return modelAndView;
    }

    @Override
    public ModelAndView updateCampusRecruitment(CampusRecruitmentDto campusRecruitmentDto, HttpSession httpSession) {
        ModelAndView modelAndView = new ModelAndView();
        try {
            if (campusRecruitmentService.updateById(campusRecruitmentDto, httpSession)) {
                modelAndView.addObject("message", "更新成功");
                modelAndView.setViewName("enterprise_campus_recruitment_index");
                return modelAndView;
            } else {
                modelAndView.addObject("errorMessage", "更新失败");
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            modelAndView.addObject("errorMessage", e.getMessage());
        }
        EnterpriseVo enterpriseVo = (EnterpriseVo)httpSession.getAttribute("enterpriseVo");
        modelAndView.addObject("professionalVoList", professionalService.list(enterpriseVo.getId()));
        modelAndView.addObject("campusRecruitmentVo", campusRecruitmentService.getById(campusRecruitmentDto.getId(), httpSession));
        modelAndView.setViewName("enterprise_campus_recruitment_update");
        return modelAndView;
    }

    @Override
    public ModelAndView applyIndex(HttpSession httpSession, ModelAndView modelAndView) {
        modelAndView.setViewName("enterprise_apply_index");
        return modelAndView;
    }

    @Override
    public PagedResult listApply(PageDto pageDto, HttpSession httpSession) {
        return deliveryInformationService.listByEnterpriseId(pageDto, httpSession);
    }

    @Override
    public ModelAndView getResumeById(Long resumeId, Long undergraduateId, ModelAndView modelAndView) {
        try{
            UndergraduateVo undergraduateVo = undergraduateService.getByUndergraduateId(undergraduateId);
            modelAndView.addObject("undergraduateVo", undergraduateVo);
            ResumeInfoVo resumeInfoVo = resumeService.getResumeInfoVoById(resumeId, undergraduateVo.getId());
            modelAndView.addObject("resumeInfoVo", resumeInfoVo);
            modelAndView.addObject("educationBackgroundVoList", educationBackgroundService.listByResumeId(resumeId));
            modelAndView.setViewName("enterprise_resume_info");
            return modelAndView;
        }catch (Exception e){
            modelAndView.addObject("errorMessage", e.getMessage());
        }
        modelAndView.setViewName("enterprise_apply_index");
        return modelAndView;
    }

    @Override
    public ModelAndView deliveryInformationUpdate(Long deliveryInformationId, ModelAndView modelAndView) {
        modelAndView.setViewName("enterprise_deliver_information_update");
        modelAndView.addObject("statusTypeList", StatusType.values());
        modelAndView.addObject("deliveryInformationVo", deliveryInformationService.getByDeliveryInformationId(deliveryInformationId));
        return modelAndView;
    }

    @Override
    public ModelMap deliveryInformationUpdate(Long deliveryInformationId, StatusType statusType) {
        ModelMap modelMap = new ModelMap();
        try {
            if (deliveryInformationService.updateStatusTypeById(deliveryInformationId, statusType)) {
                modelMap.addAttribute("message", "更新成功");
            } else {
                modelMap.addAttribute("errorMessage", "更新失败");
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            modelMap.addAttribute("errorMessage", e.getMessage());
        }
//        logger.error(modelMap.get("message").toString());
        return modelMap;
    }

    @Nullable
    @Override
    public ModelAndView resolveException(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, @Nullable Object o, Exception exception) {
        HttpSession httpSession = httpServletRequest.getSession();
        ModelAndView modelAndView = new ModelAndView();
        if (exception instanceof MaxUploadSizeExceededException) {
            modelAndView.addObject("errorMessage", "文件过大, 文件大小需小于1M");
            return addCampusRecruitment(httpSession, modelAndView);
        }
        return null;
    }

    @Override
    public ModelMap checkDeliverResumeUpdate(Long deliveryInformationId) {
        ModelMap modelMap = new ModelMap();
        try{
            DeliveryInformationVo deliveryInformationVo = deliveryInformationService.getByDeliveryInformationId(deliveryInformationId);
            String status = deliveryInformationVo.getStatus();
//            logger.error(status);
            if(status.equals(StatusType.PROCESSED.getName()) || status.equals(StatusType.WRITTEN_EXAMINATION_ALREADY_ARRANGED.getName()) || status.equals(StatusType.INTERVIEW_ALREADY_ARRANGED.getName()) || status.equals(StatusType.OFFER_HAS_BEEN_ISSUED.getName())){
                throw JobException.APPLY_ALREADY_ARRANGED_EXCEPTION;
            }
        }catch (Exception e){
            e.printStackTrace();
            modelMap.addAttribute("errorMessage", e.getMessage());
        }
//        logger.error(modelMap.get("errorMessage").toString());
        return modelMap;
    }

    @Override
    public ModelMap checkUpdateCareerTalk(Long careerTalkId, HttpSession httpSession) {
        ModelMap modelMap = new ModelMap();
        try {
//            logger.error("careerTalkId: " + careerTalkId);
            careerTalkService.selectOneById(careerTalkId, httpSession);
        } catch (Exception e) {
            logger.error(e.getMessage());
            modelMap.addAttribute("errorMessage", e.getMessage());
        }
        return modelMap;
    }

    @Override
    public ModelMap checkGetCareerTalkById(Long careerTalkId, HttpSession httpSession) {
        ModelMap modelMap = new ModelMap();
        try {
            careerTalkService.getByCareerTalkId(careerTalkId);
        } catch (Exception e) {
            logger.error(e.getMessage());
            modelMap.addAttribute("errorMessage", e.getMessage());
        }
        return modelMap;
    }

    @Override
    public ModelAndView getCareerTalkById(Long careerTalkId, HttpSession httpSession, ModelAndView modelAndView) {
        modelAndView.setViewName("enterprise_career_talk_info");
        modelAndView.addObject("careerTalkVo", careerTalkService.getByCareerTalkId(careerTalkId));
        return modelAndView;
    }

    @Override
    public ResponseEntity<InputStreamResource> downloadPreachingText(String preachingTextFileName) {
        try{
            return fileUtil.downloadFile(preachingTextFileName);
        }catch (Exception e){
            logger.error(e.getMessage());
        }
        return null;
    }
}

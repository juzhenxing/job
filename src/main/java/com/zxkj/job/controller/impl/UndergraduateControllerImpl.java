package com.zxkj.job.controller.impl;

import com.zxkj.job.bean.dto.*;
import com.zxkj.job.bean.po.CareerTalkPo;
import com.zxkj.job.bean.po.UndergraduatePo;
import com.zxkj.job.bean.vo.DeliveryInformationVo;
import com.zxkj.job.bean.vo.ResumeInfoVo;
import com.zxkj.job.bean.vo.ResumeVo;
import com.zxkj.job.common.BaseControllerImpl;
import com.zxkj.job.common.bean.PagedResult;
import com.zxkj.job.controller.UndergraduateController;
import com.zxkj.job.enums.*;
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

    @Autowired
    EducationBackgroundService educationBackgroundService;

    @Autowired
    DeliveryInformationService deliveryInformationService;

    @Autowired
    CollectService collectService;

    @Override
    public String logout(HttpSession httpSession) {
        httpSession.removeAttribute("undergraduatePo");
        return "index";
    }

    @Override
    public ModelAndView personalCenter(HttpSession httpSession, ModelAndView modelAndView) {
        UndergraduatePo undergraduatePo = (UndergraduatePo)httpSession.getAttribute("undergraduatePo");
        try{
            modelAndView.addObject("undergraduateVo", service.getByEmail(undergraduatePo.getEmail()));
        }catch (Exception e){
            modelAndView.addObject("errorMessage", e.getMessage());
        }
        modelAndView.setViewName("undergraduate_personal_center");
        return modelAndView;
    }

    @Override
    public ModelAndView infoUpdate(HttpSession httpSession) {
        ModelAndView modelAndView = new ModelAndView();
        try{
            UndergraduatePo undergraduatePo = (UndergraduatePo)httpSession.getAttribute("undergraduatePo");
            modelAndView.addObject("undergraduateVo", service.getByEmail(undergraduatePo.getEmail()));
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
        UndergraduatePo undergraduatePo = (UndergraduatePo)httpSession.getAttribute("undergraduatePo");
        try {
            if (service.infoUpdate(undergraduateDto, httpSession)) {
                modelAndView.addObject("message", "更新成功");
                modelAndView.addObject("undergraduateVo", service.getByEmail(undergraduatePo.getEmail()));
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
        modelAndView.addObject("undergraduateVo", service.getByEmail(undergraduatePo.getEmail()));
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
        UndergraduatePo undergraduatePo = (UndergraduatePo)httpSession.getAttribute("undergraduatePo");
        try {
            if (resumeService.add(resumeDto, httpSession)) {
                modelAndView.addObject("message", "添加成功");
                modelAndView.addObject("undergraduateVo", service.getByEmail(undergraduatePo.getEmail()));
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
        return resumeService.list(pageDto, httpSession);
    }

    @Override
    public ModelMap deleteResume(Long resumeId, HttpSession httpSession) {
        ModelMap modelMap = new ModelMap();
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

    @Override
    public ModelAndView updateResume(Long resumeId, HttpSession httpSession, ModelAndView modelAndView) {
        UndergraduatePo undergraduatePo = (UndergraduatePo)httpSession.getAttribute("undergraduatePo");
        modelAndView.addObject("undergraduateVo", service.getByEmail(undergraduatePo.getEmail()));
        try {
            ResumeVo resumeVo = resumeService.selectOneById(resumeId, httpSession);
            modelAndView.addObject("resumeVo", resumeVo);
            modelAndView.setViewName("undergraduate_resume_update");
            return modelAndView;
        } catch (Exception e) {
            logger.error(e.getMessage());
            modelAndView.addObject("errorMessage", e.getMessage());
        }
        modelAndView.setViewName("undergraduate_personal_center");
        return modelAndView;
    }

    @Override
    public ModelAndView resumeEducationBackgroundAdd(Long resumeId) {
        ModelAndView modelAndView = new ModelAndView("undergraduate_resume_education_background_add");
        modelAndView.addObject("educationBackgroundType", EducationBackgroundType.values());
        modelAndView.addObject("rankType", RankType.values());
        modelAndView.addObject("resumeId", resumeId);
        return modelAndView;
    }

    @Override
    public ModelAndView resumeEducationBackgroundAdd(EducationBackgroundDto educationBackgroundDto, HttpSession httpSession) {
        ModelAndView modelAndView = new ModelAndView();
        UndergraduatePo undergraduatePo = (UndergraduatePo)httpSession.getAttribute("undergraduatePo");
        try {
            if (educationBackgroundService.add(educationBackgroundDto)) {
                modelAndView.addObject("message", "添加成功");
                ResumeVo resumeVo = resumeService.selectOneById(educationBackgroundDto.getResumeId(), httpSession);
                modelAndView.addObject("resumeVo", resumeVo);
                modelAndView.addObject("undergraduateVo", service.getByEmail(undergraduatePo.getEmail()));
                modelAndView.setViewName("undergraduate_resume_update");
                return modelAndView;
            } else {
                modelAndView.addObject("errorMessage", "添加失败");
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            modelAndView.addObject("errorMessage", e.getMessage());
        }
        modelAndView.addObject("educationBackgroundType", EducationBackgroundType.values());
        modelAndView.addObject("rankType", RankType.values());
        modelAndView.addObject("resumeId", educationBackgroundDto.getResumeId());
        modelAndView.setViewName("undergraduate_resume_education_background_add");
        return modelAndView;
    }

    @Override
    public PagedResult listResumeEducationBackground(PageDto pageDto, Long resumeId) {
        return educationBackgroundService.list(pageDto, resumeId);
    }

    @Override
    public ModelMap deleteResumeEducationBackground(Long educationBackgroundId) {
        ModelMap modelMap = new ModelMap();
        try {
            if (educationBackgroundService.deleteByEducationBackgroundId(educationBackgroundId)) {
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
    public ModelAndView updateResumeEducationBackground(Long educationBackgroundId) {
        ModelAndView modelAndView = new ModelAndView();
        try {
            modelAndView.addObject("educationBackgroundVo", educationBackgroundService.selectOneById(educationBackgroundId));
            modelAndView.addObject("educationBackgroundType", EducationBackgroundType.values());
            modelAndView.addObject("rankType", RankType.values());
            modelAndView.setViewName("undergraduate_resume_education_background_update");
            return modelAndView;
        }catch (Exception e){
            logger.error(e.getMessage());
            modelAndView.addObject("errorMessage", e.getMessage());
        }
        modelAndView.setViewName("undergraduate_resume_update");
        return modelAndView;
    }

    @Override
    public ModelAndView updateResumeEducationBackground(EducationBackgroundDto educationBackgroundDto, HttpSession httpSession) {
        ModelAndView modelAndView = new ModelAndView();
        try {
            if (educationBackgroundService.updateById(educationBackgroundDto)) {
                modelAndView.addObject("message", "更新成功");
                return updateResume(educationBackgroundDto.getResumeId(), httpSession, modelAndView);
            } else {
                modelAndView.addObject("errorMessage", "更新失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.debug("e: ", e);
            modelAndView.addObject("errorMessage", e.getMessage());
        }
        return updateResumeEducationBackground(educationBackgroundDto.getId());
    }

    @Override
    public ModelAndView deliverResume(Long professionalId, Long careerTalkOrCampusRecruitmentId, HttpSession httpSession, ModelAndView modelAndView) {
        modelAndView.addObject("professionalId", professionalId);
        CareerTalkPo careerTalkPo = careerTalkService.selectById(careerTalkOrCampusRecruitmentId);
        if(careerTalkPo != null){
            modelAndView.addObject("professionalVoList", careerTalkService.getByCareerTalkId(careerTalkOrCampusRecruitmentId).getProfessionalVoList());
        }else{
            modelAndView.addObject("professionalVoList", campusRecruitmentService.getById(careerTalkOrCampusRecruitmentId).getProfessionalVoList());
        }
        UndergraduatePo undergraduatePo = (UndergraduatePo)httpSession.getAttribute("undergraduatePo");
        modelAndView.addObject("resumeVoList", resumeService.list(undergraduatePo.getId(), undergraduatePo.getAcquiescenceResumeId()));
        modelAndView.addObject("careerTalkOrCampusRecruitmentId", careerTalkOrCampusRecruitmentId);
        modelAndView.setViewName("undergraduate_deliver_resume");
        return modelAndView;
    }

    @Override
    public ModelMap deliverResume(DeliveryInformationDto deliveryInformationDto, HttpSession httpSession) {
        ModelMap modelMap = new ModelMap();
        try{
            if(deliveryInformationService.add(deliveryInformationDto, httpSession)){
                modelMap.addAttribute("message", "投递成功");
                return modelMap;
            }else{
                modelMap.addAttribute("errorMessage", "投递失败");
            }
        }catch (Exception e){
            modelMap.addAttribute("errorMessage", e.getMessage());
        }
        return modelMap;
    }

    @Override
    public ModelAndView resumeDeliverIndex(ModelAndView modelAndView) {
        modelAndView.setViewName("undergraduate_resume_deliver_index");
        return modelAndView;
    }

    @Override
    public PagedResult listResumeDeliver(PageDto pageDto, HttpSession httpSession) {
        return deliveryInformationService.list(pageDto, httpSession);
    }

    @Override
    public ModelAndView deliverResumeUpdate(Long deliveryInformationId, ModelAndView modelAndView, HttpSession httpSession) {
        try{
            UndergraduatePo undergraduatePo = (UndergraduatePo)httpSession.getAttribute("undergraduatePo");
            modelAndView.addObject("resumeVoList", resumeService.list(undergraduatePo.getId(), undergraduatePo.getAcquiescenceResumeId()));
            modelAndView.addObject("deliveryInformationVo", deliveryInformationService.getByDeliveryInformationId(deliveryInformationId));
            modelAndView.setViewName("undergraduate_deliver_resume_update");
        }catch (Exception e){
            e.printStackTrace();
            logger.debug("e: ", e);
            modelAndView.addObject("errorMessage", e.getMessage());
        }
        return modelAndView;
    }

    @Override
    public ModelMap deliverResumeUpdate(@Valid DeliveryInformationDto deliveryInformationDto) {
        ModelMap modelMap = new ModelMap();
        try {
            if (deliveryInformationService.updateById(deliveryInformationDto)) {
                modelMap.addAttribute("message", "更新成功");
            } else {
                modelMap.addAttribute("errorMessage", "更新失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.debug("e: ", e);
            modelMap.addAttribute("errorMessage", e.getMessage());
        }
        return modelMap;
    }

    @Override
    public ModelAndView getResumeById(Long resumeId, ModelAndView modelAndView, HttpSession httpSession) {
        try{
            UndergraduatePo undergraduatePo = (UndergraduatePo)httpSession.getAttribute("undergraduatePo");
            modelAndView.addObject("undergraduateVo", service.getByEmail(undergraduatePo.getEmail()));
            ResumeInfoVo resumeInfoVo = resumeService.getResumeInfoVoById(resumeId, undergraduatePo.getId());
            modelAndView.addObject("resumeInfoVo", resumeInfoVo);
            modelAndView.addObject("educationBackgroundVoList", educationBackgroundService.listByResumeId(resumeId));
            modelAndView.setViewName("undergraduate_resume_info");
            return modelAndView;
        }catch (Exception e){
            modelAndView.addObject("errorMessage", e.getMessage());
        }
        modelAndView.setViewName("undergraduate_personal_center");
        return modelAndView;
    }

    @Override
    public ModelAndView resumeBasicUpdate(Long resumeId, ModelAndView modelAndView, HttpSession httpSession) {
        try{
            modelAndView.setViewName("undergraduate_resume_basic_update");
            modelAndView.addObject("resumeVo", resumeService.selectOneById(resumeId, httpSession));
            modelAndView.addObject("politicsStatusType", PoliticsStatusType.values());
            return modelAndView;
        }catch (Exception e){
            modelAndView.addObject("errorMessage", e.getMessage());
        }
        return updateResume(resumeId, httpSession, modelAndView);
    }

    @Override
    public ModelAndView resumeBasicUpdate(@Valid ResumeDto resumeDto, HttpSession httpSession, ModelAndView modelAndView) {
        try {
            if(resumeService.updateResumeBasicById(resumeDto, httpSession)){
                modelAndView.addObject("message", "更新成功");
                return updateResume(resumeDto.getId(), httpSession, modelAndView);
            }else{
                modelAndView.addObject("errorMessage", "更新失败");
            }
        }catch (Exception e){
            e.printStackTrace();
            modelAndView.addObject("errorMessage", e.getMessage());
        }
        return updateResume(resumeDto.getId(), httpSession, modelAndView);
    }

    @Override
    public ModelMap checkDeliverResumeUpdate(Long deliveryInformationId) {
        ModelMap modelMap = new ModelMap();
        try{
            DeliveryInformationVo deliveryInformationVo = deliveryInformationService.getByDeliveryInformationId(deliveryInformationId);
            if(deliveryInformationVo.getStatus() != StatusType.POSTED.getName()){
                throw JobException.RESUME_ALREADY_ARRANGED_EXCEPTION;
            }
        }catch (Exception e){
            e.printStackTrace();
            modelMap.addAttribute("errorMessage", e.getMessage());
        }
        return modelMap;
    }

    @Override
    public ModelMap addCollect(CollectDto collectDto, HttpSession httpSession) {
        ModelMap modelMap = new ModelMap();
        try {
            if (collectService.add(collectDto, httpSession)) {
                modelMap.addAttribute("message", "收藏成功");
            } else {
                modelMap.addAttribute("errorMessage", "收藏失败");
            }
        } catch (Exception e) {
            logger.debug("e: ", e);
            modelMap.addAttribute("errorMessage", e.getMessage());
        }
        return modelMap;
    }

    @Override
    public ModelAndView collectIndex(ModelAndView modelAndView) {
        modelAndView.setViewName("undergraduate_collect_index");
        return modelAndView;
    }

    @Override
    public PagedResult listCollect(PageDto pageDto, HttpSession httpSession) {
        return collectService.list(pageDto, httpSession);
    }

    @Override
    public ModelMap deleteCollect(Long collectId) {
        ModelMap modelMap = new ModelMap();
        try {
            if (collectService.deleteByCollectId(collectId)) {
                modelMap.addAttribute("message", "取消成功");
            } else {
                modelMap.addAttribute("errorMessage", "取消失败");
            }
        } catch (Exception e) {
            logger.debug("e: ", e);
            modelMap.addAttribute("errorMessage", e.getMessage());
        }
        return modelMap;
    }
}

package com.zxkj.job.controller;

import com.zxkj.job.bean.dto.*;
import com.zxkj.job.common.bean.PagedResult;
import com.zxkj.job.enums.CollectType;
import com.zxkj.job.enums.StatusType;
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

    @GetMapping("logout")
    String logout(HttpSession httpSession);

    @GetMapping("personal-center")
    ModelAndView personalCenter(HttpSession httpSession, ModelAndView modelAndView);

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

    @GetMapping("update-resume")
    ModelAndView updateResume(Long resumeId, HttpSession httpSession, ModelAndView modelAndView);

    @GetMapping("resume-education-background-add")
    ModelAndView resumeEducationBackgroundAdd(Long resumeId);

    @PostMapping("resume-education-background-add")
    ModelAndView resumeEducationBackgroundAdd(EducationBackgroundDto educationBackgroundDto, HttpSession httpSession);

    @GetMapping("list-resume-education-background")
    @ResponseBody
    PagedResult listResumeEducationBackground(PageDto pageDto, Long resumeId);

    @PostMapping("delete-resume-education-background")
    @ResponseBody
    ModelMap deleteResumeEducationBackground(Long educationBackgroundId);

    @GetMapping("update-resume-education-background")
    ModelAndView updateResumeEducationBackground(Long educationBackgroundId);

    @PostMapping("update-resume-education-background")
    ModelAndView updateResumeEducationBackground(EducationBackgroundDto educationBackgroundDto, HttpSession httpSession);

    @GetMapping("deliver-resume")
    ModelAndView deliverResume(Long professionalId, Long careerTalkOrCampusRecruitmentId, HttpSession httpSession, ModelAndView modelAndView);

    @PostMapping("deliver-resume")
    @ResponseBody
    ModelMap deliverResume(@Valid DeliveryInformationDto deliveryInformationDto, HttpSession httpSession);

    @GetMapping("resume-deliver-index")
    ModelAndView resumeDeliverIndex(ModelAndView modelAndView);

    @GetMapping("list-resume-deliver")
    @ResponseBody
    PagedResult listResumeDeliver(PageDto pageDto, HttpSession httpSession);

    @GetMapping("deliver-resume-update")
    ModelAndView deliverResumeUpdate(Long deliveryInformationId, ModelAndView modelAndView, HttpSession httpSession);

    @PostMapping("deliver-resume-update")
    @ResponseBody
    ModelMap deliverResumeUpdate(@Valid DeliveryInformationDto deliveryInformationDto);

    @GetMapping("get-resume-by-id")
    ModelAndView getResumeById(Long resumeId, ModelAndView modelAndView, HttpSession httpSession);

    @GetMapping("resume-basic-update")
    ModelAndView resumeBasicUpdate(Long resumeId, ModelAndView modelAndView, HttpSession httpSession);

    @PostMapping("resume-basic-update")
    ModelAndView resumeBasicUpdate(@Valid ResumeDto resumeDto, HttpSession httpSession, ModelAndView modelAndView);

    @GetMapping("check-deliver-resume-update")
    @ResponseBody
    ModelMap checkDeliverResumeUpdate(Long deliveryInformationId);

    @PostMapping("add-collect")
    @ResponseBody
    ModelMap addCollect(@Valid CollectDto collectDto, HttpSession httpSession);

    @GetMapping("collect-index")
    ModelAndView collectIndex(ModelAndView modelAndView);

    @GetMapping("list-collect")
    @ResponseBody
    PagedResult listCollect(PageDto pageDto, HttpSession httpSession);

    @PostMapping("delete-collect")
    @ResponseBody
    ModelMap deleteCollect(Long collectId);

}

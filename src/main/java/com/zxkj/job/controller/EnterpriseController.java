package com.zxkj.job.controller;

import com.zxkj.job.bean.dto.*;
import com.zxkj.job.common.bean.PagedResult;
import com.zxkj.job.enums.StatusType;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public interface EnterpriseController {

    @GetMapping("pre-register")
    String preRegister();

    @GetMapping("login")
    String login();

    @GetMapping("index")
    String index();

    /**
     * 注册
     *
     * @param simpleEnterpriseDto
     * @return
     */
    @PostMapping("register")
    ModelAndView add(@Valid SimpleEnterpriseDto simpleEnterpriseDto);

    @GetMapping("hr-info-complete")
    ModelAndView hrInfoComplete(HttpSession httpSession);

    @GetMapping("register")
    ModelAndView register(
            @NotBlank(message = "email不能为空") @Email(message = "email格式不正确")String email,
            @NotBlank(message = "验证码不能为空") String code,
            HttpSession httpSession);

    @GetMapping("logout")
    String logout(HttpSession httpSession);

    @GetMapping("request-password-reset")
    String requestPasswordReset();

    @PostMapping("login")
    ModelAndView login(
            @Valid LoginEnterpriseDto loginEnterpriseDto,
            HttpSession httpSession);

    @PostMapping("request-password-reset")
    ModelAndView requestPasswordReset(@NotBlank(message = "email不能为空") @Email(message = "email格式不正确")String email);

    @GetMapping("reset-password")
    ModelAndView resetPassword(
            @NotBlank(message = "email不能为空") @Email(message = "email格式不正确")String email,
            @NotBlank(message = "验证码不能为空") String code,
            HttpSession httpSession);

    @PostMapping("reset-password-next")
    ModelAndView resetPasswordNext(
            @NotBlank(message = "密码不能为空")String password,
            HttpSession httpSession);

    @PostMapping("hr-info-complete")
    ModelAndView hrInfoComplete(
            @Valid EnterpriseDto enterpriseDto,
            HttpSession httpSession);

    @GetMapping("career-talk-index")
    ModelAndView careerTalkIndex(ModelAndView modelAndView, HttpSession httpSession);

    @PostMapping("add-career-talk")
    @ResponseBody
    ModelMap addCareerTalk(CareerTalkDto careerTalkDto, HttpSession httpSession);

    @GetMapping("list-career-talk")
    @ResponseBody
    PagedResult listCareerTalk(PageDto pageDto, HttpSession httpSession);

    @PostMapping("delete-career-talk")
    @ResponseBody
    ModelMap deleteCareerTalk(Long careerTalkId, HttpSession httpSession);

    @GetMapping("check-update-career-talk")
    @ResponseBody
    ModelMap checkUpdateCareerTalk(Long careerTalkId, HttpSession httpSession);

    @GetMapping("update-career-talk")
//    @ResponseBody
    ModelAndView updateCareerTalk(Long careerTalkId, HttpSession httpSession);

    @PostMapping("update-career-talk")
    ModelAndView updateCareerTalk(CareerTalkDto careerTalkDto, HttpSession httpSession);

    @GetMapping("add-professional")
    ModelAndView addProfessional();

    @PostMapping("add-professional")
    ModelAndView addProfessional(ProfessionalDto professionalDto, HttpSession httpSession);

    @GetMapping("list-professional")
    @ResponseBody
    PagedResult listProfessional(PageDto pageDto, HttpSession httpSession);

    @GetMapping("enterprise-professional-index")
    String enterpriseProfessionalIndex();

    @PostMapping("delete-professional")
    @ResponseBody
    ModelMap deleteProfessional(Long professionalId, HttpSession httpSession);

    @GetMapping("get-professional-by-id")
    ModelAndView getProfessionalById(Long professionalId, HttpSession httpSession);

    @GetMapping("check-get-professional-by-id")
    @ResponseBody
    ModelMap checkGetProfessionalById(Long professionalId, HttpSession httpSession);

    @GetMapping("update-professional")
    ModelAndView updateProfessional(Long professionalId, HttpSession httpSession);

    @PostMapping("update-professional")
    ModelAndView updateProfessional(ProfessionalDto professionalDto, HttpSession httpSession);

    @GetMapping("add-campus-recruitment")
    ModelAndView addCampusRecruitment(HttpSession httpSession, ModelAndView modelAndView);

    @PostMapping("add-campus-recruitment")
    ModelAndView addCampusRecruitment(CampusRecruitmentDto campusRecruitmentDto, HttpSession httpSession, ModelAndView modelAndView);

    @GetMapping("list-campus-recruitment")
    @ResponseBody
    PagedResult listCampusRecruitment(PageDto pageDto, HttpSession httpSession);

    @GetMapping("enterprise-campus-recruitment-index")
    String enterpriseCampusRecruitmentIndex();

    @PostMapping("delete-campus-recruitment")
    @ResponseBody
    ModelMap deleteCampusRecruitment(Long campusRecruitmentId, HttpSession httpSession);

    @GetMapping("check-get-campus-recruitment-by-id")
    @ResponseBody
    ModelMap checkGetCampusRecruitmentById(Long campusRecruitmentId, HttpSession httpSession);

    @GetMapping("get-campus-recruitment-by-id")
    ModelAndView getCampusRecruitmentById(Long campusRecruitmentId, HttpSession httpSession);

    @GetMapping("download-general-regulation")
    ResponseEntity<InputStreamResource> downloadGeneralRegulation(String generalRegulationFileName);

    @GetMapping("update-campus-recruitment")
    @ResponseBody
    ModelAndView updateCampusRecruitment(Long campusRecruitmentId, HttpSession httpSession);

    @PostMapping("update-campus-recruitment")
    ModelAndView updateCampusRecruitment(CampusRecruitmentDto campusRecruitmentDto, HttpSession httpSession);

    @GetMapping("apply-index")
    ModelAndView applyIndex(HttpSession httpSession, ModelAndView modelAndView);

    @GetMapping("list-apply")
    @ResponseBody
    PagedResult listApply(PageDto pageDto, HttpSession httpSession);

    @GetMapping("get-resume-by-id")
    ModelAndView getResumeById(Long resumeId, Long undergraduateId, ModelAndView modelAndView);

    @GetMapping("deliver-information-update")
    ModelAndView deliveryInformationUpdate(Long deliveryInformationId, ModelAndView modelAndView);

    @PostMapping("deliver-information-update")
    @ResponseBody
    ModelMap deliveryInformationUpdate(Long deliveryInformationId, StatusType statusType);

    @GetMapping("check-deliver-resume-update")
    @ResponseBody
    ModelMap checkDeliverResumeUpdate(Long deliveryInformationId);
}

package com.zxkj.job.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zxkj.job.bean.dto.CareerTalkDto;
import com.zxkj.job.service.CareerTalkService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("enterprise2")
public class TestController {

    @Autowired
    CareerTalkService careerTalkService;

//    @Autowired
//    ObjectMapper objectMapper;

    private Logger logger = LoggerFactory.getLogger(getClass());

    @PostMapping("delete-career-talk")
    @ResponseBody
    public ModelMap deleteCareerTalk(Long careerTalkId, Model model, HttpSession httpSession) throws JsonProcessingException {
        ModelMap modelMap = new ModelMap();
        try{
            logger.error("deleteCareerTalk----------------------");
            if(careerTalkService.deleteByCareerTalkId(careerTalkId, httpSession)){
                modelMap.addAttribute("message", "删除成功");
            }else{
                modelMap.addAttribute("errorMessage", "删除失败");
            }
        }catch (Exception e){
            logger.error(e.getMessage());
            modelMap.addAttribute("errorMessage", e.getMessage());
        }
//        return objectMapper.writeValueAsString(modelMap);
        return modelMap;
    }

    @PostMapping("test-add-career-talk")
    public ModelAndView addCareerTalk(CareerTalkDto careerTalkDto, HttpSession httpSession) {
        logger.error(careerTalkDto.getSchool());
        ModelAndView modelAndView = new ModelAndView("enterprise_career_talk_index");
        try {
            if(careerTalkService.add(careerTalkDto, httpSession)){
                modelAndView.addObject("message", "添加成功");
            }else{
                modelAndView.addObject("errorMessage", "添加失败");
            }
        }catch (Exception e){
            e.printStackTrace();
            modelAndView.addObject("errorMessage", e.getMessage());
        }
        return modelAndView;
    }

}

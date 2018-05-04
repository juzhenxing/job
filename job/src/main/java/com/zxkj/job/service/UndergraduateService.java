package com.zxkj.job.service;

import com.baomidou.mybatisplus.service.IService;
import com.zxkj.job.bean.dto.SimpleUndergraduateDto;
import com.zxkj.job.bean.po.UndergraduatePo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.mail.MessagingException;
import javax.servlet.http.HttpSession;

public interface UndergraduateService extends IService<UndergraduatePo> {

    /**
     * 注册
     *
     * @param simpleUndergraduateDto
     * @return
     */
    String add(SimpleUndergraduateDto simpleUndergraduateDto);

    /**
     * 学生登录
     * @param simpleUndergraduateDto
     * @return
     */
    Boolean login(SimpleUndergraduateDto simpleUndergraduateDto, HttpSession httpSession);

    /**
     * 学生验证身份
     * @param email
     * @return
     * @throws MessagingException
     */
    String checkIdentity(String email) throws MessagingException;

    Boolean resetPassword(String code, HttpSession httpSession);

    Boolean setSuccess(String password, HttpSession httpSession);

}

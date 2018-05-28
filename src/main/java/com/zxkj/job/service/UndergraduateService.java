package com.zxkj.job.service;

import com.baomidou.mybatisplus.service.IService;
import com.zxkj.job.bean.dto.SimpleUndergraduateDto;
import com.zxkj.job.bean.dto.UndergraduateDto;
import com.zxkj.job.bean.po.UndergraduatePo;
import com.zxkj.job.bean.vo.UndergraduateVo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.mail.MessagingException;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.ParseException;

public interface UndergraduateService extends IService<UndergraduatePo> {

    /**
     * 注册
     *
     * @param simpleUndergraduateDto
     * @return
     */
    String add(SimpleUndergraduateDto simpleUndergraduateDto, HttpSession httpSession);

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

    UndergraduateVo getByEmail(String email);

    Boolean infoUpdate(UndergraduateDto undergraduateDto, HttpSession httpSession) throws IOException, ParseException;

    UndergraduateVo getByUndergraduateId(Long undergraduateId);

    void registerAcquireCode(String email) throws MessagingException;

}

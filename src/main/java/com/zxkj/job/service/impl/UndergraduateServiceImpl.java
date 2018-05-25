package com.zxkj.job.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.zxkj.job.bean.dto.SimpleUndergraduateDto;
import com.zxkj.job.bean.dto.UndergraduateDto;
import com.zxkj.job.bean.po.UndergraduatePo;
import com.zxkj.job.bean.vo.UndergraduateVo;
import com.zxkj.job.common.BaseServiceImpl;
import com.zxkj.job.exp.JobException;
import com.zxkj.job.mapper.UndergraduateMapper;
import com.zxkj.job.service.UndergraduateService;
import com.zxkj.job.util.*;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.ParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@Service
@Transactional
public class UndergraduateServiceImpl extends BaseServiceImpl<UndergraduateMapper, UndergraduatePo> implements UndergraduateService {

    final String EMAIL = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";

    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    @Autowired
    FileUtil fileUtil;

    @Override
    public String add(SimpleUndergraduateDto simpleUndergraduateDto) {
        checkEmail(simpleUndergraduateDto.getEmail(), null);
        UndergraduatePo undergraduatePo = new UndergraduatePo();
        BeanUtil.copyProperties(simpleUndergraduateDto, undergraduatePo);
        Boolean result = super.insert(undergraduatePo);
        if (result) {
            return undergraduatePo.getEmail();
        } else {
            throw JobException.UNDERGRADUATE_ADD_EXCEPTION;
        }
    }

    @Override
    public Boolean login(SimpleUndergraduateDto simpleUndergraduateDto, HttpSession httpSession) {
        UndergraduatePo undergraduatePo = super.baseMapper.selectOneByEmail(simpleUndergraduateDto.getEmail());
        if (undergraduatePo == null) {
            throw JobException.NULL_UNDERGRADUATE_EXCEPTION;
        } else if (!undergraduatePo.getPassword().equals(simpleUndergraduateDto.getPassword())) {
            throw JobException.WRONG_PASSWORD_EXCEPTION;
        } else {
            httpSession.setAttribute("undergraduatePo", undergraduatePo);
        }
        return true;
    }

    @Override
    public String checkIdentity(String email) throws MessagingException {
        checkEmailMatcher(email);
        UndergraduatePo undergraduatePo = super.baseMapper.selectOneByEmail(email);
        if (undergraduatePo == null) {
            throw JobException.NULL_UNDERGRADUATE_EXCEPTION;
        }else{
            String code= RandomStringUtils.randomAlphanumeric(10);
            redisTemplate.opsForValue().set(undergraduatePo.getEmail(), code);
            EmailUtil.sendEmail(email, "验证身份", "验证码为：" + code);
            return code;
        }
    }

    @Override
    public Boolean resetPassword(String code, HttpSession httpSession) {
        String emailKey = checkEmailKey(httpSession);
        if(!redisTemplate.hasKey(emailKey)){
            throw JobException.NULL_CODE_EXCEPTION;
        }
        String correctCode = redisTemplate.opsForValue().get(emailKey).toString();
        if(!correctCode.equalsIgnoreCase(code)){
            throw JobException.WRONG_CODE_EXCEPTION;
        }
        return true;
    }

    @Override
    public Boolean setSuccess(String password, HttpSession httpSession) {
        String emailKey = checkEmailKey(httpSession);
        UndergraduatePo undergraduatePo = super.baseMapper.selectOneByEmail(emailKey);
        undergraduatePo.setPassword(password);
        httpSession.removeAttribute("emailKey");
        return super.updateById(undergraduatePo);
    }

    @Override
    public UndergraduateVo getByEmail(String email) {
        checkEmailMatcher(email);
        EntityWrapper entityWrapper = new EntityWrapper();
        entityWrapper.eq("email", email);
        UndergraduatePo undergraduatePo = super.selectOne(entityWrapper);
        UndergraduateVo undergraduateVo = new UndergraduateVo();
        BeanUtil.copyProperties(undergraduatePo, undergraduateVo);
        return undergraduateVo;
    }

    @Override
    public Boolean infoUpdate(UndergraduateDto undergraduateDto, HttpSession httpSession) throws IOException, ParseException {
       String email = httpSession.getAttribute("email").toString();
       EntityWrapper entityWrapper = new EntityWrapper();
       entityWrapper.eq("email", email);
       UndergraduatePo undergraduatePo = super.selectOne(entityWrapper);
       if(undergraduatePo == null){
           throw JobException.NULL_UNDERGRADUATE_EXCEPTION;
       }
       BeanUtil.copyProperties(undergraduateDto, undergraduatePo);
       String headUrl = fileUtil.saveFile(undergraduateDto.getHeadFile());
       undergraduatePo.setHeadUrl(headUrl);
       undergraduatePo.setGraduateYear(DateUtil.formatDate(undergraduateDto.getGraduateYear(), "yyyy-MM"));
       return super.updateById(undergraduatePo);
    }

    @Override
    public UndergraduateVo getByUndergraduateId(Long undergraduateId) {
        if(StringUtils.isEmpty(undergraduateId)){
            throw JobException.NULL_ID_EXCEPTION;
        }
        UndergraduatePo undergraduatePo = super.selectById(undergraduateId);
        UndergraduateVo undergraduateVo = new UndergraduateVo();
        BeanUtil.copyProperties(undergraduatePo, undergraduateVo);
        return undergraduateVo;
    }

    public void checkId(Long id){
        if(StringUtils.isEmpty(id)){
            throw JobException.NULL_ID_EXCEPTION;
        }
    }

    public String checkEmailKey(HttpSession httpSession){
        Object emailKey = httpSession.getAttribute("emailKey");
        if(emailKey == null){
            throw JobException.NULL_EMAILKEY_EXCEPTION;
        }
        return emailKey.toString();
    }

    public void checkEmail(String email, Long undergraduateId) throws JobException {
        checkEmailMatcher(email);
        UndergraduatePo undergraduatePo = super.baseMapper.selectOneByEmail(email);
        if (StringUtils.isEmpty(undergraduateId)) {
            if (undergraduatePo != null) {
                throw JobException.REPEAT_EMAIL_EXCEPTION;
            }
        } else {
            if (undergraduatePo != null && (!undergraduateId.equals(undergraduatePo.getId()))) {
                throw JobException.REPEAT_EMAIL_EXCEPTION;
            }
        }
    }

    public void checkEmailMatcher(String email){
        if (StringUtils.isEmpty(email)) {
            throw JobException.NULL_EMAIL_EXCEPTION;
        }
        Pattern emailPattern = Pattern.compile(EMAIL);
        Matcher emailMatcher = emailPattern.matcher(email);
        if (!emailMatcher.matches()) {
            throw JobException.WRONG_EMAIL_EXCEPTION;
        }
    }

}

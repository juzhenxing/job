package com.zxkj.job.service.impl;

import com.zxkj.job.bean.dto.*;
import com.zxkj.job.bean.po.AdministratorPo;
import com.zxkj.job.bean.po.EnterprisePo;
import com.zxkj.job.bean.vo.EnterpriseVo;
import com.zxkj.job.common.BaseServiceImpl;
import com.zxkj.job.common.bean.PagedResult;
import com.zxkj.job.enums.CheckStateType;
import com.zxkj.job.exp.JobException;
import com.zxkj.job.mapper.AdministratorMapper;
import com.zxkj.job.service.AdministratorService;
import com.zxkj.job.service.EnterpriseService;
import com.zxkj.job.util.BeanUtil;
import com.zxkj.job.util.EmailUtil;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.mail.MessagingException;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Transactional
public class AdministratorServiceImpl extends BaseServiceImpl<AdministratorMapper, AdministratorPo> implements AdministratorService {

    final String EMAIL = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";

    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    @Autowired
    EnterpriseService enterpriseService;

    @Override
    public String register(AdministratorDto administratorDto) throws MessagingException {
        checkUserName(administratorDto.getUserName());
        checkEmail(administratorDto.getEmail(), null);
        AdministratorPo administratorPo = new AdministratorPo();
        BeanUtil.copyProperties(administratorDto, administratorPo);
        administratorPo.setActivate(false);
        Boolean result = super.insert(administratorPo);
        if (result) {
            String code= RandomStringUtils.randomAlphanumeric(10);
            redisTemplate.opsForValue().set(administratorPo.getEmail(), code);
            EmailUtil.sendEmail(administratorDto.getEmail(), "[大学生求职网]管理员注册", "验证链接：http://localhost:8080/administrator/register?email=" + administratorPo.getEmail()
                    + "&code=" + code);
            return administratorPo.getEmail();
        } else {
            throw JobException.ADMINISTRATOR_ADD_EXCEPTION;
        }
    }

    @Override
    public String register(String email, String code) {
        checkEmailMatcher(email);
        AdministratorPo administratorPo = super.baseMapper.selectOneByEmail(email);
        if(administratorPo == null){
            throw JobException.NULL_ADMINISTRATOR_EXCEPTION;
        }
        if(administratorPo.getActivate()){
            throw JobException.USED_CODE_EXCEPTION;
        }
        if(!redisTemplate.hasKey(email)){
            throw JobException.NULL_CODE_EXCEPTION;
        }
        String correctCode = redisTemplate.opsForValue().get(email).toString();
        if(!correctCode.equals(code)){
            throw JobException.WRONG_CODE_EXCEPTION;
        }
        redisTemplate.delete(email);
        administratorPo.setActivate(true);
        administratorPo.setCheckState(CheckStateType.UNCHECKED);
        if(super.updateById(administratorPo)){
            return administratorPo.getUserName();
        }else{
            throw JobException.ADMINISTRATOR_UPDATE_EXCEPTION;
        }
    }

    @Override
    public String login(LoginAdministratorDto loginAdministratorDto) {
        AdministratorPo administratorPo = super.baseMapper.selectOneByUsernameOrEmail(loginAdministratorDto);
        if(administratorPo == null){
            throw JobException.NULL_ADMINISTRATOR_EXCEPTION;
        }else{
            if(!administratorPo.getPassword().equals(loginAdministratorDto.getPassword())){
                throw JobException.WRONG_PASSWORD_EXCEPTION;
            }
        }
        return administratorPo.getUserName();
    }

    @Override
    public String checkIdentity(String email) throws MessagingException {
        checkEmailMatcher(email);
        AdministratorPo administratorPo = super.baseMapper.selectOneByEmail(email);
        if (administratorPo == null) {
            throw JobException.NULL_ADMINISTRATOR_EXCEPTION;
        }else{
            String code= RandomStringUtils.randomAlphanumeric(10);
            redisTemplate.opsForValue().set(administratorPo.getEmail(), code);
            EmailUtil.sendEmail(email, "[大学生求职网]验证身份", "验证码为：" + code);
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
        AdministratorPo administratorPo = super.baseMapper.selectOneByEmail(emailKey);
        administratorPo.setPassword(password);
        httpSession.removeAttribute("emailKey");
        return super.updateById(administratorPo);
    }

    @Override
    public PagedResult checkApplication(PageDto pageDto) {
        Integer startLine = (pageDto.getPage() - 1) * pageDto.getLimit();
        List<EnterpriseVo> enterpriseVoList = enterpriseService.selectPageByQueryEnterpriseDto(startLine, pageDto.getLimit());
        PagedResult<EnterpriseVo> pagedResult = new PagedResult<>();
        pagedResult.setData(enterpriseVoList);
        pagedResult.setCount(enterpriseService.selectCountByQueryEnterpriseDto());
        return pagedResult;
    }

    @Override
    public Boolean checkApplication(Long enterpriseId, CheckStateType checkStateType) throws MessagingException {
        EnterprisePo enterprisePo = enterpriseService.selectById(enterpriseId);
        if(enterprisePo == null){
            throw JobException.NULL_ENTERPRISE_EXCEPTION;
        }
        enterprisePo.setCheckState(checkStateType);
        if(!enterpriseService.updateById(enterprisePo)){
            throw JobException.ENTERPRISE_UPDATE_EXCEPTION;
        }
        if(checkStateType == CheckStateType.PASS){
            EmailUtil.sendEmail(enterprisePo.getEmail(), "[大学生求职网]审核结果", "恭喜您， 您的企业账号审核通过！");
        }else if(checkStateType == CheckStateType.NO_PASS){
            EmailUtil.sendEmail(enterprisePo.getEmail(), "[大学生求职网]审核结果", "抱歉， 您的企业账号审核未通过，请修改后重试！");
        }
        return true;
    }

    private String checkEmailKey(HttpSession httpSession){
        Object emailKey = httpSession.getAttribute("emailKey");
        if(emailKey == null){
            throw JobException.NULL_EMAILKEY_EXCEPTION;
        }
        return emailKey.toString();
    }

    private void checkUserName(String userName){
        if(StringUtils.isEmpty(userName)){
            throw JobException.NULL_USERNAME_EXCEPTION;
        }
        AdministratorPo administratorPo = super.baseMapper.selectOneByUserName(userName);
        if(administratorPo != null){
            throw JobException.REPEAT_USERNAME_EXCEPTION;
        }
    }

    private void checkEmail(String email, Long administratorId) throws JobException {
        checkEmailMatcher(email);
        AdministratorPo administratorPo = super.baseMapper.selectOneByEmail(email);
        if (StringUtils.isEmpty(administratorId)) {
            if (administratorPo != null) {
                throw JobException.REPEAT_EMAIL_EXCEPTION;
            }
        } else {
            if (administratorPo != null && (!administratorId.equals(administratorPo.getId()))) {
                throw JobException.REPEAT_EMAIL_EXCEPTION;
            }
        }
    }

    private void checkEmailMatcher(String email){
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

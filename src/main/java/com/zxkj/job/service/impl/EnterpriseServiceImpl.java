package com.zxkj.job.service.impl;

import com.zxkj.job.bean.dto.*;
import com.zxkj.job.bean.po.CareerTalkPo;
import com.zxkj.job.bean.po.EnterprisePo;
import com.zxkj.job.bean.vo.EnterpriseVo;
import com.zxkj.job.common.BaseServiceImpl;
import com.zxkj.job.enums.CheckStateType;
import com.zxkj.job.exp.JobException;
import com.zxkj.job.mapper.EnterpriseMapper;
import com.zxkj.job.service.EnterpriseService;
import com.zxkj.job.util.BeanUtil;
import com.zxkj.job.util.EmailUtil;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@Transactional
public class EnterpriseServiceImpl extends BaseServiceImpl<EnterpriseMapper, EnterprisePo> implements EnterpriseService {

    private Logger logger = LoggerFactory.getLogger(getClass());

    final String EMAIL = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";

    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    @Value("${job.tmp.path}")
    private String tmpPath;

    @Override
    public String add(SimpleEnterpriseDto simpleEnterpriseDto) throws MessagingException {
        checkUserName(simpleEnterpriseDto.getUserName());
        checkEmail(simpleEnterpriseDto.getEmail(), null);
        EnterprisePo enterprisePo = new EnterprisePo();
        BeanUtil.copyProperties(simpleEnterpriseDto, enterprisePo);
        enterprisePo.setActivate(false);
        Boolean result = super.insert(enterprisePo);
        if (result) {
            String code= RandomStringUtils.randomAlphanumeric(10);
            redisTemplate.opsForValue().set(enterprisePo.getEmail(), code);
            EmailUtil.sendEmail(simpleEnterpriseDto.getEmail(), "[大学生求职网]企业注册", "验证链接：http://localhost:8080/enterprise/register?email=" + enterprisePo.getEmail()
                    + "&code=" + code);
            return enterprisePo.getEmail();
        } else {
            throw JobException.ENTERPRISE_ADD_EXCEPTION;
        }
    }

    @Override
    public String register(String email, String code) {
        checkEmailMatcher(email);
        EnterprisePo enterprisePo = super.baseMapper.selectOneByEmail(email);
//        logger.error(enterprisePo.toString());
        if(enterprisePo == null){
            throw JobException.NULL_ENTERPRISE_EXCEPTION;
        }
        if(enterprisePo.getActivate()){
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
        enterprisePo.setActivate(true);
        if(super.updateById(enterprisePo)){
            return enterprisePo.getUserName();
        }else{
            throw JobException.ENTERPRISE_UPDATE_EXCEPTION;
        }
    }

    @Override
    public EnterprisePo login(LoginEnterpriseDto loginEnterpriseDto) {
        EnterprisePo enterprisePo = super.baseMapper.selectOneByUsernameOrEmail(loginEnterpriseDto.getUsernameOrEmail());
        if(enterprisePo == null){
            throw JobException.NULL_ENTERPRISE_EXCEPTION;
        }else{
            if(!enterprisePo.getPassword().equals(loginEnterpriseDto.getPassword())){
                throw JobException.WRONG_PASSWORD_EXCEPTION;
            }
            if(enterprisePo.getActivate() == false){
                throw JobException.ENTERPRISE_ACTIVATE_EXCEPTION;
            }
        }
        return enterprisePo;
    }

    @Override
    public Boolean requestPasswordReset(String email) throws MessagingException {
        checkEmailMatcher(email);
        EnterprisePo enterprisePo = super.baseMapper.selectOneByEmail(email);
        if(enterprisePo == null){
            throw JobException.NULL_ENTERPRISE_EXCEPTION;
        }
        String code= RandomStringUtils.randomAlphanumeric(10);
        redisTemplate.opsForValue().set(enterprisePo.getEmail(), code);
        EmailUtil.sendEmail(email,
                "[大学生求职网]密码找回",
                "重置密码链接：http://localhost:8080/enterprise/reset-password?email=" + enterprisePo.getEmail()
                        + "&code=" + code);
        return true;
    }

    @Override
    public String resetPassword(String email, String code) {
        checkEmailMatcher(email);
        EnterprisePo enterprisePo = super.baseMapper.selectOneByEmail(email);
//        logger.error(enterprisePo.toString());
        if(enterprisePo == null){
            throw JobException.NULL_ENTERPRISE_EXCEPTION;
        }
        if(!redisTemplate.hasKey(email)){
            throw JobException.NULL_CODE_EXCEPTION;
        }
        String correctCode = redisTemplate.opsForValue().get(email).toString();
        if(!correctCode.equals(code)){
            throw JobException.WRONG_CODE_EXCEPTION;
        }
        redisTemplate.delete(email);
        return enterprisePo.getUserName();
    }

    @Override
    public Boolean resetPasswordNext(String password, HttpSession httpSession) {
        String userName = getUserName(httpSession);
        EnterprisePo enterprisePo = checkEnterprisePoByUserName(userName);
        enterprisePo.setPassword(password);
        return super.updateById(enterprisePo);
    }

    public EnterprisePo checkEnterprisePoByUserName(String userName){
        EnterprisePo enterprisePo = super.baseMapper.selectOneByUserName(userName);
        if(enterprisePo == null){
            throw JobException.NULL_ENTERPRISE_EXCEPTION;
        }
        return enterprisePo;
    }

    public String getUserName(HttpSession httpSession){
        Object userName = httpSession.getAttribute("userName");
        if(StringUtils.isEmpty(userName)){
            throw JobException.NULL_USERNAME_EXCEPTION;
        }
        return userName.toString();
    }

    @Override
    public EnterprisePo selectOneByUserName(String userName) {
        return super.baseMapper.selectOneByUserName(userName);
    }

    @Override
    public Boolean hrInfoComplete(EnterpriseDto enterpriseDto, HttpSession httpSession) throws IOException {
        String userName = getUserName(httpSession);
        EnterprisePo enterprisePo = checkEnterprisePoByUserName(userName);
        BeanUtil.copyProperties(enterpriseDto, enterprisePo);
        MultipartFile license = enterpriseDto.getLicense();
        String fileName = saveFile(license);
        enterprisePo.setLicenseFileName(fileName);
        enterprisePo.setCheckState(CheckStateType.UNCHECKED);
        return super.updateById(enterprisePo);
    }

    @Override
    public List<EnterpriseVo> selectPageByQueryEnterpriseDto(Integer startLine, Integer pageSize) {
        List<EnterprisePo> enterprisePoList = super.baseMapper.selectPageByQueryEnterpriseDto(startLine, pageSize);
        List<EnterpriseVo> enterpriseVoList = enterprisePoList.parallelStream().map(e -> enterprisePoToVo((EnterprisePo)e)).collect(Collectors.toList());
        return enterpriseVoList;
    }

    @Override
    public Integer selectCountByQueryEnterpriseDto() {
        return super.baseMapper.selectCountByQueryEnterpriseDto();
    }

    @Override
    public EnterpriseVo getById(Long enterpriseId) {
        EnterprisePo e = super.baseMapper.selectById(enterpriseId);
        if(e == null){
            throw JobException.NULL_ENTERPRISE_EXCEPTION;
        }
        return enterprisePoToVo(e);
    }

    @Override
    public EnterpriseVo getByUsernameOrEmail(String usernameOrEmail) {
        EnterprisePo enterprisePo = super.baseMapper.selectOneByUsernameOrEmail(usernameOrEmail);
        return enterprisePoToVo(enterprisePo);
    }

    public EnterpriseVo enterprisePoToVo(EnterprisePo enterprisePo){
        EnterpriseVo enterpriseVo = new EnterpriseVo();
        BeanUtil.copyProperties(enterprisePo, enterpriseVo);
        enterpriseVo.setLicense(enterprisePo.getLicenseFileName());
        enterpriseVo.setNature(enterprisePo.getNature().getName());
        enterpriseVo.setIndustryInvolved(enterprisePo.getIndustryInvolved().getName());
        enterpriseVo.setWorkerNumber(enterprisePo.getWorkerNumber().getName());
        enterpriseVo.setProvince(enterprisePo.getProvince().getName());
        enterpriseVo.setCheckStateType(enterprisePo.getCheckState().getName());
        return enterpriseVo;
    }

    public String saveFile(MultipartFile multipartFile) throws IOException {
        String originalFilename = multipartFile.getOriginalFilename();
        int pointIndex = originalFilename.lastIndexOf(".");
        String fileType = originalFilename.substring(pointIndex + 1);
        String fileName = originalFilename.substring(0, pointIndex) + "_" + System.currentTimeMillis() + "." + fileType;
        File file = new File(tmpPath + fileName);
        //判断目标文件所在的目录是否存在
        if (!file.getParentFile().exists()) {
            //如果目标文件所在的目录不存在，则创建父目录
            if (!file.getParentFile().mkdirs()) {
                logger.info("创建临时保存文件的目录失败");
                throw JobException.LICENSE_TEMP_DIRECTORY_EXCEPTION;
            } else {
                multipartFile.transferTo(file);
            }
        } else {
            multipartFile.transferTo(file);
        }
        return fileName;
    }

    public void checkUserName(String userName){
        if(StringUtils.isEmpty(userName)){
            throw JobException.NULL_USERNAME_EXCEPTION;
        }
        EnterprisePo enterprisePo = super.baseMapper.selectOneByUserName(userName);
        if(enterprisePo != null){
            throw JobException.REPEAT_USERNAME_EXCEPTION;
        }
    }

    public void checkEmail(String email, Long enterpriseId) throws JobException {
        checkEmailMatcher(email);
        EnterprisePo enterprisePo = super.baseMapper.selectOneByEmail(email);
        if (StringUtils.isEmpty(enterpriseId)) {
            if (enterprisePo != null) {
                throw JobException.REPEAT_EMAIL_EXCEPTION;
            }
        } else {
            if (enterprisePo != null && (!enterpriseId.equals(enterprisePo.getId()))) {
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

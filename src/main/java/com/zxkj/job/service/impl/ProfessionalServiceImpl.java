package com.zxkj.job.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.zxkj.job.bean.dto.*;
import com.zxkj.job.bean.po.CampusRecruitmentProfessionalRPo;
import com.zxkj.job.bean.po.EnterprisePo;
import com.zxkj.job.bean.po.ProfessionalPo;
import com.zxkj.job.bean.vo.EnterpriseVo;
import com.zxkj.job.bean.vo.ProfessionalUpdateVo;
import com.zxkj.job.bean.vo.ProfessionalVo;
import com.zxkj.job.common.BaseServiceImpl;
import com.zxkj.job.common.bean.PagedResult;
import com.zxkj.job.enums.CheckStateType;
import com.zxkj.job.exp.JobException;
import com.zxkj.job.mapper.EnterpriseMapper;
import com.zxkj.job.mapper.ProfessionalMapper;
import com.zxkj.job.service.CampusRecruitmentProfessionalRService;
import com.zxkj.job.service.EnterpriseService;
import com.zxkj.job.service.ProfessionalService;
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
public class ProfessionalServiceImpl extends BaseServiceImpl<ProfessionalMapper, ProfessionalPo> implements ProfessionalService {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    CampusRecruitmentProfessionalRService campusRecruitmentProfessionalRService;

    @Override
    public Boolean add(ProfessionalDto professionalDto, HttpSession httpSession) {
        EnterpriseVo enterpriseVo = checkEnterpriseVo(httpSession);
        ProfessionalPo professionalPo = new ProfessionalPo();
        BeanUtil.copyProperties(professionalDto, professionalPo);
        checkId(enterpriseVo.getId());
        professionalPo.setEnterpriseId(enterpriseVo.getId());
        return super.insert(professionalPo);
    }

    @Override
    public PagedResult list(PageDto pageDto, Long enterpriseId) {
        Integer startLine = (pageDto.getPage() - 1) * pageDto.getLimit();
        List<ProfessionalPo> professionalPoList = super.baseMapper.selectPageByEnterpriseId(enterpriseId, startLine, pageDto.getLimit());
        List<ProfessionalVo> professionalVoList = professionalPoList.parallelStream().map(e -> professionalPoToVo(e)).collect(Collectors.toList());
        PagedResult<ProfessionalVo> pagedResult = new PagedResult<>();
        pagedResult.setData(professionalVoList);
        pagedResult.setCount(super.baseMapper.selectCountByEnterpriseId(enterpriseId));
        return pagedResult;
    }

    @Override
    public Boolean deleteByProfessionalId(Long professionalId, HttpSession httpSession) {
        EnterpriseVo enterpriseVo = checkEnterpriseVo(httpSession);
        checkProfessionalPo(professionalId, enterpriseVo.getId());
        return super.deleteById(professionalId);
    }

    @Override
    public ProfessionalVo getById(Long professionalId, HttpSession httpSession) {
        EnterpriseVo enterpriseVo = checkEnterpriseVo(httpSession);
        ProfessionalPo professionalPo = checkProfessionalPo(professionalId, enterpriseVo.getId());
        return professionalPoToVo(professionalPo);
    }

    @Override
    public ProfessionalUpdateVo getProfessionalUpdateVoById(Long professionalId, HttpSession httpSession) {
        EnterpriseVo enterpriseVo = checkEnterpriseVo(httpSession);
        ProfessionalPo professionalPo = checkProfessionalPo(professionalId, enterpriseVo.getId());
        ProfessionalUpdateVo professionalUpdateVo = new ProfessionalUpdateVo();
        BeanUtil.copyProperties(professionalPo, professionalUpdateVo);
        return professionalUpdateVo;
    }

    @Override
    public Boolean updateById(ProfessionalDto professionalDto, HttpSession httpSession) {
        EnterpriseVo enterpriseVo = checkEnterpriseVo(httpSession);
        ProfessionalPo professionalPo = checkProfessionalPo(professionalDto.getId(), enterpriseVo.getId());
        BeanUtil.copyProperties(professionalDto, professionalPo);
        return super.updateById(professionalPo);
    }

    @Override
    public PagedResult listProfessionalByCampusRecruitmentId(PageDto pageDto, Long campusRecruitmentId) {
        Page page = new Page(pageDto.getPage(), pageDto.getLimit());
//        logger.error("起始页：" + pageDto.getPage() + ", 页面大小：" + pageDto.getLimit());
        Wrapper wrapper = new EntityWrapper();
        wrapper.eq("campus_recruitment_id", campusRecruitmentId);
        Page campusRecruitmentProfessionalRPoPage = campusRecruitmentProfessionalRService.selectPage(page, wrapper);
        List<CampusRecruitmentProfessionalRPo> campusRecruitmentProfessionalRPos = campusRecruitmentProfessionalRPoPage.getRecords();
         List<Long> professionalIds = campusRecruitmentProfessionalRPos.parallelStream().map(e -> e.getProfessionalId()).collect(Collectors.toList());
        List<ProfessionalPo> professionalPos = super.selectBatchIds(professionalIds);
        List<ProfessionalVo> professionalVos = professionalPos.parallelStream().map(e -> professionalPoToVo(e)).collect(Collectors.toList());
        PagedResult pagedResult = new PagedResult();
        pagedResult.setData(professionalVos);
        pagedResult.setCount(campusRecruitmentProfessionalRPoPage.getTotal());
        return pagedResult;
    }

    @Override
    public List<ProfessionalVo> list(Long enterpriseId) {
        List<ProfessionalPo> professionalPoList = super.baseMapper.selectByEnterpriseId(enterpriseId);
        return professionalPoList.parallelStream().map(e -> professionalPoToVo(e)).collect(Collectors.toList());
    }

    @Override
    public List<ProfessionalVo> listByProfessionalIds(List<Long> professionalIds) {
        List<ProfessionalPo> professionalPos = super.selectBatchIds(professionalIds);
        return professionalPos.parallelStream().map(e -> professionalPoToVo(e)).collect(Collectors.toList());
    }

    @Override
    public ProfessionalVo getById(Long professionalId) {
        ProfessionalPo professionalPo = super.selectById(professionalId);
        return professionalPoToVo(professionalPo);
    }

    private ProfessionalVo professionalPoToVo(ProfessionalPo professionalPo){
        ProfessionalVo professionalVo = new ProfessionalVo();
        BeanUtil.copyProperties(professionalPo, professionalVo);
        professionalVo.setEducationBackground(professionalPo.getEducationBackground().getName());
        professionalVo.setType(professionalPo.getType().getName());
        return professionalVo;
    }

    private ProfessionalPo checkProfessionalPo(Long professionalId, Long enterpriseId){
        ProfessionalPo professionalPo = super.baseMapper.selectOneById(professionalId, enterpriseId);
        if(professionalPo == null){
            throw JobException.NULL_PROFESSIONAL_EXCEPTION;
        }
        return professionalPo;
    }

    private void checkId(Long id) {
        if (StringUtils.isEmpty(id)) {
            throw JobException.NULL_ENTERPRISE_ID_EXCEPTION;
        }
    }

    private EnterpriseVo checkEnterpriseVo(HttpSession httpSession) {
        Object enterpriseObj = httpSession.getAttribute("enterpriseVo");
        if (StringUtils.isEmpty(enterpriseObj)) {
            throw JobException.NOT_LOGGED_IN_EXCEPTION;
        }
        return (EnterpriseVo) enterpriseObj;
    }
}

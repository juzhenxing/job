package com.zxkj.job.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.zxkj.job.bean.dto.DeliveryInformationDto;
import com.zxkj.job.bean.dto.PageDto;
import com.zxkj.job.bean.dto.ProfessionalDto;
import com.zxkj.job.bean.po.CampusRecruitmentProfessionalRPo;
import com.zxkj.job.bean.po.DeliveryInformationPo;
import com.zxkj.job.bean.po.ProfessionalPo;
import com.zxkj.job.bean.po.UndergraduatePo;
import com.zxkj.job.bean.vo.*;
import com.zxkj.job.common.BaseServiceImpl;
import com.zxkj.job.common.bean.PagedResult;
import com.zxkj.job.enums.StatusType;
import com.zxkj.job.exp.JobException;
import com.zxkj.job.mapper.DeliveryInformationMapper;
import com.zxkj.job.mapper.ProfessionalMapper;
import com.zxkj.job.service.*;
import com.zxkj.job.util.BeanUtil;
import com.zxkj.job.util.EmailUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.mail.MessagingException;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class DeliveryInformationImpl extends BaseServiceImpl<DeliveryInformationMapper, DeliveryInformationPo> implements DeliveryInformationService {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    ProfessionalService professionalService;

    @Autowired
    EnterpriseService enterpriseService;

    @Autowired
    UndergraduateService undergraduateService;

    @Override
    public Boolean add(DeliveryInformationDto deliveryInformationDto, HttpSession httpSession) {
        DeliveryInformationPo deliveryInformationPo = new DeliveryInformationPo();
        BeanUtil.copyProperties(deliveryInformationDto, deliveryInformationPo);
        UndergraduatePo undergraduatePo = (UndergraduatePo) httpSession.getAttribute("undergraduatePo");
        deliveryInformationPo.setUndergraduateId(undergraduatePo.getId());
        deliveryInformationPo.setEnterpriseId(professionalService.getById(deliveryInformationDto.getProfessionalId()).getEnterpriseId());
        deliveryInformationPo.setTime(new Date());
        deliveryInformationPo.setStatus(StatusType.POSTED);
        return super.insert(deliveryInformationPo);
    }

    @Override
    public PagedResult list(PageDto pageDto, HttpSession httpSession) {
        UndergraduatePo undergraduatePo = (UndergraduatePo) httpSession.getAttribute("undergraduatePo");
        EntityWrapper entityWrapper = new EntityWrapper();
        entityWrapper.eq("undergraduate_id", undergraduatePo.getId());
        Page page = new Page(pageDto.getPage(), pageDto.getLimit());
        Page page1 = selectPage(page, entityWrapper);
        List<DeliveryInformationPo> deliveryInformationPoList = page1.getRecords();
        List<DeliveryInformationVo> deliveryInformationVoList = deliveryInformationPoList.parallelStream().map(e -> deliveryInformationPoToVo((DeliveryInformationPo) e)).collect(Collectors.toList());
        PagedResult pagedResult = new PagedResult();
        pagedResult.setData(deliveryInformationVoList);
        pagedResult.setCount(page1.getTotal());
        return pagedResult;
    }

    @Override
    public DeliveryInformationVo getByDeliveryInformationId(Long deliveryInformationId) {
        DeliveryInformationPo deliveryInformationPo = checkDeliveryInformationPo(deliveryInformationId);
        return deliveryInformationPoToVo(deliveryInformationPo);
    }

    public DeliveryInformationPo checkDeliveryInformationPo(Long deliveryInformationId){
        DeliveryInformationPo deliveryInformationPo = super.selectById(deliveryInformationId);
        if(deliveryInformationPo == null){
            throw JobException.NULL_DELIVERYINFORMATIONPO_EXCEPTION;
        }
        return deliveryInformationPo;
    }

    @Override
    public Boolean updateById(DeliveryInformationDto deliveryInformationDto) {
        DeliveryInformationPo deliveryInformationPo = checkDeliveryInformationPo(deliveryInformationDto.getId());
        BeanUtil.copyProperties(deliveryInformationDto, deliveryInformationPo);
        return super.updateById(deliveryInformationPo);
    }

    @Override
    public List<DeliveryInformationVo> getByResumeId(Long resumeId) {
        EntityWrapper entityWrapper = new EntityWrapper();
        entityWrapper.eq("resume_id", resumeId);
        List<DeliveryInformationPo> deliveryInformationPoList = super.selectList(entityWrapper);
        return deliveryInformationPoList.parallelStream().map(e -> deliveryInformationPoToVo((DeliveryInformationPo) e)).collect(Collectors.toList());
    }

    @Override
    public PagedResult listByEnterpriseId(PageDto pageDto, HttpSession httpSession) {
        EnterpriseVo enterpriseVo = (EnterpriseVo) httpSession.getAttribute("enterpriseVo");
        EntityWrapper entityWrapper = new EntityWrapper();
        entityWrapper.eq("enterprise_id", enterpriseVo.getId());
        Page page = new Page(pageDto.getPage(), pageDto.getLimit());
        Page page1 = selectPage(page, entityWrapper);
        List<DeliveryInformationPo> deliveryInformationPoList = page1.getRecords();
        List<DeliveryInformationVo> deliveryInformationVoList = deliveryInformationPoList.parallelStream().map(e -> deliveryInformationPoToVo((DeliveryInformationPo) e)).collect(Collectors.toList());
        PagedResult pagedResult = new PagedResult();
        pagedResult.setData(deliveryInformationVoList);
        pagedResult.setCount(page1.getTotal());
        return pagedResult;
    }

    @Override
    public Boolean updateStatusTypeById(Long deliveryInformationId, StatusType statusType) throws MessagingException {
        DeliveryInformationPo deliveryInformationPo = checkDeliveryInformationPo(deliveryInformationId);
        deliveryInformationPo.setStatus(statusType);
        if(!super.updateById(deliveryInformationPo)){
            throw JobException.DELIVERYINFORMATIONPO_UPDATE_EXCEPTION;
        }
        UndergraduateVo undergraduateVo = undergraduateService.getByUndergraduateId(deliveryInformationPo.getUndergraduateId());
        DeliveryInformationVo deliveryInformationVo = getByDeliveryInformationId(deliveryInformationId);
        EmailUtil.sendEmail(undergraduateVo.getEmail(), "简历投递更新通知", "亲爱的" + undergraduateVo.getEmail() + ", 您好. 您投递的" + deliveryInformationVo.getEnterpriseName() + "的" + deliveryInformationVo.getProfessionalName() + "岗位的投递状态已更新, 请注意查收");
        return true;
    }

    @Override
    public List<DeliveryInformationVo> getByProfessionalId(Long professionalId) {
        EntityWrapper entityWrapper = new EntityWrapper();
        entityWrapper.eq("professional_id", professionalId);
        List<DeliveryInformationPo> deliveryInformationPoList = super.selectList(entityWrapper);
        return deliveryInformationPoList.parallelStream().map(e -> deliveryInformationPoToVo((DeliveryInformationPo) e)).collect(Collectors.toList());
    }

    @Override
    public List<DeliveryInformationVo> getByCareerTalkOrCampusRecruitmentId(Long careerTalkOrCampusRecruitmentId) {
        EntityWrapper entityWrapper = new EntityWrapper();
        entityWrapper.eq("career_talk_or_campus_recruitment_id", careerTalkOrCampusRecruitmentId);
        List<DeliveryInformationPo> deliveryInformationPoList = super.selectList(entityWrapper);
        return deliveryInformationPoList.parallelStream().map(e -> deliveryInformationPoToVo((DeliveryInformationPo) e)).collect(Collectors.toList());


    }

    public DeliveryInformationVo deliveryInformationPoToVo(DeliveryInformationPo deliveryInformationPo){
        DeliveryInformationVo deliveryInformationVo = new DeliveryInformationVo();
        BeanUtil.copyProperties(deliveryInformationPo, deliveryInformationVo);
        deliveryInformationVo.setDeliveryTime(deliveryInformationPo.getGmtCreate());
        deliveryInformationVo.setStatus(deliveryInformationPo.getStatus().getName());
        deliveryInformationVo.setEnterpriseName(enterpriseService.getById(deliveryInformationPo.getEnterpriseId()).getFullname());
        deliveryInformationVo.setProfessionalName(professionalService.getById(deliveryInformationPo.getProfessionalId()).getName());
        deliveryInformationVo.setUndergraduateName(undergraduateService.selectById(deliveryInformationPo.getUndergraduateId()).getName());
        return deliveryInformationVo;
    }
}

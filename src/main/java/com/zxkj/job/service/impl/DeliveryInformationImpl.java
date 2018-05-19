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
import com.zxkj.job.bean.vo.DeliveryInformationVo;
import com.zxkj.job.bean.vo.EnterpriseVo;
import com.zxkj.job.bean.vo.ProfessionalUpdateVo;
import com.zxkj.job.bean.vo.ProfessionalVo;
import com.zxkj.job.common.BaseServiceImpl;
import com.zxkj.job.common.bean.PagedResult;
import com.zxkj.job.enums.StatusType;
import com.zxkj.job.exp.JobException;
import com.zxkj.job.mapper.DeliveryInformationMapper;
import com.zxkj.job.mapper.ProfessionalMapper;
import com.zxkj.job.service.CampusRecruitmentProfessionalRService;
import com.zxkj.job.service.DeliveryInformationService;
import com.zxkj.job.service.EnterpriseService;
import com.zxkj.job.service.ProfessionalService;
import com.zxkj.job.util.BeanUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

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
        if(deliveryInformationPo.getStatus() != StatusType.POSTED){
            throw JobException.RESUME_ALREADY_ARRANGED_EXCEPTION;
        }
        return deliveryInformationPoToVo(deliveryInformationPo);
    }

    private DeliveryInformationPo checkDeliveryInformationPo(Long deliveryInformationId){
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

    private DeliveryInformationVo deliveryInformationPoToVo(DeliveryInformationPo deliveryInformationPo){
        DeliveryInformationVo deliveryInformationVo = new DeliveryInformationVo();
        BeanUtil.copyProperties(deliveryInformationPo, deliveryInformationVo);
        deliveryInformationVo.setDeliveryTime(deliveryInformationPo.getGmtCreate());
        deliveryInformationVo.setStatus(deliveryInformationPo.getStatus().getName());
        deliveryInformationVo.setEnterpriseName(enterpriseService.getById(deliveryInformationPo.getEnterpriseId()).getFullname());
        deliveryInformationVo.setProfessionalName(professionalService.getById(deliveryInformationPo.getProfessionalId()).getName());
        return deliveryInformationVo;
    }
}

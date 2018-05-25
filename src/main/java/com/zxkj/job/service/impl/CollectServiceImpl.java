package com.zxkj.job.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.zxkj.job.bean.dto.CollectDto;
import com.zxkj.job.bean.dto.PageDto;
import com.zxkj.job.bean.po.*;
import com.zxkj.job.bean.vo.*;
import com.zxkj.job.common.BaseServiceImpl;
import com.zxkj.job.common.bean.PagedResult;
import com.zxkj.job.enums.CollectType;
import com.zxkj.job.exp.JobException;
import com.zxkj.job.mapper.CampusRecruitmentProfessionalRMapper;
import com.zxkj.job.mapper.CollectMapper;
import com.zxkj.job.service.*;
import com.zxkj.job.util.BeanUtil;
import com.zxkj.job.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CollectServiceImpl extends BaseServiceImpl<CollectMapper, CollectPo> implements CollectService {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    CampusRecruitmentService campusRecruitmentService;

    @Autowired
    CareerTalkService careerTalkService;

    @Autowired
    EnterpriseService enterpriseService;

    @Override
    public Boolean add(CollectDto collectDto, HttpSession httpSession) {
        UndergraduatePo undergraduatePo = (UndergraduatePo) httpSession.getAttribute("undergraduatePo");
        switch (collectDto.getType()){
            case CAREERTALK:
                checkByCareerTalkIdOrCampusRecruitmentId(collectDto.getType(), collectDto.getCareerTalkId(), undergraduatePo.getId());
                break;
            case CAMPUSRECRUITMENT:
                checkByCareerTalkIdOrCampusRecruitmentId(collectDto.getType(), collectDto.getCampusRecruitmentId(), undergraduatePo.getId());
                break;
        }
        CollectPo collectPo = new CollectPo();
        BeanUtil.copyProperties(collectDto, collectPo);
        collectPo.setUndergraduateId(undergraduatePo.getId());
        return super.insert(collectPo);
    }

    @Override
    public CollectPo checkByCareerTalkIdOrCampusRecruitmentId(CollectType collectType, Long id, Long undergraduateId) {
        EntityWrapper entityWrapper = new EntityWrapper();
        entityWrapper.eq("type", collectType.getType());
        entityWrapper.eq("undergraduate_id", undergraduateId);
        if(collectType == CollectType.CAREERTALK){
            entityWrapper.eq("career_talk_id", id);
        }else if(collectType == CollectType.CAMPUSRECRUITMENT){
            entityWrapper.eq("campus_recruitment_id", id);
        }
        CollectPo collectPo = selectOne(entityWrapper);
        if(collectPo != null){
            throw JobException.COLLECTPO_EXIST_EXCEPTION;
        }
        return collectPo;
    }

    @Override
    public PagedResult list(PageDto pageDto, HttpSession httpSession) {
        UndergraduatePo undergraduatePo = (UndergraduatePo) httpSession.getAttribute("undergraduatePo");
        EntityWrapper entityWrapper = new EntityWrapper();
        entityWrapper.eq("undergraduate_id", undergraduatePo.getId());
        Page page = new Page(pageDto.getPage(), pageDto.getLimit());
        Page page1 = selectPage(page, entityWrapper);
        List<CollectPo> collectPoList = page1.getRecords();
        List<CollectVo> collectVoList = collectPoList.parallelStream().map(e -> collectPoToVo(e)).collect(Collectors.toList());
        PagedResult pagedResult = new PagedResult();
        pagedResult.setData(collectVoList);
        pagedResult.setCount(page1.getTotal());
        return pagedResult;
    }

    @Override
    public Boolean deleteByCollectId(Long collectId) {
        return super.deleteById(collectId);
    }

    @Override
    public List<CollectVo> listByCareerTalkOrCampusRecruitmentId(CollectType type, Long careerTalkOrCampusRecruitmentId) {
        EntityWrapper entityWrapper = new EntityWrapper();
        entityWrapper.eq("type", type);
        switch (type){
            case CAREERTALK:
                entityWrapper.eq("career_talk_id", careerTalkOrCampusRecruitmentId);
                break;
            case CAMPUSRECRUITMENT:
                entityWrapper.eq("campus_recruitment_id", careerTalkOrCampusRecruitmentId);
                break;
        }
        List<CollectPo> collectPoList = super.selectList(entityWrapper);
        List<CollectVo> collectVoList = collectPoList.parallelStream().map(e -> collectPoToVo(e)).collect(Collectors.toList());
        return collectVoList;
    }

    public CollectVo collectPoToVo(CollectPo collectPo){
        CollectVo collectVo = new CollectVo();
        BeanUtil.copyProperties(collectPo, collectVo);
        collectVo.setType(collectPo.getType().getName());
        collectVo.setCollectTime(DateUtil.formatDate(collectPo.getGmtCreate(), "yyyy-MM-dd"));
        EnterpriseVo enterpriseVo = null;
        switch (collectPo.getType()){
            case CAMPUSRECRUITMENT:
                CampusRecruitmentVo campusRecruitmentVo = campusRecruitmentService.getById(collectPo.getCampusRecruitmentId());
                enterpriseVo = enterpriseService.getById(campusRecruitmentVo.getEnterpriseId());
                break;
            case CAREERTALK:
                CareerTalkPo careerTalkPo = careerTalkService.selectById(collectPo.getCareerTalkId());
                enterpriseVo = enterpriseService.getById(careerTalkPo.getEnterpriseId());
                break;
        }
        collectVo.setEnterpriseName(enterpriseVo.getFullname());
        return collectVo;
    }
}

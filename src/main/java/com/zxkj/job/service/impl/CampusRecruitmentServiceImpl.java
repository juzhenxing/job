package com.zxkj.job.service.impl;

import com.baomidou.mybatisplus.enums.SqlLike;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.zxkj.job.bean.dto.CampusRecruitmentDto;
import com.zxkj.job.bean.dto.PageDto;
import com.zxkj.job.bean.dto.ProfessionalDto;
import com.zxkj.job.bean.dto.QueryCampusRecruitmentDto;
import com.zxkj.job.bean.po.CampusRecruitmentPo;
import com.zxkj.job.bean.po.CampusRecruitmentProfessionalRPo;
import com.zxkj.job.bean.po.ProfessionalPo;
import com.zxkj.job.bean.vo.*;
import com.zxkj.job.common.BaseServiceImpl;
import com.zxkj.job.common.bean.PagedResult;
import com.zxkj.job.enums.CollectType;
import com.zxkj.job.exp.JobException;
import com.zxkj.job.mapper.CampusRecruitmentMapper;
import com.zxkj.job.mapper.ProfessionalMapper;
import com.zxkj.job.service.*;
import com.zxkj.job.util.BeanUtil;
import com.zxkj.job.util.FileUtil;
import com.zxkj.job.util.IdUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class CampusRecruitmentServiceImpl extends BaseServiceImpl<CampusRecruitmentMapper, CampusRecruitmentPo> implements CampusRecruitmentService {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${job.tmp.path}")
    private String tmpPath;

    @Autowired
    CampusRecruitmentProfessionalRService campusRecruitmentProfessionalRService;

    @Autowired
    ProfessionalService professionalService;

    @Autowired
    FileUtil fileUtil;

    @Autowired
    CollectService collectService;

    @Autowired
    DeliveryInformationService deliveryInformationService;

    @Override
    public Boolean add(CampusRecruitmentDto campusRecruitmentDto, HttpSession httpSession) throws IOException {
        CampusRecruitmentPo campusRecruitmentPo = new CampusRecruitmentPo();
        BeanUtil.copyProperties(campusRecruitmentDto, campusRecruitmentPo);
        EnterpriseVo enterpriseVo = (EnterpriseVo)httpSession.getAttribute("enterpriseVo");
        campusRecruitmentPo.setEnterpriseId(enterpriseVo.getId());
        MultipartFile generalRegulation = campusRecruitmentDto.getGeneralRegulation();
        String fileName = fileUtil.saveFile(generalRegulation);
        campusRecruitmentPo.setGeneralRegulationFileName(fileName);
        campusRecruitmentPo.setId(IdUtil.nextId());
        campusRecruitmentPo.setGmtCreate(new Date());
        if(!super.insert(campusRecruitmentPo)){
            throw JobException.CAMPUS_RECRUITMENT_INSERT_EXCEPTION;
        }
        List<Long> professionalIds = campusRecruitmentDto.getProfessionalIds();
        List<CampusRecruitmentProfessionalRPo> campusRecruitmentProfessionalRPos = professionalIds.parallelStream().map(e -> {
            CampusRecruitmentProfessionalRPo campusRecruitmentProfessionalRPo = new CampusRecruitmentProfessionalRPo();
            campusRecruitmentProfessionalRPo.setProfessionalId(e);
            campusRecruitmentProfessionalRPo.setCampusRecruitmentId(campusRecruitmentPo.getId());
            return campusRecruitmentProfessionalRPo;
        }).collect(Collectors.toList());
        return campusRecruitmentProfessionalRService.insertBatch(campusRecruitmentProfessionalRPos);
    }

    @Override
    public PagedResult list(PageDto pageDto, Long enterpriseId) {
        Integer startLine = (pageDto.getPage() - 1) * pageDto.getLimit();
        List<CampusRecruitmentPo> campusRecruitmentPos = super.baseMapper.selectPageByEnterpriseId(enterpriseId, startLine, pageDto.getLimit());
        List<CampusRecruitmentVo> campusRecruitmentVos = campusRecruitmentPos.parallelStream().map(e -> campusRecruitmentPoToVo(e)).collect(Collectors.toList());
        PagedResult<CampusRecruitmentVo> pagedResult = new PagedResult<>();
        pagedResult.setData(campusRecruitmentVos);
        pagedResult.setCount(super.baseMapper.selectCountByEnterpriseId(enterpriseId));
        return pagedResult;
    }

    @Override
    public Boolean deleteByCampusRecruitmentId(Long campusRecruitmentId, HttpSession httpSession) {
        EnterpriseVo enterpriseVo = (EnterpriseVo)httpSession.getAttribute("enterpriseVo");
        checkCampusRecruitmentPo(campusRecruitmentId, enterpriseVo.getId());
        List<CollectVo> collectVoList = collectService.listByCareerTalkOrCampusRecruitmentId(CollectType.CAMPUSRECRUITMENT, campusRecruitmentId);
        if(collectVoList != null && collectVoList.size() > 0){
            throw JobException.CAMPUSRECRUITMENT_COLLECT_DELETE_EXCEPTION;
        }
        List<DeliveryInformationVo> deliveryInformationVoList = deliveryInformationService.getByCareerTalkOrCampusRecruitmentId(campusRecruitmentId);
        if(deliveryInformationVoList != null && deliveryInformationVoList.size() > 0){
            throw JobException.CAMPUSRECRUITMENT_PROFESSIONAL_DELETE_EXCEPTION;
        }
        if(!campusRecruitmentProfessionalRService.deleteByCampusRecruitmentId(campusRecruitmentId)){
            throw JobException.CAMPUS_RECRUITMENT_DELETE_EXCEPTION;
        }
        return super.deleteById(campusRecruitmentId);
    }

    @Override
    public CampusRecruitmentVo getById(Long campusRecruitmentId, HttpSession httpSession) {
        EnterpriseVo enterpriseVo = (EnterpriseVo)httpSession.getAttribute("enterpriseVo");
        CampusRecruitmentPo campusRecruitmentPo = checkCampusRecruitmentPo(campusRecruitmentId, enterpriseVo.getId());
        return campusRecruitmentPoToVo(campusRecruitmentPo);
    }

    @Override
    public void checkUpdateById(Long campusRecruitmentId) {
        List<CollectVo> collectVoList = collectService.listByCareerTalkOrCampusRecruitmentId(CollectType.CAMPUSRECRUITMENT, campusRecruitmentId);
        if(collectVoList != null && collectVoList.size() > 0){
            throw JobException.CAMPUSRECRUITMENT_UPDATE_COLLECT_EXCEPTION;
        }
        List<DeliveryInformationVo> deliveryInformationVoList = deliveryInformationService.getByCareerTalkOrCampusRecruitmentId(campusRecruitmentId);
        if(deliveryInformationVoList != null && deliveryInformationVoList.size() > 0){
            throw JobException.CAMPUSRECRUITMENT_UPDATE_PROFESSIONAL_EXCEPTION;
        }
    }

    @Override
    public Boolean updateById(CampusRecruitmentDto campusRecruitmentDto, HttpSession httpSession) throws IOException {
        if(StringUtils.isEmpty(campusRecruitmentDto.getId())){
            throw JobException.NULL_ID_EXCEPTION;
        }
        EnterpriseVo enterpriseVo = (EnterpriseVo)httpSession.getAttribute("enterpriseVo");
        CampusRecruitmentPo campusRecruitmentPo = checkCampusRecruitmentPo(campusRecruitmentDto.getId(), enterpriseVo.getId());
        BeanUtil.copyProperties(campusRecruitmentDto, campusRecruitmentPo);
        MultipartFile generalRegulation = campusRecruitmentDto.getGeneralRegulation();
        String fileName = fileUtil.saveFile(generalRegulation);
        campusRecruitmentPo.setGeneralRegulationFileName(fileName);
        if(!super.updateById(campusRecruitmentPo)){
            throw JobException.CAMPUS_RECRUITMENT_UPDATE_EXCEPTION;
        }
        List<Long> professionalIds = campusRecruitmentDto.getProfessionalIds();
        List<CampusRecruitmentProfessionalRPo> campusRecruitmentProfessionalRPos = campusRecruitmentProfessionalRService.listByCampusRecruitmentId(campusRecruitmentDto.getId());
        List<Long> oldProfessionalIds = campusRecruitmentProfessionalRPos.parallelStream().map(e -> e.getProfessionalId()).collect(Collectors.toList());
        List<Long> addProfessionalIds = new ArrayList<>();
        List<Long> repetitiveProfessionalIds = new ArrayList<>();
        for(Long professionalId : professionalIds){
            boolean flag = false;
            for(Long oldProfessionalId : oldProfessionalIds){
                if(professionalId.equals(oldProfessionalId)){
                    repetitiveProfessionalIds.add(professionalId);
                    flag = true;
                    break;
                }
            }
            if(!flag){
                addProfessionalIds.add(professionalId);
            }
        }
        oldProfessionalIds.removeAll(repetitiveProfessionalIds);
        if(oldProfessionalIds.size() != 0){
            if(!campusRecruitmentProfessionalRService.deleteByProfessionalIds(campusRecruitmentDto.getId(), oldProfessionalIds)){
                return false;
            }
        }
        List<CampusRecruitmentProfessionalRPo> addCampusRecruitmentProfessionalRPos = addProfessionalIds.parallelStream().map(e -> {
            CampusRecruitmentProfessionalRPo campusRecruitmentProfessionalRPo = new CampusRecruitmentProfessionalRPo();
            campusRecruitmentProfessionalRPo.setProfessionalId(e);
            campusRecruitmentProfessionalRPo.setCampusRecruitmentId(campusRecruitmentPo.getId());
            return campusRecruitmentProfessionalRPo;
        }).collect(Collectors.toList());
        if(addCampusRecruitmentProfessionalRPos.size() == 0){
            return true;
        }
        return campusRecruitmentProfessionalRService.insertBatch(addCampusRecruitmentProfessionalRPos);
    }

    @Override
    public PagedResult list(PageDto pageDto) {
        Page page = new Page(pageDto.getPage(), pageDto.getLimit());
        Page<CampusRecruitmentPo> campusRecruitmentPoPage = super.selectPage(page);
        List<CampusRecruitmentVo> campusRecruitmentVoList = campusRecruitmentPoPage.getRecords().parallelStream().map(e -> campusRecruitmentPoToVo(e)).collect(Collectors.toList());
        PagedResult<CampusRecruitmentVo> pagedResult = new PagedResult<>();
        pagedResult.setData(campusRecruitmentVoList);
//        logger.error("总数：" + careerTalkPoPage.getTotal());
        pagedResult.setCount(campusRecruitmentPoPage.getTotal());
        return pagedResult;
    }

    public CampusRecruitmentPo checkCampusRecruitmentPo(Long campusRecruitmentId, Long enterpriseId){
        CampusRecruitmentPo campusRecruitmentPo = super.baseMapper.selectOneById(campusRecruitmentId, enterpriseId);
        if(campusRecruitmentPo == null){
            throw JobException.NULL_CAMPUS_RECRUITMENT_EXCEPTION;
        }
        return campusRecruitmentPo;
    }

    @Override
    public CampusRecruitmentVo campusRecruitmentPoToVo(CampusRecruitmentPo campusRecruitmentPo){
        CampusRecruitmentVo campusRecruitmentVo = new CampusRecruitmentVo();
        BeanUtil.copyProperties(campusRecruitmentPo, campusRecruitmentVo);
        campusRecruitmentVo.setReleaseTime(campusRecruitmentPo.getGmtCreate());
        List<CampusRecruitmentProfessionalRPo> campusRecruitmentProfessionalRPos = campusRecruitmentProfessionalRService.listByCampusRecruitmentId(campusRecruitmentPo.getId());
        List<Long> professionalIds = campusRecruitmentProfessionalRPos.parallelStream().map(e -> e.getProfessionalId()).collect(Collectors.toList());
        List<ProfessionalVo> professionalVoList = professionalService.listByProfessionalIds(professionalIds);
        campusRecruitmentVo.setProfessionalVoList(professionalVoList);
//        logger.error("professionalVoList:" + professionalVoList.size());
        return campusRecruitmentVo;
    }

    @Override
    public CampusRecruitmentVo getById(Long campusRecruitmentId) {
        CampusRecruitmentPo campusRecruitmentPo = super.selectById(campusRecruitmentId);
        return campusRecruitmentPoToVo(campusRecruitmentPo);
    }

    @Override
    public PagedResult listByQueryCampusRecruitmentDto(QueryCampusRecruitmentDto queryCampusRecruitmentDto, PageDto pageDto) {
        Page page = new Page(pageDto.getPage(), pageDto.getLimit());
        EntityWrapper entityWrapper = new EntityWrapper();
        if(!StringUtils.isEmpty(queryCampusRecruitmentDto.getProvince())){
            logger.error(queryCampusRecruitmentDto.getProvince().getName());
            entityWrapper.like("workplace", queryCampusRecruitmentDto.getProvince().getName(), SqlLike.DEFAULT);
        }
        if(!StringUtils.isEmpty(queryCampusRecruitmentDto.getKey())){
            logger.error(queryCampusRecruitmentDto.getKey());
            entityWrapper.like("title", queryCampusRecruitmentDto.getKey(), SqlLike.DEFAULT);
        }
        Page<CampusRecruitmentPo> campusRecruitmentPoPage = super.selectPage(page, entityWrapper);
        List<CampusRecruitmentVo> campusRecruitmentVoList = campusRecruitmentPoPage.getRecords().parallelStream().map(e -> campusRecruitmentPoToVo(e)).collect(Collectors.toList());
        PagedResult<CampusRecruitmentVo> pagedResult = new PagedResult<>();
        pagedResult.setData(campusRecruitmentVoList);
//        logger.error("总数：" + careerTalkPoPage.getTotal());
        pagedResult.setCount(campusRecruitmentPoPage.getTotal());
        return pagedResult;
    }
}

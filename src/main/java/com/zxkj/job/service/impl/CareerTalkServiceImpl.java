package com.zxkj.job.service.impl;

import com.baomidou.mybatisplus.enums.SqlLike;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.zxkj.job.bean.dto.CareerTalkDto;
import com.zxkj.job.bean.dto.PageDto;
import com.zxkj.job.bean.dto.QueryCareerTalkDto;
import com.zxkj.job.bean.po.CareerTalkPo;
import com.zxkj.job.bean.po.CareerTalkProfessionalRPo;
import com.zxkj.job.bean.vo.*;
import com.zxkj.job.common.BaseServiceImpl;
import com.zxkj.job.common.bean.PagedResult;
import com.zxkj.job.enums.CollectType;
import com.zxkj.job.enums.OperationType;
import com.zxkj.job.enums.ProvinceType;
import com.zxkj.job.exp.JobException;
import com.zxkj.job.mapper.CareerTalkMapper;
import com.zxkj.job.service.*;
import com.zxkj.job.util.BeanUtil;
import com.zxkj.job.util.DateUtil;
import com.zxkj.job.util.FileUtil;
import com.zxkj.job.util.IdUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CareerTalkServiceImpl extends BaseServiceImpl<CareerTalkMapper, CareerTalkPo> implements CareerTalkService {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    FileUtil fileUtil;

    @Autowired
    CareerTalkProfessionalRService careerTalkProfessionalRService;

    @Autowired
    ProfessionalService professionalService;

    @Autowired
    CollectService collectService;

    @Autowired
    DeliveryInformationService deliveryInformationService;

    @Override
    public Boolean add(CareerTalkDto careerTalkDto, HttpSession httpSession) throws ParseException, IOException {
//        Date startTime = DateUtil.formatDate(careerTalkDto.getStartTime(), "yyyy-MM-dd HH:mm");
//        Date endTime = DateUtil.formatDate(careerTalkDto.getEndTime(), "yyyy-MM-dd HH:mm");
        Date startTime = careerTalkDto.getStartTime();
        Date endTime = careerTalkDto.getEndTime();
        if (startTime.after(endTime)) {
            throw JobException.WRONG_ENDTIME_EXCEPTION;
        }
        EnterpriseVo enterpriseVo = (EnterpriseVo) httpSession.getAttribute("enterpriseVo");
        CareerTalkPo careerTalkPo = new CareerTalkPo();
        BeanUtil.copyProperties(careerTalkDto, careerTalkPo);
        careerTalkPo.setStartTime(startTime);
        careerTalkPo.setEndTime(endTime);
        careerTalkPo.setEnterpriseId(enterpriseVo.getId());
        careerTalkPo.setName(enterpriseVo.getFullname());
        MultipartFile preachingIext = careerTalkDto.getPreachingText();
        String fileName = fileUtil.saveFile(preachingIext);
        careerTalkPo.setPreachingTextFileName(fileName);
        careerTalkPo.setId(IdUtil.nextId());
        careerTalkPo.setGmtCreate(new Date());
        if (!super.insert(careerTalkPo)) {
            throw JobException.CAREERTALK_ADD_EXCEPTION;
        }
        List<Long> professionalIds = careerTalkDto.getProfessionalIds();
        if(professionalIds == null || professionalIds.size() == 0){
            return true;
        }
        List<CareerTalkProfessionalRPo> careerTalkProfessionalRPos = professionalIds.parallelStream().map(e -> {
            CareerTalkProfessionalRPo careerTalkProfessionalRPo = new CareerTalkProfessionalRPo();
            careerTalkProfessionalRPo.setProfessionalId(e);
            careerTalkProfessionalRPo.setCareerTalkId(careerTalkPo.getId());
            return careerTalkProfessionalRPo;
        }).collect(Collectors.toList());
        return careerTalkProfessionalRService.insertBatch(careerTalkProfessionalRPos);
    }

    @Override
    public PagedResult list(PageDto pageDto, Long enterpriseId) {
        Integer startLine = (pageDto.getPage() - 1) * pageDto.getLimit();
        List<CareerTalkPo> careerTalkPoList = super.baseMapper.selectPageByEnterpriseId(enterpriseId, startLine, pageDto.getLimit());
        List<CareerTalkVo> careerTalkVoList = careerTalkPoList.parallelStream().map(e -> careerTalkPoToVo(e)).collect(Collectors.toList());
        PagedResult<CareerTalkVo> pagedResult = new PagedResult<>();
        pagedResult.setData(careerTalkVoList);
        pagedResult.setCount(super.baseMapper.selectCountByEnterpriseId(enterpriseId));
        return pagedResult;
    }

    @Override
    public Boolean deleteByCareerTalkId(Long careerTalkId, HttpSession httpSession) {
        logger.error("要删除的宣讲会的id：" + careerTalkId);
        EnterpriseVo enterpriseVo = (EnterpriseVo) httpSession.getAttribute("enterpriseVo");
        CareerTalkPo careerTalkPo = checkCareerTalkPo(careerTalkId, enterpriseVo.getId());
        Date startTime = careerTalkPo.getStartTime();
        if (startTime.getTime() - System.currentTimeMillis() < 24 * 60 * 60 * 1000) {
            throw JobException.CAREERTALK_DELETE_TIME_EXCEPTION;
        }
        List<CollectVo> collectVoList = collectService.listByCareerTalkOrCampusRecruitmentId(CollectType.CAREERTALK, careerTalkId);
        if (collectVoList != null && collectVoList.size() > 0) {
            throw JobException.CAREERTALK_COLLECT_DELETE_EXCEPTION;
        }
        if (!careerTalkProfessionalRService.deleteByCareerTalkId(careerTalkId)) {
            throw JobException.CAREERTALK_DELETE_EXCEPTION;
        }
        return super.deleteById(careerTalkId);
    }

    public CareerTalkPo checkCareerTalkPo(Long careerTalkId, Long enterpriseId) {
        CareerTalkPo careerTalkPo = super.baseMapper.selectOneById(careerTalkId, enterpriseId);
        if (careerTalkPo == null) {
            throw JobException.NULL_CAREERTALK_EXCEPTION;
        }
        return careerTalkPo;
    }

    @Override
    public CareerTalkUpdateVo selectOneById(Long careerTalkId, HttpSession httpSession) {
        EnterpriseVo enterpriseVo = (EnterpriseVo) httpSession.getAttribute("enterpriseVo");
        CareerTalkPo careerTalkPo = checkCareerTalkPo(careerTalkId, enterpriseVo.getId());
        Date startTime = careerTalkPo.getStartTime();
        logger.error(startTime.toString());
        if (startTime.getTime() - System.currentTimeMillis() < 24 * 60 * 60 * 1000) {
            throw JobException.CAREERTALK_UPDATE_TIME_EXCEPTION;
        }
        List<CollectVo> collectVoList = collectService.listByCareerTalkOrCampusRecruitmentId(CollectType.CAREERTALK, careerTalkId);
        if (collectVoList != null && collectVoList.size() > 0) {
            throw JobException.CAREERTALK_UPDATE_COLLECT_EXCEPTION;
        }
        List<DeliveryInformationVo> deliveryInformationVoList = deliveryInformationService.getByCareerTalkOrCampusRecruitmentId(careerTalkId);
        if (deliveryInformationVoList != null && deliveryInformationVoList.size() > 0) {
            throw JobException.CAREERTALK_UPDATE_PROFESSIONAL_EXCEPTION;
        }
        CareerTalkUpdateVo careerTalkUpdateVo = new CareerTalkUpdateVo();
        BeanUtil.copyProperties(careerTalkPo, careerTalkUpdateVo);
        careerTalkUpdateVo.setProvince(careerTalkPo.getProvince().getType());
        careerTalkUpdateVo.setOperationType(careerTalkPo.getOperationType().getType());
        careerTalkUpdateVo.setStartTime(DateUtil.formatDate(careerTalkPo.getStartTime(), "yyyy-MM-dd HH:mm"));
        careerTalkUpdateVo.setEndTime(DateUtil.formatDate(careerTalkPo.getEndTime(), "yyyy-MM-dd HH:mm"));
        List<CareerTalkProfessionalRPo> careerTalkProfessionalRPos = careerTalkProfessionalRService.listByCareerTalkId(careerTalkId);
        List<Long> professionalIds = careerTalkProfessionalRPos.parallelStream().map(e -> e.getProfessionalId()).collect(Collectors.toList());
        List<ProfessionalVo> professionalVoList = professionalService.listByProfessionalIds(professionalIds);
        careerTalkUpdateVo.setProfessionalVoList(professionalVoList);
        return careerTalkUpdateVo;
    }

    @Override
    public Boolean updateById(CareerTalkDto careerTalkDto, HttpSession httpSession) throws ParseException, IOException {
        if (StringUtils.isEmpty(careerTalkDto.getId())) {
            throw JobException.NULL_ID_EXCEPTION;
        }
        EnterpriseVo enterpriseVo = (EnterpriseVo) httpSession.getAttribute("enterpriseVo");
        CareerTalkPo careerTalkPo = checkCareerTalkPo(careerTalkDto.getId(), enterpriseVo.getId());
        BeanUtil.copyProperties(careerTalkDto, careerTalkPo);
        MultipartFile generalRegulation = careerTalkDto.getPreachingText();
        String fileName = fileUtil.saveFile(generalRegulation);
        careerTalkPo.setPreachingTextFileName(fileName);
        if (!super.updateById(careerTalkPo)) {
            throw JobException.CAREERTALK_UPDATE_EXCEPTION;
        }
        logger.error("operationType", careerTalkPo.getOperationType());
        if(careerTalkPo.getOperationType() == OperationType.KNOWWELL){
            return true;
        }
        List<Long> professionalIds = careerTalkDto.getProfessionalIds();
        List<CareerTalkProfessionalRPo> careerTalkProfessionalRPoList = careerTalkProfessionalRService.listByCareerTalkId(careerTalkDto.getId());
        List<Long> oldProfessionalIds = careerTalkProfessionalRPoList.parallelStream().map(e -> e.getProfessionalId()).collect(Collectors.toList());
        List<Long> addProfessionalIds = new ArrayList<>();
        List<Long> repetitiveProfessionalIds = new ArrayList<>();
        for (Long professionalId : professionalIds) {
            boolean flag = false;
            for (Long oldProfessionalId : oldProfessionalIds) {
                if (professionalId.equals(oldProfessionalId)) {
                    repetitiveProfessionalIds.add(professionalId);
                    flag = true;
                    break;
                }
            }
            if (!flag) {
                addProfessionalIds.add(professionalId);
            }
        }
        oldProfessionalIds.removeAll(repetitiveProfessionalIds);
        if (oldProfessionalIds.size() != 0) {
            if (!careerTalkProfessionalRService.deleteByProfessionalIds(careerTalkDto.getId(), oldProfessionalIds)) {
                return false;
            }
        }
        List<CareerTalkProfessionalRPo> addCareerTalkProfessionalRPos = addProfessionalIds.parallelStream().map(e -> {
            CareerTalkProfessionalRPo careerTalkProfessionalRPo = new CareerTalkProfessionalRPo();
            careerTalkProfessionalRPo.setProfessionalId(e);
            careerTalkProfessionalRPo.setCareerTalkId(careerTalkDto.getId());
            return careerTalkProfessionalRPo;
        }).collect(Collectors.toList());
        if (addCareerTalkProfessionalRPos.size() == 0) {
            return true;
        }
        return careerTalkProfessionalRService.insertBatch(addCareerTalkProfessionalRPos);
    }

    @Override
    public PagedResult list(PageDto pageDto) {
        Page page = new Page(pageDto.getPage(), pageDto.getLimit());
        Page<CareerTalkPo> careerTalkPoPage = super.selectPage(page);
        List<CareerTalkVo> careerTalkVoList = careerTalkPoPage.getRecords().parallelStream().map(e -> careerTalkPoToVo(e)).collect(Collectors.toList());
        PagedResult<CareerTalkVo> pagedResult = new PagedResult<>();
        pagedResult.setData(careerTalkVoList);
//        logger.error("总数：" + careerTalkPoPage.getTotal());
        pagedResult.setCount(careerTalkPoPage.getTotal());
        return pagedResult;
    }

    @Override
    public PagedResult listByQueryDto(QueryCareerTalkDto queryCareerTalkDto, PageDto pageDto) {
        Page page = new Page(pageDto.getPage(), pageDto.getLimit());
        EntityWrapper entityWrapper = new EntityWrapper();
        if (!StringUtils.isEmpty(queryCareerTalkDto.getProvince())) {
            logger.error(queryCareerTalkDto.getProvince().getName());
            entityWrapper.eq("province", queryCareerTalkDto.getProvince().getType());
        }
        if (!StringUtils.isEmpty(queryCareerTalkDto.getKey())) {
            logger.error(queryCareerTalkDto.getKey());
            entityWrapper.like("name", queryCareerTalkDto.getKey(), SqlLike.DEFAULT);
        }
        if(!StringUtils.isEmpty(queryCareerTalkDto.getSchool())){
            entityWrapper.eq("school", queryCareerTalkDto.getSchool());
        }
        Page<CareerTalkPo> careerTalkPoPage = super.selectPage(page, entityWrapper);
        List<CareerTalkVo> careerTalkVoList = careerTalkPoPage.getRecords().parallelStream().map(e -> careerTalkPoToVo(e)).collect(Collectors.toList());
        PagedResult<CareerTalkVo> pagedResult = new PagedResult<>();
        pagedResult.setData(careerTalkVoList);
//        logger.error("总数：" + careerTalkPoPage.getTotal());
        pagedResult.setCount(careerTalkPoPage.getTotal());
        return pagedResult;
    }

    @Override
    public CareerTalkVo getByCareerTalkId(Long careerTalkId) {
        CareerTalkPo careerTalkPo = super.selectById(careerTalkId);
        return careerTalkPoToVo(careerTalkPo);
    }

    public CareerTalkVo careerTalkPoToVo(CareerTalkPo careerTalkPo) {
        CareerTalkVo careerTalkVo = new CareerTalkVo();
        BeanUtil.copyProperties(careerTalkPo, careerTalkVo);
        careerTalkVo.setProvince(careerTalkPo.getProvince().getName());
        careerTalkVo.setOperationType(careerTalkPo.getOperationType().getName());
        careerTalkVo.setCreateTime(careerTalkPo.getGmtCreate());
        List<CareerTalkProfessionalRPo> careerTalkProfessionalRPos = careerTalkProfessionalRService.listByCareerTalkId(careerTalkVo.getId());
        List<Long> professionalIds = careerTalkProfessionalRPos.parallelStream().map(e -> e.getProfessionalId()).collect(Collectors.toList());
        List<ProfessionalVo> professionalVoList = professionalService.listByProfessionalIds(professionalIds);
        careerTalkVo.setProfessionalVoList(professionalVoList);
        return careerTalkVo;
    }
}

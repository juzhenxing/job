package com.zxkj.job.service.impl;

import com.baomidou.mybatisplus.enums.SqlLike;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.zxkj.job.bean.dto.CareerTalkDto;
import com.zxkj.job.bean.dto.PageDto;
import com.zxkj.job.bean.dto.QueryCareerTalkDto;
import com.zxkj.job.bean.po.CareerTalkPo;
import com.zxkj.job.bean.po.CareerTalkProfessionalRPo;
import com.zxkj.job.bean.vo.CareerTalkUpdateVo;
import com.zxkj.job.bean.vo.CareerTalkVo;
import com.zxkj.job.bean.vo.EnterpriseVo;
import com.zxkj.job.common.BaseServiceImpl;
import com.zxkj.job.common.bean.PagedResult;
import com.zxkj.job.enums.ProvinceType;
import com.zxkj.job.exp.JobException;
import com.zxkj.job.mapper.CareerTalkMapper;
import com.zxkj.job.service.CareerTalkProfessionalRService;
import com.zxkj.job.service.CareerTalkService;
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

    @Override
    public Boolean add(CareerTalkDto careerTalkDto, HttpSession httpSession) throws ParseException, IOException {
//        Date startTime = DateUtil.formatDate(careerTalkDto.getStartTime(), "yyyy-MM-dd HH:mm");
//        Date endTime = DateUtil.formatDate(careerTalkDto.getEndTime(), "yyyy-MM-dd HH:mm");
        Date startTime = careerTalkDto.getStartTime();
        Date endTime = careerTalkDto.getEndTime();
        if(startTime.after(endTime)){
            throw JobException.WRONG_ENDTIME_EXCEPTION;
        }
        EnterpriseVo enterpriseVo = checkEnterpriseVo(httpSession);
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
        if(!super.insert(careerTalkPo)){
            throw JobException.CAREERTALK_ADD_EXCEPTION;
        }
        List<Long> professionalIds = careerTalkDto.getProfessionalIds();
        List<CareerTalkProfessionalRPo> careerTalkProfessionalRPos = professionalIds.parallelStream().map(e -> {
            CareerTalkProfessionalRPo careerTalkProfessionalRPo = new CareerTalkProfessionalRPo();
            careerTalkProfessionalRPo.setProfessionalId(e);
            careerTalkProfessionalRPo.setCareerTalkId(careerTalkPo.getId());
            return careerTalkProfessionalRPo;
        }).collect(Collectors.toList());
        return careerTalkProfessionalRService.insertBatch(careerTalkProfessionalRPos);
    }

    public EnterpriseVo checkEnterpriseVo(HttpSession httpSession){
        Object enterpriseObj = httpSession.getAttribute("enterpriseVo");
        if (StringUtils.isEmpty(enterpriseObj)) {
            throw JobException.NOT_LOGGED_IN_EXCEPTION;
        }
        return (EnterpriseVo) enterpriseObj;
    }

    @Override
    public PagedResult list(PageDto pageDto, Long enterpriseId) {
        Integer startLine = (pageDto.getPage() - 1) * pageDto.getLimit();
        List<CareerTalkPo> careerTalkPoList = super.baseMapper.selectPageByEnterpriseId(enterpriseId, startLine, pageDto.getLimit());
        List<CareerTalkVo> careerTalkVoList = careerTalkPoList.parallelStream().map(e -> {
            CareerTalkVo careerTalkVo = new CareerTalkVo();
            BeanUtil.copyProperties(e, careerTalkVo);
            careerTalkVo.setProvince(e.getProvince().getName());
            careerTalkVo.setOperationType(e.getOperationType().getName());
            careerTalkVo.setCreateTime(e.getGmtCreate());
//            logger.error("发布时间：" + careerTalkVo.getCreateTime());
//            logger.error("开始时间：" + careerTalkVo.getStartTime());
//            logger.error("结束时间：" + careerTalkVo.getEndTime());
            return careerTalkVo;
        }).collect(Collectors.toList());
        PagedResult<CareerTalkVo> pagedResult = new PagedResult<>();
        pagedResult.setData(careerTalkVoList);
        pagedResult.setCount(super.baseMapper.selectCountByEnterpriseId(enterpriseId));
        return pagedResult;
    }

    @Override
    public Boolean deleteByCareerTalkId(Long careerTalkId, HttpSession httpSession) {
        logger.error("要删除的宣讲会的id：" + careerTalkId);
        EnterpriseVo enterpriseVo = checkEnterpriseVo(httpSession);
        CareerTalkPo careerTalkPo = checkCareerTalkPo(careerTalkId, enterpriseVo.getId());
        Date startTime = careerTalkPo.getStartTime();
        if(startTime.getTime() - System.currentTimeMillis() < 24*60*60*1000){
            throw JobException.CAREERTALK_DELETE_EXCEPTION;
        }
        return super.deleteById(careerTalkId);
    }

    public CareerTalkPo checkCareerTalkPo(Long careerTalkId, Long enterpriseId){
        CareerTalkPo careerTalkPo = super.baseMapper.selectOneById(careerTalkId, enterpriseId);
        if(careerTalkPo == null){
            throw JobException.NULL_CAREERTALK_EXCEPTION;
        }
        return careerTalkPo;
    }

    @Override
    public CareerTalkUpdateVo selectOneById(Long careerTalkId, HttpSession httpSession) {
        EnterpriseVo enterpriseVo = checkEnterpriseVo(httpSession);
        CareerTalkPo careerTalkPo = checkCareerTalkPo(careerTalkId, enterpriseVo.getId());
        Date startTime = careerTalkPo.getStartTime();
        logger.error(startTime.toString());
        if(startTime.getTime() - System.currentTimeMillis() < 24*60*60*1000){
            throw JobException.CAREERTALK_UPDATE_EXCEPTION;
        }
        CareerTalkUpdateVo careerTalkUpdateVo = new CareerTalkUpdateVo();
        BeanUtil.copyProperties(careerTalkPo, careerTalkUpdateVo);
        careerTalkUpdateVo.setProvince(careerTalkPo.getProvince().getType());
        careerTalkUpdateVo.setOperationType(careerTalkPo.getOperationType().getType());
        careerTalkUpdateVo.setStartTime(DateUtil.formatDate(careerTalkPo.getStartTime(),"yyyy-MM-dd HH:mm"));
        careerTalkUpdateVo.setEndTime(DateUtil.formatDate(careerTalkPo.getEndTime(),"yyyy-MM-dd HH:mm"));
        return careerTalkUpdateVo;
    }

    @Override
    public Boolean updateById(CareerTalkDto careerTalkDto, HttpSession httpSession) throws ParseException {
        if(StringUtils.isEmpty(careerTalkDto.getId())){
            throw JobException.NULL_ID_EXCEPTION;
        }
        EnterpriseVo enterpriseVo = checkEnterpriseVo(httpSession);
        CareerTalkPo careerTalkPo = checkCareerTalkPo(careerTalkDto.getId(), enterpriseVo.getId());
        BeanUtil.copyProperties(careerTalkDto, careerTalkPo);
//        careerTalkPo.setStartTime(DateUtil.formatDate(careerTalkDto.getStartTime(), "yyyy-MM-dd HH:mm"));
//        careerTalkPo.setEndTime(DateUtil.formatDate(careerTalkDto.getEndTime(), "yyyy-MM-dd HH:mm"));
        return super.updateById(careerTalkPo);
    }

    @Override
    public PagedResult list(PageDto pageDto) {
        Page page = new Page(pageDto.getPage(), pageDto.getLimit());
        Page<CareerTalkPo> careerTalkPoPage = super.selectPage(page);
        List<CareerTalkVo> careerTalkVoList = careerTalkPoPage.getRecords().parallelStream().map(e -> {
            CareerTalkVo careerTalkVo = new CareerTalkVo();
            BeanUtil.copyProperties(e, careerTalkVo);
            careerTalkVo.setProvince(e.getProvince().getName());
            careerTalkVo.setOperationType(e.getOperationType().getName());
            careerTalkVo.setCreateTime(e.getGmtCreate());
            return careerTalkVo;
        }).collect(Collectors.toList());
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
        if(!StringUtils.isEmpty(queryCareerTalkDto.getProvince())){
            logger.error(queryCareerTalkDto.getProvince().getName());
            entityWrapper.eq("province", queryCareerTalkDto.getProvince().getType());
        }
            if(!StringUtils.isEmpty(queryCareerTalkDto.getKey())){
                logger.error(queryCareerTalkDto.getKey());
                entityWrapper.like("name", queryCareerTalkDto.getKey(), SqlLike.DEFAULT);
        }
        Page<CareerTalkPo> careerTalkPoPage = super.selectPage(page, entityWrapper);
        List<CareerTalkVo> careerTalkVoList = careerTalkPoPage.getRecords().parallelStream().map(e -> {
            CareerTalkVo careerTalkVo = new CareerTalkVo();
            BeanUtil.copyProperties(e, careerTalkVo);
            careerTalkVo.setProvince(e.getProvince().getName());
            careerTalkVo.setOperationType(e.getOperationType().getName());
            careerTalkVo.setCreateTime(e.getGmtCreate());
            return careerTalkVo;
        }).collect(Collectors.toList());
        PagedResult<CareerTalkVo> pagedResult = new PagedResult<>();
        pagedResult.setData(careerTalkVoList);
//        logger.error("总数：" + careerTalkPoPage.getTotal());
        pagedResult.setCount(careerTalkPoPage.getTotal());
        return pagedResult;
    }
}

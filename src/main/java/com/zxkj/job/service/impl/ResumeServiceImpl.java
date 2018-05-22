package com.zxkj.job.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.zxkj.job.bean.dto.AdministratorDto;
import com.zxkj.job.bean.dto.LoginAdministratorDto;
import com.zxkj.job.bean.dto.PageDto;
import com.zxkj.job.bean.dto.ResumeDto;
import com.zxkj.job.bean.po.AdministratorPo;
import com.zxkj.job.bean.po.EnterprisePo;
import com.zxkj.job.bean.po.ResumePo;
import com.zxkj.job.bean.po.UndergraduatePo;
import com.zxkj.job.bean.vo.EnterpriseVo;
import com.zxkj.job.bean.vo.ResumeInfoVo;
import com.zxkj.job.bean.vo.ResumeVo;
import com.zxkj.job.common.BaseServiceImpl;
import com.zxkj.job.common.bean.PagedResult;
import com.zxkj.job.enums.CheckStateType;
import com.zxkj.job.exp.JobException;
import com.zxkj.job.mapper.ResumeMapper;
import com.zxkj.job.service.DeliveryInformationService;
import com.zxkj.job.service.ResumeService;
import com.zxkj.job.service.UndergraduateService;
import com.zxkj.job.util.BeanUtil;
import com.zxkj.job.util.DateUtil;
import com.zxkj.job.util.IdUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


@Service
@Transactional
public class ResumeServiceImpl extends BaseServiceImpl<ResumeMapper, ResumePo> implements ResumeService {

    @Autowired
    UndergraduateService undergraduateService;

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    DeliveryInformationService deliveryInformationService;

    @Override
    public Boolean add(ResumeDto resumeDto, HttpSession httpSession) throws ParseException {
        ResumePo resumePo = resumeDtoToPo(resumeDto, httpSession, true);
        return super.insert(resumePo);
    }

    public ResumePo resumeDtoToPo(ResumeDto resumeDto, HttpSession httpSession, Boolean isAdd) throws ParseException {
        ResumePo resumePo = new ResumePo();
        BeanUtil.copyProperties(resumeDto, resumePo);
        resumePo.setCompleteness(0.1f);
        resumePo.setBirthDate(DateUtil.formatDate(resumeDto.getBirthDate(), "yyyy-MM-dd"));
        UndergraduatePo undergraduatePo = (UndergraduatePo) httpSession.getAttribute("undergraduatePo");
        resumePo.setUndergraduateId(undergraduatePo.getId());
        resumePo.setType(0);
        if(isAdd){
            resumePo.setId(IdUtil.nextId());
            resumePo.setGmtCreate(new Date());
        }
        if(resumeDto.getAcquiescence()){
            undergraduatePo.setAcquiescenceResumeId(resumePo.getId());
            if(!undergraduateService.updateById(undergraduatePo)){
                throw JobException.UNDERGRADUATE_UPDATE_EXCEPTION;
            }
            httpSession.setAttribute("undergraduatePo", undergraduatePo);
        }
        return resumePo;
    }

    @Override
    public PagedResult list(PageDto pageDto, HttpSession httpSession) {
        UndergraduatePo undergraduatePo = (UndergraduatePo) httpSession.getAttribute("undergraduatePo");
        Long undergraduateId = undergraduatePo.getId();
        Page page = new Page(pageDto.getPage(), pageDto.getLimit());
        EntityWrapper entityWrapper = new EntityWrapper();
        entityWrapper.eq("undergraduate_id", undergraduateId);
        Page page1 = super.selectPage(page, entityWrapper);
        PagedResult pagedResult = new PagedResult();
        Long acquiescenceResumeId = undergraduatePo.getAcquiescenceResumeId();
        List<ResumeVo> resumeVos = (List<ResumeVo>) page1.getRecords().parallelStream().map(e -> resumePoToVo((ResumePo) e, acquiescenceResumeId)).collect(Collectors.toList());
        pagedResult.setData(resumeVos);
        pagedResult.setCount(page1.getTotal());
        return pagedResult;
    }

    public ResumeVo resumePoToVo(ResumePo resumePo, Long acquiescenceResumeId){
        ResumeVo resumeVo = new ResumeVo();
        BeanUtil.copyProperties(resumePo, resumeVo);
        if(resumeVo.getId().equals(acquiescenceResumeId)){
            resumeVo.setAcquiescence(true);
        }else{
            resumeVo.setAcquiescence(false);
        }
        resumeVo.setCreateTime(resumePo.getGmtCreate());
        resumeVo.setBirthDate(DateUtil.formatDate(resumePo.getBirthDate(), "yyyy-MM-dd"));
        return resumeVo;
    }

    @Override
    public Boolean deleteByResumeId(Long resumeId, HttpSession httpSession) {
        UndergraduatePo undergraduatePo = (UndergraduatePo) httpSession.getAttribute("undergraduatePo");
        checkResumePo(resumeId, undergraduatePo.getId());
        if(deliveryInformationService.getByResumeId(resumeId).size() > 0){
            throw JobException.EXIST_DELIVERYINFORMATIONPO_EXCEPTION;
        }
        return super.deleteById(resumeId);
    }

    @Override
    public ResumeVo selectOneById(Long resumeId, HttpSession httpSession) {
        UndergraduatePo undergraduatePo = (UndergraduatePo) httpSession.getAttribute("undergraduatePo");
        ResumePo resumePo = checkResumePo(resumeId, undergraduatePo.getId());
        Long acquiescenceResumeId = undergraduatePo.getAcquiescenceResumeId();
        return resumePoToVo(resumePo, acquiescenceResumeId);
    }

    @Override
    public List<ResumeVo> list(Long undergraduateId, Long acquiescenceResumeId) {
        EntityWrapper entityWrapper = new EntityWrapper();
        entityWrapper.eq("undergraduate_id", undergraduateId);
        List<ResumePo> resumePoList = super.selectList(entityWrapper);
        return resumePoList.parallelStream().map(e -> resumePoToVo((ResumePo) e, acquiescenceResumeId)).collect(Collectors.toList());
    }

    @Override
    public ResumeInfoVo getResumeInfoVoById(Long resumeId, Long undergraduateId) {
        ResumePo resumePo = checkResumePo(resumeId, undergraduateId);
        ResumeInfoVo resumeInfoVo = new ResumeInfoVo();
        BeanUtil.copyProperties(resumePo, resumeInfoVo);
        resumeInfoVo.setPoliticsStatus(resumePo.getPoliticsStatus().getName());
        Date birthDate = resumePo.getBirthDate();
        Calendar now = Calendar.getInstance();
        String currentYear = String.valueOf(now.get(Calendar.YEAR));
//        logger.error(currentYear);
        String dateStr = DateUtil.formatDate(birthDate, "yyyy-MM-dd HH:mm:ss");
//        logger.error(dateStr);
        String year = dateStr.substring(0, 4);
//        logger.error(year);
        resumeInfoVo.setAge(Integer.valueOf(currentYear) - Integer.valueOf(year));
        return resumeInfoVo;
    }

    @Override
    public Boolean updateResumeBasicById(ResumeDto resumeDto, HttpSession httpSession) throws ParseException {
        UndergraduatePo undergraduatePo = (UndergraduatePo) httpSession.getAttribute("undergraduatePo");
        checkResumePo(resumeDto.getId(), undergraduatePo.getId());
        ResumePo resumePo = resumeDtoToPo(resumeDto, httpSession, false);
        return super.updateById(resumePo);
    }

    public ResumePo checkResumePo(Long resumeId, Long undergraduateId){
        EntityWrapper entityWrapper = new EntityWrapper();
        entityWrapper.eq("undergraduate_id", undergraduateId);
        entityWrapper.eq("id", resumeId);
        ResumePo resumePo = super.selectOne(entityWrapper);
        if(resumePo == null){
            throw JobException.NULL_RESUME_EXCEPTION;
        }
        return resumePo;
    }
}

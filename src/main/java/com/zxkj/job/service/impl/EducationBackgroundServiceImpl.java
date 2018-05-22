package com.zxkj.job.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.zxkj.job.bean.dto.EducationBackgroundDto;
import com.zxkj.job.bean.dto.PageDto;
import com.zxkj.job.bean.dto.ResumeDto;
import com.zxkj.job.bean.po.EducationBackgroundPo;
import com.zxkj.job.bean.po.ResumePo;
import com.zxkj.job.bean.po.UndergraduatePo;
import com.zxkj.job.bean.vo.EducationBackgroundVo;
import com.zxkj.job.bean.vo.ResumeVo;
import com.zxkj.job.common.BaseServiceImpl;
import com.zxkj.job.common.bean.PagedResult;
import com.zxkj.job.exp.JobException;
import com.zxkj.job.mapper.EducationBackgroundMapper;
import com.zxkj.job.mapper.ResumeMapper;
import com.zxkj.job.service.EducationBackgroundService;
import com.zxkj.job.service.ResumeService;
import com.zxkj.job.service.UndergraduateService;
import com.zxkj.job.util.BeanUtil;
import com.zxkj.job.util.DateUtil;
import com.zxkj.job.util.IdUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpSession;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


@Service
@Transactional
public class EducationBackgroundServiceImpl extends BaseServiceImpl<EducationBackgroundMapper, EducationBackgroundPo> implements EducationBackgroundService {

    @Override
    public Boolean add(EducationBackgroundDto educationBackgroundDto) throws ParseException {
        EducationBackgroundPo educationBackgroundPo = new EducationBackgroundPo();
        BeanUtil.copyProperties(educationBackgroundDto, educationBackgroundPo);
        educationBackgroundPo.setStartTime(DateUtil.formatDate(educationBackgroundDto.getStartTime(), "yyyy-MM"));
        educationBackgroundPo.setEndTime(DateUtil.formatDate(educationBackgroundDto.getEndTime(), "yyyy-MM"));
        return super.insert(educationBackgroundPo);
    }

    @Override
    public PagedResult list(PageDto pageDto, Long resumeId) {
            Page page = new Page(pageDto.getPage(), pageDto.getLimit());
            EntityWrapper entityWrapper = new EntityWrapper();
            entityWrapper.eq("resume_id", resumeId);
            Page page1 = super.selectPage(page, entityWrapper);
            PagedResult pagedResult = new PagedResult();
            List<EducationBackgroundVo> educationBackgroundVos = (List<EducationBackgroundVo>) page1.getRecords().parallelStream().map(e -> educationBackgroundPoToVo((EducationBackgroundPo) e)).collect(Collectors.toList());
            pagedResult.setData(educationBackgroundVos);
            pagedResult.setCount(page1.getTotal());
            return pagedResult;
    }

    @Override
    public Boolean deleteByEducationBackgroundId(Long educationBackgroundId) {
        checkEducationBackgroundPo(educationBackgroundId);
        return super.deleteById(educationBackgroundId);
    }

    @Override
    public EducationBackgroundVo selectOneById(Long educationBackgroundId) {
        EducationBackgroundPo educationBackgroundPo = checkEducationBackgroundPo(educationBackgroundId);
        return educationBackgroundPoToVo(educationBackgroundPo);
    }

    @Override
    public Boolean updateById(EducationBackgroundDto educationBackgroundDto) throws ParseException {
        checkEducationBackgroundPo(educationBackgroundDto.getId());
        EducationBackgroundPo educationBackgroundPo = new EducationBackgroundPo();
        BeanUtil.copyProperties(educationBackgroundDto, educationBackgroundPo);
        educationBackgroundPo.setStartTime(DateUtil.formatDate(educationBackgroundDto.getStartTime(), "yyyy-MM"));
        educationBackgroundPo.setEndTime(DateUtil.formatDate(educationBackgroundDto.getEndTime(), "yyyy-MM"));
        return super.updateById(educationBackgroundPo);
    }

    @Override
    public List<EducationBackgroundVo> listByResumeId(Long resumeId) {
        EntityWrapper entityWrapper = new EntityWrapper();
        entityWrapper.eq("resume_id", resumeId);
        List<EducationBackgroundPo> educationBackgroundPoList = super.selectList(entityWrapper);
        return educationBackgroundPoList.parallelStream().map(e -> educationBackgroundPoToVo((EducationBackgroundPo) e)).collect(Collectors.toList());
    }

    public EducationBackgroundPo checkEducationBackgroundPo(Long educationBackgroundId){
        if(StringUtils.isEmpty(educationBackgroundId)){
            throw JobException.NULL_ID_EXCEPTION;
        }
        EducationBackgroundPo educationBackgroundPo = super.selectById(educationBackgroundId);
        if(educationBackgroundPo == null){
            throw JobException.NULL_EDUCATION_BACKGROUND_EXCEPTION;
        }
        return educationBackgroundPo;
    }

    public EducationBackgroundVo educationBackgroundPoToVo(EducationBackgroundPo educationBackgroundPo){
        EducationBackgroundVo educationBackgroundVo = new EducationBackgroundVo();
        BeanUtil.copyProperties(educationBackgroundPo, educationBackgroundVo);
        educationBackgroundVo.setEducationBackground(educationBackgroundPo.getEducationBackground().getName());
        educationBackgroundVo.setRanking(educationBackgroundPo.getRanking().getName());
        educationBackgroundVo.setProvince(educationBackgroundPo.getProvince().getName());
        educationBackgroundVo.setStartTime(DateUtil.formatDate(educationBackgroundPo.getStartTime(), "yyyy-MM"));
        educationBackgroundVo.setEndTime(DateUtil.formatDate(educationBackgroundPo.getEndTime(), "yyyy-MM"));
        return educationBackgroundVo;
    }
}

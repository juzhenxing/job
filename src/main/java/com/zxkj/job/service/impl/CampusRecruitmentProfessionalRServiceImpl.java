package com.zxkj.job.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.zxkj.job.bean.dto.CampusRecruitmentDto;
import com.zxkj.job.bean.dto.PageDto;
import com.zxkj.job.bean.dto.QueryCampusRecruitmentDto;
import com.zxkj.job.bean.dto.QueryProfessionalDto;
import com.zxkj.job.bean.po.CampusRecruitmentPo;
import com.zxkj.job.bean.po.CampusRecruitmentProfessionalRPo;
import com.zxkj.job.bean.vo.CampusRecruitmentVo;
import com.zxkj.job.bean.vo.DeliveryInformationVo;
import com.zxkj.job.bean.vo.ProfessionalVo;
import com.zxkj.job.common.BaseServiceImpl;
import com.zxkj.job.common.bean.PagedResult;
import com.zxkj.job.enums.JobCategoryType;
import com.zxkj.job.exp.JobException;
import com.zxkj.job.mapper.CampusRecruitmentMapper;
import com.zxkj.job.mapper.CampusRecruitmentProfessionalRMapper;
import com.zxkj.job.service.CampusRecruitmentProfessionalRService;
import com.zxkj.job.service.CampusRecruitmentService;
import com.zxkj.job.service.DeliveryInformationService;
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
public class CampusRecruitmentProfessionalRServiceImpl extends BaseServiceImpl<CampusRecruitmentProfessionalRMapper, CampusRecruitmentProfessionalRPo> implements CampusRecruitmentProfessionalRService {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    DeliveryInformationService deliveryInformationService;

    @Autowired
    CampusRecruitmentService campusRecruitmentService;

    @Override
    public List<CampusRecruitmentProfessionalRPo> listByCampusRecruitmentId(Long campusRecruitmentId) {
        EntityWrapper<CampusRecruitmentProfessionalRPo> ew = new EntityWrapper<>();
        ew.eq("campus_recruitment_id", campusRecruitmentId);
        return super.selectList(ew);
    }

    @Override
    public Boolean deleteByCampusRecruitmentId(Long campusRecruitmentId) {
        EntityWrapper<CampusRecruitmentProfessionalRPo> ew = new EntityWrapper<>();
        ew.eq("campus_recruitment_id", campusRecruitmentId);
        return super.delete(ew);
    }

    @Override
    public Boolean deleteByProfessionalIds(Long campusRecruitmentId, List<Long> professionalIds) {
        EntityWrapper<CampusRecruitmentProfessionalRPo> ew = new EntityWrapper<>();
        ew.eq("campus_recruitment_id", campusRecruitmentId);
        ew.in("professional_id", professionalIds);
        return super.delete(ew);
    }

    @Override
    public PagedResult listCampusRecruitmentByQueryCampusRecruitmentDto(QueryCampusRecruitmentDto queryCampusRecruitmentDto, PageDto pageDto) {
        Integer startLine = (pageDto.getPage() - 1) * pageDto.getLimit();
        List<CampusRecruitmentPo> campusRecruitmentPos = super.baseMapper.selectPageByQueryCampusRecruitmentDto(queryCampusRecruitmentDto, startLine, pageDto.getLimit());
        List<CampusRecruitmentVo> campusRecruitmentVos = campusRecruitmentPos.parallelStream().map(e -> campusRecruitmentService.campusRecruitmentPoToVo(e)).collect(Collectors.toList());
        PagedResult<CampusRecruitmentVo> pagedResult = new PagedResult<>();
        pagedResult.setData(campusRecruitmentVos);
        pagedResult.setCount(super.baseMapper.selectCountByQueryCampusRecruitmentDto(queryCampusRecruitmentDto));
        return pagedResult;
    }

    @Override
    public List<CampusRecruitmentProfessionalRPo> listByProfessionalId(Long professionalId) {
        EntityWrapper<CampusRecruitmentProfessionalRPo> ew = new EntityWrapper<>();
        ew.eq("professional_id", professionalId);
        return super.selectList(ew);
    }

}

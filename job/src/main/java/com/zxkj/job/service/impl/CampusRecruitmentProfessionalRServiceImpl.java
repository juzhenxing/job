package com.zxkj.job.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.zxkj.job.bean.dto.CampusRecruitmentDto;
import com.zxkj.job.bean.po.CampusRecruitmentPo;
import com.zxkj.job.bean.po.CampusRecruitmentProfessionalRPo;
import com.zxkj.job.common.BaseServiceImpl;
import com.zxkj.job.mapper.CampusRecruitmentMapper;
import com.zxkj.job.mapper.CampusRecruitmentProfessionalRMapper;
import com.zxkj.job.service.CampusRecruitmentProfessionalRService;
import com.zxkj.job.service.CampusRecruitmentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.util.List;

@Service
@Transactional
public class CampusRecruitmentProfessionalRServiceImpl extends BaseServiceImpl<CampusRecruitmentProfessionalRMapper, CampusRecruitmentProfessionalRPo> implements CampusRecruitmentProfessionalRService {

    private Logger logger = LoggerFactory.getLogger(getClass());

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
}

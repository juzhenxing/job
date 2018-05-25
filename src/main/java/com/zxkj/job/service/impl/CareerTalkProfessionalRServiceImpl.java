package com.zxkj.job.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.zxkj.job.bean.dto.PageDto;
import com.zxkj.job.bean.dto.QueryProfessionalDto;
import com.zxkj.job.bean.po.CampusRecruitmentPo;
import com.zxkj.job.bean.po.CampusRecruitmentProfessionalRPo;
import com.zxkj.job.bean.po.CareerTalkProfessionalRPo;
import com.zxkj.job.bean.vo.CampusRecruitmentVo;
import com.zxkj.job.bean.vo.CareerTalkVo;
import com.zxkj.job.bean.vo.DeliveryInformationVo;
import com.zxkj.job.bean.vo.ProfessionalVo;
import com.zxkj.job.common.BaseServiceImpl;
import com.zxkj.job.common.bean.PagedResult;
import com.zxkj.job.exp.JobException;
import com.zxkj.job.mapper.CampusRecruitmentProfessionalRMapper;
import com.zxkj.job.mapper.CareerTalkProfessionalRMapper;
import com.zxkj.job.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CareerTalkProfessionalRServiceImpl extends BaseServiceImpl<CareerTalkProfessionalRMapper, CareerTalkProfessionalRPo> implements CareerTalkProfessionalRService {

    private Logger logger = LoggerFactory.getLogger(getClass());



    @Autowired
    CareerTalkService careerTalkService;

    @Autowired
    DeliveryInformationService deliveryInformationService;

    @Override
    public List<CareerTalkProfessionalRPo> listByCareerTalkId(Long careerTalkId) {
        EntityWrapper entityWrapper = new EntityWrapper();
        entityWrapper.eq("career_talk_id", careerTalkId);
        List<CareerTalkProfessionalRPo> careerTalkProfessionalRPos = super.selectList(entityWrapper);
        return careerTalkProfessionalRPos;
    }

    @Override
    public Boolean deleteByCareerTalkId(Long careerTalkId) {
        List<DeliveryInformationVo> deliveryInformationVoList = deliveryInformationService.getByCareerTalkOrCampusRecruitmentId(careerTalkId);
        if(deliveryInformationVoList != null && deliveryInformationVoList.size() > 0){
            throw JobException.CAREERTALK_PROFESSIONAL_DELETE_EXCEPTION;
        }
        EntityWrapper ew = new EntityWrapper<>();
        ew.eq("career_talk_id", careerTalkId);
        return super.delete(ew);
    }

    @Override
    public Boolean deleteByProfessionalIds(Long careerTalkId, List<Long> professionalIds) {
        EntityWrapper ew = new EntityWrapper<>();
        ew.eq("career_talk_id", careerTalkId);
        ew.in("professional_id", professionalIds);
        return super.delete(ew);
    }
}

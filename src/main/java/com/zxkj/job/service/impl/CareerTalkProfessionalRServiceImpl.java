package com.zxkj.job.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.zxkj.job.bean.dto.PageDto;
import com.zxkj.job.bean.dto.QueryProfessionalDto;
import com.zxkj.job.bean.po.CampusRecruitmentPo;
import com.zxkj.job.bean.po.CampusRecruitmentProfessionalRPo;
import com.zxkj.job.bean.po.CareerTalkProfessionalRPo;
import com.zxkj.job.bean.vo.CampusRecruitmentVo;
import com.zxkj.job.bean.vo.DeliveryInformationVo;
import com.zxkj.job.bean.vo.ProfessionalVo;
import com.zxkj.job.common.BaseServiceImpl;
import com.zxkj.job.common.bean.PagedResult;
import com.zxkj.job.exp.JobException;
import com.zxkj.job.mapper.CampusRecruitmentProfessionalRMapper;
import com.zxkj.job.mapper.CareerTalkProfessionalRMapper;
import com.zxkj.job.service.CampusRecruitmentProfessionalRService;
import com.zxkj.job.service.CampusRecruitmentService;
import com.zxkj.job.service.CareerTalkProfessionalRService;
import com.zxkj.job.service.DeliveryInformationService;
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


}

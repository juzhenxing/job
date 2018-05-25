package com.zxkj.job.service;

import com.baomidou.mybatisplus.service.IService;
import com.zxkj.job.bean.dto.PageDto;
import com.zxkj.job.bean.dto.QueryProfessionalDto;
import com.zxkj.job.bean.po.CampusRecruitmentProfessionalRPo;
import com.zxkj.job.bean.po.CareerTalkProfessionalRPo;
import com.zxkj.job.common.bean.PagedResult;

import java.util.List;

public interface CareerTalkProfessionalRService extends IService<CareerTalkProfessionalRPo> {

    List<CareerTalkProfessionalRPo> listByCareerTalkId(Long careerTalkId);

    Boolean deleteByCareerTalkId(Long careerTalkId);

    Boolean deleteByProfessionalIds(Long careerTalkId, List<Long> professionalIds);

}

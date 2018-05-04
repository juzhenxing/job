package com.zxkj.job.service;

import com.baomidou.mybatisplus.service.IService;
import com.zxkj.job.bean.dto.CampusRecruitmentDto;
import com.zxkj.job.bean.po.CampusRecruitmentPo;
import com.zxkj.job.bean.po.CampusRecruitmentProfessionalRPo;

import javax.servlet.http.HttpSession;
import java.util.List;

public interface CampusRecruitmentProfessionalRService extends IService<CampusRecruitmentProfessionalRPo> {

    List<CampusRecruitmentProfessionalRPo> listByCampusRecruitmentId(Long campusRecruitmentId);

    Boolean deleteByCampusRecruitmentId(Long campusRecruitmentId);

    Boolean deleteByProfessionalIds(Long campusRecruitmentId, List<Long> professionalIds);
}

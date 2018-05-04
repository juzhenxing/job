package com.zxkj.job.service;

import com.baomidou.mybatisplus.service.IService;
import com.zxkj.job.bean.dto.CampusRecruitmentDto;
import com.zxkj.job.bean.dto.PageDto;
import com.zxkj.job.bean.dto.ProfessionalDto;
import com.zxkj.job.bean.po.CampusRecruitmentPo;
import com.zxkj.job.bean.po.ProfessionalPo;
import com.zxkj.job.bean.vo.CampusRecruitmentVo;
import com.zxkj.job.bean.vo.ProfessionalUpdateVo;
import com.zxkj.job.bean.vo.ProfessionalVo;
import com.zxkj.job.common.bean.PagedResult;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

public interface CampusRecruitmentService extends IService<CampusRecruitmentPo> {

    Boolean add(CampusRecruitmentDto campusRecruitmentDto, HttpSession httpSession) throws IOException;

    PagedResult list(PageDto pageDto, Long enterpriseId);

    Boolean deleteByCampusRecruitmentId(Long campusRecruitmentId, HttpSession httpSession);

    CampusRecruitmentVo getById(Long campusRecruitmentId, HttpSession httpSession);

    ResponseEntity<InputStreamResource> downloadGeneralRegulation(String generalRegulationFileName) throws IOException;

    Boolean updateById(CampusRecruitmentDto campusRecruitmentDto, HttpSession httpSession) throws IOException;
}

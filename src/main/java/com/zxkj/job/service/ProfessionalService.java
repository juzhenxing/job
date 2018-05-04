package com.zxkj.job.service;

import com.baomidou.mybatisplus.service.IService;
import com.zxkj.job.bean.dto.*;
import com.zxkj.job.bean.po.EnterprisePo;
import com.zxkj.job.bean.po.ProfessionalPo;
import com.zxkj.job.bean.vo.EnterpriseVo;
import com.zxkj.job.bean.vo.ProfessionalUpdateVo;
import com.zxkj.job.bean.vo.ProfessionalVo;
import com.zxkj.job.common.bean.PagedResult;

import javax.mail.MessagingException;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

public interface ProfessionalService extends IService<ProfessionalPo> {

    Boolean add(ProfessionalDto professionalDto, HttpSession httpSession);

    PagedResult list(PageDto pageDto, Long enterpriseId);

    Boolean deleteByProfessionalId(Long professionalId, HttpSession httpSession);

    ProfessionalVo getById(Long professionalId, HttpSession httpSession);

    ProfessionalUpdateVo getProfessionalUpdateVoById(Long professionalId, HttpSession httpSession);

    Boolean updateById(ProfessionalDto professionalDto, HttpSession httpSession);

    List<ProfessionalVo> list(Long enterpriseId);

    List<ProfessionalVo> listByProfessionalIds(List<Long> professionalIds);
}

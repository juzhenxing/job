package com.zxkj.job.service;

import com.baomidou.mybatisplus.service.IService;
import com.zxkj.job.bean.dto.CollectDto;
import com.zxkj.job.bean.dto.PageDto;
import com.zxkj.job.bean.po.CampusRecruitmentProfessionalRPo;
import com.zxkj.job.bean.po.CollectPo;
import com.zxkj.job.bean.vo.CollectVo;
import com.zxkj.job.common.bean.PagedResult;
import com.zxkj.job.enums.CollectType;

import javax.servlet.http.HttpSession;
import java.util.List;

public interface CollectService extends IService<CollectPo> {

    Boolean add(CollectDto collectDto, HttpSession httpSession);

    CollectPo checkByCareerTalkIdOrCampusRecruitmentId(CollectType collectType, Long id, Long undergraduateId);

    PagedResult list(PageDto pageDto, HttpSession httpSession);

    Boolean deleteByCollectId(Long collectId);

    List<CollectVo> listByCareerTalkOrCampusRecruitmentId(CollectType type, Long careerTalkOrCampusRecruitmentId);

}

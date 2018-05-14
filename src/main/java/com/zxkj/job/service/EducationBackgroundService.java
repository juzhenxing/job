package com.zxkj.job.service;

import com.baomidou.mybatisplus.service.IService;
import com.zxkj.job.bean.dto.EducationBackgroundDto;
import com.zxkj.job.bean.dto.PageDto;
import com.zxkj.job.bean.dto.ResumeDto;
import com.zxkj.job.bean.po.EducationBackgroundPo;
import com.zxkj.job.bean.po.ResumePo;
import com.zxkj.job.bean.vo.EducationBackgroundVo;
import com.zxkj.job.bean.vo.ResumeVo;
import com.zxkj.job.common.bean.PagedResult;

import javax.servlet.http.HttpSession;
import java.text.ParseException;

public interface EducationBackgroundService extends IService<EducationBackgroundPo> {

    Boolean add(EducationBackgroundDto educationBackgroundDto) throws ParseException;

    PagedResult list(PageDto pageDto, Long resumeId);

    Boolean deleteByEducationBackgroundId(Long educationBackgroundId);

    EducationBackgroundVo selectOneById(Long educationBackgroundId);

    Boolean updateById(EducationBackgroundDto educationBackgroundDto) throws ParseException;

}

package com.zxkj.job.service;

import com.baomidou.mybatisplus.service.IService;
import com.zxkj.job.bean.dto.AdministratorDto;
import com.zxkj.job.bean.dto.LoginAdministratorDto;
import com.zxkj.job.bean.dto.PageDto;
import com.zxkj.job.bean.dto.ResumeDto;
import com.zxkj.job.bean.po.AdministratorPo;
import com.zxkj.job.bean.po.ResumePo;
import com.zxkj.job.common.bean.PagedResult;
import com.zxkj.job.enums.CheckStateType;

import javax.mail.MessagingException;
import javax.servlet.http.HttpSession;
import java.text.ParseException;

public interface ResumeService extends IService<ResumePo> {

    Boolean add(ResumeDto resumeDto, HttpSession httpSession) throws ParseException;

    PagedResult list(PageDto pageDto, HttpSession httpSession);

    Boolean deleteByResumeId(Long resumeId, HttpSession httpSession);

}

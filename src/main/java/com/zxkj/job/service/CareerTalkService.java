package com.zxkj.job.service;

import com.baomidou.mybatisplus.service.IService;
import com.zxkj.job.bean.dto.CareerTalkDto;
import com.zxkj.job.bean.dto.PageDto;
import com.zxkj.job.bean.dto.QueryCareerTalkDto;
import com.zxkj.job.bean.dto.SimpleUndergraduateDto;
import com.zxkj.job.bean.po.CareerTalkPo;
import com.zxkj.job.bean.po.UndergraduatePo;
import com.zxkj.job.bean.vo.CareerTalkUpdateVo;
import com.zxkj.job.common.bean.PagedResult;
import com.zxkj.job.enums.ProvinceType;

import javax.mail.MessagingException;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.ParseException;

public interface CareerTalkService extends IService<CareerTalkPo> {

    Boolean add(CareerTalkDto careerTalkDto, HttpSession httpSession) throws ParseException, IOException;

    PagedResult list(PageDto pageDto, Long enterpriseId);

    Boolean deleteByCareerTalkId(Long careerTalkId, HttpSession httpSession);

    CareerTalkUpdateVo selectOneById(Long careerTalkId, HttpSession httpSession);

    Boolean updateById(CareerTalkDto careerTalkDto, HttpSession httpSession) throws ParseException;

    PagedResult list(PageDto pageDto);

    PagedResult listByQueryDto(QueryCareerTalkDto queryCareerTalkDto, PageDto pageDto);

}

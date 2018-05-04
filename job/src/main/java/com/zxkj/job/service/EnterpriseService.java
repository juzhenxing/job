package com.zxkj.job.service;

import com.baomidou.mybatisplus.service.IService;
import com.zxkj.job.bean.dto.*;
import com.zxkj.job.bean.po.EnterprisePo;
import com.zxkj.job.bean.vo.EnterpriseVo;

import javax.mail.MessagingException;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

public interface EnterpriseService extends IService<EnterprisePo> {

    /**
     * 注册
     *
     * @param simpleEnterpriseDto
     * @return
     */
    String add(SimpleEnterpriseDto simpleEnterpriseDto) throws MessagingException;

    String register(String email, String code);

    EnterprisePo login(LoginEnterpriseDto loginEnterpriseDto);

    Boolean requestPasswordReset(String email) throws MessagingException;

    String resetPassword(String email, String code);

    Boolean resetPasswordNext(String password, HttpSession httpSession);

    /**
     * 根据userName查询企业信息
     *
     * @param userName
     * @return
     */
    EnterprisePo selectOneByUserName(String userName);

    Boolean hrInfoComplete(EnterpriseDto enterpriseDto, HttpSession httpSession) throws IOException;

    List<EnterpriseVo> selectPageByQueryEnterpriseDto(Integer startLine, Integer pageSize);

    Integer selectCountByQueryEnterpriseDto();

    EnterpriseVo getById(Long enterpriseId);

}

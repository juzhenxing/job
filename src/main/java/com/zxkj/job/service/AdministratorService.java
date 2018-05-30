package com.zxkj.job.service;

import com.baomidou.mybatisplus.service.IService;
import com.zxkj.job.bean.dto.*;
import com.zxkj.job.bean.po.AdministratorPo;
import com.zxkj.job.bean.vo.AdministratorVo;
import com.zxkj.job.common.bean.PagedResult;
import com.zxkj.job.enums.CheckStateType;

import javax.mail.MessagingException;
import javax.servlet.http.HttpSession;

public interface AdministratorService extends IService<AdministratorPo> {

    /**
     * 注册
     *
     * @param administratorDto
     * @return
     */
    AdministratorPo register(AdministratorDto administratorDto) throws MessagingException;

    AdministratorPo register(String email, String code);

    AdministratorPo login(LoginAdministratorDto loginAdministratorDto);

    /**
     * 管理员验证身份
     * @param email
     * @return
     * @throws MessagingException
     */
    String checkIdentity(String email) throws MessagingException;

    Boolean resetPassword(String code, HttpSession httpSession);

    Boolean setSuccess(String password, HttpSession httpSession);

    PagedResult checkApplication(PageDto pageDto);

    Boolean checkApplication(Long enterpriseId, CheckStateType checkStateType) throws MessagingException;

    AdministratorVo getByUsernameOrEmail(String usernameOrEmail);

}

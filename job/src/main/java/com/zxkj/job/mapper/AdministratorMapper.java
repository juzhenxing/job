package com.zxkj.job.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.zxkj.job.bean.dto.LoginAdministratorDto;
import com.zxkj.job.bean.dto.LoginEnterpriseDto;
import com.zxkj.job.bean.po.AdministratorPo;
import com.zxkj.job.bean.po.EnterprisePo;
import org.apache.ibatis.annotations.Param;

public interface AdministratorMapper extends BaseMapper<AdministratorPo> {

    /**
     * 根据userName查询管理员信息
     *
     * @param userName
     * @return
     */
    AdministratorPo selectOneByUserName(String userName);

    /**
     * 根据email查询管理员信息
     *
     * @param email
     * @return
     */
    AdministratorPo selectOneByEmail(String email);

    /**
     * 根据usernameOrEmail查询管理员信息
     *
     * @param loginAdministratorDto
     * @return
     */
    AdministratorPo selectOneByUsernameOrEmail(@Param("loginAdministratorDto") LoginAdministratorDto loginAdministratorDto);

}

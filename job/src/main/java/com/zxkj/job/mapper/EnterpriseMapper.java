package com.zxkj.job.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.zxkj.job.bean.dto.LoginEnterpriseDto;
import com.zxkj.job.bean.po.EnterprisePo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface EnterpriseMapper extends BaseMapper<EnterprisePo> {

    /**
     * 根据email查询企业信息
     *
     * @param email
     * @return
     */
    EnterprisePo selectOneByEmail(String email);

    /**
     * 根据userName查询企业信息
     *
     * @param userName
     * @return
     */
    EnterprisePo selectOneByUserName(String userName);

    /**
     * 根据usernameOrEmail查询企业信息
     *
     * @param loginEnterpriseDto
     * @return
     */
    EnterprisePo selectOneByUsernameOrEmail(@Param("loginEnterpriseDto") LoginEnterpriseDto loginEnterpriseDto);

    List<EnterprisePo> selectPageByQueryEnterpriseDto(
            @Param("startLine") Integer startLine,
            @Param("pageSize") Integer pageSize);

    Integer selectCountByQueryEnterpriseDto();
}

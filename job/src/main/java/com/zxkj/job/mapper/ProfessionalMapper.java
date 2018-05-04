package com.zxkj.job.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.zxkj.job.bean.po.ProfessionalPo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProfessionalMapper extends BaseMapper<ProfessionalPo> {

    List<ProfessionalPo> selectPageByEnterpriseId(
            @Param("enterpriseId") Long enterpriseId,
            @Param("startLine") Integer startLine,
            @Param("pageSize") Integer pageSize);

    Integer selectCountByEnterpriseId(@Param("enterpriseId") Long enterpriseId);

    /**
     *
     * @param id
     * @param enterpriseId
     * @return
     */
    ProfessionalPo selectOneById(
            @Param("id") Long id,
            @Param("enterpriseId") Long enterpriseId);

    /**
     *
     * @param enterpriseId
     * @return
     */
    List<ProfessionalPo> selectByEnterpriseId(Long enterpriseId);
}

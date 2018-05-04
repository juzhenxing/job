package com.zxkj.job.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.zxkj.job.bean.po.CareerTalkPo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CareerTalkMapper extends BaseMapper<CareerTalkPo> {

    List<CareerTalkPo> selectPageByEnterpriseId(
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
    CareerTalkPo selectOneById(
            @Param("id") Long id,
            @Param("enterpriseId") Long enterpriseId);

}

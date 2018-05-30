package com.zxkj.job.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.zxkj.job.bean.dto.QueryCampusRecruitmentDto;
import com.zxkj.job.bean.dto.QueryProfessionalDto;
import com.zxkj.job.bean.po.CampusRecruitmentPo;
import com.zxkj.job.bean.po.CampusRecruitmentProfessionalRPo;
import com.zxkj.job.enums.JobCategoryType;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CampusRecruitmentProfessionalRMapper extends BaseMapper<CampusRecruitmentProfessionalRPo> {

    List<CampusRecruitmentPo> selectPageByQueryCampusRecruitmentDto(
            @Param("queryCampusRecruitmentDto") QueryCampusRecruitmentDto queryCampusRecruitmentDto,
            @Param("startLine") Integer startLine,
            @Param("pageSize") Integer pageSize);

    Integer selectCountByQueryCampusRecruitmentDto(@Param("queryCampusRecruitmentDto") QueryCampusRecruitmentDto queryCampusRecruitmentDto);
}

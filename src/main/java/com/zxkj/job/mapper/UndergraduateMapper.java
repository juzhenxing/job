package com.zxkj.job.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.zxkj.job.bean.po.UndergraduatePo;

public interface UndergraduateMapper extends BaseMapper<UndergraduatePo> {

    /**
     * 根据email查询学生信息
     * @param email
     * @return
     */
    UndergraduatePo selectOneByEmail(String email);

}
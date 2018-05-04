package com.zxkj.job.common;

import com.baomidou.mybatisplus.service.IService;
import com.zxkj.job.common.bean.BasePo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class BaseControllerImpl<S extends IService<T>, T extends BasePo<T>> {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    protected S service;

}

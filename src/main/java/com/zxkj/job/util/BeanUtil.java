package com.zxkj.job.util;

import com.zxkj.job.common.bean.BasePo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.beans.PropertyDescriptor;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * bean转换工具类
 * Created by jzx on 2017/11/15.
 */
public class BeanUtil {

    private static final Logger logger = LoggerFactory.getLogger(BeanUtil.class);

    /**
     * 加载执行时间
     * @param t
     * @param <T>
     */
//    public static <T extends BaseCacheBean> void loadExecTime(T t) {
//        t.setExecTime(new Date());
//    }

    /**
     * 插入数据 装载id和时间
     *
     * @param entity
     * @param <T>
     */
    public static <T extends BasePo<T>> void load(T entity) {
        if (entity == null) {
            return;
        }
        if (entity.getId() == null) {
            IdUtil.loadId(entity);
            loadTime(entity);
        } else {
            refreshTime(entity);
        }
    }

    /**
     * 插入多条数据 装载id和时间
     *
     * @param entities
     * @param <T>
     */
    public static <T extends BasePo<T>> void loads(List<T> entities) {
        for (T entity : entities) {
            if (entity == null) {
                continue;
            }
            load(entity);
        }
    }

    /**
     * 加载时间
     *
     * @param t
     * @param <T>
     */
    public static <T extends BasePo<T>> void loadTime(T t) {
        if (t.getGmtCreate() == null) {
            t.setGmtCreate(new Date());
        }
        t.setGmtModified(t.getGmtCreate());
    }

    /**
     * 更新更新时间
     *
     * @param t
     * @param <T>
     */
    public static <T extends BasePo> void refreshTime(T t) {
        t.setGmtModified(new Date());
    }

    /**
     * 复制对象
     *
     * @param source
     * @param target
     */
    public static void copyProperties(Object source, Object target) {
        if (source == null || target == null) {
            logger.warn("请勿输入空对象");
            return;
        }

        try {
            BeanUtils.copyProperties(source, target);
        } catch (Exception e) {
            logger.warn("转换[{}]失败，请自行转换[{}]", source.getClass(), target.getClass());
        }

    }

    /**
     * 复制Map到对象
     */
    public static <T> void copyMapProperties(Map<String, Object> map, T t) {
        BeanWrapper beanWrapper = new BeanWrapperImpl(t);
        beanWrapper.setPropertyValues(map);
    }

    /**
     * 复制对象到Map
     */
    public static <T> void copyPropertiesMap(T t, Map<String, Object> map) {
        BeanWrapper beanWrapper = new BeanWrapperImpl(t);
        PropertyDescriptor[] descriptor = beanWrapper.getPropertyDescriptors();
        String name;
        for (int i = 0; i < descriptor.length; i++) {
            name = descriptor[i].getName();
            if ("class".equalsIgnoreCase(name)) {
                continue;
            }
            map.put(name, beanWrapper.getPropertyValue(name));
        }
    }

}

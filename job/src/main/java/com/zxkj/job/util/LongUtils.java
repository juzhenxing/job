package com.zxkj.job.util;

import org.springframework.util.StringUtils;

/**
 * Long转换String工具类
 * Created by juzhenxing on 2018/01/08
 */
public class LongUtils {

    /**
     * 将Long类型的id转换为String类型
     * @param id
     * @return
     */
    public static String LongToString(Long id){
        if(!StringUtils.isEmpty(id)){
            return id.toString();
        }else{
            return null;
        }
    }

    /**
     * 将String类型的id转换为Long类型
     * @param id
     * @return
     */
    public static Long StringToLong(String id){
        if(!StringUtils.isEmpty(id)){
            return Long.parseLong(id);
        }else{
            return null;
        }
    }
}

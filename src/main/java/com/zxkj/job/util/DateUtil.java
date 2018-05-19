package com.zxkj.job.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author juzhenxing 2018/2/28 19:21
 */
@Component
public class DateUtil {

    /**
     * 将日期对象转换为指定的日期字符串
     * @param date  传入的日期对象
     * @param format 格式
     * @return 日期字符串
     */
    public static String formatDate(Date date, String format){
        String string="";
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat(format);
        if(date!=null){
            string=simpleDateFormat.format(date);
        }
        return string;
    }
    /**
     * 将日期字符串转换为一个日期对象
     * @param datestr 日期字符串
     * @param format 格式
     * @return Date
     * @throws ParseException
     */
    public static Date formatDate(String datestr, String format) throws ParseException {
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat(format);
        return  simpleDateFormat.parse(datestr);
    }

}
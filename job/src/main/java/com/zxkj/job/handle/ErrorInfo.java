package com.zxkj.job.handle;

import lombok.Data;

/**
 * @author juzhenxing 2018/3/31 20:01
 */
@Data
public class ErrorInfo<T> {

    public static final Integer OK = 0;
    public static final Integer ERROR = 100;

    private Integer code;
    private String message;
    private String url;
    private T data;

}
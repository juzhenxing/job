package com.zxkj.job.common.exp;

public abstract class BaseException extends RuntimeException {

    public BaseException(Object object) {
        super(object.toString());
    }
}

package com.zxkj.job.common.bean;

import lombok.Data;

import java.util.List;

@Data
public class PagedResult<T> {

    private List<T> data;

    private long count;

    private Integer code = 0;

    private String msg = "";

}

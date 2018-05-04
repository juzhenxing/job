package com.zxkj.job.enums;

import com.baomidou.mybatisplus.enums.IEnum;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

@Getter
public enum CheckStateType implements IEnum {

    UNCHECKED(1, "未审核"),
    PASS(2, "审核通过"),
    NO_PASS(3, "审核不通过")
    ;

    private int type;
    private String name;

    CheckStateType(int type, String name) {
        this.type = type;
        this.name = name;
    }

    @Override
    public Serializable getValue() {
        return type;
    }

    public static CheckStateType select(String name) {
        if(StringUtils.isBlank(name)) {
            return null;
        }
        for(CheckStateType statusType : CheckStateType.values()) {
            if(name.equals(statusType.getName())) {
                return statusType;
            }
        }
        return null;
    }

}

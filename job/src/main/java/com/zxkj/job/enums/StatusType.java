package com.zxkj.job.enums;

import com.baomidou.mybatisplus.enums.IEnum;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

@Getter
public enum StatusType implements IEnum {

    POSTED(1, "已投递")
    ;

    private int type;
    private String name;

    StatusType(int type, String name) {
        this.type = type;
        this.name = name;
    }

    @Override
    public Serializable getValue() {
        return type;
    }

    public static StatusType select(String name) {
        if(StringUtils.isBlank(name)) {
            return null;
        }
        for(StatusType statusType : StatusType.values()) {
            if(name.equals(statusType.getName())) {
                return statusType;
            }
        }
        return null;
    }

}

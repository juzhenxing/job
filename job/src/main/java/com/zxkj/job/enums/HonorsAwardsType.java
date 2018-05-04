package com.zxkj.job.enums;

import com.baomidou.mybatisplus.enums.IEnum;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

@Getter
public enum HonorsAwardsType implements IEnum {

    WORLD(1, "世界级"),

    NATIONAL(2, "国家级"),

    PROVINCIAL(3, "省部级"),

    SCHOOL(4, "校级奖"),

    OTHERS(5, "其他奖")
    ;

    private int type;
    private String name;

    HonorsAwardsType(int type, String name) {
        this.type = type;
        this.name = name;
    }

    @Override
    public Serializable getValue() {
        return type;
    }

    public static HonorsAwardsType select(String name) {
        if(StringUtils.isBlank(name)) {
            return null;
        }
        for(HonorsAwardsType honorsAwardsType : HonorsAwardsType.values()) {
            if(name.equals(honorsAwardsType.getName())) {
                return honorsAwardsType;
            }
        }
        return null;
    }
}

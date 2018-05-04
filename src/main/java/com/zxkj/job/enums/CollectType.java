package com.zxkj.job.enums;

import com.baomidou.mybatisplus.enums.IEnum;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

@Getter
public enum CollectType implements IEnum {

    CAREERTALK(1, "宣讲会"),

    CAMPUSRECRUITMENT(2, "校招")
    ;

    private int type;
    private String name;

    CollectType(int type, String name) {
        this.type = type;
        this.name = name;
    }

    @Override
    public Serializable getValue() {
        return type;
    }

    public static CollectType select(String name) {
        if(StringUtils.isBlank(name)) {
            return null;
        }
        for(CollectType collectType : CollectType.values()) {
            if(name.equals(collectType.getName())) {
                return collectType;
            }
        }
        return null;
    }

}

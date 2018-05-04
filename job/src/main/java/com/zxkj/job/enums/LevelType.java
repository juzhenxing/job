package com.zxkj.job.enums;

import com.baomidou.mybatisplus.enums.IEnum;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

@Getter
public enum LevelType implements IEnum {

    ORDINARY(1, "一班"),

    KNOWWELL(2, "熟悉"),

    MASTER(3, "精通")
    ;

    private int type;
    private String name;

    LevelType(int type, String name) {
        this.type = type;
        this.name = name;
    }

    @Override
    public Serializable getValue() {
        return type;
    }

    public static LevelType select(String name) {
        if(StringUtils.isBlank(name)) {
            return null;
        }
        for(LevelType levelType : LevelType.values()) {
            if(name.equals(levelType.getName())) {
                return levelType;
            }
        }
        return null;
    }

}

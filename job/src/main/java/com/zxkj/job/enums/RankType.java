package com.zxkj.job.enums;

import com.baomidou.mybatisplus.enums.IEnum;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

@Getter
public enum RankType implements IEnum {

    TEN(1, "前10%"),

    THIRTY(2, "前30%"),

    FIFTY(3, "前50%"),

    OTHERS(4, "其他"),
    ;

    private int type;
    private String name;

    RankType(int type, String name) {
        this.type = type;
        this.name = name;
    }

    @Override
    public Serializable getValue() {
        return type;
    }

    public static RankType select(String name) {
        if(StringUtils.isBlank(name)) {
            return null;
        }
        for(RankType rankType : RankType.values()) {
            if(name.equals(rankType.getName())) {
                return rankType;
            }
        }
        return null;
    }

}

package com.zxkj.job.enums;

import com.baomidou.mybatisplus.enums.IEnum;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

@Getter
public enum OperationType implements IEnum {

    ORDINARY(1, "投简历"),

    KNOWWELL(2, "网申")
    ;

    private int type;
    private String name;

    OperationType(int type, String name) {
        this.type = type;
        this.name = name;
    }

    @Override
    public Serializable getValue() {
        return type;
    }

    public static OperationType select(String name) {
        if(StringUtils.isBlank(name)) {
            return null;
        }
        for(OperationType levelType : OperationType.values()) {
            if(name.equals(levelType.getName())) {
                return levelType;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return "{" + this.name() + ":" + this.type +'}';
    }

}

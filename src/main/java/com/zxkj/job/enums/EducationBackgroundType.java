package com.zxkj.job.enums;

import com.baomidou.mybatisplus.enums.IEnum;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

@Getter
public enum EducationBackgroundType implements IEnum {

    SENIOR(1, "高中"), JUNIOR(2, "大专"), UNDERGRADUATE(3, "本科"), MASTER(4, "硕士"), DOCTOR(5, "博士");

    private int type;
    private String name;

    EducationBackgroundType(int type, String name) {
        this.type = type;
        this.name = name;
    }

    @Override
    public Serializable getValue() {
        return type;
    }

    public static EducationBackgroundType select(String name) {
        if(StringUtils.isBlank(name)) {
            return null;
        }
        for(EducationBackgroundType educationBackgroundType : EducationBackgroundType.values()) {
            if(name.equals(educationBackgroundType.getName())) {
                return educationBackgroundType;
            }
        }
        return null;
    }

}

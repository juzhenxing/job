package com.zxkj.job.enums;

import com.baomidou.mybatisplus.enums.IEnum;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

@Getter
public enum LanguageType implements IEnum {

    ENGLISH(1, "英语"),

    JAPANESE(2, "日语"),

    FRENCH(3, "法语"),

    GERMAN(4, "德语"),

    RUSSIAN(5, "俄语"),

    KOREAN(6, "韩语"),

    SPANISH(7, "西班牙语"),

    CHINESE(8, "普通话")

    ;

    private int type;
    private String name;

    LanguageType(int type, String name) {
        this.type = type;
        this.name = name;
    }

    @Override
    public Serializable getValue() {
        return type;
    }

    public static LanguageType select(String name) {
        if(StringUtils.isBlank(name)) {
            return null;
        }
        for(LanguageType languageType : LanguageType.values()) {
            if(name.equals(languageType.getName())) {
                return languageType;
            }
        }
        return null;
    }

}

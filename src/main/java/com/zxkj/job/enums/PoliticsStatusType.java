package com.zxkj.job.enums;

import com.baomidou.mybatisplus.enums.IEnum;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

@Getter
public enum PoliticsStatusType implements IEnum {

    COMMUNIST_YOUTH_LEAGUE_MEMBERS(1, "共青团员"),

    CPC_MEMBER(2, "中共党员"),

    MASSES(3, "群众"),

    DEMOCRATIC_PARTY(4, "民主党派")
    ;

    private int type;
    private String name;

    PoliticsStatusType(int type, String name) {
        this.type = type;
        this.name = name;
    }

    @Override
    public Serializable getValue() {
        return type;
    }

    public static PoliticsStatusType select(String name) {
        if(StringUtils.isBlank(name)) {
            return null;
        }
        for(PoliticsStatusType politicsStatusType : PoliticsStatusType.values()) {
            if(name.equals(politicsStatusType.getName())) {
                return politicsStatusType;
            }
        }
        return null;
    }
}
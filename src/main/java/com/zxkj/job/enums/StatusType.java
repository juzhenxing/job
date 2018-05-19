package com.zxkj.job.enums;

import com.baomidou.mybatisplus.enums.IEnum;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

@Getter
public enum StatusType implements IEnum {

    POSTED(1, "已投递"),

    PROCESSED(2, "已处理"),

    WRITTEN_EXAMINATION_TO_BE_ARRANGE(3, "待安排笔试"),

    WRITTEN_EXAMINATION_ALREADY_ARRANGED(4, "笔试已安排"),

    INTERVIEW_TO_BE_ARRANGE(5, "待安排面试"),

    INTERVIEW_ALREADY_ARRANGED(6, "面试已安排"),

    OFFER_HAS_BEEN_ISSUED(7, "offer已发放")

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

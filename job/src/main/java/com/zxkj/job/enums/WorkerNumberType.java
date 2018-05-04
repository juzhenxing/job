package com.zxkj.job.enums;

import com.baomidou.mybatisplus.enums.IEnum;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

@Getter
public enum WorkerNumberType implements IEnum {

    FIFTY_OR_LESS(1, "50人以下"),
    ONE_HUNDRED_AND_FIFTY(2, "50-150人"),
    FIVE_HUNDRED(3, "150-500人"),
    ONE_THOUSAND(4, "500-1000人"),
    FIVE_THOUSAND(5, "1000-5000人"),
    TEN_THOUSAND(6, "5000-10000人"),
    MORE_THAN_10000(7, "10000人以上")
    ;

    private int type;
    private String name;

    WorkerNumberType(int type, String name) {
        this.type = type;
        this.name = name;
    }

    @Override
    public Serializable getValue() {
        return type;
    }

    public static WorkerNumberType select(String name) {
        if(StringUtils.isBlank(name)) {
            return null;
        }
        for(WorkerNumberType statusType : WorkerNumberType.values()) {
            if(name.equals(statusType.getName())) {
                return statusType;
            }
        }
        return null;
    }

}

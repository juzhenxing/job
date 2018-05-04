package com.zxkj.job.enums;

import com.baomidou.mybatisplus.enums.IEnum;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

@Getter
public enum NatureType implements IEnum {

    US(1, "欧美外资"),
    NON_US(2, "非欧美外资"),
    EURAMERICAN_JOINT_VENTURE(3, "欧美合资"),
    NON_EURAMERICAN_JOINT_VENTURE(4, "非欧美合资"),
    STATE_OWNED_ENTERPRISES(5, "国企"),
    PRIVATE_COMPANY(6, "民营公司"),
    FOREIGN_REPRESENTATIVE_OFFICE(7, "外企代表处"),
    GOVERNMENT_AGENCIES(8, "政府机关"),
    INSTITUTIONS(9, "事业单位"),
    NON_PROFIT_ORGANIZATION(10, "非盈利机构"),
    OTHER_PROPERTIES(11, "其他性质"),
    ;

    private int type;
    private String name;

    NatureType(int type, String name) {
        this.type = type;
        this.name = name;
    }

    @Override
    public Serializable getValue() {
        return type;
    }

    public static NatureType select(String name) {
        if(StringUtils.isBlank(name)) {
            return null;
        }
        for(NatureType natureType : NatureType.values()) {
            if(name.equals(natureType.getName())) {
                return natureType;
            }
        }
        return null;
    }

}

package com.zxkj.job.enums;

import com.baomidou.mybatisplus.enums.IEnum;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

@Getter
public enum ProvinceType implements IEnum {

    UNLIMITED(0, "不限"),
    BEI_JING(1, "北京市"),
    SHANG_HAI(2, "上海市"),
    TIAN_JIN(3, "天津市"),
    CHONG_QING(4, "重庆市"),
    HEI_LONG_JIANG(5, "黑龙江省"),
    JI_LIN(6, "吉林省"),
    LIAO_NING(7, "辽宁省"),
    SHAN_DONG(8, "山东省"),
    SHAN_XI(9, "山西省"),
    SAN_XI(10, "陕西省"),
    HE_BEI(11, "河北省"),
    HE_NAN(12, "河南省"),
    HU_BEI(13, "湖北省"),
    HU_NAN(14, "湖南省"),
    HAI_NAN(15, "海南省"),
    JIANG_SU(16, "江苏省"),
    JIANG_XI(17, "江西省"),
    GUANG_DONG(18, "广东省"),
    GUANG_XI(19, "广西省"),
    YUN_NAN(20, "云南省"),
    GUI_ZHOU(21, "贵州省"),
    SI_CHUAN(22, "四川省"),
    NEI_MENG_GU(23, "内蒙古自治区"),
    NING_XIA(24, "宁夏自治区"),
    GAN_SU(25, "甘肃省"),
    QING_HAI(26, "青海省"),
    XI_ZANG(27, "西藏自治区"),
    XIN_JIANG(28, "新疆自治区"),
    AN_HUI(29, "安徽省"),
    ZHE_JIANG(30, "浙江省"),
    FU_JIAN(31, "福建省"),
    TAI_WAN(32, "台湾省"),
    XIANG_GANG(33, "香港"),
    AO_MEN(34, "澳门"),
    GUO_WAI(35, "国外"),
    ;

    private int type;
    private String name;

    ProvinceType(int type, String name) {
        this.type = type;
        this.name = name;
    }

    @Override
    public Serializable getValue() {
        return type;
    }

    public static ProvinceType select(String name) {
        if(StringUtils.isBlank(name)) {
            return null;
        }
        for(ProvinceType statusType : ProvinceType.values()) {
            if(name.equals(statusType.getName())) {
                return statusType;
            }
        }
        return null;
    }

}

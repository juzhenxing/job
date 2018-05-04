package com.zxkj.job.enums;

import com.baomidou.mybatisplus.enums.IEnum;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

@Getter
public enum JobCategoryType implements IEnum {

    INTERNET(1, "互联网/软件"),

    DESIGN(2, "设计/动画"),

    MANAGE(3, "管培/管理"),

    ADMINISTRATION(4, "行政/人事"),

    MARKRTING(5, "营销/公关"),

    FINANCE(6, "财税/金融"),

    FOREIGNLANGUAGES(7, "外语/民族语"),

    LAWWORKS(8, "法务"),

    EDUCATION(9, "教育/机关"),

    CORRESPONDENCE(10, "通信/电子"),

    REALESTATE(11, "地产/土建"),

    ENERGY(12, "能源/电力"),

    MECHANICAL(13, "机械/控制"),

    LOGISTICS(14, "物流/生产/维修"),

    CHEMICAL(15, "化工/材料/环卫"),

    MINERALPRODUCTS(16, "矿产/勘测"),

    BIOCHEMISTRY(17, "生化/医药"),

    FARMING(18, "农牧/生态/食品"),

    NEWS(19, "新闻/表演"),

    MATH(20, "数学/物理/统计"),

    SERVICE(21, "服务/职能")

    ;

    private int type;
    private String name;

    JobCategoryType(int type, String name) {
        this.type = type;
        this.name = name;
    }

    @Override
    public Serializable getValue() {
        return type;
    }

    public static JobCategoryType select(String name) {
        if(StringUtils.isBlank(name)) {
            return null;
        }
        for(JobCategoryType jobCategoryType : JobCategoryType.values()) {
            if(name.equals(jobCategoryType.getName())) {
                return jobCategoryType;
            }
        }
        return null;
    }
}

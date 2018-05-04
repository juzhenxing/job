package com.zxkj.job.enums;

import com.baomidou.mybatisplus.enums.IEnum;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

@Getter
public enum IndustryInvolvedType implements IEnum {

    COMPUTER_SOFTWARE(1, "计算机软件"),
    COMPUTER_HARDWARE(2, "计算机硬件"),
    IT_SERVICES(3, "IT服务"),
    INTERNET(4, "互联网"),
    E_BUSINESS(5, "电子商务"),
    GAME(6, "游戏"),
    CORRESPONDENCE(7, "通信"),
    ELECTRONICS_SEMICONDUCTORS(8, "电子/半导体"),
    BANK(9, "银行"),
    INSURANCE(10, "保险"),
    SECURITIES(11, "证券"),
    INVESTMENT(12, "投资"),
    ACCOUNTING(13, "会计/审计"),
    HUMAN_RESOURCES(14, "人力资源"),
    MANAGEMENT_CONSULTANCY(15, "管理咨询"),
    LEGAL(16, "法律"),
    TESTING(17, "检测/认证"),
    TRANSLATION(18, "翻译"),
    HIGHER_EDUCATION(19, "高等教育"),
    PRIMARY_AND_SECONDARY_EDUCATION(20, "初中等教育"),
    TRAINING(21, "培训"),
    DAILY_NECESSITIES(22, "日用品/化妆品"),
    FOOD(23, "食品/饮料"),
    CLOTHING(24, "服装/纺织"),
    APPLIANCE(25, "家电/数码产品"),
    LUXURY(26, "奢侈品/珠宝"),
    FURNITURE(27, "家具/家居"),
    ALCOHOL(28, "奢侈品/珠宝"),
    TOBACCO_INDUSTRY(29, "烟草业"),
    OFFICE_SUPPLIES(30, "办公用品"),
    ADVERTISING(31, "广告/公关/会展"),
    NEWSPAPER(32, "报纸/杂志"),
    BROADCAST(33, "广播"),
    TV(34, "影视"),
    PUBLISHING(35, "出版"),
    ART(36, "艺术/工艺"),
    PHYSICAL_EDUCATION(37, "体育"),
    ANIME(38, "动漫"),
    ARCHITECTURAL_DESIGN(39, "建筑设计/规划"),
    CIVIL_ENGINEERING(40, "土木工程"),
    REAL_ESTATE(41, "房地产"),
    PROPERTY_MANAGEMENT(42, "物业管理"),
    BUILDING_MATERIALS(43, "建材"),
    DECORATION_DECORATION(44, "装修装潢"),
    IMPORT_AND_EXPORT(45, "进出口"),
    WHOLESALE_AND_RETAIL(46, "批发/零售"),
    STORE(47, "商店/超市"),
    LOGISTICS(48, "物流/仓储"),
    TRANSPORT(49, "运输/铁路/航空"),
    CHEMICAL_INDUSTRY(50, "化工"),
    RAW_MATERIALS(51, "原材料/加工"),
    NEW_MATERIAL(52, "新材料"),
    CAR(53, "汽车"),
    MECHANICAL(54, "机械/自动化"),
    MILITARY_WORKERS(55, "军工/国防"),
    MINING(56, "采矿/金属"),
    CRUDE(57, "原油/能源"),
    PAPERMAKING(58, "造纸"),
    PRINT(59, "印刷/包装"),
    AEROSPACE_SHIPBUILDING(60, "航天/造船"),
    MEDICAL_TREATMENT(61, "医疗/护理"),
    MEDICAL_INSTRUMENTS(62, "医疗器械"),
    BIOTECHNOLOGY(63, "生物技术"),
    MEDICINE(64, "医药"),
    ANIMAL_HEALTH(65, "动物医疗"),
    HOTEL(66, "酒店"),
    REPAST(67, "餐饮"),
    TOURISM(68, "旅游"),
    CASUAL(69, "休闲/娱乐/健身"),
    PRIVATE(70, "私人/家政服务"),
    LIBRARY(71, "图书馆/展览馆"),
    SURROUNDINGS(72, "环境"),
    AGRICULTURE(73, "农/林/牧/渔"),
    GRADUATE_SCHOOL(74, "研究所/研究院"),
    PUBLIC_UTILITIES(75, "公共事业"),
    NON_PROFIT_ORGANIZATIONS(76, "非营利组织"),
    GOVERNMENT_DEPARTMENT(77, "政府部门"),
    ;

    private int type;
    private String name;

    IndustryInvolvedType(int type, String name) {
        this.type = type;
        this.name = name;
    }

    @Override
    public Serializable getValue() {
        return type;
    }

    public static IndustryInvolvedType select(String name) {
        if(StringUtils.isBlank(name)) {
            return null;
        }
        for(IndustryInvolvedType natureType : IndustryInvolvedType.values()) {
            if(name.equals(natureType.getName())) {
                return natureType;
            }
        }
        return null;
    }

}

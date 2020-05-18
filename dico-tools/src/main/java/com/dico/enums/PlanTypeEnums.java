package com.dico.enums;

import lombok.Getter;

/**
 * 检查计划类型
 */
@Getter
public enum PlanTypeEnums {
    DAY(0, "日常检查"),
    WEEK(1, "周检查"),
    MONTH(2, "月度检查"),
    QUARTER(3, "季度检查"),
    SPECIAL(4, "专项检查");

    private int key;
    private String value;


    PlanTypeEnums(int key, String value) {
        this.key = key;
        this.value = value;
    }

    public static PlanTypeEnums getPlanTypeEnums(int key) {
        for (PlanTypeEnums planTypeEnums : PlanTypeEnums.values()) {
            if (planTypeEnums.key == key) {
                return planTypeEnums;
            }
        }
        return null;
    }

    public static String getValue(int key) {
        for (PlanTypeEnums planTypeEnums : PlanTypeEnums.values()) {
            if (planTypeEnums.key == key) {
                return planTypeEnums.value;
            }
        }
        return null;
    }
}

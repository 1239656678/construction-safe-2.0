package com.dico.enums;

import lombok.Getter;

/**
 * 检查计划类型
 */
@Getter
public enum PlanStatusEnums {
    WILL(0, "待巡检"),
    DOING(1, "巡检中"),
    FINISH(2, "已完成"),
    TIMEOUT(3, "逾期");

    private int key;
    private String value;


    PlanStatusEnums(int key, String value) {
        this.key = key;
        this.value = value;
    }

    public static PlanStatusEnums getPlanTypeEnums(int key) {
        for (PlanStatusEnums planTypeEnums : PlanStatusEnums.values()) {
            if (planTypeEnums.key == key) {
                return planTypeEnums;
            }
        }
        return null;
    }

    public static String getValue(int key) {
        for (PlanStatusEnums planTypeEnums : PlanStatusEnums.values()) {
            if (planTypeEnums.key == key) {
                return planTypeEnums.value;
            }
        }
        return null;
    }
}

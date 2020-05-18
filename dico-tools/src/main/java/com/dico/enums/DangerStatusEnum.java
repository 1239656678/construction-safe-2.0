package com.dico.enums;

import lombok.Getter;

/**
 * 检查计划类型
 */
@Getter
public enum DangerStatusEnum {
    ZERO(0, "未开始"),
    REPAIR(1, "待整改"),
    REVIEW(2, "待复查"),
    SUCCESS(3, "已完成");

    private int key;
    private String value;


    DangerStatusEnum(int key, String value) {
        this.key = key;
        this.value = value;
    }

    public static DangerStatusEnum getDangerFormEnums(int key) {
        for (DangerStatusEnum dangerStatusEnum : DangerStatusEnum.values()) {
            if (dangerStatusEnum.key == key) {
                return dangerStatusEnum;
            }
        }
        return null;
    }

    public static String getValue(int key) {
        for (DangerStatusEnum dangerStatusEnum : DangerStatusEnum.values()) {
            if (dangerStatusEnum.key == key) {
                return dangerStatusEnum.value;
            }
        }
        return null;
    }
}

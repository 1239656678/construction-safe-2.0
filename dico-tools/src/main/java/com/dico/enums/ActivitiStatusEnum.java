package com.dico.enums;

import lombok.Getter;

/**
 * 检查计划类型
 */
@Getter
public enum ActivitiStatusEnum {
    NO(0, "未发起"),
    DOING(1, "办理中"),
    UNALLOW(2, "拒绝"),
    ALLOW(3, "通过");

    private int key;
    private String value;


    ActivitiStatusEnum(int key, String value) {
        this.key = key;
        this.value = value;
    }

    public static ActivitiStatusEnum get(int key) {
        for (ActivitiStatusEnum dangerStatusEnum : ActivitiStatusEnum.values()) {
            if (dangerStatusEnum.key == key) {
                return dangerStatusEnum;
            }
        }
        return null;
    }

    public static String getValue(int key) {
        for (ActivitiStatusEnum dangerStatusEnum : ActivitiStatusEnum.values()) {
            if (dangerStatusEnum.key == key) {
                return dangerStatusEnum.value;
            }
        }
        return null;
    }
}

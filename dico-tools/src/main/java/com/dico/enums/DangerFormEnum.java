package com.dico.enums;

import lombok.Getter;

/**
 * 检查计划类型
 */
@Getter
public enum DangerFormEnum {
    PHOTO(0, "随手拍"),
    PLAN(1, "检查计划");

    private int key;
    private String value;


    DangerFormEnum(int key, String value) {
        this.key = key;
        this.value = value;
    }

    public static DangerFormEnum getDangerFormEnums(int key) {
        for (DangerFormEnum dangerFormEnum : DangerFormEnum.values()) {
            if (dangerFormEnum.key == key) {
                return dangerFormEnum;
            }
        }
        return null;
    }

    public static String getValue(int key) {
        for (DangerFormEnum dangerFormEnum : DangerFormEnum.values()) {
            if (dangerFormEnum.key == key) {
                return dangerFormEnum.value;
            }
        }
        return null;
    }
}

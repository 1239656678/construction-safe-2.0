package com.dico.common.enums;

import lombok.Getter;

/**
 * 检查计划类型
 */
@Getter
public enum MaintenanceStatusEnums {
    WILL(0, "待保养"),
    FINISH(1, "待复查"),
    FINISHLoading(2, "复查中"),
    TIMEOUT(3, "逾期"),
    OVER(4,"已完成");

    private int key;
    private String value;


    MaintenanceStatusEnums(int key, String value) {
        this.key = key;
        this.value = value;
    }

    public static MaintenanceStatusEnums getMaintenanceTypeEnums(int key) {
        for (MaintenanceStatusEnums maintenanceTypeEnums : MaintenanceStatusEnums.values()) {
            if (maintenanceTypeEnums.key == key) {
                return maintenanceTypeEnums;
            }
        }
        return null;
    }

    public static String getValue(int key) {
        for (MaintenanceStatusEnums maintenanceTypeEnums : MaintenanceStatusEnums.values()) {
            if (maintenanceTypeEnums.key == key) {
                return maintenanceTypeEnums.value;
            }
        }
        return null;
    }
}

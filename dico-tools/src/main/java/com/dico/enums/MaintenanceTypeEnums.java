package com.dico.enums;

import lombok.Getter;

/**
 * 保养计划类型
 */
@Getter
public enum MaintenanceTypeEnums {
    WEEK(0, "周"),
    MONTH(1, "月"),
    QUARTER(2, "季"),
    YEAR(3, "年");

    private int key;
    private String value;


    MaintenanceTypeEnums(int key, String value) {
        this.key = key;
        this.value = value;
    }

    public static MaintenanceTypeEnums getMaintenanceTypeEnums(int key) {
        for (MaintenanceTypeEnums planTypeEnums : MaintenanceTypeEnums.values()) {
            if (planTypeEnums.key == key) {
                return planTypeEnums;
            }
        }
        return null;
    }

    public static String getValue(int key) {
        for (MaintenanceTypeEnums maintenanceTypeEnums : MaintenanceTypeEnums.values()) {
            if (maintenanceTypeEnums.key == key) {
                return maintenanceTypeEnums.value;
            }
        }
        return null;
    }
}

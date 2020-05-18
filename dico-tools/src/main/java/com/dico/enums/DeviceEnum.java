package com.dico.enums;

/**
 * 过滤字段枚举类型
 *
 * @author Gaodl
 * @version v1.0
 * 文件名称: DeviceEnum
 * 创建时间: 2018/12/26
 */
public enum DeviceEnum {
    WEB("web"), MOBILE("mobile"), TABLE("table");

    @SuppressWarnings("unused")
    private String value;

    private DeviceEnum(String value) {
        this.value = value;
    }
}

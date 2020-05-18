package com.dico.annotation;

import com.dico.enums.DeviceEnum;

import java.lang.annotation.*;

/**
 * 定义该字段为视图展现字段
 *
 * @author Gaodl
 * @version v1.0
 * 文件名称: ViewField
 * 创建时间: 2019/2/26
 */
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(value = ElementType.FIELD)
public @interface ViewField {
    DeviceEnum[] targets() default DeviceEnum.WEB;
}

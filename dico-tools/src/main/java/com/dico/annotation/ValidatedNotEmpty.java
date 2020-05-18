package com.dico.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 定义校验类字段
 *
 * @author Gaodl
 * @version v1.0
 * 文件名称: ValidatedFiled
 * 创建时间: 2018年12月21日
 */
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(value = ElementType.FIELD)
public @interface ValidatedNotEmpty {

    /**
     * 定义出错提示语
     *
     * @author Gaodl
     * 方法名称: message
     * 参数： []
     * 返回值： java.lang.String
     * 创建时间: 2018/12/21
     */
    String message() default "不能为空";
}

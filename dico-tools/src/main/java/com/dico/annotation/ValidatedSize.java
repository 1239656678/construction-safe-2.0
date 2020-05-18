package com.dico.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 定义校验字段长度
 *
 * @author Gaodl
 * @version v1.0
 * 文件名称: ValidatedSize
 * 创建时间: 2018/12/21
 */
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(value = ElementType.FIELD)
public @interface ValidatedSize {

    /**
     * 定义出错提示语
     *
     * @author Gaodl
     * 方法名称: message
     * 参数： []
     * 返回值： java.lang.String
     * 创建时间: 2018/12/21
     */
    String message() default "长度应在{min}~{max}之间";

    /**
     * 定义字段最小长度
     *
     * @author Gaodl
     * 方法名称: min
     * 参数： []
     * 返回值： int
     * 创建时间: 2018/12/21
     */
    int min() default 0;

    /**
     * 定义字段最大长度
     *
     * @author Gaodl
     * 方法名称: max
     * 参数： []
     * 返回值： int
     * 创建时间: 2018/12/21
     */
    int max() default 2147483647;
}

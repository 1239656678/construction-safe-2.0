package com.dico.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 定义该实体类需要过滤
 *
 * @author Gaodl
 * @version v1.0
 * 文件名称: FilterBean
 * 创建时间: 2018/12/26
 */
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(value = ElementType.TYPE)
public @interface FilterBean {

}

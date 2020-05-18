package com.dico.util;

import io.swagger.annotations.ApiModelProperty;

import java.lang.reflect.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import lombok.extern.java.Log;
import org.apache.commons.lang.StringUtils;
import com.dico.Exception.ErrorUsedException;
import com.dico.annotation.ValidatedNotEmpty;
import com.dico.annotation.ValidatedSize;
import com.dico.result.ValidatedResult;
import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;

/**
 * 校验实体类字段工具类
 *
 * @author Gaodl
 * @version v1.0
 * 文件名称: ValiDatedUtils
 * 创建时间: 2018年12月21日
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class ValiDatedUtils {

    /**
     * 获取字段值的方法
     *
     * @throws SecurityException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @author Gaodl
     * 方法名称: valiDatedBean
     * 参数： [Object]
     * 返回值： ValidatedResult
     * 创建时间: 2018/12/21
     */
    public static ValidatedResult valiDatedBean(Object obj) throws NoSuchMethodException, SecurityException, ErrorUsedException,
            IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Class cls = obj.getClass();
        Field[] fields = cls.getDeclaredFields();
        for (Field field : fields) {
            Type genericType = field.getGenericType();
            if (genericType instanceof ParameterizedTypeImpl) {
                Object objValue = getFieldValue(obj, field);
                if (null == objValue) {
                    if (field.isAnnotationPresent(ValidatedNotEmpty.class)) {
                        ApiModelProperty apiModelProperty = field.getAnnotation(ApiModelProperty.class);
                        String fieldName = apiModelProperty.name();
                        ValidatedNotEmpty validatedNotEmpty = field.getAnnotation(ValidatedNotEmpty.class);
                        return new ValidatedResult().setStatus(false).setMessage(fieldName + validatedNotEmpty.message());
                    }
                    continue;
                }
                List<Object> objectList = (List<Object>) objValue;
                for (Object objCls : objectList) {
                    ValidatedResult validatedResult = valiDatedBean(objCls);
                    if (!validatedResult.isStatus()) {
                        return validatedResult;
                    }
                }
                continue;
            }
            if (!isPrimitive((Class) genericType) && !(field.getGenericType().getTypeName().equalsIgnoreCase("java.lang.String"))) {
                try {
                    Object objValue1 = getFieldValue(obj, field);
                    if (null == objValue1) {
                        if (field.isAnnotationPresent(ValidatedNotEmpty.class)) {
                            ApiModelProperty apiModelProperty = field.getAnnotation(ApiModelProperty.class);
                            String fieldName = apiModelProperty.name();
                            ValidatedNotEmpty validatedNotEmpty = field.getAnnotation(ValidatedNotEmpty.class);
                            return new ValidatedResult().setStatus(false).setMessage(fieldName + validatedNotEmpty.message());
                        }
                        continue;
                    }
                    List<Object> objectList = (List<Object>) objValue1;
                    for (Object objCls : objectList) {
                        ValidatedResult validatedResult = valiDatedBean(objValue1);
                        if (!validatedResult.isStatus()) {
                            return validatedResult;
                        }
                    }
                } catch (Exception e) {
                    Object objValue1 = getFieldValue(obj, field);
                    if (null == objValue1) {
                        if (field.isAnnotationPresent(ValidatedNotEmpty.class)) {
                            ApiModelProperty apiModelProperty = field.getAnnotation(ApiModelProperty.class);
                            String fieldName = apiModelProperty.name();
                            ValidatedNotEmpty validatedNotEmpty = field.getAnnotation(ValidatedNotEmpty.class);
                            return new ValidatedResult().setStatus(false).setMessage(fieldName + validatedNotEmpty.message());
                        }
                        continue;
                    }
                    ValidatedResult validatedResult = valiDatedBean(objValue1);
                    if (!validatedResult.isStatus()) {
                        return validatedResult;
                    }
                }
            }

            Logger.getLogger("").info("begin validate class " + cls.getName() + " field [" + field.getName() + "]");
            String fieldName = field.getName();
            if (field.isAnnotationPresent(ApiModelProperty.class)) {
                ApiModelProperty apiModelProperty = field.getAnnotation(ApiModelProperty.class);
                fieldName = apiModelProperty.name();
            }
            // 判断是否有ValidatedNotEmpty注解
            boolean flagNotEmpty = field.isAnnotationPresent(ValidatedNotEmpty.class);
            if (flagNotEmpty) {
                ValidatedNotEmpty validatedNotEmpty = field.getAnnotation(ValidatedNotEmpty.class);
                // 获取字段的值
                Object objValue = getFieldValue(obj, field);
                if (null == objValue) {
                    return new ValidatedResult().setStatus(false).setMessage(fieldName + validatedNotEmpty.message());
                }
                String strValue = String.valueOf(objValue);
                if (!StringUtils.isNotEmpty(strValue)) {
                    return new ValidatedResult().setStatus(false).setMessage(fieldName + validatedNotEmpty.message());
                }
            }
            // 判断是否有ValidatedSize注解, 进而校验字段值的长度是否符合
            boolean flagSize = field.isAnnotationPresent(ValidatedSize.class);
            if (flagSize) {
                // 获取注解的最大值和最小值
                ValidatedSize validatedSize = field.getAnnotation(ValidatedSize.class);
                int minSize = validatedSize.min();
                int maxSize = validatedSize.max();
                // 获取字段的值
                Object objValue = getFieldValue(obj, field);
                if (null == objValue) {
                    return new ValidatedResult().setStatus(true);
                }
                String strValue = String.valueOf(objValue);
                if (!(minSize <= strValue.length() && strValue.length() <= maxSize)) {
                    return new ValidatedResult().setStatus(false).setMessage(
                            fieldName
                                    + validatedSize.message().replaceFirst("\\{min\\}", String.valueOf(minSize))
                                    .replaceFirst("\\{max\\}", String.valueOf(maxSize)));
                }
            }
        }
        return new ValidatedResult().setStatus(true);
    }

    /**
     * 获取字段值的方法
     *
     * @throws SecurityException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @author Gaodl
     * 方法名称: getFieldValue
     * 参数： [Object, Field]
     * 返回值： java.lang.Object
     * 创建时间: 2018/12/21
     */
    private static Object getFieldValue(Object obj, Field field) throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        Class cls = obj.getClass();
        String prefix = "get";
        String fieldName = field.getName();
        if (field.getGenericType().getTypeName().equalsIgnoreCase("boolean")) {
            prefix = "is";
        }
        fieldName = fieldName.replaceFirst(fieldName.substring(0, 1), fieldName.substring(0, 1).toUpperCase());
        Method fieldMethod = cls.getMethod(prefix + fieldName);
        return fieldMethod.invoke(obj);
    }

    /**
     * 判断是否为基础数据类型
     *
     * @author Gaodl
     * 方法名称: isPrimitive
     * 参数： [clazz]
     * 返回值： boolean
     * 创建时间: 2019/5/7
     */
    private static boolean isPrimitive(Class clazz) {
        return (clazz.equals(String.class) ||
                clazz.equals(Integer.class) ||
                clazz.equals(Byte.class) ||
                clazz.equals(Long.class) ||
                clazz.equals(Double.class) ||
                clazz.equals(Float.class) ||
                clazz.equals(Character.class) ||
                clazz.equals(Short.class) ||
                clazz.equals(BigDecimal.class) ||
                clazz.equals(BigInteger.class) ||
                clazz.equals(Boolean.class) ||
                clazz.equals(Date.class) ||
                clazz.equals(Timestamp.class) ||
                clazz.isPrimitive()
        );
    }
}

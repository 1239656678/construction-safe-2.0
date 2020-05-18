package com.dico.util;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * 反射工具类
 *
 * @author Gaodl
 * @version v1.0
 * 文件名称: HashUtils
 * 创建时间: 2018/12/13
 */
public class ReflectUtils {

    /**
     * 得到指定类型的指定位置的泛型实参
     *
     * @author Gaodl
     * 方法名称: findParameterizedType
     * 参数： [clazz, index]
     * 返回值： Class
     * 创建时间: 2018/12/13
     */
    @SuppressWarnings("unchecked")
    public static <T> Class<T> findParameterizedType(Class<?> clazz, int index) {
        Type parameterizedType = clazz.getGenericSuperclass();

        //CGLUB subclass target object(泛型在父类上)
        if (!(parameterizedType instanceof ParameterizedType)) {
            parameterizedType = clazz.getSuperclass().getGenericSuperclass();
        }
        if (!(parameterizedType instanceof ParameterizedType)) {
            return null;
        }
        Type[] actualTypeArguments = ((ParameterizedType) parameterizedType).getActualTypeArguments();
        if (actualTypeArguments == null || actualTypeArguments.length == 0) {
            return null;
        }
        return (Class<T>) actualTypeArguments[index];
    }

    /**
     * 设置字段值
     *
     * @author Gaodl
     * 方法名称: setFieldValue
     * 参数： [clazz, fieldName, value]
     * 返回值： T
     * 创建时间: 2018/12/13
     */
    public static <T> T setFieldValue(Class<T> clazz, String fieldName, Object value) {

        try {
            T model = clazz.newInstance();
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(model, value);
            return model;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return null;
    }
}

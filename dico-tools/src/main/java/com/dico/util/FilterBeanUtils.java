package com.dico.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

import com.dico.annotation.ViewField;
import org.hibernate.proxy.HibernateProxy;
import org.springframework.mobile.device.Device;
import org.springframework.mobile.device.DeviceUtils;
import com.dico.annotation.FilterBean;
import com.dico.enums.DeviceEnum;

/**
 * 过滤实体类字段工具类
 *
 * @author Gaodl
 * @version v1.0
 * 文件名称: FilterBeanUtils
 * 创建时间: 2018年12月26日
 */
public class FilterBeanUtils<T> {

    /**
     * 过滤字段值的方法
     *
     * @throws NoSuchMethodException
     * @throws SecurityException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     * @author Gaodl
     * 方法名称: filterBeans
     * 参数： [Object, HttpServletRequest]
     * 返回值： java.com.dico.util.List
     * 创建时间: 2018/12/26
     */
    public static <T> List<Map<String, Object>> filterBeans(List<T> t, HttpServletRequest request) throws NoSuchMethodException, SecurityException,
            IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
        for (T target : t) {
            dataList.add(filter(target, getDeviceEnum(request)));
        }
        return dataList;
    }

    /**
     * 过滤字段值的方法
     *
     * @throws NoSuchMethodException
     * @throws SecurityException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     * @author Gaodl
     * 方法名称: filterBeans
     * 参数： [Object, HttpServletRequest]
     * 返回值： java.com.dico.util.Map<String, Object>
     * 创建时间: 2018/12/26
     */
    public static <T> Map<String, Object> filterBean(T t, HttpServletRequest request) throws NoSuchMethodException, SecurityException,
            IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        return filter(t, getDeviceEnum(request));
    }

    /**
     * 根据请求终端类型过滤字段值的方法
     *
     * @throws NoSuchMethodException
     * @throws SecurityException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     * @author Gaodl
     * 方法名称: filter
     * 参数： [Object, DeviceEnum]
     * 返回值： java.com.dico.util.Map<String, Object>
     * 创建时间: 2018/12/26
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    private static <T> Map<String, Object> filter(T t, DeviceEnum deviceEnum) throws NoSuchMethodException, SecurityException,
            IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Map<String, Object> dataMap = new HashMap<String, Object>();
        Class cls = t.getClass();
        t.getClass().isInstance(HibernateProxy.class);
        if (cls.isAnnotationPresent(FilterBean.class)) {
            Field[] fields = cls.getDeclaredFields();
            if (cls.getName().contains("$HibernateProxy")) {
                fields = cls.getSuperclass().getDeclaredFields();
            }
            for (Field field : fields) {
                String fieldName = field.getName();
                if (field.isAnnotationPresent(ViewField.class)) {
                    boolean isFlag = false;
                    ViewField viewField = field.getAnnotation(ViewField.class);
                    DeviceEnum[] deviceEnums = viewField.targets();
                    for (DeviceEnum de : deviceEnums) {
                        if (de == deviceEnum) {
                            isFlag = true;
                        }
                    }
                    if (isFlag) {
                        dataMap.put(fieldName, getValue(field, t));
                    }
                }
            }
        } else {
            dataMap = JavaBeanUtils.bean2Map(t);
        }
        return dataMap;
    }

    /**
     * 获取字段值
     *
     * @author Gaodl
     * 方法名称: getValue
     * 参数： [field, t]
     * 返回值： java.lang.Object
     * 创建时间: 2019/2/26
     */
    private static <T> Object getValue(Field field, T t) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        String prefix = "get";
        String fieldName = field.getName();
        if (field.getGenericType().getTypeName().equalsIgnoreCase("boolean")) {
            prefix = "is";
        }
        String firstUpperfieldName = fieldName.replaceFirst(fieldName.substring(0, 1), fieldName.substring(0, 1).toUpperCase());
        Method fieldMethod = t.getClass().getMethod(prefix + firstUpperfieldName);
        return fieldMethod.invoke(t);
    }

    /**
     * 获取终端设备类型
     *
     * @author Gaodl
     * 方法名称: getDeviceEnum
     * 参数： [HttpServletRequest]
     * 返回值： DeviceEnum
     * 创建时间: 2018/12/26
     */
    private static DeviceEnum getDeviceEnum(HttpServletRequest request) {
        Device device = DeviceUtils.getCurrentDevice(request);
        DeviceEnum deviceEnum;
        if (device.isMobile()) {
            deviceEnum = DeviceEnum.MOBILE;
        } else if (device.isTablet()) {
            deviceEnum = DeviceEnum.TABLE;
        } else {
            deviceEnum = DeviceEnum.WEB;
        }
        return deviceEnum;
    }
}

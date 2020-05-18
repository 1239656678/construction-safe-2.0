package com.dico.util;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class JavaBeanUtils<T> {

    /**
     * 实体类转map
     *
     * @author Gaodl
     * 方法名称: bean2Map
     * 参数： [T]
     * 返回值： java.com.dico.util.Map
     * 创建时间: 2018/12/26
     */
    public static <T> Map<String, Object> bean2Map(T t) {
        JSONObject jsonObject = (JSONObject) JSON.toJSON(t);
        Set<Entry<String, Object>> entrySet = jsonObject.entrySet();
        Map<String, Object> dataMap = new HashMap<String, Object>();
        for (Entry<String, Object> entry : entrySet) {
            dataMap.put(entry.getKey(), entry.getValue());
        }
        return dataMap;
    }

    /**
     * Map转成实体对象
     *
     * @param map   map实体对象包含属性
     * @param clazz 实体对象类型
     * @return
     */
    public static <T> T map2Object(Map<String, Object> map, T t) {
        if (map == null) {
            return null;
        }
        try {
            Field[] fields = t.getClass().getDeclaredFields();
            for (Field field : fields) {
                int mod = field.getModifiers();
                if (Modifier.isStatic(mod) || Modifier.isFinal(mod)) {
                    continue;
                }
                field.setAccessible(true);
                String filedTypeName = field.getType().getName();
                if (filedTypeName.equalsIgnoreCase("java.com.dico.util.date")) {
                    String datetimestamp = String.valueOf(map.get(field.getName()));
                    if (datetimestamp.equalsIgnoreCase("null")) {
                        field.set(t, null);
                    } else {
                        field.set(t, new Date(Long.parseLong(datetimestamp)));
                    }
                } else {
                    field.set(t, map.get(field.getName()));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return t;
    }
}
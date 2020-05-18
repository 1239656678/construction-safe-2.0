package com.dico.util;

import org.dozer.DozerBeanMapper;
import org.dozer.loader.api.BeanMappingBuilder;
import org.dozer.loader.api.TypeMappingOptions;

import java.lang.ref.WeakReference;

/**
 * 对象转换类
 *
 * @author Gaodl
 * @version v1.0
 * 文件名称: TransmitUtils
 * 创建时间: 2018/12/13
 */
public class TransmitUtils {

    private static DozerBeanMapper dozer = new DozerBeanMapper();

    /**
     * 构造新的destinationClass实例对象，通过source对象中的字段内容映射到destinationClass实例对象中，并返回新的destinationClass实例对象。
     *
     * @author Gaodl
     * 方法名称: map
     * 参数： [source, destinationClass]
     * 返回值： java.lang.String
     * 创建时间: 2018/12/13
     */
    public static <T> T map(Object source, Class<T> destinationClass) {
        return dozer.map(source, destinationClass);
    }

    /**
     * 禁止控制传递
     *
     * @author Gaodl
     * 方法名称: sources2destination
     * 参数： [sources, destination]
     * 返回值： java.lang.String
     * 创建时间: 2018/12/13
     */
    public static void sources2destination(final Object sources, final Object destination) {
        WeakReference<Object> weakReference = new WeakReference<Object>(new DozerBeanMapper());
        DozerBeanMapper mapper = (DozerBeanMapper) weakReference.get();
        mapper.addMapping(new BeanMappingBuilder() {
            @Override
            protected void configure() {
                mapping(sources.getClass(), destination.getClass(), TypeMappingOptions.mapNull(false), TypeMappingOptions.mapEmptyString(false));
            }
        });
        mapper.map(sources, destination);
        mapper.destroy();
        weakReference.clear();
    }

    /**
     * 将对象source的所有属性值拷贝到对象destination中.
     *
     * @author Gaodl
     * 方法名称: sources2destination
     * 参数： [sources, destination]
     * 返回值： java.lang.String
     * 创建时间: 2018/12/13
     */
    public static void source2destination(Object source, Object destinationObject) {
        dozer.map(source, destinationObject);
    }
}
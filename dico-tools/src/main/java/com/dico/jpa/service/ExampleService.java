package com.dico.jpa.service;

import org.springframework.data.domain.Page;

import java.util.List;

/**
 * 统一封装
 *
 * @author Gaodl
 * @version v1.0
 * 文件名称: ExampleService
 * 创建时间: 2018/12/13
 */
public interface ExampleService<T> {

    /**
     * 获取数据集合
     *
     * @author Gaodl
     * 方法名称: findAll
     * 参数： [t]
     * 返回值：java.com.dico.util.List
     * 创建时间: 2018/12/13
     */
    List<T> findAll(T t);

    /**
     * 获取数据集合
     *
     * @author Gaodl
     * 方法名称: findAll
     * 参数： [t]
     * 返回值： org.springframework.data.domain.Page
     * 创建时间: 2018/12/13
     */
    Page<T> findAll(T t, int pageNum, int pageSize);
}
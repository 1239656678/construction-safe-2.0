package com.dico.jpa.service;

import java.util.List;

/**
 * Service 层 基础接口，其他Service 接口 请继承该接口
 */
public interface Service<T, K> {
    void save(T model);//持久化

    void save(List<T> models);//批量持久化

    void deleteById(K id);//通过主鍵刪除

    void update(T model);//更新

    T findOne(K id);
}

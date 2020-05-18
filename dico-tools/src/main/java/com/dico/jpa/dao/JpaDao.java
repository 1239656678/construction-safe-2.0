package com.dico.jpa.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;

/**
 * 方法统一封装
 *
 * @author Gaodl
 * @version v1.0
 * 文件名称: JpaDao
 * 创建时间: 2018/12/13
 */
@NoRepositoryBean
public interface JpaDao<T, K extends Serializable> extends JpaRepository<T, K>, JpaSpecificationExecutor<T> {
}

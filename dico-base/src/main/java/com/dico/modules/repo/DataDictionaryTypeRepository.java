package com.dico.modules.repo;

import com.dico.jpa.dao.JpaDao;
import com.dico.modules.domain.DataDictionaryType;

/**
 * 数据字典类型持久层业务类
 *
 * @author Gaodl
 * @version v1.0
 * 文件名称: DataDictionaryRepository
 * 创建时间: 2018/12/27
 */
public interface DataDictionaryTypeRepository extends JpaDao<DataDictionaryType, String> {
    /**
     * 根据名称查询未删除的数据
     *
     * @author Gaodl
     * 方法名称: getByNameEqualsAndDelFlagIsFalse
     * 参数： [name]
     * 返回值： com.dico.modules.domain.DataDictionaryType
     * 创建时间: 2018/12/27
     */
    DataDictionaryType getByNameEqualsAndDelFlagIsFalse(String name);

    void deleteByIdIn(String[] ids);

    /**
     * 根据ID查询未删除的数据
     *
     * @author Gaodl
     * 方法名称: getByIdAndDelFlagIsFalse
     * 参数： [id]
     * 返回值： com.dico.modules.domain.DataDictionaryType
     * 创建时间: 2018/12/27
     */
    DataDictionaryType getByIdAndDelFlagIsFalse(String id);
}
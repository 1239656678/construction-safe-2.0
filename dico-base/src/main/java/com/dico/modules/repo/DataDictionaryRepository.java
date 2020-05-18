package com.dico.modules.repo;

import com.dico.jpa.dao.JpaDao;
import com.dico.modules.domain.DataDictionary;

/**
 * 数据字典持久层业务类
 *
 * @author Gaodl
 * @version v1.0
 * 文件名称: DataDictionaryRepository
 * 创建时间: 2018/12/27
 */
public interface DataDictionaryRepository extends JpaDao<DataDictionary, String> {

    /**
     * 根据ID查询未删除的数据字典
     *
     * @author Gaodl
     * 方法名称: getByIdAndDelFlagIsFalse
     * 参数： [id]
     * 返回值： com.dico.modules.domain.DataDictionary
     * 创建时间: 2018/12/27
     */
    DataDictionary getByIdAndDelFlagIsFalse(String id);

    void deleteByIdIn(String[] ids);

    /**
     * 根据名称和数据类型查询未删除的数据
     *
     * @author Gaodl
     * 方法名称: getByNameEqualsAndTypeIdEqualsAndDelFlagIsFalse
     * 参数： [name, typeId]
     * 返回值： void
     * 创建时间: 2018/12/27
     */
    DataDictionary getByNameEqualsAndTypeIdEqualsAndDelFlagIsFalse(String name, String typeId);
}
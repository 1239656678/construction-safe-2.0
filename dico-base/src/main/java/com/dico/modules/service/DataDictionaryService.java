package com.dico.modules.service;

import com.dico.jpa.dao.JpaDao;
import com.dico.jpa.service.JpaService;
import com.dico.modules.domain.DataDictionary;
import com.dico.modules.repo.DataDictionaryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 数据字典service层
 *
 * @author Gaodl
 * @version v1.0
 * 文件名称: SysDataDictionaryService
 * 创建时间: 2018/12/27
 */
@Service
public class DataDictionaryService extends JpaService<DataDictionary, String> {

    @Autowired
    private DataDictionaryRepository dataDictionaryRepository;

    @Override
    protected JpaDao<DataDictionary, String> getDao() {
        return dataDictionaryRepository;
    }

    /**
     * 根据ID查询未删除的数据字典
     *
     * @author Gaodl
     * 方法名称: getByIdAndDelFlagIsFalse
     * 参数： [id]
     * 返回值： com.dico.modules.domain.DataDictionary
     * 创建时间: 2018/12/27
     */
    public DataDictionary getByIdAndDelFlagIsFalse(String id) {
        return dataDictionaryRepository.getByIdAndDelFlagIsFalse(id);
    }

    public void deleteByIdIn(String[] ids) {
        dataDictionaryRepository.deleteByIdIn(ids);
    }

    /**
     * 根据名称和数据类型查询未删除的数据
     *
     * @author Gaodl
     * 方法名称: getByNameEqualsAndTypeIdEqualsAndDelFlagIsFalse
     * 参数： [name, typeId]
     * 返回值： com.dico.modules.domain.DataDictionary
     * 创建时间: 2018/12/27
     */
    public DataDictionary getByNameEqualsAndTypeIdEqualsAndDelFlagIsFalse(String name, String typeId) {
        return dataDictionaryRepository.getByNameEqualsAndTypeIdEqualsAndDelFlagIsFalse(name, typeId);
    }
}

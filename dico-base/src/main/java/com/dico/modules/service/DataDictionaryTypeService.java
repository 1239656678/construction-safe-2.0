package com.dico.modules.service;

import com.dico.jpa.dao.JpaDao;
import com.dico.jpa.service.JpaService;
import com.dico.modules.domain.DataDictionaryType;
import com.dico.modules.repo.DataDictionaryTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 数据字典类型service层
 *
 * @author Gaodl
 * @version v1.0
 * 文件名称: SysDataDictionaryService
 * 创建时间: 2018/12/27
 */
@Service
public class DataDictionaryTypeService extends JpaService<DataDictionaryType, String> {

    @Autowired
    private DataDictionaryTypeRepository dataDictionaryTypeRepository;

    @Override
    protected JpaDao<DataDictionaryType, String> getDao() {
        return dataDictionaryTypeRepository;
    }

    /**
     * 根据名称查询未删除的数据
     *
     * @author Gaodl
     * 方法名称: getByNameEqualsAndDelFlagIsFalse
     * 参数： [name]
     * 返回值： com.dico.modules.domain.DataDictionaryType
     * 创建时间: 2018/12/27
     */
    public DataDictionaryType getByNameEqualsAndDelFlagIsFalse(String name) {
        return dataDictionaryTypeRepository.getByNameEqualsAndDelFlagIsFalse(name);
    }

    public void deleteByIdIn(String[] ids) {
        dataDictionaryTypeRepository.deleteByIdIn(ids);
    }

    /**
     * 根据ID查询未删除的数据
     *
     * @author Gaodl
     * 方法名称: getByIdAndDelFlagIsFalse
     * 参数： [id]
     * 返回值： com.dico.modules.domain.DataDictionaryType
     * 创建时间: 2018/12/27
     */
    public DataDictionaryType getByIdAndDelFlagIsFalse(String id) {
        return dataDictionaryTypeRepository.getByIdAndDelFlagIsFalse(id);
    }
}

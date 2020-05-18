package com.dico.modules.repo;

import com.dico.jpa.dao.JpaDao;
import com.dico.modules.domain.SmsEquipmentClass;

import java.util.List;

/**
 * @author Gaodl
 * @version v1.0
 * 文件名称: $file_name$
 * 创建时间: $date$
 */
public interface SmsEquipmentClassRepository extends JpaDao<SmsEquipmentClass, String> {

    /**
     * 根据ID查询未删除的数据
     */
    SmsEquipmentClass getSmsEquipmentClassByIdAndDelFlagIsFalse(String smsEquipmentClassId);

    void deleteByIdIn(String[] ids);

    /**
     * 根据code查询未删除的数据
     */
    List<SmsEquipmentClass> getSmsEquipmentClassesByClassNameLike(String className);

    /**
     * 根据设备类型名称和父类型查询设备类型
     *
     * @author Gaodl
     * 方法名称: findByClassNameAndParentClass
     * 参数： [className, parentClass]
     * 返回值： com.dico.modules.domain.SmsEquipmentClass
     * 创建时间: 2019/4/10
     */
    SmsEquipmentClass findByClassNameEqualsAndParentClassEqualsAndDelFlagIsFalse(String className, String parentClass);

    /**
     * 根据ID集合查询所有的设备类型信息
     *
     * @author Gaodl
     * 方法名称: findEquipmentClassByIds
     * 参数： [ids]
     * 返回值： java.util.List<com.dico.modules.domain.SmsEquipmentClass>
     * 创建时间: 2019/4/19
     */
    List<SmsEquipmentClass> findSmsEquipmentClassesByIdInAndDelFlagIsFalse(List<String> ids);

    /**
     * 获取父ID为null的所有根组织
     *
     * @author Gaodl
     * 方法名称: findByParentClassIsNullAndDelFlagIsFalse
     * 参数： []
     * 返回值： java.util.List<com.dico.modules.domain.SmsEquipmentClass>
     * 创建时间: 2019/4/22
     */
    List<SmsEquipmentClass> findByParentClassIsNullAndDelFlagIsFalse();

    /**
     * 根据ID集合查询数据
     *
     * @param ids
     * @return
     */
    List<SmsEquipmentClass> findByIdInAndDelFlagIsFalse(String[] ids);

    List<SmsEquipmentClass> findByParentClassAndDelFlagIsFalse(String classId);
}

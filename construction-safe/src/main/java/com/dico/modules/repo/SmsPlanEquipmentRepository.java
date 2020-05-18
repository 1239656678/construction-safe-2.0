package com.dico.modules.repo;

import com.dico.jpa.dao.JpaDao;
import com.dico.modules.domain.SmsPlanEquipmentClass;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author Gaodl
 * @version v1.0
 * 文件名称: $file_name$
 * 创建时间: $date$
 */
public interface SmsPlanEquipmentRepository extends JpaDao<SmsPlanEquipmentClass, String> {

    /**
     * 根据ID查询未删除的数据
     */
    SmsPlanEquipmentClass getByIdAndDelFlagIsFalse(String SmsPlanEquipmentClassId);

    void deleteByIdIn(String[] ids);

    /**
     * 根据计划ID和设备类型ID查询设备类型绑定信息
     *
     * @author Gaodl
     * 方法名称: findByPlanIdEqualsAndEquipmentClassIdEqualsAndDelFlagIsFalse
     * 参数： [planId, equipmentClassId]
     * 返回值： com.dico.modules.domain.SmsPlanEquipment
     * 创建时间: 2019/4/19
     */
    SmsPlanEquipmentClass findByPlanIdEqualsAndEquipmentClassIdEqualsAndDelFlagIsFalse(String planId, String equipmentClassId);

    /**
     * 根据计划ID查询计划绑定的设备类型
     *
     * @author Gaodl
     * 方法名称: findByPlanIdAndDelFlagIsFalse
     * 参数： [planId]
     * 返回值： java.util.List<com.dico.modules.domain.SmsPlanEquipment>
     * 创建时间: 2019/4/19
     */
    List<SmsPlanEquipmentClass> findByPlanIdAndDelFlagIsFalse(String planId);

    /**
     * 根据计划ID查询未删除的设备ID
     *
     * @author Gaodl
     * 方法名称: findEquipmentIdsByPlanIdAndDelFlagIsFalse
     * 参数： [planId]
     * 返回值： java.util.List<java.lang.String>
     * 创建时间: 2019/5/10
     */
    @Query("select spec.equipmentClassId from SmsPlanEquipmentClass spec where spec.planId = :planId and spec.delFlag = false ")
    List<String> findEquipmentClassIdsByPlanIdAndDelFlagIsFalse(@Param("planId") String planId);

    /**
     * 根据设备分类ID查询绑定信息
     *
     * @param equipmentClassId
     * @return
     */
    SmsPlanEquipmentClass findPlanIdByEquipmentClassIdAndDelFlagIsFalse(String equipmentClassId);
}

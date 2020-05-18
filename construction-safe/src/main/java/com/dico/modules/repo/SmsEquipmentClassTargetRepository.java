package com.dico.modules.repo;

import com.dico.jpa.dao.JpaDao;
import com.dico.modules.domain.SmsEquipmentClassTarget;
import com.dico.modules.domain.SmsInspectionTarget;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author Gaodl
 * @version v1.0
 * 文件名称: $file_name$
 * 创建时间: $date$
 */
public interface SmsEquipmentClassTargetRepository extends JpaDao<SmsEquipmentClassTarget, String> {

    /**
     * 根据ID查询未删除的数据
     */
    SmsEquipmentClassTarget getSmsEquipmentClassTargetByIdAndDelFlagIsFalse(String smsEquipmentClassTargetId);

    void deleteByIdIn(String[] ids);

    /**
     * 获取设备绑定的巡检项
     *
     * @author Gaodl
     * 方法名称: findBindTargets
     * 参数： [equipmentId]
     * 返回值： java.util.List<com.dico.modules.domain.SmsInspectionTarget>
     * 创建时间: 2019/4/19
     */
    @Query("select sit from SmsInspectionTarget sit, SmsEquipmentClassTarget sect where sit.id = sect.targetId and sit.delFlag is false and sect.delFlag is false and sect.equipmentClassId = :equipmentClassId")
    List<SmsInspectionTarget> findBindTargets(@Param("equipmentClassId") String equipmentClassId);

    /**
     * 根据设备ID和巡检项ID查询设备巡检项绑定信息
     *
     * @author Gaodl
     * 方法名称: findByEquipmentIdEqualsAndTargetIdEqualsAndDelFlagIsFalse
     * 参数： [equipmentId, targetId]
     * 返回值： com.dico.modules.domain.SmsEquipmentTarget
     * 创建时间: 2019/4/19
     */
    SmsEquipmentClassTarget findByEquipmentClassIdEqualsAndTargetIdEqualsAndDelFlagIsFalse(String equipmentClassId, String targetId);
}

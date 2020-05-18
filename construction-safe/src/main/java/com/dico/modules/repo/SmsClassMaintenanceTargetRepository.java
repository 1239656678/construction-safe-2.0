package com.dico.modules.repo;

import com.dico.jpa.dao.JpaDao;
import com.dico.modules.domain.SmsClassMaintenanceTarget;

import java.util.List;

/**
 * @author Gaodl
 * @version v1.0
 * 文件名称: $file_name$
 * 创建时间: $date$
 */
public interface SmsClassMaintenanceTargetRepository extends JpaDao<SmsClassMaintenanceTarget,String>{

    /**
     * 根据ID查询未删除的数据
     */
    SmsClassMaintenanceTarget getByIdAndDelFlagIsFalse(String smsClassMaintenanceTargetId);

    SmsClassMaintenanceTarget findByEquipmentClassIdEqualsAndTargetIdEqualsAndDelFlagIsFalse(String equipmentClassId, String targetId);

    List<SmsClassMaintenanceTarget> findSmsClassMaintenanceTargetByEquipmentClassId(String equipmentClassId);
    void deleteByIdIn(String[]ids);

    void deleteById(String id);
}

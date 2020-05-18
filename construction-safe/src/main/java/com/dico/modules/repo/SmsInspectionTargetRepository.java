package com.dico.modules.repo;

import com.dico.jpa.dao.JpaDao;
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
public interface SmsInspectionTargetRepository extends JpaDao<SmsInspectionTarget, String> {

    /**
     * 根据ID查询未删除的数据
     */
    SmsInspectionTarget getSmsInspectionTargetByIdAndDelFlagIsFalse(String smsInspectionTargetId);

    void deleteByIdIn(String[] ids);

    @Query("select sit from SmsInspectionTarget sit left join SmsEquipmentClassTarget sect on sit.id = sect.targetId where sect.equipmentClassId = :equipmentClassId and sect.delFlag is false and sit.delFlag is false")
    List<SmsInspectionTarget> findByEquipmentClassId(@Param(value = "equipmentClassId") String equipmentClassId);

}

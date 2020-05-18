package com.dico.modules.repo;

import com.dico.jpa.dao.JpaDao;
import com.dico.modules.domain.SmsEquipment;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

/**
 * @author Gaodl
 * @version v1.0
 * 文件名称: $file_name$
 * 创建时间: $date$
 */
public interface SmsEquipmentRepository extends JpaDao<SmsEquipment, String> {

    /**
     * 根据ID查询未删除的数据
     *
     * @param smsEquipmentId
     * @return
     */
    SmsEquipment getSmsEquipmentByIdAndDelFlagIsFalse(String smsEquipmentId);

    void deleteByIdIn(String[] ids);

    /**
     * 根据多个ID查询
     *
     * @param ids
     * @return
     */
    List<SmsEquipment> findByIdInAndDelFlagIsFalse(String[] ids);

    /**
     * 根据多个区域ID查询设备
     *
     * @param ids
     * @return
     */
    List<SmsEquipment> findByInstallRegionsIdInAndDelFlagIsFalse(String[] ids);

    /**
     * 通过设备分类查询设备
     *
     * @param equipmentClassId
     * @return
     */
    List<SmsEquipment> findByTypeIdAndDelFlagIsFalse(String equipmentClassId);

    List<SmsEquipment> findByInstallRegionsIdIsNullAndDelFlagIsFalse();

    List<SmsEquipment> findByInstallRegionsIdAndDelFlagIsFalse(String itemId);

    @Query("select count(se.id) from SmsEquipment se where se.delFlag is false")
    int findCount();

    @Query("select count(se.id) from SmsEquipment se where se.delFlag is false and se.createDate between :firstDate and :lastDate")
    int findCountByCreateDateBetween(Date firstDate, Date lastDate);

    List<SmsEquipment> findByTypeIdAndInstallRegionsIdAndDelFlagIsFalse(String typeId, String regionsId);

    List<SmsEquipment> findByTypeIdInAndDelFlagIsFalse(String[] ids);
}

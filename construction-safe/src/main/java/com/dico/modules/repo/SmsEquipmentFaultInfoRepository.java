package com.dico.modules.repo;

import com.dico.jpa.dao.JpaDao;
import com.dico.modules.domain.SmsEquipmentFaultInfo;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;

/**
 * @author Gaodl
 * @version v1.0
 * 文件名称: $file_name$
 * 创建时间: $date$
 */
public interface SmsEquipmentFaultInfoRepository extends JpaDao<SmsEquipmentFaultInfo, String> {

    /**
     * 根据ID查询未删除的数据
     */
    SmsEquipmentFaultInfo getByIdAndDelFlagIsFalse(String smsEquipmentFaultInfoId);

    void deleteByIdIn(String[] ids);

    @Query("select count(sefi.id) from SmsEquipmentFaultInfo sefi where sefi.delFlag is false AND sefi.reportDate BETWEEN ?1 AND ?2")
    int findCount(Date preDate, Date nextDate);

    @Query("select count(sefi.id) from SmsEquipmentFaultInfo sefi where sefi.status is true AND sefi.reportDate BETWEEN ?1 AND ?2 AND sefi.delFlag is false")
    int findFinishedCount(Date preDate, Date nextDate);
}

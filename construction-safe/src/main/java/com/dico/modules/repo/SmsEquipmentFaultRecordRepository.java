package com.dico.modules.repo;

import com.dico.jpa.dao.JpaDao;
import com.dico.modules.domain.SmsEquipmentFaultRecord;

/**
 * @author Gaodl
 * @version v1.0
 * 文件名称: $file_name$
 * 创建时间: $date$
 */
public interface SmsEquipmentFaultRecordRepository extends JpaDao<SmsEquipmentFaultRecord,String>{

    /**
     * 根据ID查询未删除的数据
     */
    SmsEquipmentFaultRecord getByIdAndDelFlagIsFalse(String smsEquipmentFaultRecordId);

    void deleteByIdIn(String[]ids);
}

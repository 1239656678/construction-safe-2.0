package com.dico.modules.repo;

import com.dico.jpa.dao.JpaDao;
import com.dico.modules.domain.SmsEquipmentFaultAssign;

/**
 * @author Gaodl
 * @version v1.0
 * 文件名称: $file_name$
 * 创建时间: $date$
 */
public interface SmsEquipmentFaultAssignRepository extends JpaDao<SmsEquipmentFaultAssign,String>{

    /**
     * 根据ID查询未删除的数据
     */
    SmsEquipmentFaultAssign getByIdAndDelFlagIsFalse(String smsEquipmentFaultAssignId);

    SmsEquipmentFaultAssign getSmsEquipmentFaultAssignByFaultInfoIdAndDelFlagIsFalse(String faultInfoId);

    void deleteByIdIn(String[] ids);
}

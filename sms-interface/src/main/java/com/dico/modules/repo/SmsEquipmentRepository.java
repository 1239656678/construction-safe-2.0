package com.dico.modules.repo;

import com.dico.jpa.dao.JpaDao;
import com.dico.modules.domain.SmsEquipment;

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
    SmsEquipment getByIdAndDelFlagIsFalse(String smsEquipmentId);


    List<SmsEquipment> findByTypeIdInAndDelFlagIsFalse(String[] ids);

    /**
     * 根据ID查询未删除的数据
     *
     * @param smsEquipmentId
     * @return
     */
    SmsEquipment getSmsEquipmentByIdAndDelFlagIsFalse(String smsEquipmentId);
}

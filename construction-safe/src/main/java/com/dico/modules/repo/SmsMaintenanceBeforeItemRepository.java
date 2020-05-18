package com.dico.modules.repo;

import com.dico.jpa.dao.JpaDao;
import com.dico.modules.domain.SmsMaintenanceBeforeItem;

/**
 * @author Gaodl
 * @version v1.0
 * 文件名称: $file_name$
 * 创建时间: $date$
 */
public interface SmsMaintenanceBeforeItemRepository extends JpaDao<SmsMaintenanceBeforeItem,String>{

    /**
     * 根据ID查询未删除的数据
     */
    SmsMaintenanceBeforeItem getByIdAndDelFlagIsFalse(String smsMaintenanceBeforeItemId);

    void deleteByIdIn(String[]ids);
}

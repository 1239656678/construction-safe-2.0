package com.dico.modules.repo;

import com.dico.jpa.dao.JpaDao;
import com.dico.modules.domain.SmsUserMaintenancePlan;

/**
 * @author Gaodl
 * @version v1.0
 * 文件名称: $file_name$
 * 创建时间: $date$
 */
public interface SmsUserMaintenancePlanRepository extends JpaDao<SmsUserMaintenancePlan,String>{

    /**
     * 根据ID查询未删除的数据
     */
    SmsUserMaintenancePlan getByIdAndDelFlagIsFalse(String smsUserMaintenancePlanId);

    /**
     * 根据保养计划ID查询未删除的数据
     */
    SmsUserMaintenancePlan getByMaintenancePlanIdAndDelFlagIsFalse(String smsUserMaintenancePlanId);

    void deleteByIdIn(String[] ids);

    void deleteByMaintenancePlanId(String maintenancePlanId);
}

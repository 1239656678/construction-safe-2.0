package com.dico.modules.repo;

import com.dico.jpa.dao.JpaDao;
import com.dico.modules.domain.SmsMaintenancePlan;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;

/**
 * @author Gaodl
 * @version v1.0
 * 文件名称: $file_name$
 * 创建时间: $date$
 */
public interface SmsMaintenancePlanRepository extends JpaDao<SmsMaintenancePlan,String>{

    /**
     * 根据ID查询未删除的数据
     */
    SmsMaintenancePlan getByIdAndDelFlagIsFalse(String smsMaintenancePlanId);

    void deleteByIdIn(String[]ids);

    @Query("select count(smp.id) from SmsMaintenancePlan smp left join SmsUserMaintenancePlan sump on sump.maintenancePlanId = smp.id and sump.delFlag is false where sump.beginDate BETWEEN ?1 AND ?2 AND smp.delFlag is false")
    int findCount(Date preDate, Date nextDate);

    @Query("select count(smp.id) from SmsMaintenancePlan smp left join SmsUserMaintenancePlan sump on sump.maintenancePlanId = smp.id and sump.delFlag is false where sump.beginDate BETWEEN ?1 AND ?2 AND sump.status = 1 AND smp.delFlag is false")
    int findFinishedCount(Date preDate, Date nextDate);
}

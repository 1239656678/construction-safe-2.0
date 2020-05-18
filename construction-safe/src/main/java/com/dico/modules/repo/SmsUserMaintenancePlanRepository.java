package com.dico.modules.repo;

import com.dico.jpa.dao.JpaDao;
import com.dico.modules.domain.SmsUserMaintenancePlan;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

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

    void deleteByIdIn(String[]ids);

    void deleteByMaintenancePlanId(String maintenancePlanId);
    /**
     * 逾期和已完成的数据
     */
    @Query(value = "select * from sms_user_maintenance_plan sump where sump.STATUS>2 and sump.DEL_FLAG=false",nativeQuery = true)
    List<SmsUserMaintenancePlan> findAllList();

    /**
     * 待复查的数据
     */
    @Query(value = "select * from sms_user_maintenance_plan sump where sump.STATUS>0 and sump.STATUS<3 and sump.DEL_FLAG=false",nativeQuery = true)
    List<SmsUserMaintenancePlan> findAllListByMainten();
}

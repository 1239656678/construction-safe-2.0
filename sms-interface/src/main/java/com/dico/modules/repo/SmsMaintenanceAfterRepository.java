package com.dico.modules.repo;

import com.dico.jpa.dao.JpaDao;
import com.dico.modules.domain.SmsMaintenanceAfter;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author Gaodl
 * @version v1.0
 * 文件名称: $file_name$
 * 创建时间: $date$
 */
public interface SmsMaintenanceAfterRepository extends JpaDao<SmsMaintenanceAfter,String>{

    /**
     * 根据ID查询未删除的数据
     */
    SmsMaintenanceAfter getByIdAndDelFlagIsFalse(String smsMaintenanceAfterId);

    /**
     * 根据ID查询未删除的数据
     */
    List<SmsMaintenanceAfter> getSmsMaintenanceAfterByUserMaintenanceIdAndDelFlagIsFalse(String userMaintenanceId);

    void deleteByIdIn(String[] ids);
    @Query(value = "delete from sms_maintenance_after where USER_MAINTENANCE_ID=:id",nativeQuery = true)
    void deleteByUserMaintenanceId(String id);
}

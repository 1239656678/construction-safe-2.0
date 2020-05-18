package com.dico.modules.repo;

import com.dico.jpa.dao.JpaDao;
import com.dico.modules.domain.SmsInspectionStatus;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

/**
 * @author Gaodl
 * @version v1.0
 * 文件名称: $file_name$
 * 创建时间: $date$
 */
public interface SmsInspectionStatusRepository extends JpaDao<SmsInspectionStatus, String> {

    /**
     * 根据ID查询未删除的数据
     */
    SmsInspectionStatus getByIdAndDelFlagIsFalse(String smsInspectionStatusId);

    void deleteByIdIn(String[] ids);

    @Query("select count(sis.id) from SmsInspectionStatus sis where sis.delFlag is false and sis.status = :status")
    int findCount(int status);

    @Query("select count(sis.id) from SmsInspectionStatus sis where sis.delFlag is false and sis.status = :status and sis.updateDate between :firstDate and :lastDate")
    int findCountByUpdateDateBetween(int status, Date firstDate, Date lastDate);

    List<SmsInspectionStatus> findByUserInspectionPlanIdAndDelFlagIsFalse(String id);
}

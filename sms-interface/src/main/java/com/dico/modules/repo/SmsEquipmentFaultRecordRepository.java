package com.dico.modules.repo;

import com.dico.jpa.dao.JpaDao;
import com.dico.modules.domain.SmsEquipmentFaultRecord;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

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

    /**
     * 根据ID查询未删除的数据
     */
    @Query("SELECT SEFR FROM SmsEquipmentFaultRecord SEFR WHERE SEFR.reviewUserId=:reviewUserId and SEFR.staus=1 and SEFR.delFlag=false")
    List<SmsEquipmentFaultRecord> getByReviewUserIdAndDelFlagIsFalse(@Param("reviewUserId") String reviewUserId);

    void deleteByIdIn(String[] ids);

    SmsEquipmentFaultRecord findByFaultInfoIdAndDelFlagIsFalse(String faultInfoId);
}

package com.dico.modules.repo;

import com.dico.jpa.dao.JpaDao;
import com.dico.modules.domain.SmsEquipmentClassTarget;
import com.dico.modules.domain.SmsInspectionTarget;
import com.dico.modules.dto.InspectionTargetDTO;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author Gaodl
 * @version v1.0
 * 文件名称: $file_name$
 * 创建时间: $date$
 */
public interface SmsEquipmentClassTargetRepository extends JpaDao<SmsEquipmentClassTarget, String> {

    /**
     * 根据ID查询未删除的数据
     */
    SmsEquipmentClassTarget getByIdAndDelFlagIsFalse(String smsEquipmentClassTargetId);
}

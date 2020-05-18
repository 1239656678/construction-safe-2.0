package com.dico.modules.repo;

import com.dico.jpa.dao.JpaDao;
import com.dico.modules.domain.SmsUserInspectionPlan;

import java.util.List;

/**
 * @author Gaodl
 * @version v1.0
 * 文件名称: $file_name$
 * 创建时间: $date$
 */
public interface SmsUserInspectionPlanRepository extends JpaDao<SmsUserInspectionPlan, String> {

    /**
     * 根据ID查询未删除的数据
     */
    SmsUserInspectionPlan getByIdAndDelFlagIsFalse(String smsUserInspectionPlanId);

    List<SmsUserInspectionPlan> findBySmsInspectionPlanIdAndDelFlagIsFalse(String inspectionId);
}

package com.dico.modules.repo;

import com.dico.jpa.dao.JpaDao;
import com.dico.modules.domain.SmsUserMaintenancePlanReview;

/**
 * @author Gaodl
 * @version v1.0
 * 文件名称: $file_name$
 * 创建时间: $date$
 */
public interface SmsUserMaintenancePlanReviewRepository extends JpaDao<SmsUserMaintenancePlanReview,String>{

    /**
     * 根据ID查询未删除的数据
     */
    SmsUserMaintenancePlanReview getByIdAndDelFlagIsFalse(String smsUserMaintenancePlanReviewId);

    void deleteByIdIn(String[]ids);
}

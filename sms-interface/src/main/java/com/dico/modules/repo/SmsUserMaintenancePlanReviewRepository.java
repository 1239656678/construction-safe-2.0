package com.dico.modules.repo;

import com.dico.jpa.dao.JpaDao;
import com.dico.modules.domain.SmsUserMaintenancePlanReview;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

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

    @Query(value = "select sumpr from SmsUserMaintenancePlanReview sumpr where sumpr.maintainUserId= :maintainUserId and sumpr.delFlag=false and sumpr.maintainStatus=0")
    List<SmsUserMaintenancePlanReview> getByMaintainUserIdList(@Param("maintainUserId") String maintainUserId);

    void deleteByIdIn(String[] ids);

    void deleteByMaintainId(String id);
}

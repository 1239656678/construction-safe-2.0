package com.dico.modules.repo;

import com.dico.jpa.dao.JpaDao;
import com.dico.modules.domain.SmsInspectionPlan;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

/**
 * @author Gaodl
 * @version v1.0
 * 文件名称: $file_name$
 * 创建时间: $date$
 */
public interface SmsInspectionPlanRepository extends JpaDao<SmsInspectionPlan, String> {

    /**
     * 根据ID查询未删除的数据
     */
    SmsInspectionPlan getSmsInspectionPlanByIdAndDelFlagIsFalse(String smsInspectionPlanId);

    void deleteByIdIn(String[] ids);

    /**
     * 获取当前用户所有可执行的计划
     *
     * @author Gaodl
     * 方法名称: findByPersonLiableIdEqualsAndBeginDateLessThanEqualAndEndDateGreaterThanEqual
     * 参数： [personLiableId, beginDate, endDate]
     * 返回值： java.util.List<com.dico.modules.domain.SmsInspectionPlan>
     * 创建时间: 2019/5/10
     */
    List<SmsInspectionPlan> findByPersonLiableIdEqualsAndBeginDateLessThanEqualAndEndDateGreaterThanEqual(String personLiableId, Date beginDate, Date endDate);

    @Query("select count(sip.id) from SmsInspectionPlan sip where sip.delFlag is false")
    int findCount();

    @Query("select count(sip.id) from SmsInspectionPlan sip where sip.endDate < ?1 and sip.delFlag is false")
    int findFinishedCount(Date now);
}

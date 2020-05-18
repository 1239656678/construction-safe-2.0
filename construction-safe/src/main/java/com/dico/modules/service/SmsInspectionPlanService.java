package com.dico.modules.service;

import com.dico.jpa.dao.JpaDao;
import com.dico.jpa.service.JpaService;
import com.dico.modules.domain.SmsInspectionPlan;
import com.dico.modules.repo.SmsInspectionPlanRepository;
import org.apache.commons.lang.StringUtils;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class SmsInspectionPlanService extends JpaService<SmsInspectionPlan, String> {

    @Autowired
    private SmsInspectionPlanRepository smsInspectionPlanRepository;

    @Override
    protected JpaDao<SmsInspectionPlan, String> getDao() {
        return smsInspectionPlanRepository;
    }

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * 根据ID查询未删除的数据
     *
     * @author Gaodl
     * 方法名称: getSmsInspectionPlanByIdAndDelFlagIsFalse
     * 参数： [smsInspectionPlanId]
     * 返回值： com.dico.modules.domain.SmsInspectionPlan
     * 创建时间: 2019/4/18
     */
    public SmsInspectionPlan getSmsInspectionPlanByIdAndDelFlagIsFalse(String smsInspectionPlanId) {
        return smsInspectionPlanRepository.getSmsInspectionPlanByIdAndDelFlagIsFalse(smsInspectionPlanId);
    }

    public void deleteByIdIn(String[] ids) {
        smsInspectionPlanRepository.deleteByIdIn(ids);
    }

    /**
     * 获取所有当前人需要执行的计划
     *
     * @author Gaodl
     * 方法名称: findBy
     * 参数： [userId]
     * 返回值： java.util.List<com.dico.modules.domain.SmsInspectionPlan>
     * 创建时间: 2019/5/10
     */
    public List<SmsInspectionPlan> findCanExecuteByUserId(String personLiableId) {
        return smsInspectionPlanRepository.findByPersonLiableIdEqualsAndBeginDateLessThanEqualAndEndDateGreaterThanEqual(personLiableId, new Date(), new Date());
    }

    /**
     * 条件查询所有的巡检计划
     *
     * @author Gaodl
     * 方法名称: findInspectionPlanList
     * 参数： [planName, beginDate, endDate]
     * 返回值： java.util.List<com.dico.modules.domain.SmsInspectionPlan>
     * 创建时间: 2019/5/22
     */
    public List<SmsInspectionPlan> findInspectionPlanList(String userId, String planName, String planStatus) throws IllegalArgumentException {
        String sql = "SELECT * FROM sms_inspection_plan sip where sip.DEL_FLAG is FALSE AND sip.EXAMINE_STATUS = '2'";
        sql = this.addColumns(sql, userId, planName, planStatus);
        Query query = entityManager.createNativeQuery(sql, SmsInspectionPlan.class);
        query.unwrap(NativeQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        List list = query.getResultList();
        List<SmsInspectionPlan> smsInspectionPlanList = new ArrayList<>();
        for (Object obj : list) {
            Map row = (Map) obj;
            smsInspectionPlanList.add((SmsInspectionPlan) row.get("alias1"));
        }
        return smsInspectionPlanList;
    }

    private String addColumns(String sql, String userId, String planName, String planStatus) throws IllegalArgumentException {
        if (StringUtils.isNotBlank(planStatus)) {
            switch (planStatus) {
                // 表示计划未开始
                case "0":
                    sql += " AND DATE_FORMAT(sip.BEGIN_DATE, 'yyyy-MM-dd') > DATE_FORMAT(now(), 'yyyy-MM-dd')";
                    break;
                // 表示计划进行中
                case "1":
                    sql += " AND (DATE_FORMAT(sip.BEGIN_DATE, 'yyyy-MM-dd') <= DATE_FORMAT(now(), 'yyyy-MM-dd') AND DATE_FORMAT(now(), 'yyyy-MM-dd') <= DATE_FORMAT(sip.END_DATE, 'yyyy-MM-dd'))";
                    break;
                // 表示计划已结束
                case "2":
                    sql += " AND DATE_FORMAT(sip.END_DATE, 'yyyy-MM-dd') < DATE_FORMAT(now(), 'yyyy-MM-dd')";
                    break;
                default:
                    throw new IllegalArgumentException("计划状态不存在");
            }
        }
        if (StringUtils.isNotBlank(userId)) {
            sql += " AND sip.PERSON_LIABLE_ID = '" + userId + "'";
        }
        if (StringUtils.isNotBlank(planName)) {
            sql += " AND sip.PLAN_NAME LIKE '%" + planName + "%'";
        }
        sql += " order by sip.CREATE_DATE desc";
        return sql;
    }

    public int findCount(){
        return smsInspectionPlanRepository.findCount();
    }

    public int findFinishedCount(){
        return smsInspectionPlanRepository.findFinishedCount(new Date());
    }

    public List<Map<String, Object>> findTimeoutInfo(Date firstDate, Date lastDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT sip.PLAN_CODE AS code, sip.PLAN_NAME AS name, CASE sip.PLAN_TYPE WHEN 0 THEN '日检查' WHEN 1 THEN '周检查' WHEN 2 THEN '月检查' WHEN 3 THEN '季度检查' WHEN 4 THEN '专项检查' END AS type, suip.BEGIN_DATE AS beginDate, suip.END_DATE AS endDate, su.`NAME` AS person, o.`NAME` AS organization FROM sms_user_inspection_plan suip LEFT JOIN sms_inspection_plan sip ON sip.id = suip.INSPECTION_ID AND sip.DEL_FLAG IS FALSE LEFT JOIN sys_user su ON su.id = suip.PERSON_LIABLE_ID AND su.DEL_FLAG IS FALSE LEFT JOIN organization o ON o.id = su.ORGANIZATION_ID AND o.DEL_FLAG IS FALSE WHERE suip.`STATUS` = 3")
                .append(" AND ( (suip.BEGIN_DATE BETWEEN '").append(sdf.format(firstDate)).append("' AND '").append(sdf.format(lastDate)).append("') OR (suip.END_DATE BETWEEN '").append(sdf.format(firstDate)).append("' AND '").append(sdf.format(lastDate)).append("') )");
        Query query = entityManager.createNativeQuery(sb.toString());
        query.unwrap(NativeQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return query.getResultList();
    }
}

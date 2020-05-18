package com.dico.modules.service;

import com.dico.jpa.dao.JpaDao;
import com.dico.jpa.service.JpaService;
import com.dico.modules.domain.SmsMaintenancePlan;
import com.dico.modules.dto.SmsMaintenancePlanDto;
import com.dico.modules.dto.SmsMaintenanceRecordDto;
import com.dico.modules.repo.SmsMaintenancePlanRepository;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class SmsMaintenancePlanService extends JpaService<SmsMaintenancePlan,String>{

    @Autowired
    private SmsMaintenancePlanRepository smsMaintenancePlanRepository;

    @Autowired
    private EntityManager entityManager;

    @Override
    protected JpaDao<SmsMaintenancePlan,String>getDao(){
        return smsMaintenancePlanRepository;
    }

    /**
    * 根据ID查询未删除的数据
    */
    public SmsMaintenancePlan getById(String smsMaintenancePlanId){
        return smsMaintenancePlanRepository.getByIdAndDelFlagIsFalse(smsMaintenancePlanId);
    }

    public void deleteByIdIn(String[]ids){
        smsMaintenancePlanRepository.deleteByIdIn(ids);
    }

    public Object findList() {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT smp.id AS id, sec.CLASS_NAME AS class_name, se.`CODE` AS equipment_code, se.`NAME` AS equipment_name, smp.CYCLE AS cycle, smp.BEGIN_DATE AS begin_date, smp.END_DATE AS end_date, su.`ORGANIZATION_NAME` AS maintenance_organization, su.`NAME` AS maintenance_user, smp.REMARK AS remark FROM sms_maintenance_plan smp LEFT JOIN sms_equipment se ON se.ID = smp.EQUIPMENT_ID AND se.DEL_FLAG = FALSE LEFT JOIN sms_equipment_class sec ON sec.id = se.TYPE_ID AND sec.DEL_FLAG = FALSE LEFT JOIN sys_user su ON su.id = smp.MAINTENANCE_USER AND su.DEL_FLAG = FALSE WHERE smp.DEL_FLAG = FALSE");
        Query query = entityManager.createNativeQuery(sb.toString(), SmsMaintenancePlanDto.class);
        List<SmsMaintenancePlanDto> smsMaintenancePlanDtoList = query.getResultList();
        return smsMaintenancePlanDtoList;
    }

    public int findCount(Date preDate, Date nextDate) {
        return smsMaintenancePlanRepository.findCount(preDate, nextDate);
    }

    public int findFinishedCount(Date preDate, Date nextDate){
        return smsMaintenancePlanRepository.findFinishedCount(preDate, nextDate);
    }

    public List<Map<String, Object>> findWordInfoWillDoing(Date firstDate, Date lastDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT se.`CODE` AS equipmentCode, se.`NAME` AS equipmentName, sec.CLASS_NAME AS className, o.`NAME` AS organization, su.`NAME` AS maintenanceUser, sump.CYCLE AS cycle FROM sms_user_maintenance_plan sump LEFT JOIN sms_maintenance_plan smp ON smp.id = sump.MAINTENANCE_PLAN_ID AND smp.DEL_FLAG IS FALSE LEFT JOIN sms_equipment se ON se.ID = smp.EQUIPMENT_ID AND se.DEL_FLAG IS FALSE LEFT JOIN sms_equipment_class sec ON sec.ID = se.TYPE_ID AND sec.DEL_FLAG IS FALSE LEFT JOIN sys_user su ON su.id = smp.MAINTENANCE_USER AND su.DEL_FLAG IS FALSE LEFT JOIN organization o ON o.id = su.ORGANIZATION_ID AND o.DEL_FLAG IS FALSE WHERE sump.DEL_FLAG IS FALSE AND sump.`STATUS` IN (0, 2)")
                .append(" AND ( (sump.BEGIN_DATE BETWEEN '").append(sdf.format(firstDate)).append("' AND '").append(sdf.format(lastDate)).append("') OR (sump.END_DATE BETWEEN '").append(sdf.format(firstDate)).append("' AND '").append(sdf.format(lastDate)).append("') )");
        Query query = entityManager.createNativeQuery(sb.toString());
        query.unwrap(NativeQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return query.getResultList();
    }
}

package com.dico.modules.service;

import com.dico.jpa.dao.JpaDao;
import com.dico.jpa.service.JpaService;
import com.dico.modules.domain.SmsUserMaintenancePlan;
import com.dico.modules.dto.SmsMaintenanceRecordInfoDto;
import com.dico.modules.dto.SmsMaintenanceTargetDto;
import com.dico.modules.dto.SmsUserMaintenanceListDto;
import com.dico.modules.repo.SmsUserMaintenancePlanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

@Service
public class SmsUserMaintenancePlanService extends JpaService<SmsUserMaintenancePlan,String>{

    @Autowired
    private SmsUserMaintenancePlanRepository smsUserMaintenancePlanRepository;

    @Autowired
    private EntityManager entityManager;

    @Override
    protected JpaDao<SmsUserMaintenancePlan,String>getDao(){
        return smsUserMaintenancePlanRepository;
    }

    /**
    * 根据ID查询未删除的数据
    */
    public SmsUserMaintenancePlan getById(String smsUserMaintenancePlanId){
        return smsUserMaintenancePlanRepository.getByIdAndDelFlagIsFalse(smsUserMaintenancePlanId);
    }
    /**
     * 根据保养计划ID查询未删除的数据
     */
    public SmsUserMaintenancePlan getByMainId(String maintenancePlanId){
        return smsUserMaintenancePlanRepository.getByMaintenancePlanIdAndDelFlagIsFalse(maintenancePlanId);
    }

    public void deleteByIdIn(String[] ids){
        smsUserMaintenancePlanRepository.deleteByIdIn(ids);
    }

    public List<SmsUserMaintenanceListDto> findUserMaintenancePlanList(String userId) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT sump.id AS id, smp.cycle AS cycle, se.`CODE` AS equipment_code, se.`NAME` AS equipment_name, sump. STATUS AS STATUS, sump.REMARK AS REMARK, sr.`NAME` AS install_area, su.`ORGANIZATION_NAME` AS maintenance_organization, su.`NAME` AS maintenance_user, sump.END_DATE AS end_date FROM sms_user_maintenance_plan sump LEFT JOIN sms_maintenance_plan smp ON smp.id = sump.MAINTENANCE_PLAN_ID AND smp.DEL_FLAG = FALSE LEFT JOIN sms_equipment se ON se.ID = smp.EQUIPMENT_ID AND se.DEL_FLAG = FALSE LEFT JOIN sms_regions sr ON sr.ID = se.INSTALL_REGIONS_ID AND sr.DEL_FLAG = FALSE LEFT JOIN sys_user su ON su.id = smp.MAINTENANCE_USER AND su.DEL_FLAG = FALSE WHERE sump.`STATUS` = 0 AND sump.DEL_FLAG = FALSE  AND (NOW() BETWEEN sump.BEGIN_DATE AND sump.END_DATE) AND smp.MAINTENANCE_USER = '").append(userId).append("'");
        Query query = entityManager.createNativeQuery(sb.toString(), SmsUserMaintenanceListDto.class);
        List<SmsUserMaintenanceListDto> smsUserMaintenanceListDtoList = query.getResultList();
        return smsUserMaintenanceListDtoList;
    }

    public List<SmsUserMaintenanceListDto> findUserMaintenancePlanRecordList() {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT sump.id AS id, smp.cycle AS cycle, se.`CODE` AS equipment_code,sump.REMARK AS REMARK,se.`NAME` AS equipment_name, sump.`STATUS` AS STATUS, sr.`NAME` AS install_area, su.`ORGANIZATION_NAME` AS maintenance_organization, su.`NAME` AS maintenance_user, sump.END_DATE AS end_date FROM sms_user_maintenance_plan sump LEFT JOIN sms_maintenance_plan smp ON smp.id = sump.MAINTENANCE_PLAN_ID AND smp.DEL_FLAG = FALSE LEFT JOIN sms_equipment se ON se.ID = smp.EQUIPMENT_ID AND se.DEL_FLAG = FALSE LEFT JOIN sms_regions sr ON sr.ID = se.INSTALL_REGIONS_ID AND sr.DEL_FLAG = FALSE LEFT JOIN sys_user su ON su.id = smp.MAINTENANCE_USER AND su.DEL_FLAG = FALSE WHERE sump.DEL_FLAG = FALSE AND (sump.`STATUS` = 4) ORDER BY END_DATE DESC");
        Query query = entityManager.createNativeQuery(sb.toString(), SmsUserMaintenanceListDto.class);
        List<SmsUserMaintenanceListDto> smsUserMaintenanceListDtoList = query.getResultList();
        return smsUserMaintenanceListDtoList;
    }

    public List<SmsMaintenanceTargetDto> findMaintenanceTarget(String maintenanceId) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT smt.id AS target_id, smt.TARGET_NAME AS target_name FROM sms_user_maintenance_plan sump LEFT JOIN sms_maintenance_plan smp ON smp.id = sump.MAINTENANCE_PLAN_ID AND smp.DEL_FLAG = FALSE LEFT JOIN sms_equipment se ON se.ID = smp.EQUIPMENT_ID AND se.DEL_FLAG = FALSE LEFT JOIN sms_equipment_class sec ON sec.id = se.TYPE_ID AND sec.DEL_FLAG = FALSE LEFT JOIN sms_class_maintenance_target scmt ON scmt.EQUIPMENT_CLASS_ID = sec.id AND scmt.DEL_FLAG = FALSE LEFT JOIN sms_maintenance_target smt ON smt.id = scmt.TARGET_ID AND smt.DEL_FLAG = FALSE WHERE sump.id = '").append(maintenanceId).append("' GROUP BY target_id");
        Query query = entityManager.createNativeQuery(sb.toString(), SmsMaintenanceTargetDto.class);
        List<SmsMaintenanceTargetDto> smsMaintenanceTargetDtoList = query.getResultList();
        return smsMaintenanceTargetDtoList;
    }

    public SmsMaintenanceRecordInfoDto findRecordInfo(String maintenanceRecordId) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT sump.id AS id, se.`NAME` AS equipment_name, sump.`STATUS` AS STATUS, sr.`NAME` AS install_area, se.`CODE` AS equipment_code, su.ORGANIZATION_NAME AS maintenance_organization, su.`NAME` AS maintenance_user, smp.CYCLE AS cycle, sma.COST AS cost, smb.REMARK AS remark, sma.CREATE_DATE AS finish_date, sma.REMARK AS result, smb.id AS smb_id, sma.id AS sma_id FROM sms_user_maintenance_plan sump LEFT JOIN sms_maintenance_plan smp ON smp.id = sump.MAINTENANCE_PLAN_ID AND smp.DEL_FLAG = FALSE LEFT JOIN sms_equipment se ON se.ID = smp.EQUIPMENT_ID AND se.DEL_FLAG = FALSE LEFT JOIN sms_regions sr ON sr.ID = se.INSTALL_REGIONS_ID AND sr.DEL_FLAG = FALSE LEFT JOIN sys_user su ON smp.MAINTENANCE_USER = su.id AND su.DEL_FLAG = FALSE LEFT JOIN sms_maintenance_before smb ON smb.USER_MAINTENANCE_ID = sump.id AND smb.DEL_FLAG = FALSE LEFT JOIN sms_maintenance_after sma ON sma.USER_MAINTENANCE_ID = sump.id AND sma.DEL_FLAG = FALSE WHERE sump.id = '").append(maintenanceRecordId).append("'");
        Query query = entityManager.createNativeQuery(sb.toString(), SmsMaintenanceRecordInfoDto.class);
        List<SmsMaintenanceRecordInfoDto> smsMaintenanceRecordInfoDtoList = query.getResultList();
        return null == smsMaintenanceRecordInfoDtoList || smsMaintenanceRecordInfoDtoList.size() == 0 ? null : smsMaintenanceRecordInfoDtoList.get(0);
    }
}

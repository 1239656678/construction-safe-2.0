package com.dico.modules.service;

import com.dico.jpa.dao.JpaDao;
import com.dico.jpa.service.JpaService;
import com.dico.modules.domain.SmsUserInspectionPlan;
import com.dico.modules.domain.SmsUserMaintenancePlan;
import com.dico.modules.dto.SmsMaintenanceRecordDto;
import com.dico.modules.dto.SmsMaintenanceRecordInfoDto;
import com.dico.modules.dto.SmsUserMaintenanceListDto;
import com.dico.modules.repo.SmsUserMaintenancePlanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.Transactional;
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

    public void deleteByIdIn(String[] ids){
        smsUserMaintenancePlanRepository.deleteByIdIn(ids);
    }

    @Transactional
    public void deleteByMaintenancePlanId(String maintenancePlanId){
        smsUserMaintenancePlanRepository.deleteByMaintenancePlanId(maintenancePlanId);
    }

    public SmsUserMaintenancePlan findByCycleAndPlanId(String cycle, String id) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT * FROM sms_user_maintenance_plan sump WHERE sump.cycle = '" + cycle + "' AND sump.maintenance_plan_id = '" + id + "' AND sump.del_flag = 0");
        Query query = entityManager.createNativeQuery(sb.toString(), SmsUserMaintenancePlan.class);
        List<SmsUserMaintenancePlan> smsUserMaintenancePlanList = query.getResultList();
        return null == smsUserMaintenancePlanList || smsUserMaintenancePlanList.size() == 0 ? null : smsUserMaintenancePlanList.get(0);
    }

    public List<SmsMaintenanceRecordDto> findList() {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT sump.id AS id, se.`CODE` AS requipment_code, se.`NAME` AS requipment_name, sec.CLASS_NAME AS class_name, CASE WHEN sump.`STATUS` = 0 THEN 2 ELSE 1 END AS 'status', su.`NAME` AS maintenance_user, su.ORGANIZATION_NAME AS maintenance_organization, smp.CYCLE AS cycle, sma.COST AS cost, sma.CREATE_DATE AS finish_date FROM sms_user_maintenance_plan sump LEFT JOIN sms_maintenance_plan smp ON sump.MAINTENANCE_PLAN_ID = smp.id AND smp.DEL_FLAG = FALSE LEFT JOIN sys_user su ON smp.MAINTENANCE_USER = su.id AND su.DEL_FLAG = FALSE LEFT JOIN sms_equipment se ON se.ID = smp.EQUIPMENT_ID AND se.DEL_FLAG = FALSE LEFT JOIN sms_equipment_class sec ON sec.id = se.TYPE_ID AND sec.DEL_FLAG = FALSE LEFT JOIN sms_maintenance_after sma ON sma.USER_MAINTENANCE_ID = sump.id AND sma.DEL_FLAG = FALSE WHERE sump.DEL_FLAG = FALSE AND (( sump. STATUS = 0 AND sump.END_DATE < NOW()) OR sump. STATUS = 1 ) ORDER BY sump.CREATE_DATE DESC");
        Query query = entityManager.createNativeQuery(sb.toString(), SmsMaintenanceRecordDto.class);
        List<SmsMaintenanceRecordDto> smsMaintenanceRecordDtoList = query.getResultList();
        return smsMaintenanceRecordDtoList;
    }

    public List<SmsMaintenanceRecordDto> findListByStatus() {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT sump.id AS id, se.`CODE` AS requipment_code, se.`NAME` AS requipment_name, sec.CLASS_NAME AS class_name, sump.`STATUS` = 1  AS 'status', su.`NAME` AS maintenance_user, su.ORGANIZATION_NAME AS maintenance_organization, smp.CYCLE AS cycle, sma.COST AS cost, sma.CREATE_DATE AS finish_date FROM sms_user_maintenance_plan sump LEFT JOIN sms_maintenance_plan smp ON sump.MAINTENANCE_PLAN_ID = smp.id AND smp.DEL_FLAG = FALSE LEFT JOIN sys_user su ON smp.MAINTENANCE_USER = su.id AND su.DEL_FLAG = FALSE LEFT JOIN sms_equipment se ON se.ID = smp.EQUIPMENT_ID AND se.DEL_FLAG = FALSE LEFT JOIN sms_equipment_class sec ON sec.id = se.TYPE_ID AND sec.DEL_FLAG = FALSE LEFT JOIN sms_maintenance_after sma ON sma.USER_MAINTENANCE_ID = sump.id AND sma.DEL_FLAG = FALSE WHERE sump.DEL_FLAG = FALSE AND (sump. STATUS = 1 AND sump.END_DATE < NOW() ) ORDER BY sump.CREATE_DATE DESC");
        Query query = entityManager.createNativeQuery(sb.toString(), SmsMaintenanceRecordDto.class);
        List<SmsMaintenanceRecordDto> smsMaintenanceRecordDtoList = query.getResultList();
        return smsMaintenanceRecordDtoList;
    }


    public SmsMaintenanceRecordInfoDto findRecordInfo(String maintenanceRecordId) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT sump.id AS id, se.`NAME` AS equipment_name, sump.`STATUS` AS STATUS, sr.`NAME` AS install_area, se.`CODE` AS equipment_code, su.ORGANIZATION_NAME AS maintenance_organization, su.`NAME` AS maintenance_user, smp.CYCLE AS cycle, sma.COST AS cost, smb.REMARK AS remark, sma.CREATE_DATE AS finish_date, sma.REMARK AS result, smb.id AS smb_id, sma.id AS sma_id FROM sms_user_maintenance_plan sump LEFT JOIN sms_maintenance_plan smp ON smp.id = sump.MAINTENANCE_PLAN_ID AND smp.DEL_FLAG = FALSE LEFT JOIN sms_equipment se ON se.ID = smp.EQUIPMENT_ID AND se.DEL_FLAG = FALSE LEFT JOIN sms_regions sr ON sr.ID = se.INSTALL_REGIONS_ID AND sr.DEL_FLAG = FALSE LEFT JOIN sys_user su ON smp.MAINTENANCE_USER = su.id AND su.DEL_FLAG = FALSE LEFT JOIN sms_maintenance_before smb ON smb.USER_MAINTENANCE_ID = sump.id AND smb.DEL_FLAG = FALSE LEFT JOIN sms_maintenance_after sma ON sma.USER_MAINTENANCE_ID = sump.id AND sma.DEL_FLAG = FALSE WHERE sump.id = '").append(maintenanceRecordId).append("'");
        Query query = entityManager.createNativeQuery(sb.toString(), SmsMaintenanceRecordInfoDto.class);
        List<SmsMaintenanceRecordInfoDto> smsMaintenanceRecordInfoDtoList = query.getResultList();
        return null == smsMaintenanceRecordInfoDtoList || smsMaintenanceRecordInfoDtoList.size() == 0 ? null : smsMaintenanceRecordInfoDtoList.get(0);
    }

    public List<SmsUserMaintenanceListDto> findUserMaintenanceRecordList(String id) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT sump.id AS id, smp.cycle AS cycle, se.`CODE` AS equipment_code, se.`NAME` AS equipment_name, CASE WHEN sump. STATUS = 1 THEN 1 ELSE 2 END AS STATUS, sr.`NAME` AS install_area, su.`ORGANIZATION_NAME` AS maintenance_organization, su.`NAME` AS maintenance_user, sump.END_DATE AS end_date FROM sms_user_maintenance_plan sump LEFT JOIN sms_maintenance_plan smp ON smp.id = sump.MAINTENANCE_PLAN_ID AND smp.DEL_FLAG = FALSE LEFT JOIN sms_equipment se ON se.ID = smp.EQUIPMENT_ID AND se.DEL_FLAG = FALSE LEFT JOIN sms_regions sr ON sr.ID = se.INSTALL_REGIONS_ID AND sr.DEL_FLAG = FALSE LEFT JOIN sys_user su ON su.id = smp.MAINTENANCE_USER AND su.DEL_FLAG = FALSE WHERE sump.DEL_FLAG = FALSE AND ( sump.`STATUS` = 1 OR ( sump.`STATUS` = 0 AND NOW() > sump.END_DATE )) AND smp.EQUIPMENT_ID = '").append(id).append("' ORDER BY END_DATE DESC");
        Query query = entityManager.createNativeQuery(sb.toString(), SmsUserMaintenanceListDto.class);
        List<SmsUserMaintenanceListDto> smsUserMaintenanceListDtoList = query.getResultList();
        return smsUserMaintenanceListDtoList;
    }
    public List<SmsUserMaintenancePlan> findAllList(){
        return smsUserMaintenancePlanRepository.findAllList();
    }

    public List<SmsUserMaintenancePlan> findAllListByMaintain(){
        return smsUserMaintenancePlanRepository.findAllListByMainten();
    }
}

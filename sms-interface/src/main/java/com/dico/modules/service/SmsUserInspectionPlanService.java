package com.dico.modules.service;

import com.dico.enums.PlanStatusEnums;
import com.dico.enums.PlanTypeEnums;
import com.dico.jpa.dao.JpaDao;
import com.dico.jpa.service.JpaService;
import com.dico.modules.domain.SmsEquipmentClass;
import com.dico.modules.domain.SmsPlanEquipmentClass;
import com.dico.modules.domain.SmsRegions;
import com.dico.modules.domain.SmsUserInspectionPlan;
import com.dico.modules.dto.EquipmentClassDTO;
import com.dico.modules.dto.InspectionEquipmentDTO;
import com.dico.modules.dto.InspectionPlanDTO;
import com.dico.modules.dto.InspectionRecordDTO;
import com.dico.modules.repo.SmsUserInspectionPlanRepository;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class SmsUserInspectionPlanService extends JpaService<SmsUserInspectionPlan, String> {

    @Autowired
    private SmsUserInspectionPlanRepository smsUserInspectionPlanRepository;

    @Autowired
    private SmsPlanEquipmentClassService smsPlanEquipmentClassService;

    @Autowired
    private SmsEquipmentClassService smsEquipmentClassService;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private SmsRegionsService smsRegionsService;

    @Override
    protected JpaDao<SmsUserInspectionPlan, String> getDao() {
        return smsUserInspectionPlanRepository;
    }

    /**
     * 根据ID查询未删除的数据
     *
     * @param smsUserInspectionPlanId
     * @return
     */
    public SmsUserInspectionPlan getByIdAndDelFlagIsFalse(String smsUserInspectionPlanId) {
        return smsUserInspectionPlanRepository.getByIdAndDelFlagIsFalse(smsUserInspectionPlanId);
    }

    /**
     * 获取用户的所有检查计划列表
     *
     * @param userIds
     * @param currentDate
     * @return
     */
    public List<InspectionPlanDTO> findInspectionPlan(String[] userIds, String currentDate, boolean isSpecial) {
        String id = "";
        for (String userId : userIds) {
            id += "'" + userId + "',";
        }
        id = id.substring(0, id.length() - 1);
        String param = " AND sip.PLAN_TYPE = " + PlanTypeEnums.SPECIAL.getKey() + "";
        if (!isSpecial) {
            param = " AND sip.PLAN_TYPE in ('" + PlanTypeEnums.DAY.getKey() + "','" + PlanTypeEnums.WEEK.getKey() + "','" + PlanTypeEnums.MONTH.getKey() + "','" + PlanTypeEnums.QUARTER.getKey() + "')";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT suip.ID AS id, sip.PLAN_CODE AS code, sip.PLAN_NAME AS name, sip.PLAN_TYPE AS type, suip.STATUS AS 'status',").
                append(" suip.BEGIN_DATE AS begin_date, suip.END_DATE AS end_date, su.`NAME` AS 'inspection_user', o.`NAME` AS 'organization_name' FROM sms_user_inspection_plan suip LEFT JOIN").
                append(" sms_inspection_plan sip ON sip.id = suip.INSPECTION_ID LEFT JOIN sys_user su ON suip.PERSON_LIABLE_ID = su.id").
                append(" LEFT JOIN organization o ON o.id = su.ORGANIZATION_ID WHERE").
                append(" suip.PERSON_LIABLE_ID in (").append(id).
                append(")").append(param).append(" AND suip.STATUS in ('").append(PlanStatusEnums.WILL.getKey() + "','" + PlanStatusEnums.DOING.getKey()).append("')").append(" AND '").append(currentDate).append("' BETWEEN suip.BEGIN_DATE AND suip.END_DATE AND suip.DEL_FLAG IS FALSE");
        Query query = entityManager.createNativeQuery(sb.toString(), InspectionPlanDTO.class);
        return query.getResultList();
    }


    /**
     * 根据计划ID获取所有未检查的设备信息
     *
     * @param userPlanId
     * @return
     */
    public List<InspectionEquipmentDTO> findInspectionEquipment(String userPlanId, String regionId, String equipmentClassId) {
        StringBuilder sb = new StringBuilder();
        String param = " AND 1=1";
        if (StringUtils.isNotBlank(regionId)) {
            List<SmsRegions> regionsList = smsRegionsService.findChildrens(regionId);
            String ids = "";
            ids = this.iterableRegionsId(regionsList, ids).substring(1);
            param += " AND se.INSTALL_REGIONS_ID in (" + ids + ")";
        }
        if (StringUtils.isNotBlank(equipmentClassId)) {
            param += " AND se.TYPE_ID = '" + equipmentClassId + "'";
        }
        sb.append("SELECT sis.id AS status_id, sis. STATUS AS STATUS, se.TYPE_ID AS equipment_class_id, se.id AS equipment_id, se.`CODE` AS equipment_code, se.`NAME` AS equipment_name, sec.CLASS_NAME AS class_name,").
                append(" suip.BEGIN_DATE AS begin_date, suip.END_DATE AS end_date, sr.`NAME` AS install_area FROM `sms_inspection_status` sis LEFT JOIN sms_equipment_class sec ON sec.id = sis.EQUIPMENT_CLASS_ID").
                append(" LEFT JOIN sms_equipment se ON se.ID = sis.EQUIPMENT_ID LEFT JOIN sms_regions sr ON sr.ID = se.INSTALL_REGIONS_ID LEFT JOIN sms_user_inspection_plan suip ON suip.id = sis.USER_INSPECTION_PLAN_ID").
                append(" WHERE se.del_flag = 0 AND suip.id = '").append(userPlanId).append("' AND sis.`STATUS` = ").append(PlanStatusEnums.WILL.getKey()).append(param);
        Query query = entityManager.createNativeQuery(sb.toString(), InspectionEquipmentDTO.class);
        return query.getResultList();
    }

    private String iterableRegionsId(List<SmsRegions> regionsList, String ids) {
        for (SmsRegions smsRegions : regionsList) {
            List<SmsRegions> childrenList = smsRegions.getChildrens();
            if (null != childrenList && childrenList.size() > 0) {
                ids += this.iterableRegionsId(childrenList, ids);
            }
            ids += ",'" + smsRegions.getId() + "'";
        }
        return ids;
    }

    /**
     * 根据计划ID查询计划下的所有设备分类
     *
     * @param inspectionId
     * @return
     */
    public List<EquipmentClassDTO> findInspectionEquipmentClass(String inspectionId) {
        SmsUserInspectionPlan smsUserInspectionPlan = smsUserInspectionPlanRepository.getByIdAndDelFlagIsFalse(inspectionId);
        List<SmsPlanEquipmentClass> smsPlanEquipmentClassList = smsPlanEquipmentClassService.findByPlanId(smsUserInspectionPlan.getSmsInspectionPlan().getId());
        List<String> equipmentClassIdList = new ArrayList<>();
        smsPlanEquipmentClassList.forEach(smsPlanEquipmentClass -> {
            equipmentClassIdList.add(smsPlanEquipmentClass.getEquipmentClassId());
        });
        List<SmsEquipmentClass> smsEquipmentClassList = smsEquipmentClassService.findByIdIn(equipmentClassIdList.toArray(new String[0]));
        List<EquipmentClassDTO> equipmentClassDTOList = new ArrayList<>();
        smsEquipmentClassList.forEach(smsEquipmentClass -> {
            EquipmentClassDTO equipmentClassDTO = new EquipmentClassDTO();
            equipmentClassDTO.setId(smsEquipmentClass.getId());
            equipmentClassDTO.setName(smsEquipmentClass.getClassName());
            equipmentClassDTOList.add(equipmentClassDTO);
        });
        return equipmentClassDTOList;
    }

    public List<InspectionPlanDTO> findInspectionPlanRecord(String[] userIds, String dateFormat) {
        String id = "";
        for (String userId : userIds) {
            id += "'" + userId + "',";
        }
        id = id.substring(0, id.length() - 1);
        StringBuilder sb = new StringBuilder();
        String param = " AND 1=1";
        if (StringUtils.isNotBlank(dateFormat)) {
            SimpleDateFormat sdf_day = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat sdf_second = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                param = " AND '" + sdf_second.format(sdf_day.parse(dateFormat)) + "' BETWEEN suip.BEGIN_DATE AND suip.END_DATE";
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        sb.append("SELECT suip.ID AS id, sip.PLAN_CODE AS CODE, sip.PLAN_NAME AS NAME, sip.PLAN_TYPE AS type, suip. STATUS AS 'status', suip.BEGIN_DATE AS begin_date, suip.END_DATE AS end_date, su.`NAME` AS 'inspection_user', o.`NAME` AS 'organization_name' FROM sms_user_inspection_plan suip LEFT JOIN sms_inspection_plan sip ON sip.id = suip.INSPECTION_ID LEFT JOIN sys_user su ON suip.PERSON_LIABLE_ID = su.id LEFT JOIN organization o ON o.id = su.ORGANIZATION_ID WHERE suip.PERSON_LIABLE_ID IN (" + id + ") AND suip.`STATUS` IN (1, 2)").append(param);
        Query query = entityManager.createNativeQuery(sb.toString(), InspectionPlanDTO.class);
        return query.getResultList();
    }

    public List<InspectionRecordDTO> findInspectionRecordByPlanId(String planId) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT sis.id AS status_id, se.name AS equipment_name, sr.`NAME` AS address, su.`NAME` AS inspection_user, sis.UPDATE_DATE AS inspection_date, sis.INSTRUCTIONS AS instructions, sis.RESULT AS result FROM sms_inspection_status sis LEFT JOIN sms_equipment se ON se.id = sis.EQUIPMENT_ID LEFT JOIN sms_regions sr ON sr.ID = se.INSTALL_REGIONS_ID LEFT JOIN sys_user su ON su.id = sis.UPDATE_USER WHERE sis. STATUS = 2 AND sis.USER_INSPECTION_PLAN_ID = '" + planId + "'");
        Query query = entityManager.createNativeQuery(sb.toString(), InspectionRecordDTO.class);
        return query.getResultList();
    }
}

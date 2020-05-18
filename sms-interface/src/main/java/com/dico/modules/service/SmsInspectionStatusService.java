package com.dico.modules.service;

import com.dico.enums.PlanStatusEnums;
import com.dico.jpa.dao.JpaDao;
import com.dico.jpa.service.JpaService;
import com.dico.modules.domain.SmsInspectionStatus;
import com.dico.modules.dto.WillInspectionDTO;
import com.dico.modules.repo.SmsInspectionStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.Date;
import java.util.List;

@Service
public class SmsInspectionStatusService extends JpaService<SmsInspectionStatus, String> {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private SmsInspectionStatusRepository smsInspectionStatusRepository;

    @Override
    protected JpaDao<SmsInspectionStatus, String> getDao() {
        return smsInspectionStatusRepository;
    }

    /**
     * 根据ID查询未删除的数据
     */
    public SmsInspectionStatus getByIdAndDelFlagIsFalse(String smsInspectionStatusId) {
        return smsInspectionStatusRepository.getByIdAndDelFlagIsFalse(smsInspectionStatusId);
    }

    /**
     * 根据用户ID获取用户的所有待巡检设备
     *
     * @param currentUserId
     * @return
     */
    public List<WillInspectionDTO> findCurrentUserInspection(String currentUserId, String dateFormat) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT sis.id AS status_id, se.id AS equipment_id, se.type_id AS equipment_class_id, se.`NAME` AS equipment_name, se.`CODE` AS equipment_code, sr.`NAME` AS install_regions, su.`NAME` AS person, suip.END_DATE AS end_date FROM sms_inspection_status sis")
                .append(" LEFT JOIN sms_user_inspection_plan suip ON suip.id = sis.USER_INSPECTION_PLAN_ID LEFT JOIN sms_equipment se ON sis.EQUIPMENT_ID = se.ID LEFT JOIN sms_regions sr ON sr.ID = se.INSTALL_REGIONS_ID LEFT JOIN sys_user su ON su.id = suip.PERSON_LIABLE_ID")
                .append(" WHERE sis.`STATUS` = " + PlanStatusEnums.WILL.getKey() + " AND suip.`STATUS` IN (" + PlanStatusEnums.WILL.getKey() + "," + PlanStatusEnums.DOING.getKey() + ") AND sis.DEL_FLAG = 0 AND suip.DEL_FLAG = 0 AND se.DEL_FLAG = 0")
                .append(" AND sr.DEL_FLAG = 0 AND suip.PERSON_LIABLE_ID = '" + currentUserId + "' AND '" + dateFormat + "' BETWEEN suip.BEGIN_DATE AND suip.END_DATE");
        Query query = entityManager.createNativeQuery(sb.toString(), WillInspectionDTO.class);
        return query.getResultList();
    }
}

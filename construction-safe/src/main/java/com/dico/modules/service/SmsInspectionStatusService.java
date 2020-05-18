package com.dico.modules.service;

import com.dico.enums.PlanStatusEnums;
import com.dico.jpa.dao.JpaDao;
import com.dico.jpa.service.JpaService;
import com.dico.modules.domain.SmsInspectionStatus;
import com.dico.modules.dto.SmsInspectionStatusDto;
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
    private SmsInspectionStatusRepository smsInspectionStatusRepository;

    @Autowired
    private EntityManager entityManager;

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

    public void deleteByIdIn(String[] ids) {
        smsInspectionStatusRepository.deleteByIdIn(ids);
    }

    public int findCount() {
        return smsInspectionStatusRepository.findCount(PlanStatusEnums.FINISH.getKey());
    }

    public int findCountByUpdateDateBetween(Date firstDate, Date lastDate) {
        return smsInspectionStatusRepository.findCountByUpdateDateBetween(PlanStatusEnums.FINISH.getKey(), firstDate, lastDate);
    }

    public List<SmsInspectionStatus> findByUserInspectionPlanId(String id) {
        return smsInspectionStatusRepository.findByUserInspectionPlanIdAndDelFlagIsFalse(id);
    }

    public List<SmsInspectionStatusDto> findByUserPlanId(String id) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT se.`CODE` AS equipment_code, se.`name` AS equipment_name, sec.CLASS_NAME AS equipment_class, sr.`NAME` AS install_area, sis.`STATUS` AS inspection_status FROM sms_inspection_status sis LEFT JOIN sms_equipment se ON sis.EQUIPMENT_ID = se.ID AND se.DEL_FLAG = 0 LEFT JOIN sms_equipment_class sec ON se.TYPE_ID = sec.id AND sec.DEL_FLAG = 0 LEFT JOIN sms_regions sr ON se.INSTALL_REGIONS_ID = sr.ID AND sr.DEL_FLAG = 0 WHERE sis.USER_INSPECTION_PLAN_ID = '").append(id).append("' AND sis.DEL_FLAG = 0");
        Query query = entityManager.createNativeQuery(sb.toString(), SmsInspectionStatusDto.class);
        return query.getResultList();
    }
}

package com.dico.modules.service;

import com.dico.jpa.dao.JpaDao;
import com.dico.jpa.service.JpaService;
import com.dico.modules.domain.SmsEquipmentFaultInfo;
import com.dico.modules.dto.SmsEquipmentFaultInfoDto;
import com.dico.modules.repo.SmsEquipmentFaultInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

@Service
public class SmsEquipmentFaultInfoService extends JpaService<SmsEquipmentFaultInfo, String> {

    @Autowired
    private SmsEquipmentFaultInfoRepository smsEquipmentFaultInfoRepository;

    @Autowired
    private EntityManager entityManager;

    @Override
    protected JpaDao<SmsEquipmentFaultInfo, String> getDao() {
        return smsEquipmentFaultInfoRepository;
    }

    /**
     * 根据ID查询未删除的数据
     */
    public SmsEquipmentFaultInfo getById(String smsEquipmentFaultInfoId) {
        return smsEquipmentFaultInfoRepository.getByIdAndDelFlagIsFalse(smsEquipmentFaultInfoId);
    }

    public void deleteByIdIn(String[] ids) {
        smsEquipmentFaultInfoRepository.deleteByIdIn(ids);
    }

    public List<SmsEquipmentFaultInfoDto> findByStatusAndAssignUserId(boolean status, String userId) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT sefi.id AS id, sefi.status AS status, se.`CODE` AS equipment_code,sefi.REVIEW_RESULT AS review_result,sefi.REMARK AS remark, se.`NAME` AS equipment_name, sr.`NAME` AS install_area, su.`NAME` AS report_user, sefi.CREATE_DATE AS report_date FROM sms_equipment_fault_assign sefa LEFT JOIN sms_equipment_fault_info sefi ON sefa.FAULT_INFO_ID = sefi.id AND sefi.DEL_FLAG = 0 LEFT JOIN sms_equipment se ON sefi.EQUIPMENT_ID = se.ID AND se.DEL_FLAG = 0 LEFT JOIN sms_regions sr ON se.INSTALL_REGIONS_ID = sr.ID AND sr.DEL_FLAG = 0 LEFT JOIN sys_user su ON su.id = sefi.CREATE_USER AND su.DEL_FLAG = 0 WHERE sefi.DEL_FLAG = 0 AND sefi.`STATUS` = ").append(status).append(" AND sefa.FAULT_USER_ID = '").append(userId).append("' ORDER BY sefi.CREATE_DATE DESC");
        Query query = entityManager.createNativeQuery(sb.toString(), SmsEquipmentFaultInfoDto.class);
        return query.getResultList();
    }

    public List<SmsEquipmentFaultInfoDto> findByStatus(boolean status) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT sefi.id AS id, sefi.status AS status, se.`CODE` AS equipment_code,sefi.REVIEW_RESULT AS review_result,sefi.REMARK AS remark, se.`NAME` AS equipment_name, sr.`NAME` AS install_area, su.`NAME` AS report_user, sefi.CREATE_DATE AS report_date FROM sms_equipment_fault_info sefi LEFT JOIN sms_equipment se ON sefi.EQUIPMENT_ID = se.ID AND se.DEL_FLAG = 0 LEFT JOIN sms_regions sr ON se.INSTALL_REGIONS_ID = sr.ID AND sr.DEL_FLAG = 0 LEFT JOIN sys_user su ON su.id = sefi.CREATE_USER AND su.DEL_FLAG = 0 WHERE sefi.DEL_FLAG = 0 AND sefi.`STATUS` = ").append(status).append(" ORDER BY sefi.CREATE_DATE DESC");
        Query query = entityManager.createNativeQuery(sb.toString(), SmsEquipmentFaultInfoDto.class);
        return query.getResultList();
    }

    public List<SmsEquipmentFaultInfoDto> findByCreateByUserId(String userId) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT sefi.id AS id, sefi.status AS status, se.`CODE` AS equipment_code,sefi.REVIEW_RESULT AS review_result,sefi.REMARK AS remark, se.`NAME` AS equipment_name, sr.`NAME` AS install_area, su.`NAME` AS report_user, sefi.CREATE_DATE AS report_date FROM sms_equipment_fault_info sefi LEFT JOIN sms_equipment se ON sefi.EQUIPMENT_ID = se.ID AND se.DEL_FLAG = 0 LEFT JOIN sms_regions sr ON se.INSTALL_REGIONS_ID = sr.ID AND sr.DEL_FLAG = 0 LEFT JOIN sys_user su ON su.id = sefi.CREATE_USER AND su.DEL_FLAG = 0 WHERE sefi.DEL_FLAG = 0 AND sefi.CREATE_USER = '").append(userId).append("' ORDER BY sefi.CREATE_DATE DESC");
        Query query = entityManager.createNativeQuery(sb.toString(), SmsEquipmentFaultInfoDto.class);
        return query.getResultList();
    }
}

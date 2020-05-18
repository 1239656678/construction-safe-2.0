package com.dico.modules.service;

import com.dico.jpa.dao.JpaDao;
import com.dico.jpa.service.JpaService;
import com.dico.modules.domain.SmsEquipmentFaultInfo;
import com.dico.modules.dto.SmsEquipmentFaultInfoDto;
import com.dico.modules.repo.SmsEquipmentFaultInfoRepository;
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

    public List<SmsEquipmentFaultInfoDto> findByStatusAndEquipmentId(boolean status, String equipmentId) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT sefi.id AS id, sefi.status AS status, se.`CODE` AS equipment_code, sefi.REMARK AS remark, se.`NAME` AS equipment_name, sr.`NAME` AS install_area, su.`NAME` AS report_user, sefi.CREATE_DATE AS report_date FROM sms_equipment_fault_assign sefa LEFT JOIN sms_equipment_fault_info sefi ON sefa.FAULT_INFO_ID = sefi.id AND sefi.DEL_FLAG = 0 LEFT JOIN sms_equipment se ON sefi.EQUIPMENT_ID = se.ID AND se.DEL_FLAG = 0 LEFT JOIN sms_regions sr ON se.INSTALL_REGIONS_ID = sr.ID AND sr.DEL_FLAG = 0 LEFT JOIN sys_user su ON su.id = sefi.CREATE_USER AND su.DEL_FLAG = 0 WHERE sefi.DEL_FLAG = 0 AND sefi.`STATUS` = ").append(status).append(" AND sefi.EQUIPMENT_ID = '").append(equipmentId).append("' ORDER BY sefi.CREATE_DATE DESC");
        Query query = entityManager.createNativeQuery(sb.toString(), SmsEquipmentFaultInfoDto.class);
        return query.getResultList();
    }

    public int findCount(Date preDate, Date nextDate) {
        return smsEquipmentFaultInfoRepository.findCount(preDate, nextDate);
    }

    public int findFinishedCount(Date preDate, Date nextDate){
        return smsEquipmentFaultInfoRepository.findFinishedCount(preDate, nextDate);
    }

    public List<Map<String, Object>> findWordInfoWillDoing(Date firstDate, Date lastDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT se.`CODE` AS equipmentCode, se.`NAME` AS equipmentName, sr.`NAME` AS installArea, CASE WHEN sefi.IS_ASSIGN THEN o.`NAME` ELSE '维修小组' END AS organization, CASE WHEN su.`NAME` IS NULL THEN '' ELSE su.`NAME` END AS faultUser FROM sms_equipment_fault_info sefi LEFT JOIN sms_equipment se ON se.ID = sefi.EQUIPMENT_ID AND se.DEL_FLAG IS FALSE LEFT JOIN sms_regions sr ON sr.id = se.INSTALL_REGIONS_ID AND sr.DEL_FLAG IS FALSE LEFT JOIN sms_equipment_fault_assign sefa ON sefa.FAULT_INFO_ID = sefi.id AND sefa.DEL_FLAG IS FALSE LEFT JOIN sys_user su ON su.id = sefa.FAULT_USER_ID AND su.DEL_FLAG IS FALSE LEFT JOIN organization o ON o.id = su.ORGANIZATION_ID AND o.DEL_FLAG IS FALSE WHERE sefi.`STATUS` = 0")
                .append(" AND sefi.REPORT_DATE BETWEEN '").append(sdf.format(firstDate)).append("' AND '").append(sdf.format(lastDate)).append("'");
        Query query = entityManager.createNativeQuery(sb.toString());
        query.unwrap(NativeQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return query.getResultList();
    }
}

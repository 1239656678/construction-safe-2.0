package com.dico.modules.service;

import com.dico.jpa.dao.JpaDao;
import com.dico.jpa.service.JpaService;
import com.dico.modules.domain.SmsEquipmentClassTarget;
import com.dico.modules.domain.SmsInspectionTarget;
import com.dico.modules.dto.EquipmentClassDTO;
import com.dico.modules.dto.InspectionTargetDTO;
import com.dico.modules.repo.SmsEquipmentClassTargetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

@Service
public class SmsEquipmentClassTargetService extends JpaService<SmsEquipmentClassTarget, String> {

    @Autowired
    private SmsEquipmentClassTargetRepository smsEquipmentClassTargetRepository;

    @Autowired
    private EntityManager entityManager;

    @Override
    protected JpaDao<SmsEquipmentClassTarget, String> getDao() {
        return smsEquipmentClassTargetRepository;
    }

    /**
     * 根据ID查询未删除的数据
     */
    public SmsEquipmentClassTarget getByIdAndDelFlagIsFalse(String smsEquipmentClassTargetId) {
        return smsEquipmentClassTargetRepository.getByIdAndDelFlagIsFalse(smsEquipmentClassTargetId);
    }

    /**
     * 根据设备分类ID查询巡检项信息
     *
     * @param equipmentClassId
     * @return
     */
    public List<InspectionTargetDTO> findInspectionTarget(String equipmentClassId) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT sit.id AS id, sit.TARGET_NAME AS NAME, TRUE AS normal ").
                append("FROM sms_inspection_target sit LEFT JOIN sms_equipment_class_target sect ON sit.id = sect.TARGET_ID WHERE sect.EQUIPMENT_CLASS_ID = '").
                append(equipmentClassId).append("'");
        Query query = entityManager.createNativeQuery(sb.toString(), InspectionTargetDTO.class);
        return query.getResultList();
    }
}

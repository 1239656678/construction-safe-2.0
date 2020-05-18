package com.dico.modules.service;

import com.dico.jpa.dao.JpaDao;
import com.dico.jpa.service.JpaService;
import com.dico.modules.domain.SmsEquipment;
import com.dico.modules.domain.SmsEquipmentClass;
import com.dico.modules.dto.ClassStatistice;
import com.dico.modules.dto.SmsEquipmentDto;
import com.dico.modules.repo.SmsEquipmentRepository;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.Date;
import java.util.List;

@Service
public class SmsEquipmentService extends JpaService<SmsEquipment, String> {

    @Autowired
    private SmsEquipmentRepository smsEquipmentRepository;

    @Autowired
    private EntityManager entityManager;

    @Override
    protected JpaDao<SmsEquipment, String> getDao() {
        return smsEquipmentRepository;
    }

    /**
     * 根据ID查询未删除的数据
     */
    public SmsEquipment getSmsEquipmentByIdAndDelFlagIsFalse(String smsEquipmentId) {
        return smsEquipmentRepository.getSmsEquipmentByIdAndDelFlagIsFalse(smsEquipmentId);
    }

    public void deleteByIdIn(String[] ids) {
        smsEquipmentRepository.deleteByIdIn(ids);
    }

    /**
     * 根据多个ID查询设备
     *
     * @param ids
     * @return
     */
    public List<SmsEquipment> findByIds(String[] ids) {
        return smsEquipmentRepository.findByIdInAndDelFlagIsFalse(ids);
    }

    /**
     * 根据多个区域ID查询设备
     *
     * @param ids
     * @return
     */
    public List<SmsEquipment> findByInstallRegionsIdIn(String[] ids) {
        return smsEquipmentRepository.findByInstallRegionsIdInAndDelFlagIsFalse(ids);
    }

    /**
     * 通过设备分类查询设备
     *
     * @param equipmentClassId
     * @return
     */
    public List<SmsEquipment> findByClassId(String equipmentClassId) {
        return smsEquipmentRepository.findByTypeIdAndDelFlagIsFalse(equipmentClassId);
    }

    public List<SmsEquipment> findByInstallRegionsIdIsNull(String code, String name) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT * FROM sms_equipment se where se.DEL_FLAG = 0 AND se.INSTALL_REGIONS_ID IS NULL");
        String param = "";
        if (StringUtils.isNotBlank(code)) {
            param = " AND se.`CODE` LIKE '%" + code + "%'";
        }
        if (StringUtils.isNotBlank(name)) {
            param = " AND se.`NAME` LIKE '%" + name + "%'";
        }
        sb.append(param);
        Query query = entityManager.createNativeQuery(sb.toString(), SmsEquipment.class);
        return query.getResultList();
    }

    public List<SmsEquipment> findByInstallRegionsId(String itemId) {
        return smsEquipmentRepository.findByInstallRegionsIdAndDelFlagIsFalse(itemId);
    }

    public int findCount() {
        return smsEquipmentRepository.findCount();
    }

    public int findCountByCreateDateBetween(Date firstDate, Date lastDate) {
        return smsEquipmentRepository.findCountByCreateDateBetween(firstDate, lastDate);
    }

    public List<ClassStatistice> findClassStatistice() {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT sec.CLASS_NAME AS 'class_name', count(se.id) AS 'num' FROM sms_equipment se LEFT JOIN sms_equipment_class sec ON se.TYPE_ID = sec.id WHERE se.DEL_FLAG = 0 AND sec.PARENT_CLASS IS NULL OR sec.PARENT_CLASS IN ( SELECT s.id FROM sms_equipment_class s WHERE s.PARENT_CLASS IS NULL ) GROUP BY se.TYPE_ID");
        Query query = entityManager.createNativeQuery(sb.toString(), ClassStatistice.class);
        return query.getResultList();
    }

    public List<SmsEquipment> findByClassIdAndRegionsId(String typeId, String regionsId) {
        return smsEquipmentRepository.findByTypeIdAndInstallRegionsIdAndDelFlagIsFalse(typeId, regionsId);
    }

    public List<SmsEquipment> findByTypeIdIn(String[] ids) {
        return smsEquipmentRepository.findByTypeIdInAndDelFlagIsFalse(ids);
    }

    public SmsEquipmentDto getSmsEquipmentDtoById(String equipmentId) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT se.id AS id, se.`CODE` AS equipment_code, se.`NAME` AS equipment_name, sr.`NAME` AS install_area, sec.CLASS_NAME AS equipment_class, se.PRODUCE_DATE AS produce_date, se.SCRAPPED_DATE AS scrapped_date, se.MODEL AS model, o.`NAME` AS organization_liable, su.`NAME` AS person_liable, se.REMARK AS remark FROM sms_equipment se LEFT JOIN organization o ON se.RESPONSIBLE_ORGANIZATION_ID = o.id AND o.DEL_FLAG IS FALSE LEFT JOIN sys_user su ON se.RESPONSIBLE_USER_ID = su.id AND su.DEL_FLAG IS FALSE LEFT JOIN sms_regions sr ON se.INSTALL_REGIONS_ID = sr.ID AND sr.DEL_FLAG IS FALSE LEFT JOIN sms_equipment_class sec ON se.TYPE_ID = sec.id AND sec.DEL_FLAG IS FALSE WHERE se.id = '").append(equipmentId).append("' AND se.DEL_FLAG IS FALSE");
        Query query = entityManager.createNativeQuery(sb.toString(), SmsEquipmentDto.class);
        List<SmsEquipmentDto> smsEquipmentDtoList = query.getResultList();
        return null != smsEquipmentDtoList && smsEquipmentDtoList.size() > 0 ? smsEquipmentDtoList.get(0) : null;
    }
}

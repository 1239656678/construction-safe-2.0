package com.dico.modules.service;

import com.dico.jpa.dao.JpaDao;
import com.dico.jpa.service.JpaService;
import com.dico.modules.domain.SmsEquipment;
import com.dico.modules.repo.SmsEquipmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SmsEquipmentService extends JpaService<SmsEquipment, String> {

    @Autowired
    private SmsEquipmentRepository smsEquipmentRepository;

    @Override
    protected JpaDao<SmsEquipment, String> getDao() {
        return smsEquipmentRepository;
    }

    /**
     * 根据ID查询未删除的数据
     */
    public SmsEquipment getByIdAndDelFlagIsFalse(String smsEquipmentId) {
        return smsEquipmentRepository.getByIdAndDelFlagIsFalse(smsEquipmentId);
    }

    public List<SmsEquipment> findByTypeIdIn(String[] ids) {
        return smsEquipmentRepository.findByTypeIdInAndDelFlagIsFalse(ids);
    }

    /**
     * 根据ID查询未删除的数据
     */
    public SmsEquipment getSmsEquipmentByIdAndDelFlagIsFalse(String smsEquipmentId) {
        return smsEquipmentRepository.getSmsEquipmentByIdAndDelFlagIsFalse(smsEquipmentId);
    }

}

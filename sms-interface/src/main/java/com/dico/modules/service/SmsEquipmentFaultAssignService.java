package com.dico.modules.service;

import com.dico.jpa.dao.JpaDao;
import com.dico.jpa.service.JpaService;
import com.dico.modules.domain.SmsEquipmentFaultAssign;
import com.dico.modules.repo.SmsEquipmentFaultAssignRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SmsEquipmentFaultAssignService extends JpaService<SmsEquipmentFaultAssign, String> {

    @Autowired
    private SmsEquipmentFaultAssignRepository smsEquipmentFaultAssignRepository;

    @Override
    protected JpaDao<SmsEquipmentFaultAssign, String> getDao() {
        return smsEquipmentFaultAssignRepository;
    }

    /**
     * 根据ID查询未删除的数据
     */
    public SmsEquipmentFaultAssign getById(String smsEquipmentFaultAssignId) {
        return smsEquipmentFaultAssignRepository.getByIdAndDelFlagIsFalse(smsEquipmentFaultAssignId);
    }

    /**
     * 根据计划详情ID查询未删除的数据
     */
    public SmsEquipmentFaultAssign getSmsEquipmentFaultAssignByFaultInfoIdAndDelFlagIsFalse(String smsEquipmentFaultAssignId) {
        return smsEquipmentFaultAssignRepository.getSmsEquipmentFaultAssignByFaultInfoIdAndDelFlagIsFalse(smsEquipmentFaultAssignId);
    }

    public void deleteByIdIn(String[] ids) {
        smsEquipmentFaultAssignRepository.deleteByIdIn(ids);
    }
}

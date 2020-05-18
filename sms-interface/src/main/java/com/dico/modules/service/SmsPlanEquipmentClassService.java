package com.dico.modules.service;

import com.dico.jpa.dao.JpaDao;
import com.dico.jpa.service.JpaService;
import com.dico.modules.domain.SmsPlanEquipmentClass;
import com.dico.modules.repo.SmsPlanEquipmentClassRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SmsPlanEquipmentClassService extends JpaService<SmsPlanEquipmentClass, String> {

    @Autowired
    private SmsPlanEquipmentClassRepository smsPlanEquipmentClassRepository;

    @Autowired
    private SmsEquipmentService smsEquipmentService;

    @Override
    protected JpaDao<SmsPlanEquipmentClass, String> getDao() {
        return smsPlanEquipmentClassRepository;
    }

    /**
     * 根据ID查询未删除的数据
     */
    public SmsPlanEquipmentClass getByIdAndDelFlagIsFalse(String SmsPlanEquipmentId) {
        return smsPlanEquipmentClassRepository.getByIdAndDelFlagIsFalse(SmsPlanEquipmentId);
    }

    public List<SmsPlanEquipmentClass> findByPlanId(String planId) {
        return smsPlanEquipmentClassRepository.findByPlanIdAndDelFlagIsFalse(planId);
    }
}

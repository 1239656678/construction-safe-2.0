package com.dico.modules.service;

import com.dico.jpa.dao.JpaDao;
import com.dico.jpa.service.JpaService;
import com.dico.modules.domain.SmsMaintenancePlan;
import com.dico.modules.repo.SmsMaintenancePlanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.persistence.EntityManager;

@Service
public class SmsMaintenancePlanService extends JpaService<SmsMaintenancePlan,String>{

    @Autowired
    private SmsMaintenancePlanRepository smsMaintenancePlanRepository;

    @Autowired
    private EntityManager entityManager;

    @Override
    protected JpaDao<SmsMaintenancePlan,String>getDao(){
        return smsMaintenancePlanRepository;
    }

    /**
    * 根据ID查询未删除的数据
    */
    public SmsMaintenancePlan getById(String smsMaintenancePlanId){
        return smsMaintenancePlanRepository.getByIdAndDelFlagIsFalse(smsMaintenancePlanId);
    }

    public void deleteByIdIn(String[]ids){
        smsMaintenancePlanRepository.deleteByIdIn(ids);
    }
}

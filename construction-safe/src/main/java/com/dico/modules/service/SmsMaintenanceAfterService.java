package com.dico.modules.service;

import com.dico.jpa.dao.JpaDao;
import com.dico.jpa.service.JpaService;
import com.dico.modules.domain.SmsMaintenanceAfter;
import com.dico.modules.repo.SmsMaintenanceAfterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SmsMaintenanceAfterService extends JpaService<SmsMaintenanceAfter,String>{

    @Autowired
    private SmsMaintenanceAfterRepository smsMaintenanceAfterRepository;

    @Override
    protected JpaDao<SmsMaintenanceAfter,String>getDao(){
        return smsMaintenanceAfterRepository;
    }

    /**
    * 根据ID查询未删除的数据
    */
    public SmsMaintenanceAfter getById(String smsMaintenanceAfterId){
        return smsMaintenanceAfterRepository.getByIdAndDelFlagIsFalse(smsMaintenanceAfterId);
    }

    public void deleteByIdIn(String[]ids){
        smsMaintenanceAfterRepository.deleteByIdIn(ids);
    }
}

package com.dico.modules.service;

import com.dico.jpa.dao.JpaDao;
import com.dico.jpa.service.JpaService;
import com.dico.modules.domain.SmsMaintenanceBefore;
import com.dico.modules.repo.SmsMaintenanceBeforeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SmsMaintenanceBeforeService extends JpaService<SmsMaintenanceBefore,String>{

    @Autowired
    private SmsMaintenanceBeforeRepository smsMaintenanceBeforeRepository;

    @Override
    protected JpaDao<SmsMaintenanceBefore,String>getDao(){
        return smsMaintenanceBeforeRepository;
    }

    /**
    * 根据ID查询未删除的数据
    */
    public SmsMaintenanceBefore getById(String smsMaintenanceBeforeId){
        return smsMaintenanceBeforeRepository.getByIdAndDelFlagIsFalse(smsMaintenanceBeforeId);
    }

    public void deleteByIdIn(String[]ids){
        smsMaintenanceBeforeRepository.deleteByIdIn(ids);
    }
}

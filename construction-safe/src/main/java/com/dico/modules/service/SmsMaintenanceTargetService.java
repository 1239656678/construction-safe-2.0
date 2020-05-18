package com.dico.modules.service;

import com.dico.jpa.dao.JpaDao;
import com.dico.jpa.service.JpaService;
import com.dico.modules.domain.SmsMaintenanceTarget;
import com.dico.modules.repo.SmsMaintenanceTargetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SmsMaintenanceTargetService extends JpaService<SmsMaintenanceTarget,String>{

    @Autowired
    private SmsMaintenanceTargetRepository smsMaintenanceTargetRepository;

    @Override
    protected JpaDao<SmsMaintenanceTarget,String>getDao(){
        return smsMaintenanceTargetRepository;
    }

    /**
    * 根据ID查询未删除的数据
    */
    public SmsMaintenanceTarget getById(String smsMaintenanceTargetId){
        return smsMaintenanceTargetRepository.getByIdAndDelFlagIsFalse(smsMaintenanceTargetId);
    }

    public void deleteByIdIn(String[]ids){
        smsMaintenanceTargetRepository.deleteByIdIn(ids);
    }
}

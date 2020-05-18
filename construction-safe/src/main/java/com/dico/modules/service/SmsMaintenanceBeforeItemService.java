package com.dico.modules.service;

import com.dico.jpa.dao.JpaDao;
import com.dico.jpa.service.JpaService;
import com.dico.modules.domain.SmsMaintenanceBeforeItem;
import com.dico.modules.repo.SmsMaintenanceBeforeItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SmsMaintenanceBeforeItemService extends JpaService<SmsMaintenanceBeforeItem,String>{

    @Autowired
    private SmsMaintenanceBeforeItemRepository smsMaintenanceBeforeItemRepository;

    @Override
    protected JpaDao<SmsMaintenanceBeforeItem,String>getDao(){
        return smsMaintenanceBeforeItemRepository;
    }

    /**
    * 根据ID查询未删除的数据
    */
    public SmsMaintenanceBeforeItem getById(String smsMaintenanceBeforeItemId){
        return smsMaintenanceBeforeItemRepository.getByIdAndDelFlagIsFalse(smsMaintenanceBeforeItemId);
    }

    public void deleteByIdIn(String[]ids){
        smsMaintenanceBeforeItemRepository.deleteByIdIn(ids);
    }
}

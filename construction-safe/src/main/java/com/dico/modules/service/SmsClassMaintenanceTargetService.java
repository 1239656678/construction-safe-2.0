package com.dico.modules.service;

import com.dico.jpa.dao.JpaDao;
import com.dico.jpa.service.JpaService;
import com.dico.modules.domain.SmsClassMaintenanceTarget;
import com.dico.modules.repo.SmsClassMaintenanceTargetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SmsClassMaintenanceTargetService extends JpaService<SmsClassMaintenanceTarget,String>{

    @Autowired
    private SmsClassMaintenanceTargetRepository smsClassMaintenanceTargetRepository;

    @Override
    protected JpaDao<SmsClassMaintenanceTarget,String>getDao(){
        return smsClassMaintenanceTargetRepository;
    }

    /**
    * 根据ID查询未删除的数据
    */
    public SmsClassMaintenanceTarget getById(String smsClassMaintenanceTargetId){
        return smsClassMaintenanceTargetRepository.getByIdAndDelFlagIsFalse(smsClassMaintenanceTargetId);
    }
    /**
     * 根据设备分类ID查询未删除的数据
     */
    public List<SmsClassMaintenanceTarget> findSmsClassMaintenanceTargetByEquipmentClassId(String equipmentClassId){
        return smsClassMaintenanceTargetRepository.findSmsClassMaintenanceTargetByEquipmentClassId(equipmentClassId);
    }
    /**
     * 根据设备分类ID查询存在数据
     */
    public SmsClassMaintenanceTarget findByEquipmentClassIdEqualsAndTargetIdEqualsAndDelFlagIsFalse(String equipmentClassId, String targetId) {
        return smsClassMaintenanceTargetRepository.findByEquipmentClassIdEqualsAndTargetIdEqualsAndDelFlagIsFalse(equipmentClassId, targetId);
    }
    public void deleteByIdIn(String[]ids){
        smsClassMaintenanceTargetRepository.deleteByIdIn(ids);
    }
    public void deleteById(String id){
        smsClassMaintenanceTargetRepository.deleteById(id);
    }
}

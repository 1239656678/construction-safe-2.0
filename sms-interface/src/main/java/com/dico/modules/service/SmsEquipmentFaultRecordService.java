package com.dico.modules.service;

import com.dico.jpa.dao.JpaDao;
import com.dico.jpa.service.JpaService;
import com.dico.modules.domain.SmsEquipmentFaultRecord;
import com.dico.modules.repo.SmsEquipmentFaultRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SmsEquipmentFaultRecordService extends JpaService<SmsEquipmentFaultRecord,String>{

    @Autowired
    private SmsEquipmentFaultRecordRepository smsEquipmentFaultRecordRepository;

    @Override
    protected JpaDao<SmsEquipmentFaultRecord,String>getDao(){
        return smsEquipmentFaultRecordRepository;
    }

    /**
    * 根据ID查询未删除的数据
    */
    public SmsEquipmentFaultRecord getById(String smsEquipmentFaultRecordId){
        return smsEquipmentFaultRecordRepository.getByIdAndDelFlagIsFalse(smsEquipmentFaultRecordId);
    }
    /**
     * 根据ReviewUserID查询未删除的数据
     */
    public List<SmsEquipmentFaultRecord> getByReviewUserId(String reviewUserID){
        return smsEquipmentFaultRecordRepository.getByReviewUserIdAndDelFlagIsFalse(reviewUserID);
    }

    public void deleteByIdIn(String[]ids){
        smsEquipmentFaultRecordRepository.deleteByIdIn(ids);
    }

    public SmsEquipmentFaultRecord findByFaultInfoId(String faultInfoId) {
        return smsEquipmentFaultRecordRepository.findByFaultInfoIdAndDelFlagIsFalse(faultInfoId);
    }
}

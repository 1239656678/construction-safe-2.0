package com.dico.modules.service;

import com.dico.common.enums.MaintenanceStatusEnums;
import com.dico.jpa.dao.JpaDao;
import com.dico.jpa.service.JpaService;
import com.dico.modules.domain.SmsUserMaintenancePlanReview;
import com.dico.modules.repo.SmsUserMaintenancePlanReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SmsUserMaintenancePlanReviewService extends JpaService<SmsUserMaintenancePlanReview,String>{

    @Autowired
    private SmsUserMaintenancePlanReviewRepository smsUserMaintenancePlanReviewRepository;

    @Override
    protected JpaDao<SmsUserMaintenancePlanReview,String>getDao(){
        return smsUserMaintenancePlanReviewRepository;
    }

    /**
    * 根据ID查询未删除的数据
    */
    public SmsUserMaintenancePlanReview getById(String smsUserMaintenancePlanReviewId){
        return smsUserMaintenancePlanReviewRepository.getByIdAndDelFlagIsFalse(smsUserMaintenancePlanReviewId);
    }

    public void deleteByIdIn(String[]ids){
        smsUserMaintenancePlanReviewRepository.deleteByIdIn(ids);
    }

    public void deleteByMainId(String id){
        smsUserMaintenancePlanReviewRepository.deleteByMaintainId(id);
    }

    public  List<SmsUserMaintenancePlanReview> getByMaintainUserIdList( String maintainUserId){
        return smsUserMaintenancePlanReviewRepository.getByMaintainUserIdList(maintainUserId);
    }
}

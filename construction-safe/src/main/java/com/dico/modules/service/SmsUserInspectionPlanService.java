package com.dico.modules.service;

import com.dico.jpa.dao.JpaDao;
import com.dico.jpa.service.JpaService;
import com.dico.modules.domain.SmsUserInspectionPlan;
import com.dico.modules.repo.SmsUserInspectionPlanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

@Service
public class SmsUserInspectionPlanService extends JpaService<SmsUserInspectionPlan, String> {

    @Autowired
    private SmsUserInspectionPlanRepository smsUserInspectionPlanRepository;

    @Autowired
    private EntityManager entityManager;

    @Override
    protected JpaDao<SmsUserInspectionPlan, String> getDao() {
        return smsUserInspectionPlanRepository;
    }

    /**
     * 根据ID查询未删除的数据
     */
    public SmsUserInspectionPlan getSmsUserInspectionPlanByIdAndDelFlagIsFalse(String smsUserInspectionPlanId) {
        return smsUserInspectionPlanRepository.getSmsUserInspectionPlanByIdAndDelFlagIsFalse(smsUserInspectionPlanId);
    }

    public void deleteByIdIn(String[] ids) {
        smsUserInspectionPlanRepository.deleteByIdIn(ids);
    }

    public void deleteByInspectionId(String id) {
        smsUserInspectionPlanRepository.deleteByInspectionId(id);
    }

    public SmsUserInspectionPlan findByCycleAndPlanId(String cycle, String id) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT * FROM sms_user_inspection_plan suip WHERE suip.cycle = '" + cycle + "' AND suip.inspection_id = '" + id + "' AND suip.del_flag = 0");
        Query query = entityManager.createNativeQuery(sb.toString(), SmsUserInspectionPlan.class);
        List<SmsUserInspectionPlan> smsUserInspectionPlanList = query.getResultList();
        return null == smsUserInspectionPlanList || smsUserInspectionPlanList.size() == 0 ? null : smsUserInspectionPlanList.get(0);
    }

    public SmsUserInspectionPlan findByCycleIsNullAndPlanId(String id) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT * FROM sms_user_inspection_plan suip WHERE suip.cycle IS NULL AND suip.inspection_id = '" + id + "' AND suip.del_flag = 0");
        Query query = entityManager.createNativeQuery(sb.toString(), SmsUserInspectionPlan.class);
        List<SmsUserInspectionPlan> smsUserInspectionPlanList = query.getResultList();
        return null == smsUserInspectionPlanList || smsUserInspectionPlanList.size() == 0 ? null : smsUserInspectionPlanList.get(0);
    }

    public List<SmsUserInspectionPlan> findByInspectionId(String id) {
        return smsUserInspectionPlanRepository.findByInspectionIdAndDelFlagIsFalse(id);
    }

}

package com.dico.modules.service;

import com.dico.jpa.dao.JpaDao;
import com.dico.jpa.service.JpaService;
import com.dico.modules.domain.SmsInspectionPlan;
import com.dico.modules.repo.SmsInspectionPlanRepository;
import org.apache.commons.lang.StringUtils;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class SmsInspectionPlanService extends JpaService<SmsInspectionPlan, String> {

    @Autowired
    private SmsInspectionPlanRepository smsInspectionPlanRepository;

    @Override
    protected JpaDao<SmsInspectionPlan, String> getDao() {
        return smsInspectionPlanRepository;
    }

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * 根据ID查询未删除的数据
     *
     * @param smsInspectionPlanId
     * @return
     */
    public SmsInspectionPlan getByIdAndDelFlagIsFalse(String smsInspectionPlanId) {
        return smsInspectionPlanRepository.getByIdAndDelFlagIsFalse(smsInspectionPlanId);
    }
}

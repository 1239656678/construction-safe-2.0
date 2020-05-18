package com.dico.modules.service;

import com.dico.jpa.dao.JpaDao;
import com.dico.jpa.service.JpaService;
import com.dico.modules.domain.SmsInspectionTarget;
import com.dico.modules.repo.SmsInspectionTargetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Stephen
 */
@Service
public class SmsInspectionTargetService extends JpaService<SmsInspectionTarget, String> {

    @Autowired
    private SmsInspectionTargetRepository smsInspectionTargetRepository;

    @Override
    protected JpaDao<SmsInspectionTarget, String> getDao() {
        return smsInspectionTargetRepository;
    }

    /**
     * 根据ID查询未删除的数据
     */
    public SmsInspectionTarget getByIdAndDelFlagIsFalse(String smsInspectionTargetId) {
        return smsInspectionTargetRepository.getByIdAndDelFlagIsFalse(smsInspectionTargetId);
    }
}

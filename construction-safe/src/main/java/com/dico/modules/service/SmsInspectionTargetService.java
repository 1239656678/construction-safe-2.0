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
    public SmsInspectionTarget getSmsInspectionTargetByIdAndDelFlagIsFalse(String smsInspectionTargetId) {
        return smsInspectionTargetRepository.getSmsInspectionTargetByIdAndDelFlagIsFalse(smsInspectionTargetId);
    }

    public void deleteByIdIn(String[] ids) {
        smsInspectionTargetRepository.deleteByIdIn(ids);
    }

    /**
     * 根据设备分类获取巡检项
     *
     * @author Gaodl
     * 方法名称: findByEquipmentClassId
     * 参数： [equipmentClassId]
     * 返回值： java.util.List<com.dico.modules.domain.SmsInspectionTarget>
     * 创建时间: 2019/5/22
     */
    public List<SmsInspectionTarget> findByEquipmentClassId(String equipmentClassId) {
        return smsInspectionTargetRepository.findByEquipmentClassId(equipmentClassId);
    }
}

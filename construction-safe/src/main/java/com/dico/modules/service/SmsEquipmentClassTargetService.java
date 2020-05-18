package com.dico.modules.service;

import com.dico.jpa.dao.JpaDao;
import com.dico.jpa.service.JpaService;
import com.dico.modules.domain.SmsEquipmentClassTarget;
import com.dico.modules.domain.SmsInspectionTarget;
import com.dico.modules.repo.SmsEquipmentClassTargetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SmsEquipmentClassTargetService extends JpaService<SmsEquipmentClassTarget, String> {

    @Autowired
    private SmsEquipmentClassTargetRepository smsEquipmentClassTargetRepository;

    @Override
    protected JpaDao<SmsEquipmentClassTarget, String> getDao() {
        return smsEquipmentClassTargetRepository;
    }

    /**
     * 根据ID查询未删除的数据
     */
    public SmsEquipmentClassTarget getSmsEquipmentClassTargetByIdAndDelFlagIsFalse(String smsEquipmentClassTargetId) {
        return smsEquipmentClassTargetRepository.getSmsEquipmentClassTargetByIdAndDelFlagIsFalse(smsEquipmentClassTargetId);
    }

    public void deleteByIdIn(String[] ids) {
        smsEquipmentClassTargetRepository.deleteByIdIn(ids);
    }

    /**
     * 获取设备绑定的巡检项
     *
     * @author Gaodl
     * 方法名称: findBindTargets
     * 参数： [equpmentIds]
     * 返回值： java.util.List<com.dico.modules.domain.SmsInspectionTarget>
     * 创建时间: 2019/4/19
     */
    public List<SmsInspectionTarget> findBindTargets(String equpmentClassIds) {
        return smsEquipmentClassTargetRepository.findBindTargets(equpmentClassIds);
    }

    /**
     * 根据设备ID和巡检项ID查询设备巡检项绑定信息
     *
     * @author Gaodl
     * 方法名称: findByEquipmentIdEqualsAndTargetIdEqualsAndDelFlagIsFalse
     * 参数： [equipmentId, targetId]
     * 返回值： com.dico.modules.domain.SmsEquipmentTarget
     * 创建时间: 2019/4/19
     */
    public SmsEquipmentClassTarget findByEquipmentClassIdEqualsAndTargetIdEqualsAndDelFlagIsFalse(String equipmentClassId, String targetId) {
        return smsEquipmentClassTargetRepository.findByEquipmentClassIdEqualsAndTargetIdEqualsAndDelFlagIsFalse(equipmentClassId, targetId);
    }
}

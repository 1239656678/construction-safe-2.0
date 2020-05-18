package com.dico.modules.service;

import com.dico.jpa.dao.JpaDao;
import com.dico.jpa.service.JpaService;
import com.dico.modules.domain.SmsPlanEquipmentClass;
import com.dico.modules.repo.SmsPlanEquipmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SmsPlanEquipmentService extends JpaService<SmsPlanEquipmentClass, String> {

    @Autowired
    private SmsPlanEquipmentRepository smsPlanEquipmentRepository;

    @Autowired
    private SmsEquipmentService smsEquipmentService;

    @Override
    protected JpaDao<SmsPlanEquipmentClass, String> getDao() {
        return smsPlanEquipmentRepository;
    }

    /**
     * 根据ID查询未删除的数据
     */
    public SmsPlanEquipmentClass getById(String SmsPlanEquipmentId) {
        return smsPlanEquipmentRepository.getByIdAndDelFlagIsFalse(SmsPlanEquipmentId);
    }

    public void deleteByIdIn(String[] ids) {
        smsPlanEquipmentRepository.deleteByIdIn(ids);
    }

    /**
     * 根据计划ID和设备类型ID查询设备类型绑定信息
     *
     * @author Gaodl
     * 方法名称: findByPlanIdEqualsAndEquipmentClassIdEqualsAndDelFlagIsFalse
     * 参数： [planId, equipmentClassId]
     * 返回值： com.dico.modules.domain.SmsPlanEquipment
     * 创建时间: 2019/4/19
     */
    public SmsPlanEquipmentClass findByPlanIdAndEquipmentClassId(String planId, String equipmentClassId) {
        return smsPlanEquipmentRepository.findByPlanIdEqualsAndEquipmentClassIdEqualsAndDelFlagIsFalse(planId, equipmentClassId);
    }

    /**
     * 根据计划ID查询计划绑定的设备
     *
     * @author Gaodl
     * 方法名称: findBindEquipmentClass
     * 参数： [planId]
     * 返回值： java.util.List<com.dico.feign.domain.SmsEquipmentClass>
     * 创建时间: 2019/4/19
     */
    public List<SmsPlanEquipmentClass> findByPlanId(String planId) {
        return smsPlanEquipmentRepository.findByPlanIdAndDelFlagIsFalse(planId);
    }

    /**
     * 根据设备ID获取计划ID
     *
     * @author Gaodl
     * 方法名称: findPlanIdByEquipmentId
     * 参数： [equipmentId]
     * 返回值： java.lang.String
     * 创建时间: 2019/5/24
     */
    public String findPlanIdByEquipmentId(String equipmentId) {
        SmsPlanEquipmentClass smsPlanEquipment = smsPlanEquipmentRepository.findPlanIdByEquipmentClassIdAndDelFlagIsFalse(equipmentId);
        if (null == smsPlanEquipment) {
            return null;
        }
        return smsPlanEquipment.getPlanId();
    }
}

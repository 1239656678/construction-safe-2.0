package com.dico.modules.service;

import com.dico.jpa.dao.JpaDao;
import com.dico.jpa.service.JpaService;
import com.dico.modules.domain.SmsEquipmentClass;
import com.dico.modules.repo.SmsEquipmentClassRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SmsEquipmentClassService extends JpaService<SmsEquipmentClass, String> {

    @Autowired
    private SmsEquipmentClassRepository smsEquipmentClassRepository;

    @Override
    protected JpaDao<SmsEquipmentClass, String> getDao() {
        return smsEquipmentClassRepository;
    }

    /**
     * 根据ID查询未删除的数据
     */
    public SmsEquipmentClass getByIdAndDelFlagIsFalse(String smsEquipmentClassId) {
        return smsEquipmentClassRepository.getByIdAndDelFlagIsFalse(smsEquipmentClassId);
    }

    public List<SmsEquipmentClass> findByIdIn(String[] ids) {
        return smsEquipmentClassRepository.findByIdInAndDelFlagIsFalse(ids);
    }

    /**
     * 根据ID查询未删除的数据
     */
    public List<SmsEquipmentClass> getSmsEquipmentClassByClassNameLike(String className) {
        return smsEquipmentClassRepository.getSmsEquipmentClassesByClassNameLike(className);
    }
    /**
     * 查询根节点
     *
     * @return
     */
    public List<SmsEquipmentClass> findParentList() {
        return smsEquipmentClassRepository.findByParentClassIsNullAndDelFlagIsFalse();
    }

    public List<SmsEquipmentClass> findChildrens(String classId) {
        SmsEquipmentClass smsEquipmentClass = smsEquipmentClassRepository.getSmsEquipmentClassByIdAndDelFlagIsFalse(classId);
        if(null == smsEquipmentClass){
            return null;
        }
        smsEquipmentClass.setChildren(this.findChildrens(smsEquipmentClassRepository.findByParentClassAndDelFlagIsFalse(smsEquipmentClass.getId())));
        List<SmsEquipmentClass> smsEquipmentClassList = new ArrayList<>();
        smsEquipmentClassList.add(smsEquipmentClass);
        return smsEquipmentClassList;
    }

}

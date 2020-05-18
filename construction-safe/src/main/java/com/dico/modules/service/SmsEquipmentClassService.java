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
    public SmsEquipmentClass getSmsEquipmentClassByIdAndDelFlagIsFalse(String smsEquipmentClassId) {
        return smsEquipmentClassRepository.getSmsEquipmentClassByIdAndDelFlagIsFalse(smsEquipmentClassId);
    }

    public void deleteByIdIn(String[] ids) {
        smsEquipmentClassRepository.deleteByIdIn(ids);
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

    /**
     * 根据设备类型名称和父类型查询设备类型
     *
     * @author Gaodl
     * 方法名称: findByClassNameAndParentClass
     * 参数： [className, parentClass]
     * 返回值： com.dico.modules.domain.SmsEquipmentClass
     * 创建时间: 2019/4/10
     */
    public SmsEquipmentClass findByClassNameEqualsAndParentClassEqualsAndDelFlagIsFalse(String className, String parentClass) {
        return smsEquipmentClassRepository.findByClassNameEqualsAndParentClassEqualsAndDelFlagIsFalse(className, parentClass);
    }

    /**
     * 根据ID集合查询数据
     *
     * @param ids
     * @return
     */
    public List<SmsEquipmentClass> findByIds(String[] ids) {
        return smsEquipmentClassRepository.findByIdInAndDelFlagIsFalse(ids);
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

    public List<SmsEquipmentClass> findByParentClass(String parentClassId){
        return smsEquipmentClassRepository.findByParentClassAndDelFlagIsFalse(parentClassId);
    }

    private List<SmsEquipmentClass> findChildrens(List<SmsEquipmentClass> smsEquipmentClassList){
        if(null == smsEquipmentClassList || smsEquipmentClassList.size() == 0){
            return smsEquipmentClassList;
        }
        for (SmsEquipmentClass smsEquipmentClass : smsEquipmentClassList) {
            List<SmsEquipmentClass> childrenSmsEquipmentClassList = smsEquipmentClassRepository.findByParentClassAndDelFlagIsFalse(smsEquipmentClass.getId());
            if(null != childrenSmsEquipmentClassList && childrenSmsEquipmentClassList.size() > 0){
                this.findChildrens(childrenSmsEquipmentClassList);
            }
            smsEquipmentClass.setChildren(childrenSmsEquipmentClassList);
        }
        return smsEquipmentClassList;
    }
}

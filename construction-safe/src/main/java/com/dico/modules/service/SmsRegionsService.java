package com.dico.modules.service;

import com.dico.jpa.dao.JpaDao;
import com.dico.jpa.service.JpaService;
import com.dico.modules.domain.SmsRegions;
import com.dico.modules.repo.SmsRegionsRepository;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SmsRegionsService extends JpaService<SmsRegions, String> {

    @Autowired
    private SmsRegionsRepository smsRegionsRepository;

    @Override
    protected JpaDao<SmsRegions, String> getDao() {
        return smsRegionsRepository;
    }

    /**
     * 根据ID查询未删除的数据
     */
    public SmsRegions getSmsRegionsByIdAndDelFlagIsFalse(String smsRegionsId) {
        return smsRegionsRepository.getSmsRegionsByIdAndDelFlagIsFalse(smsRegionsId);
    }
    /**
     * 根据Name查询未删除的数据
     */
    public List<SmsRegions> getSmsRegionsByNameAndDelFlagIsFalse(String name) {
        return smsRegionsRepository.getSmsRegionsByNameAndDelFlagIsFalse(name);
    }

    public void deleteByIdIn(String[] ids) {
        smsRegionsRepository.deleteByIdIn(ids);
    }

    /**
     * 根据上级区域ID查询下级区域
     *
     * @param pid
     * @return
     */
    public List<SmsRegions> findChildrens(String pid) {
        List<SmsRegions> smsRegionsList = smsRegionsRepository.findByPidAndDelFlagIsFalse(pid);
        return smsRegionsList;
    }

    /**
     * 查询树形区域
     *
     * @return
     */
    public List<SmsRegions> findTreeData() {
        List<SmsRegions> smsRegionsList = smsRegionsRepository.findByPidIsNullAndDelFlagIsFalse();
        if (null == smsRegionsList || smsRegionsList.size() == 0) {
            return smsRegionsList;
        }
        return this.findChildrenData(smsRegionsList);
    }

    /**
     * 递归查询树
     *
     * @param smsRegionsList
     * @return
     */
    private List<SmsRegions> findChildrenData(List<SmsRegions> smsRegionsList) {
        for (SmsRegions smsRegions : smsRegionsList) {
            List<SmsRegions> childrenSmsRegionsList = this.findChildrens(smsRegions.getId());
            if (null != childrenSmsRegionsList && childrenSmsRegionsList.size() > 0) {
                this.findChildrenData(childrenSmsRegionsList);
            }
            smsRegions.setChildren(childrenSmsRegionsList);
        }
        return smsRegionsList;
    }

    /**
     * 查询所有子区域，返回ID
     *
     * @param smsRegions
     * @return
     */
    public List<String> findChildrensId(SmsRegions smsRegions) {
        List<String> idList = new ArrayList<>();
        idList.add(smsRegions.getId());
        return this.findChildrensId(idList);
    }

    /**
     * 递归查询返回所有ID
     *
     * @param idList
     * @return
     */
    private List<String> findChildrensId(List<String> idList) {
        int index = idList.size();
        for (int i = 0; i < index; i++) {
            List<SmsRegions> childrenSmsRegionsList = this.findChildrens(idList.get(i));
            if (null != childrenSmsRegionsList && childrenSmsRegionsList.size() > 0) {
                List<String> childrenIdList = new ArrayList<>();
                for (SmsRegions smsRegions : childrenSmsRegionsList) {
                    childrenIdList.add(smsRegions.getId());
                }
                this.findChildrensId(childrenIdList);
                idList.addAll(childrenIdList);
            }
        }
        return idList;
    }

    public String findRegionsName(String regionsId) {
        String regionsName = "";
        SmsRegions smsRegions = smsRegionsRepository.getSmsRegionsByIdAndDelFlagIsFalse(regionsId);
        regionsName = smsRegions.getName();
        if (StringUtils.isNotBlank(smsRegions.getPid())) {
            regionsName = this.regionsName(smsRegions, regionsName);
        }
        return regionsName;
    }

    private String regionsName(SmsRegions smsRegions, String regionsName) {
        SmsRegions parentSmsRegions = smsRegionsRepository.getSmsRegionsByIdAndDelFlagIsFalse(smsRegions.getPid());
        regionsName = parentSmsRegions.getName() + " " + regionsName;
        if (StringUtils.isNotBlank(parentSmsRegions.getPid())) {
            regionsName = this.regionsName(parentSmsRegions, regionsName);
        }
        return regionsName;
    }
}

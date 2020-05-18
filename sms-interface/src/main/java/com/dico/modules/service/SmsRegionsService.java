package com.dico.modules.service;

import com.dico.jpa.dao.JpaDao;
import com.dico.jpa.service.JpaService;
import com.dico.modules.domain.SmsRegions;
import com.dico.modules.repo.SmsRegionsRepository;
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
    public SmsRegions getByIdAndDelFlagIsFalse(String smsRegionsId) {
        return smsRegionsRepository.getByIdAndDelFlagIsFalse(smsRegionsId);
    }

    /**
     * 递归查询子区域
     *
     * @param regionsId
     * @return
     */
    public List<SmsRegions> findChildrens(String regionsId) {
        List<SmsRegions> regionsList = new ArrayList<>();
        regionsList.add(this.getByIdAndDelFlagIsFalse(regionsId));
        return this.findChildrens(regionsList);
    }

    /**
     * 递归查询子区域
     *
     * @param regionsList
     * @return
     */
    private List<SmsRegions> findChildrens(List<SmsRegions> regionsList) {
        for (SmsRegions smsRegions : regionsList) {
            List<SmsRegions> smsRegionsList = smsRegionsRepository.findByPidAndDelFlagIsFalse(smsRegions.getId());
            if (null != smsRegionsList && smsRegionsList.size() > 0) {
                this.findChildrens(smsRegionsList);
                smsRegions.setChildrens(smsRegionsList);
            }
        }
        return regionsList;
    }

}

package com.dico.modules.service;

import com.dico.jpa.dao.JpaDao;
import com.dico.jpa.service.JpaService;
import com.dico.modules.domain.SmsDangerInfo;
import com.dico.modules.repo.SmsDangerInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SmsDangerInfoService extends JpaService<SmsDangerInfo, String> {

    @Autowired
    private SmsDangerInfoRepository smsDangerInfoRepository;

    @Override
    protected JpaDao<SmsDangerInfo, String> getDao() {
        return smsDangerInfoRepository;
    }

    /**
     * 根据ID查询未删除的数据
     */
    public SmsDangerInfo getById(String smsDangerInfoId) {
        return smsDangerInfoRepository.getSmsDangerInfoByIdAndDelFlagIsFalse(smsDangerInfoId);
    }
}

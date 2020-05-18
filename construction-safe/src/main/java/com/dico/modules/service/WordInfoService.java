package com.dico.modules.service;

import com.dico.jpa.dao.JpaDao;
import com.dico.jpa.service.JpaService;
import com.dico.modules.domain.WordInfo;
import com.dico.modules.repo.WordInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WordInfoService extends JpaService<WordInfo, String> {

    @Autowired
    private WordInfoRepository wordInfoRepository;

    @Override
    protected JpaDao<WordInfo, String> getDao() {
        return wordInfoRepository;
    }
}

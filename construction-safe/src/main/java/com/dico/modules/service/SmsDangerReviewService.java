package com.dico.modules.service;

import com.dico.jpa.dao.JpaDao;
import com.dico.jpa.service.JpaService;
import com.dico.modules.domain.SmsDangerReview;
import com.dico.modules.repo.SmsDangerReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SmsDangerReviewService extends JpaService<SmsDangerReview, String> {

    @Autowired
    private SmsDangerReviewRepository smsDangerReviewRepository;

    @Override
    protected JpaDao<SmsDangerReview, String> getDao() {
        return smsDangerReviewRepository;
    }

    /**
     * 根据ID查询未删除的数据
     */
    public SmsDangerReview getSmsDangerReviewByIdAndDelFlagIsFalse(String smsDangerReviewId) {
        return smsDangerReviewRepository.getSmsDangerReviewByIdAndDelFlagIsFalse(smsDangerReviewId);
    }

    public void deleteByIdIn(String[] ids) {
        smsDangerReviewRepository.deleteByIdIn(ids);
    }

    public SmsDangerReview getByDangerId(String smsDangerId) {
        return smsDangerReviewRepository.getByDangerIdAndDelFlagIsFalse(smsDangerId);
    }
}

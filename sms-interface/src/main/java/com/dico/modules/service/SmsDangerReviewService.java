package com.dico.modules.service;

import com.dico.enums.DangerStatusEnum;
import com.dico.enums.PlanStatusEnums;
import com.dico.jpa.dao.JpaDao;
import com.dico.jpa.service.JpaService;
import com.dico.modules.domain.SmsDangerReview;
import com.dico.modules.dto.WillInspectionDTO;
import com.dico.modules.dto.WillRepairDTO;
import com.dico.modules.dto.WillReviewDTO;
import com.dico.modules.repo.SmsDangerReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

@Service
public class SmsDangerReviewService extends JpaService<SmsDangerReview, String> {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private SmsDangerReviewRepository smsDangerReviewRepository;

    @Override
    protected JpaDao<SmsDangerReview, String> getDao() {
        return smsDangerReviewRepository;
    }

    /**
     * 根据ID查询未删除的数据
     */
    public SmsDangerReview getById(String smsDangerReviewId) {
        return smsDangerReviewRepository.getSmsDangerReviewByIdAndDelFlagIsFalse(smsDangerReviewId);
    }

    /**
     * 根据用户ID查询待复查信息
     *
     * @param currentUserId
     * @return
     */
    public List<WillReviewDTO> findCurrentUserReview(String currentUserId) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT sdr.id AS review_id, se.`CODE` AS equipment_code, se.`NAME` AS equipment_name, sdi.DANGER_LEVEL_ID AS danger_level, sdi.DANGER_ADDRESS AS danger_address, su.`NAME` AS review_user, sde.REPAIR_RESULT AS repair_result FROM sms_danger_info sdi")
                .append(" LEFT JOIN sms_danger_review sdr ON sdi.id = sdr.DANGER_ID AND sdi.DANGER_STATUS = "+ DangerStatusEnum.REVIEW.getKey() +" LEFT JOIN sms_danger_repair sde ON sde.DANGER_ID = sdi.id LEFT JOIN sms_equipment se ON sdi.EQUIPMENT_ID = se.ID LEFT JOIN sys_user su ON su.id = sdr.REVIEW_USER_ID")
                .append(" WHERE sdr.review_status = 0 AND sdr.DEL_FLAG = 0 AND sdr.REVIEW_USER_ID = '" + currentUserId + "'");
        Query query = entityManager.createNativeQuery(sb.toString(), WillReviewDTO.class);
        return query.getResultList();
    }
}

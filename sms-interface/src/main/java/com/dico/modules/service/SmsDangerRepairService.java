package com.dico.modules.service;

import com.dico.enums.DangerStatusEnum;
import com.dico.enums.PlanStatusEnums;
import com.dico.jpa.dao.JpaDao;
import com.dico.jpa.service.JpaService;
import com.dico.modules.domain.SmsDangerRepair;
import com.dico.modules.dto.WillInspectionDTO;
import com.dico.modules.dto.WillRepairDTO;
import com.dico.modules.repo.SmsDangerRepairRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class SmsDangerRepairService extends JpaService<SmsDangerRepair, String> {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private SmsDangerRepairRepository smsDangerRepairRepository;

    @Override
    protected JpaDao<SmsDangerRepair, String> getDao() {
        return smsDangerRepairRepository;
    }

    /**
     * 根据ID查询未删除的数据
     */
    public SmsDangerRepair getById(String smsDangerRepairId) {
        return smsDangerRepairRepository.getSmsDangerRepairByIdAndDelFlagIsFalse(smsDangerRepairId);
    }

    /**
     * 查询用户所有待整改
     *
     * @param currentUserId
     * @return
     */
    public List<WillRepairDTO> findCurrentUserRepair(String currentUserId) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT sdr.id AS repair_id, se.`NAME` AS equipment_name, sdi.DANGER_LEVEL_ID AS danger_level, sdi.DANGER_ADDRESS AS danger_address, su.`NAME` AS inspection_user, sdr.REPAIR_LIMIT AS repair_limit, sdr.REPAIR_OPINION AS repair_opinion FROM sms_danger_info sdi")
                .append(" LEFT JOIN sms_danger_repair sdr ON sdi.id = sdr.DANGER_ID AND sdi.DANGER_STATUS = "+ DangerStatusEnum.REPAIR.getKey() +" LEFT JOIN sms_equipment se ON se.ID = sdi.EQUIPMENT_ID LEFT JOIN sys_user su ON su.id = sdi.CREATE_USER")
                .append(" WHERE sdr.repair_status = 0 AND sdr.REPAIR_LIMIT > '").append(sdf.format(new Date())).append("' AND sdi.DEL_FLAG = 0 AND sdr.REPAIR_USER_ID = '" + currentUserId + "'");
        Query query = entityManager.createNativeQuery(sb.toString(), WillRepairDTO.class);
        return query.getResultList();
    }

    /**
     * 超期隐患
     *
     * @param currentUserId
     * @return
     */
    public List<WillRepairDTO> findCurrentUserOutRepair(String currentUserId) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT sdr.id AS repair_id, se.`NAME` AS equipment_name, sdi.DANGER_LEVEL_ID AS danger_level, sdi.DANGER_ADDRESS AS danger_address, su.`NAME` AS inspection_user, sdr.REPAIR_LIMIT AS repair_limit, sdr.REPAIR_OPINION AS repair_opinion FROM sms_danger_repair sdr")
                .append(" LEFT JOIN sms_danger_info sdi ON sdi.id = sdr.DANGER_ID LEFT JOIN sms_equipment se ON se.ID = sdi.EQUIPMENT_ID LEFT JOIN sys_user su ON su.id = sdi.CREATE_USER")
                .append(" WHERE sdr.repair_status = 0 AND sdr.REPAIR_LIMIT < '").append(sdf.format(new Date())).append("' AND sdi.DEL_FLAG = 0 AND sdr.REPAIR_USER_ID = '" + currentUserId + "'");
        Query query = entityManager.createNativeQuery(sb.toString(), WillRepairDTO.class);
        return query.getResultList();
    }

    public SmsDangerRepair getByDangerId(String dangerId) {
        return smsDangerRepairRepository.getByDangerIdAndDelFlagIsFalse(dangerId);
    }
}

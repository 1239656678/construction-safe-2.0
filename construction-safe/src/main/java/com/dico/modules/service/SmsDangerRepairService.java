package com.dico.modules.service;

import com.dico.jpa.dao.JpaDao;
import com.dico.jpa.service.JpaService;
import com.dico.modules.domain.SmsDangerRepair;
import com.dico.modules.dto.ClassStatistice;
import com.dico.modules.dto.RepairStatistice;
import com.dico.modules.repo.SmsDangerRepairRepository;
import com.netflix.discovery.converters.Auto;
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
    private SmsDangerRepairRepository smsDangerRepairRepository;

    @Autowired
    private EntityManager entityManager;

    @Override
    protected JpaDao<SmsDangerRepair, String> getDao() {
        return smsDangerRepairRepository;
    }

    /**
     * 根据ID查询未删除的数据
     */
    public SmsDangerRepair getSmsDangerRepairByIdAndDelFlagIsFalse(String smsDangerRepairId) {
        return smsDangerRepairRepository.getSmsDangerRepairByIdAndDelFlagIsFalse(smsDangerRepairId);
    }

    public void deleteByIdIn(String[] ids) {
        smsDangerRepairRepository.deleteByIdIn(ids);
    }

    public SmsDangerRepair getByDangerId(String dangerId) {
        return smsDangerRepairRepository.getByDangerIdAndDelFlagIsFalse(dangerId);
    }

    public int findCount() {
        return smsDangerRepairRepository.findCount();
    }

    public int findCountByUpdateDateBetween(Date firstDate, Date lastDate) {
        return smsDangerRepairRepository.findCountByUpdateDateBetween(firstDate, lastDate);
    }

    public List<RepairStatistice> findRepairStatisticsByYear(Date yearFirstDate, Date yearLastDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT DATE_FORMAT(sdi.CREATE_DATE, '%Y-%m') AS datetime, count(sdi.id) AS danger_num, count( CASE sdr.REPAIR_STATUS WHEN 1 THEN 1 ELSE NULL END ) AS repair_num FROM sms_danger_info sdi LEFT JOIN sms_danger_repair sdr ON sdi.id = sdr.DANGER_ID AND sdr.DEL_FLAG = 0 WHERE sdi.DEL_FLAG = 0 AND sdi.CREATE_DATE BETWEEN '").append(sdf.format(yearFirstDate)).append("' AND '").append(sdf.format(yearLastDate)).append("' GROUP BY datetime");
        Query query = entityManager.createNativeQuery(sb.toString(), RepairStatistice.class);
        return query.getResultList();
    }

    public int findCount(Date firstDate, Date lastDate) {
        return smsDangerRepairRepository.findCount(firstDate, lastDate);
    }
}

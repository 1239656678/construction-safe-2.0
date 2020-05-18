package com.dico.modules.service;

import com.dico.jpa.dao.JpaDao;
import com.dico.jpa.service.JpaService;
import com.dico.modules.domain.SmsDangerInfo;
import com.dico.modules.dto.DangerStatistice;
import com.dico.modules.dto.RepairStatistice;
import com.dico.modules.repo.SmsDangerInfoRepository;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class SmsDangerInfoService extends JpaService<SmsDangerInfo, String> {

    @Autowired
    private SmsDangerInfoRepository smsDangerInfoRepository;

    @Autowired
    private EntityManager entityManager;

    @Override
    protected JpaDao<SmsDangerInfo, String> getDao() {
        return smsDangerInfoRepository;
    }

    /**
     * 根据ID查询未删除的数据
     */
    public SmsDangerInfo getSmsDangerInfoByIdAndDelFlagIsFalse(String smsDangerInfoId) {
        return smsDangerInfoRepository.getSmsDangerInfoByIdAndDelFlagIsFalse(smsDangerInfoId);
    }

    public void deleteByIdIn(String[] ids) {
        smsDangerInfoRepository.deleteByIdIn(ids);
    }

    public int findCount() {
        return smsDangerInfoRepository.findCount();
    }

    public int findCount(Date preDate, Date nextDate) {
        return smsDangerInfoRepository.findCount(preDate, nextDate);
    }

    public int findCountByCreateDateBetween(Date firstDate, Date lastDate) {
        return smsDangerInfoRepository.findCountByCreateDateBetween(firstDate, lastDate);
    }

    public List<DangerStatistice> findDangerStatisticsByYear(Date yearFirstDate, Date yearLastDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT DATE_FORMAT(sdi.CREATE_DATE, '%Y-%m') AS datetime, count(sdi.id) AS danger_num, count( CASE sdr.REPAIR_STATUS WHEN 1 THEN 1 ELSE NULL END ) AS repair_num, count( CASE sdr1.REVIEW_STATUS WHEN 1 THEN 1 ELSE NULL END ) AS review_num FROM sms_danger_info sdi LEFT JOIN sms_danger_repair sdr ON sdi.id = sdr.DANGER_ID AND sdr.DEL_FLAG = 0 LEFT JOIN sms_danger_review sdr1 ON sdi.id = sdr1.DANGER_ID AND sdr1.DEL_FLAG = 0 WHERE sdi.DEL_FLAG = 0 AND sdi.CREATE_DATE BETWEEN '").append(sdf.format(yearFirstDate)).append("' AND '").append(sdf.format(yearLastDate)).append("' GROUP BY datetime");
        Query query = entityManager.createNativeQuery(sb.toString(), DangerStatistice.class);
        return query.getResultList();
    }

    public List<Map<String, Object>> findWordInfo(Date firstDate, Date lastDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT CASE when se.`NAME` IS null then '' ELSE se.`NAME` END AS equipmentName, sdi.DANGER_ADDRESS AS dangerAddress, CASE sdi.DANGER_LEVEL_ID WHEN 0 THEN '一般隐患' WHEN 1 THEN '较大隐患' WHEN 2 THEN '重大隐患' END AS dangerLevel, CASE sdi.DANGER_FROM WHEN 0 THEN '随手拍' WHEN 1 THEN '检查计划' END AS dangerFrom, su.`NAME` AS createUser, sdi.CREATE_DATE AS createDate, CASE sdi.DANGER_STATUS WHEN 0 THEN '未处理' WHEN 1 THEN '待整改' WHEN 2 THEN '待复查' WHEN 3 THEN '已完成' END AS dangerStatus FROM sms_danger_info sdi LEFT JOIN sms_equipment se ON se.ID = sdi.EQUIPMENT_ID AND se.DEL_FLAG IS FALSE LEFT JOIN sys_user su ON su.id = sdi.CREATE_USER AND su.DEL_FLAG IS FALSE WHERE sdi.DEL_FLAG IS FALSE")
                .append(" AND sdi.CREATE_DATE BETWEEN '").append(sdf.format(firstDate)).append("' AND '").append(sdf.format(lastDate)).append("'");
        Query query = entityManager.createNativeQuery(sb.toString());
        query.unwrap(NativeQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return query.getResultList();
    }

    public List<Map<String, Object>> findWordInfoWillDoing(Date firstDate, Date lastDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT CASE WHEN se.`NAME` IS NULL THEN '' ELSE se.`NAME` END AS equipmentName, CASE sdi.DANGER_LEVEL_ID WHEN 0 THEN '一般隐患' WHEN 1 THEN '较大隐患' WHEN 2 THEN '重大隐患' END AS dangerLevel, su.`NAME` AS repairUser, sdr.REPAIR_LIMIT AS repairLimit, o.`NAME` AS organization FROM sms_danger_info sdi LEFT JOIN sms_danger_repair sdr ON sdr.DANGER_ID = sdi.id AND sdr.DEL_FLAG IS FALSE LEFT JOIN sms_equipment se ON se.ID = sdi.EQUIPMENT_ID AND se.DEL_FLAG IS FALSE LEFT JOIN sys_user su ON su.id = sdr.REPAIR_USER_ID AND su.DEL_FLAG IS FALSE LEFT JOIN organization o ON o.id = su.ORGANIZATION_ID AND o.DEL_FLAG IS FALSE WHERE sdi.DEL_FLAG IS FALSE")
                .append("  AND sdi.CREATE_DATE BETWEEN '").append(sdf.format(firstDate)).append("' AND '").append(sdf.format(lastDate)).append("' AND ( sdi.DANGER_STATUS = 0 OR sdi.DANGER_STATUS = 1 )");
        Query query = entityManager.createNativeQuery(sb.toString());
        query.unwrap(NativeQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return query.getResultList();
    }
}

package com.dico.modules.controller;

import com.dico.enums.PlanStatusEnums;
import com.dico.modules.domain.SmsUserInspectionPlan;
import com.dico.modules.service.SmsUserInspectionPlanService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author Gaodl
 * @version v1.0
 * 文件名称: $file_name$
 * 创建时间: $date$
 */
@Slf4j
@Component
public class InspectionPlanTask extends QuartzJobBean {

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private SmsUserInspectionPlanService smsUserInspectionPlanService;

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT * FROM sms_user_inspection_plan suip where suip.`STATUS` <> 2 AND suip.END_DATE < '" + sdf.format(new Date()) + "' AND suip.DEL_FLAG = 0");
        Query query = entityManager.createNativeQuery(sb.toString(), SmsUserInspectionPlan.class);
        List<SmsUserInspectionPlan> smsUserInspectionPlanList = query.getResultList();
        if (null != smsUserInspectionPlanList && smsUserInspectionPlanList.size() > 0) {
            smsUserInspectionPlanList.forEach(smsUserInspectionPlan -> {
                smsUserInspectionPlan.setStatus(PlanStatusEnums.TIMEOUT.getKey());
                smsUserInspectionPlanService.update(smsUserInspectionPlan);
            });
        }
    }
}

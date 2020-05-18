package com.dico.common.task;

import com.dico.dateutils.DateUtils;
import com.dico.modules.controller.ExportMyWord;
import com.dico.modules.domain.WordInfo;
import com.dico.modules.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author Administrator
 * @date 2020-04-20 16:14
 */
@Component
public class WordInfoTask {

    @Autowired
    private SmsEquipmentService smsEquipmentService;
    @Autowired
    private SmsInspectionPlanService smsInspectionPlanService;
    @Autowired
    private SmsDangerInfoService smsDangerInfoService;
    @Autowired
    private SmsDangerRepairService smsDangerRepairService;
    @Autowired
    private SmsMaintenancePlanService smsMaintenancePlanService;
    @Autowired
    private SmsEquipmentFaultInfoService smsEquipmentFaultInfoService;
    @Autowired
    private WordInfoService wordInfoService;

    @Value("${diy.word.save-url}")
    private String WORD_URL;

    @Scheduled(cron = "0 0 1 1 * ?")
//    @Scheduled(cron = "0/5 * * * * ?")
    public void test(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MONTH, -1);
        String dateFormat = sdf1.format(calendar.getTime());
        ExportMyWord emw = new ExportMyWord();
        Map<String, Object> dataMap = new HashMap<String, Object>();
        dataMap.put("company", "缔科网络科技有限公司");
        dataMap.put("createDate", sdf.format(new Date()));
        dataMap.put("equipmentCount", smsEquipmentService.findCount());
        dataMap.put("inspectionPlanCount", smsInspectionPlanService.findCount());
        dataMap.put("inspectionPlanFinishCount", smsInspectionPlanService.findFinishedCount());
        dataMap.put("dangerCount", smsDangerInfoService.findCount(DateUtils.getMonthFirstDay(calendar.getTime()), DateUtils.getMonthLastDay(calendar.getTime())));
        dataMap.put("dangerRepairCount", smsDangerRepairService.findCount(DateUtils.getMonthFirstDay(calendar.getTime()), DateUtils.getMonthLastDay(calendar.getTime())));
        dataMap.put("maintenancePlanCount", smsMaintenancePlanService.findCount(DateUtils.getMonthFirstDay(calendar.getTime()), DateUtils.getMonthLastDay(calendar.getTime())));
        dataMap.put("maintenancePlanFinishCount", smsMaintenancePlanService.findFinishedCount(DateUtils.getMonthFirstDay(calendar.getTime()), DateUtils.getMonthLastDay(calendar.getTime())));
        dataMap.put("faultReportCount", smsEquipmentFaultInfoService.findCount(DateUtils.getMonthFirstDay(calendar.getTime()), DateUtils.getMonthLastDay(calendar.getTime())));
        dataMap.put("faultFinishCount", smsEquipmentFaultInfoService.findFinishedCount(DateUtils.getMonthFirstDay(calendar.getTime()), DateUtils.getMonthLastDay(calendar.getTime())));
        dataMap.put("timeoutInspectionPlan", smsInspectionPlanService.findTimeoutInfo(DateUtils.getMonthFirstDay(calendar.getTime()), DateUtils.getMonthLastDay(calendar.getTime())));
        dataMap.put("dangerList", smsDangerInfoService.findWordInfo(DateUtils.getMonthFirstDay(calendar.getTime()), DateUtils.getMonthLastDay(calendar.getTime())));
        dataMap.put("dangerWillDoingList", smsDangerInfoService.findWordInfoWillDoing(DateUtils.getMonthFirstDay(calendar.getTime()), DateUtils.getMonthLastDay(calendar.getTime())));
        dataMap.put("maintenanceWillDoingList", smsMaintenancePlanService.findWordInfoWillDoing(DateUtils.getMonthFirstDay(calendar.getTime()), DateUtils.getMonthLastDay(calendar.getTime())));
        dataMap.put("faultWillDoingList", smsEquipmentFaultInfoService.findWordInfoWillDoing(DateUtils.getMonthFirstDay(calendar.getTime()), DateUtils.getMonthLastDay(calendar.getTime())));
        // 新增列表数据
        WordInfo wordInfo = new WordInfo();
        wordInfo.setName(dateFormat+"月-报表");
        wordInfo.setType(0);
        wordInfo.setCreateDate(new Date());
        wordInfoService.save(wordInfo);
        emw.createWord(dataMap, "wordTest.ftl", WORD_URL+wordInfo.getId()+".doc");
    }
}

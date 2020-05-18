package com.dico.modules.controller;

import com.dico.modules.domain.SmsDangerInfo;
import com.dico.modules.domain.SmsDangerRepair;
import com.dico.modules.domain.SmsEquipment;
import com.dico.modules.domain.SmsRegions;
import com.dico.modules.dto.ClassStatistice;
import com.dico.modules.dto.DangerStatistice;
import com.dico.modules.dto.RepairStatistice;
import com.dico.modules.service.*;
import com.dico.result.ResultData;
import com.dico.util.DateUtils;
import freemarker.template.Configuration;
import freemarker.template.Template;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 区域模块
 */
@RestController
@Api(tags = "后台首页接口", produces = "后台首页接口Api")
public class HomeController {

    @Autowired
    private SmsEquipmentService smsEquipmentService;

    @Autowired
    private SmsInspectionStatusService smsInspectionStatusService;

    @Autowired
    private SmsDangerInfoService smsDangerInfoService;

    @Autowired
    private SmsDangerRepairService smsDangerRepairService;

    /**
     * 设备量、检查量、隐患量、整改量统计
     */
    @ResponseBody
    @GetMapping("/home/statistics")
    @ApiOperation(value = "设备量、检查量、隐患量、整改量统计", notes = "设备量、检查量、隐患量、整改量统计")
    public ResultData statistics() {
        Map<String, Object> dataMap = new HashMap<>();
        /*
         * 设备量统计
         */
        int equipmentCount = smsEquipmentService.findCount();
        Date firstDate = DateUtils.getCurrentMonthFirstDay();
        Date lastDate = DateUtils.getCurrentMonthLastDay();
        int newEquipmentCount = smsEquipmentService.findCountByCreateDateBetween(firstDate, lastDate);
        Map<String, Object> equipmentMap = new HashMap<>();
        equipmentMap.put("count", equipmentCount);
        equipmentMap.put("newCount", newEquipmentCount);
        dataMap.put("equipment", equipmentMap);

        /*
         * 巡检量统计
         */
        int inspectionCount = smsInspectionStatusService.findCount();
        int newInspectionCount = smsInspectionStatusService.findCountByUpdateDateBetween(firstDate, lastDate);
        Map<String, Object> inspectionMap = new HashMap<>();
        inspectionMap.put("count", inspectionCount);
        inspectionMap.put("newCount", newInspectionCount);
        dataMap.put("inspection", inspectionMap);

        /*
         * 隐患量统计
         */
        int dangerCount = smsDangerInfoService.findCount();
        int newDangerCount = smsDangerInfoService.findCountByCreateDateBetween(firstDate, lastDate);
        Map<String, Object> dangerMap = new HashMap<>();
        dangerMap.put("count", dangerCount);
        dangerMap.put("newCount", newDangerCount);
        dataMap.put("danger", dangerMap);

        /*
         * 整改量统计
         */
        int repairCount = smsDangerRepairService.findCount();
        int newRepairCount = smsDangerRepairService.findCountByUpdateDateBetween(firstDate, lastDate);
        Map<String, Object> repairMap = new HashMap<>();
        repairMap.put("count", repairCount);
        repairMap.put("newCount", newRepairCount);
        dataMap.put("repair", repairMap);
        return ResultData.getSuccessResult(dataMap);
    }

    /**
     * 设备分类统计
     */
    @ResponseBody
    @GetMapping("/home/equipmentClassStatistics")
    @ApiOperation(value = "设备分类统计", notes = "设备分类统计")
    public ResultData equipmentClassStatistics() {
        int equiipmentCount = smsEquipmentService.findCount();
        List<ClassStatistice> classStatisticeList = smsEquipmentService.findClassStatistice();
        for (ClassStatistice classStatistice : classStatisticeList){
            classStatistice.setStatistice(String.valueOf(Double.valueOf(String.format("%.2f", (classStatistice.getNum()/Double.valueOf(equiipmentCount))*100)))+"%");
        }
        return ResultData.getSuccessResult(classStatisticeList);
    }

    /**
     * 整改统计
     */
    @ResponseBody
    @GetMapping("/home/repairStatistics")
    @ApiOperation(value = "整改统计", notes = "整改统计")
    public ResultData repairStatistics() {
        Date yearFirstDate = DateUtils.getCurrentYearFirstDay();
        Date yearLastDate = DateUtils.getCurrentYearLastDay();
        List<RepairStatistice> repairStatisticeList = smsDangerRepairService.findRepairStatisticsByYear(yearFirstDate, yearLastDate);
        return ResultData.getSuccessResult(repairStatisticeList);
    }

    /**
     * 隐患统计
     */
    @ResponseBody
    @GetMapping("/home/dangerStatistics")
    @ApiOperation(value = "隐患统计", notes = "隐患统计")
    public ResultData dangerStatistics() {
        Date yearFirstDate = DateUtils.getCurrentYearFirstDay();
        Date yearLastDate = DateUtils.getCurrentYearLastDay();
        List<DangerStatistice> dangerStatisticeList = smsDangerInfoService.findDangerStatisticsByYear(yearFirstDate, yearLastDate);
        return ResultData.getSuccessResult(dangerStatisticeList);
    }

    /**
     * 测试导出word
     */
    @ResponseBody
    @GetMapping("/home/test")
    @ApiOperation(value = "测试导出word", notes = "测试导出word")
    public ResultData test(HttpServletResponse response) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        ExportMyWord emw = new ExportMyWord();
        Map<String, Object> dataMap = new HashMap<String, Object>();
        dataMap.put("company", "缔科网络科技有限公司");
        dataMap.put("createDate", sdf.format(new Date()));
        dataMap.put("equipmentCount", "1");
        dataMap.put("inspectionPlanCount", "2");
        dataMap.put("inspectionPlanFinishCount", "3");
        dataMap.put("dangerCount", "5");
        dataMap.put("dangerRepairCount", "3");
        dataMap.put("maintenancePlanCount", "5");
        dataMap.put("maintenancePlanFinishCount", "5");
        dataMap.put("faultReportCount", "5");
        dataMap.put("faultFinishCount", "5");

        List<Map<String, Object>> dataList = new ArrayList<>();
        for(int i = 0; i < 10; i++){
            Map<String, Object> proMap = new HashMap<>();
            proMap.put("code", "code"+i);
            proMap.put("name", "name"+i);
            proMap.put("type", "type"+i);
            proMap.put("beginDate", "beginDate"+i);
            proMap.put("endDate", "endDate"+i);
            proMap.put("code", "code"+i);
            proMap.put("person", "person"+i);
            proMap.put("organization", "organization"+i);
            dataList.add(proMap);
        }
        dataMap.put("project", dataList);
        dataMap.put("faultWillDoingList", new ArrayList<>());
        dataMap.put("dangerList", new ArrayList<>());
        dataMap.put("dangerWillDoingList", new ArrayList<>());
        dataMap.put("maintenanceWillDoingList", new ArrayList<>());
        emw.createWord(dataMap, "wordTest.ftl", "E:/test.doc");
        return ResultData.getSuccessResult();
    }

}

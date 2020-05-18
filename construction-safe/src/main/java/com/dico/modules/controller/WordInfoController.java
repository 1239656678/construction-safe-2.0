package com.dico.modules.controller;

import com.dico.dateutils.DateUtils;
import com.dico.modules.domain.WordInfo;
import com.dico.modules.service.*;
import com.dico.result.ResultData;
import com.dico.util.TokenUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@Api(tags = "报表模块", produces = "报表模块Api")
public class WordInfoController {

    @Autowired
    private WordInfoService wordInfoService;

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

    @Value("${diy.word.save-url}")
    private String WORD_URL;

    /**
     * 月度报表列表
     */
    @ResponseBody
    @GetMapping("/wordInfo/dataList")
    @ApiOperation(value = "月度报表列表", notes = "月度报表列表")
    public ResultData dataList(HttpServletRequest request, @RequestParam(required = false) String name, @RequestParam(required = false) Integer type) {
        List<Map<String, Object>> listDataMap;
        try {
            Map<String, Object> queryMap = new HashMap<>(2);
            if(null != name){
                queryMap.put("name_LIKE", name);
            }
            if(null != type){
                queryMap.put("type_EQ", type);
            }
            Sort sort = new Sort(Sort.Direction.DESC, "createDate");
            listDataMap = wordInfoService.findAll(queryMap, sort, request);

        } catch (Exception e) {
            return ResultData.getFailResult(e.getMessage());
        }
        return ResultData.getSuccessResult(listDataMap);
    }

    /**
     * 生成季度或者年度报表
     */
    @ResponseBody
    @GetMapping("/wordInfo/generatorWord")
    @ApiOperation(value = "生成季度或者年度报表", notes = "生成季度或者年度报表")
    public ResultData generatorWord(HttpServletRequest request, @RequestParam boolean isYear, @RequestParam int year, @RequestParam(required = false, defaultValue = "0") int quarter) {
        String userId = TokenUtils.getUserIdByRequest(request);
        Date beginDate = null;
        Date endDate = null;
        if(!isYear){
            List<Date> dateList = DateUtils.getQuarterBeginAndEndDate(year, quarter);
            beginDate = dateList.get(0);
            endDate = dateList.get(1);
        }else{
            Calendar c = Calendar.getInstance();
            c.set(Calendar.YEAR, year);
            beginDate = DateUtils.getYearFirstDay(c.getTime());
            endDate = DateUtils.getYearLastDay(c.getTime());
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Map<String, Object> dataMap = new HashMap<String, Object>();
        dataMap.put("company", "缔科网络科技有限公司");
        dataMap.put("createDate", sdf.format(new Date()));
        dataMap.put("equipmentCount", smsEquipmentService.findCount());
        dataMap.put("inspectionPlanCount", smsInspectionPlanService.findCount());
        dataMap.put("inspectionPlanFinishCount", smsInspectionPlanService.findFinishedCount());
        dataMap.put("dangerCount", smsDangerInfoService.findCount(beginDate, endDate));
        dataMap.put("dangerRepairCount", smsDangerRepairService.findCount(beginDate, endDate));
        dataMap.put("maintenancePlanCount", smsMaintenancePlanService.findCount(beginDate, endDate));
        dataMap.put("maintenancePlanFinishCount", smsMaintenancePlanService.findFinishedCount(beginDate, endDate));
        dataMap.put("faultReportCount", smsEquipmentFaultInfoService.findCount(beginDate, endDate));
        dataMap.put("faultFinishCount", smsEquipmentFaultInfoService.findFinishedCount(beginDate, endDate));
        dataMap.put("timeoutInspectionPlan", smsInspectionPlanService.findTimeoutInfo(beginDate, endDate));
        dataMap.put("dangerList", smsDangerInfoService.findWordInfo(beginDate, endDate));
        dataMap.put("dangerWillDoingList", smsDangerInfoService.findWordInfoWillDoing(beginDate, endDate));
        dataMap.put("maintenanceWillDoingList", smsMaintenancePlanService.findWordInfoWillDoing(beginDate, endDate));
        dataMap.put("faultWillDoingList", smsEquipmentFaultInfoService.findWordInfoWillDoing(beginDate, endDate));
        // 新增列表数据
        WordInfo wordInfo = new WordInfo();
        if(isYear){
            wordInfo.setName(year+"年-报表");
            wordInfo.setType(2);
        }else{
            wordInfo.setName(year+"年第"+(quarter+1)+"季度-报表");
            wordInfo.setType(1);
        }
        wordInfo.setCreateUser(userId);
        wordInfo.setCreateDate(new Date());
        wordInfoService.save(wordInfo);
        ExportMyWord emw = new ExportMyWord();
        emw.createWord(dataMap, "wordTest.ftl", WORD_URL+wordInfo.getId()+".zip");
        return ResultData.getSuccessResult();
    }
}

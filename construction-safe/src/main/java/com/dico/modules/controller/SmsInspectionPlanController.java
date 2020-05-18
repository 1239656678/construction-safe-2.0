package com.dico.modules.controller;

import com.dico.enums.PlanStatusEnums;
import com.dico.enums.PlanTypeEnums;
import com.dico.feign.domain.OrganizationFeignDomain;
import com.dico.feign.domain.SysUser;
import com.dico.feign.feignClient.DicoBaseClient;
import com.dico.modules.domain.*;
import com.dico.modules.service.*;
import com.dico.result.ResultData;
import com.dico.result.ValidatedResult;
import com.dico.util.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.dico.enums.PlanTypeEnums.*;

/**
 * @author Stephen
 */
@RestController
@Api(tags = "巡检计划模块", produces = "巡检计划模块Api")
public class SmsInspectionPlanController {

    @Autowired
    private SmsInspectionPlanService smsInspectionPlanService;

    @Autowired
    private SmsEquipmentService smsEquipmentService;

    @Autowired
    private SmsEquipmentClassService smsEquipmentClassService;

    @Autowired
    private SmsPlanEquipmentService smsPlanEquipmentService;

    @Autowired
    private SmsUserInspectionPlanService smsUserInspectionPlanService;

    @Autowired
    private SmsInspectionStatusService smsInspectionStatusService;

    @Autowired
    private DicoBaseClient dicoBaseClient;


    private static final String BEGIN_SECOND = " 00:00:00";
    private static final String END_SECOND = " 23:59:59";

    private static final SimpleDateFormat sdf_day = new SimpleDateFormat("yyyy-MM-dd");
    private static final SimpleDateFormat sdf_second = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 分页查询方法
     *
     * @param request
     * @param planName
     * @param planStatus
     * @param planCode
     * @param pageNum
     * @param pageSize
     * @return
     */
    @ResponseBody
    @GetMapping("/smsInspectionPlan/dataPage")
    @ApiOperation(value = "获取计划分页数据", notes = "获取计划分页数据")
    public ResultData dataPage(HttpServletRequest request, String planName, String planStatus, String planCode, @RequestParam(required = false, defaultValue = "0", value = "pageNum") int pageNum, @RequestParam(required = false, defaultValue = "10", value = "pageSize") int pageSize) {
        Page<SmsInspectionPlan> smsInspectionPlanPage;
        try {
            Map<String, Object> queryMap = new HashMap<>(3);
            if (StringUtils.isNotBlank(planName)) {
                queryMap.put("planName_LIKE", planName);
            }
            if (StringUtils.isNotBlank(planCode)) {
                queryMap.put("planCode_LIKE", planCode);
            }
            if (StringUtils.isNotBlank(planStatus)) {
                queryMap.put("planStatus_LIKE", planStatus);
            }
            queryMap.put("delFlag_EQ", false);
            Sort sort = new Sort(Sort.Direction.DESC, "createDate");
            smsInspectionPlanPage = smsInspectionPlanService.findAll(queryMap, pageNum, pageSize, sort);
        } catch (Exception e) {
            return ResultData.getFailResult(e.getMessage());
        }
        return ResultData.getSuccessResult(smsInspectionPlanPage);
    }

    /**
     * 不分页查询方法
     *
     * @param request
     * @param planName
     * @return
     */
    @ResponseBody
    @GetMapping("/smsInspectionPlan/dataList")
    @ApiOperation(value = "获取数据", notes = "获取数据方法")
    public ResultData dataList(HttpServletRequest request, @RequestParam(required = false) String planCode, @RequestParam(required = false) String planName, @RequestParam(name = "planType", defaultValue = "false", required = false) boolean planType, @RequestParam(name = "beginTime", defaultValue = "", required = false) String beginTime, @RequestParam(name = "endTime", defaultValue = "", required = false) String endTime) {
        List<Map<String, Object>> listDataMap;
        try {
            Map<String, Object> queryMap = new HashMap<>(4);
            if (StringUtils.isNotBlank(planCode)) {
                queryMap.put("planCode_LIKE", planCode);
            }
            if (StringUtils.isNotBlank(planName)) {
                queryMap.put("planName_LIKE", planName);
            }
            if (!planType) {
                String[] param = {DAY.getKey() + "", WEEK.getKey() + "", MONTH.getKey() + "", QUARTER.getKey() + ""};
                queryMap.put("planType_IN", param);
            } else {
                queryMap.put("planType_EQ", SPECIAL.getKey() + "");
            }
            if (StringUtils.isNotBlank(beginTime) && StringUtils.isNotBlank(endTime)) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Map<String, Date> dateMap = new HashMap<>();
                dateMap.put("beginDate", sdf.parse(beginTime));
                dateMap.put("endDate", sdf.parse(endTime));
                queryMap.put("beginDate_BETWEEN", dateMap);
            }
            queryMap.put("delFlag_EQ", false);
            Sort sort = new Sort(Sort.Direction.DESC, "createDate");
            listDataMap = smsInspectionPlanService.findAll(queryMap, sort, request);
            for (Map<String, Object> stringObjectMap : listDataMap) {
                SysUser sysUser = dicoBaseClient.findUserById(stringObjectMap.get("personLiableId").toString());
                if (null != sysUser) {
                    stringObjectMap.put("personLiableName", sysUser.getName());
                }
                ResultData resultData = dicoBaseClient.findOrganizationById(stringObjectMap.get("organizationId").toString());
                if (null != resultData && resultData.getCode() == 0) {
                    Map<String, String> dataMap = (Map<String, String>) resultData.getData();
                    stringObjectMap.put("organizationName", dataMap.get("name"));
                }
                List<SmsPlanEquipmentClass> smsPlanEquipmentClassList = smsPlanEquipmentService.findByPlanId(stringObjectMap.get("id").toString());
                if (!planType) {
                    if (null != smsPlanEquipmentClassList && smsPlanEquipmentClassList.size() > 0) {
                        Map<String, Object> dataMap = new HashMap<>();
                        for (SmsPlanEquipmentClass smsPlanEquipmentClass : smsPlanEquipmentClassList) {
                            dataMap.put(smsPlanEquipmentClass.getRegionsId(), smsPlanEquipmentClass.getEquipmentClassId());
                        }
                        Set<String> keys = dataMap.keySet();
                        String[] items = new String[keys.size()];
                        int index = 0;
                        for (String key : keys) {
                            items[index] = key;
                            index++;
                        }
                        stringObjectMap.put("itemIds", items);
                    }
                } else {
                    if (null != smsPlanEquipmentClassList && smsPlanEquipmentClassList.size() > 0) {
                        String[] items = new String[smsPlanEquipmentClassList.size()];
                        for (int i = 0; i < smsPlanEquipmentClassList.size(); i++) {
                            items[i] = smsPlanEquipmentClassList.get(i).getEquipmentClassId();
                        }
                        stringObjectMap.put("itemIds", items);
                    }
                }
                stringObjectMap.put("smsUserInspectionPlanList", smsUserInspectionPlanService.findByInspectionId(stringObjectMap.get("id").toString()));
            }
        } catch (Exception e) {
            return ResultData.getFailResult(e.getMessage());
        }
        return ResultData.getSuccessResult(listDataMap);
    }

    /**
     * 获取数据详情
     *
     * @param smsInspectionPlanId
     * @return
     */
    @ResponseBody
    @GetMapping("/smsInspectionPlan/dataInfo")
    @ApiOperation(value = "获取数据详情", notes = "获取数据详情")
    public ResultData dataInfo(String smsInspectionPlanId) {
        SmsInspectionPlan smsInspectionPlan = smsInspectionPlanService.getSmsInspectionPlanByIdAndDelFlagIsFalse(smsInspectionPlanId);
        if (null == smsInspectionPlan) {
            return ResultData.getFailResult("数据不存在");
        }
        SysUser sysUser = dicoBaseClient.findUserById(smsInspectionPlan.getPersonLiableId());
        if (null != sysUser) {
            smsInspectionPlan.setPersonLiableName(sysUser.getName());
        }
        ResultData resultData = dicoBaseClient.findOrganizationById(smsInspectionPlan.getOrganizationId());
        if (null != resultData && resultData.getCode() == 0) {
            Map<String, String> dataMap = (Map<String, String>) resultData.getData();
            smsInspectionPlan.setOrganizationName(dataMap.get("name"));
        }
        smsInspectionPlan.setSmsUserInspectionPlanList(smsUserInspectionPlanService.findByInspectionId(smsInspectionPlan.getId()));
        return ResultData.getSuccessResult(smsInspectionPlan);
    }

    /**
     * 新增方法
     *
     * @param request
     * @param smsInspectionPlan
     * @return
     */
    @ResponseBody
    @Transactional
    @PostMapping("/smsInspectionPlan/save")
    @ApiOperation(value = "保存", notes = "保存方法")
    public ResultData save(HttpServletRequest request, @RequestBody SmsInspectionPlan smsInspectionPlan) {
        try {
            // 获取当前登录用户
            String currentUserId = TokenUtils.getUserIdByRequest(request);
            if (StringUtils.isBlank(currentUserId)) {
                return ResultData.getFailResult("获取当前登录人失败");
            }
            // 校验实体类，如果校验不通过，则返回错误信息
            ValidatedResult validatedResult = ValiDatedUtils.valiDatedBean(smsInspectionPlan);
            if (!validatedResult.isStatus()) {
                return ResultData.getFailResult(validatedResult.getMessage());
            }
            smsInspectionPlan.setCreateUser(currentUserId);
            smsInspectionPlan.setCreateDate(new Date());
            smsInspectionPlan.setDelFlag(false);
            smsInspectionPlan.setEndDate(sdf_second.parse(sdf_day.format(smsInspectionPlan.getEndDate()) + " 23:59:59"));
            smsInspectionPlanService.save(smsInspectionPlan);
            List<SmsUserInspectionPlan> smsUserInspectionPlanList = this.generatorUserInspectionPlan(currentUserId, smsInspectionPlan);
            if (null == smsUserInspectionPlanList || smsUserInspectionPlanList.size() == 0) {
                throw new RuntimeException("生成用户计划详细列表失败");
            }
            smsUserInspectionPlanService.save(smsUserInspectionPlanList);
            ResultData resultData = this.bindEquipmentClass(currentUserId, smsInspectionPlan, smsUserInspectionPlanList);
            if (resultData.getCode() != 0) {
                throw new RuntimeException("绑定区域或设备类型失败");
            }
        } catch (Exception e) {
            return ResultData.getFailResult(e.getMessage());
        }
        return ResultData.getSuccessResult();
    }

    /**
     * 更新方法
     *
     * @param request
     * @param smsInspectionPlan
     * @return
     */
    @ResponseBody
    @Transactional
    @PutMapping("/smsInspectionPlan/update")
    @ApiOperation(value = "修改", notes = "修改方法")
    public ResultData update(HttpServletRequest request, @RequestBody SmsInspectionPlan smsInspectionPlan) {
        try {
            // 获取当前登录用户
            String userId = TokenUtils.getUserIdByRequest(request);
            if (StringUtils.isBlank(userId)) {
                return ResultData.getFailResult("获取当前登录人失败");
            }
            // 根据ID查询数据
            SmsInspectionPlan _smsInspectionPlan = smsInspectionPlanService.getSmsInspectionPlanByIdAndDelFlagIsFalse(smsInspectionPlan.getId());
            if (null == _smsInspectionPlan) {
                return ResultData.getFailResult("该信息不存在");
            }
            // 校验实体类，如果校验不通过，则返回错误信息
            ValidatedResult validatedResult = ValiDatedUtils.valiDatedBean(smsInspectionPlan);
            if (!validatedResult.isStatus()) {
                return ResultData.getFailResult(validatedResult.getMessage());
            }
            _smsInspectionPlan.setUpdateUser(userId);
            _smsInspectionPlan.setUpdateDate(new Date());
            TransmitUtils.sources2destination(smsInspectionPlan, _smsInspectionPlan);
            smsInspectionPlan.setEndDate(sdf_second.parse(sdf_day.format(_smsInspectionPlan.getEndDate()) + " 23:59:59"));
            List<SmsUserInspectionPlan> smsUserInspectionPlanList = this.generatorUserInspectionPlan(userId, smsInspectionPlan);
            if (smsUserInspectionPlanList.size() > 0) {
                smsUserInspectionPlanService.save(smsUserInspectionPlanList);
            }
            smsInspectionPlanService.update(_smsInspectionPlan);
            ResultData resultData = this.bindEquipmentClass(userId, _smsInspectionPlan, smsUserInspectionPlanList);
            if (resultData.getCode() != 0) {
                throw new RuntimeException("绑定区域或设备类型失败");
            }
        } catch (Exception e) {
            return ResultData.getFailResult(e.getMessage());
        }
        return ResultData.getSuccessResult();
    }

    /**
     * 计划绑定设备分类
     *
     * @param currentUserId
     * @param smsInspectionPlan
     * @return
     */
    private ResultData bindEquipmentClass(String currentUserId, SmsInspectionPlan smsInspectionPlan, List<SmsUserInspectionPlan> smsUserInspectionPlanList) throws RuntimeException {
        List<String> idList = new ArrayList<>();
        List<SmsPlanEquipmentClass> smsPlanEquipmentClassList = new ArrayList<>();
        List<SmsInspectionStatus> smsInspectionStatusList = new ArrayList<>();
        if (smsInspectionPlan.getPlanType() == SPECIAL.getKey()) {
            List<SmsEquipmentClass> smsEquipmentClassList = smsEquipmentClassService.findByIds(smsInspectionPlan.getItemIds());
            smsEquipmentClassList.forEach(smsEquipmentClass -> {
                idList.add(smsEquipmentClass.getId());
            });
        } else {
            Map<String, Object> dataMap = new HashMap<>();
            for (int i = 0; i < smsInspectionPlan.getItemIds().length; i++) {
                List<SmsEquipment> smsEquipmentList = smsEquipmentService.findByInstallRegionsId(smsInspectionPlan.getItemIds()[i]);
                for (SmsEquipment smsEquipment : smsEquipmentList) {
                    dataMap.put(smsEquipment.getTypeId(), smsInspectionPlan.getItemIds()[i]);
                }
            }
            Set<String> keys = dataMap.keySet();
            int index = 0;
            for (String key : keys) {
                idList.add(key + "-" + dataMap.get(key));
                index++;
            }
        }
        if (null == idList) {
            throw new RuntimeException("查询设备分类信息出错");
        }
        // 开始绑定设备类型
        for (String id : idList) {
            // 查询该计划是否已经绑定该设备，如果已经绑定则不再次绑定
            SmsPlanEquipmentClass smsPlanEquipmentClass = smsPlanEquipmentService.findByPlanIdAndEquipmentClassId(smsInspectionPlan.getId(), smsInspectionPlan.getPlanType() != SPECIAL.getKey() ? id.split("-")[0] : id);
            if (null == smsPlanEquipmentClass) {
                SmsPlanEquipmentClass newSmsPlanEquipmentClass = new SmsPlanEquipmentClass();
                newSmsPlanEquipmentClass.setPlanId(smsInspectionPlan.getId());
                newSmsPlanEquipmentClass.setEquipmentClassId(smsInspectionPlan.getPlanType() != SPECIAL.getKey() ? id.split("-")[0] : id);
                if (smsInspectionPlan.getPlanType() != SPECIAL.getKey()) {
                    newSmsPlanEquipmentClass.setRegionsId(id.split("-")[1]);
                }
                newSmsPlanEquipmentClass.setCreateDate(new Date());
                newSmsPlanEquipmentClass.setCreateUser(currentUserId);
                newSmsPlanEquipmentClass.setDelFlag(false);
                smsPlanEquipmentClassList.add(newSmsPlanEquipmentClass);
            }
            // 查询设备分类下的所有设备
            List<SmsEquipment> smsEquipmentList = new ArrayList<>();
            if(smsInspectionPlan.getPlanType() != SPECIAL.getKey()){
                smsEquipmentList = smsEquipmentService.findByClassIdAndRegionsId(id.split("-")[0], id.split("-")[1]);
            }else{
                smsEquipmentList = smsEquipmentService.findByClassId(id);
            }
            smsEquipmentList.forEach(smsEquipment -> {
                for (SmsUserInspectionPlan smsUserInspectionPlan : smsUserInspectionPlanList) {
                    SmsInspectionStatus smsInspectionStatus = new SmsInspectionStatus();
                    smsInspectionStatus.setUserInspectionPlanId(smsUserInspectionPlan.getId());
                    smsInspectionStatus.setEquipmentClassId(smsEquipment.getTypeId());
                    smsInspectionStatus.setEquipmentId(smsEquipment.getId());
                    smsInspectionStatus.setCreateDate(new Date());
                    smsInspectionStatus.setCreateUser(currentUserId);
                    smsInspectionStatus.setDelFlag(false);
                    smsInspectionStatusList.add(smsInspectionStatus);
                }
            });
        }
        smsInspectionStatusService.save(smsInspectionStatusList);
        smsPlanEquipmentService.save(smsPlanEquipmentClassList);
        return ResultData.getSuccessResult();
    }

    /**
     * 生成用户计划详细列表
     *
     * @param smsInspectionPlan
     * @return
     */
    private List<SmsUserInspectionPlan> generatorUserInspectionPlan(String currentUserId, SmsInspectionPlan smsInspectionPlan) {
        SimpleDateFormat sdf_second = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat sdf_day = new SimpleDateFormat("yyyy-MM-dd");
        smsUserInspectionPlanService.deleteByInspectionId(smsInspectionPlan.getId());
        List<SmsUserInspectionPlan> smsUserInspectionPlanList = null;
        try {
            Calendar calendar = Calendar.getInstance();//定义日期实例
            switch (PlanTypeEnums.getPlanTypeEnums(smsInspectionPlan.getPlanType())) {
                // 日检查生成详细计划
                case DAY:
                    List<String> dayList = DateUtils.getDayBetweenDateList(sdf_day.format(smsInspectionPlan.getBeginDate()), sdf_day.format(smsInspectionPlan.getEndDate()));
                    smsUserInspectionPlanList = new ArrayList<>();
                    for (String day : dayList) {
                        Date beginDate = sdf_second.parse(day + BEGIN_SECOND);
                        Date endDate = sdf_second.parse(day + END_SECOND);
                        SmsUserInspectionPlan smsUserInspectionPlan = smsUserInspectionPlanService.findByCycleAndPlanId(day, smsInspectionPlan.getId());
                        if (null == smsUserInspectionPlan) {
                            smsUserInspectionPlanList = this.addUserInspectionPlan(smsUserInspectionPlanList, day, smsInspectionPlan, beginDate, endDate, currentUserId);
                        } else {
                            smsUserInspectionPlan.setBeginDate(beginDate);
                            smsUserInspectionPlan.setEndDate(endDate);
                            smsUserInspectionPlan.setUpdateDate(new Date());
                            smsUserInspectionPlan.setUpdateUser(currentUserId);
                            smsUserInspectionPlanService.update(smsUserInspectionPlan);
                        }
                    }
                    break;
                // 周检查生成详细计划
                case WEEK:
                    List<String> weekList = DateUtils.getWeekBetweenDateList(sdf_day.format(smsInspectionPlan.getBeginDate()), sdf_day.format(smsInspectionPlan.getEndDate()));
                    smsUserInspectionPlanList = new ArrayList<>();
                    calendar.setTime(smsInspectionPlan.getBeginDate());//设置日期起始时间
                    for (String week : weekList) {
                        Date beginTime = calendar.getTime();
                        Date beginDate = sdf_second.parse(sdf_day.format(beginTime) + BEGIN_SECOND);
                        int currentDayOfWeek = DateUtils.dateToWeek(sdf_day.format(smsInspectionPlan.getBeginDate()));
                        calendar.add(Calendar.DAY_OF_MONTH, 6 - currentDayOfWeek);
                        Date endTime = calendar.getTime();
                        Date endDate = sdf_second.parse(sdf_day.format(endTime) + END_SECOND);
                        if (endDate.after(smsInspectionPlan.getEndDate())) {
                            endDate = smsInspectionPlan.getEndDate();
                        }
                        SmsUserInspectionPlan smsUserInspectionPlan = smsUserInspectionPlanService.findByCycleAndPlanId(week, smsInspectionPlan.getId());
                        if (null == smsUserInspectionPlan) {
                            smsUserInspectionPlanList = this.addUserInspectionPlan(smsUserInspectionPlanList, week, smsInspectionPlan, beginDate, endDate, currentUserId);
                        } else {
                            smsUserInspectionPlan.setBeginDate(beginDate);
                            smsUserInspectionPlan.setEndDate(endDate);
                            smsUserInspectionPlan.setUpdateDate(new Date());
                            smsUserInspectionPlan.setUpdateUser(currentUserId);
                            smsUserInspectionPlanService.update(smsUserInspectionPlan);
                        }
                        calendar.add(Calendar.DAY_OF_MONTH, 7 - currentDayOfWeek);
                    }
                    break;
                // 月检查生成详细计划
                case MONTH:
                    List<String> monthList = DateUtils.getMonthBetweenDateList(sdf_day.format(smsInspectionPlan.getBeginDate()), sdf_day.format(smsInspectionPlan.getEndDate()));
                    smsUserInspectionPlanList = new ArrayList<>();
                    calendar.setTime(smsInspectionPlan.getBeginDate());//设置日期起始时间
                    for (String month : monthList) {
                        Date beginTime = calendar.getTime();
                        Date beginDate = sdf_second.parse(sdf_day.format(beginTime) + BEGIN_SECOND);
                        Calendar lastDayCalendar = Calendar.getInstance();
                        lastDayCalendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, 0);
                        Date endTime = lastDayCalendar.getTime();
                        Date endDate = sdf_second.parse(sdf_day.format(endTime) + END_SECOND);
                        if (endDate.after(sdf_day.parse(sdf_day.format(smsInspectionPlan.getEndDate()) + END_SECOND))) {
                            endDate = sdf_day.parse(sdf_day.format(smsInspectionPlan.getEndDate()) + END_SECOND);
                        }
                        SmsUserInspectionPlan smsUserInspectionPlan = smsUserInspectionPlanService.findByCycleAndPlanId(month, smsInspectionPlan.getId());
                        if (null == smsUserInspectionPlan) {
                            smsUserInspectionPlanList = this.addUserInspectionPlan(smsUserInspectionPlanList, month, smsInspectionPlan, beginDate, endDate, currentUserId);
                        } else {
                            smsUserInspectionPlan.setBeginDate(beginDate);
                            smsUserInspectionPlan.setEndDate(endDate);
                            smsUserInspectionPlan.setUpdateDate(new Date());
                            smsUserInspectionPlan.setUpdateUser(currentUserId);
                            smsUserInspectionPlanService.update(smsUserInspectionPlan);
                        }
                        calendar.set(lastDayCalendar.get(Calendar.YEAR), lastDayCalendar.get(Calendar.MONTH), lastDayCalendar.get(Calendar.DAY_OF_MONTH) + 1);
                    }
                    break;
                // 季度检查生成详细计划
                case QUARTER:
                    List<String> quarterList = DateUtils.getQuarterList(sdf_day.format(smsInspectionPlan.getBeginDate()), sdf_day.format(smsInspectionPlan.getEndDate()));
                    smsUserInspectionPlanList = new ArrayList<>();
                    calendar.setTime(smsInspectionPlan.getBeginDate());//设置日期起始时间
                    for (String month : quarterList) {
                        Date beginTime = calendar.getTime();
                        Date beginDate = sdf_second.parse(sdf_day.format(beginTime) + BEGIN_SECOND);
                        Date nextBeginTime = DateUtils.getNextQuarterBeginDate(beginDate);
                        Date nextBeginDate = sdf_second.parse(sdf_day.format(nextBeginTime) + BEGIN_SECOND);
                        Calendar lastDayCalendar = Calendar.getInstance();
                        lastDayCalendar.setTime(nextBeginTime);
                        lastDayCalendar.set(lastDayCalendar.get(Calendar.YEAR), lastDayCalendar.get(Calendar.MONTH), 0);
                        Date endTime = lastDayCalendar.getTime();
                        Date endDate = sdf_second.parse(sdf_day.format(endTime) + END_SECOND);
                        if (endDate.after(sdf_second.parse(sdf_day.format(smsInspectionPlan.getEndDate()) + END_SECOND))) {
                            endDate = sdf_second.parse(sdf_day.format(smsInspectionPlan.getEndDate()) + END_SECOND);
                        }
                        SmsUserInspectionPlan smsUserInspectionPlan = smsUserInspectionPlanService.findByCycleAndPlanId(month, smsInspectionPlan.getId());
                        if (null == smsUserInspectionPlan) {
                            smsUserInspectionPlanList = this.addUserInspectionPlan(smsUserInspectionPlanList, month, smsInspectionPlan, beginDate, endDate, currentUserId);
                        } else {
                            smsUserInspectionPlan.setBeginDate(beginDate);
                            smsUserInspectionPlan.setEndDate(endDate);
                            smsUserInspectionPlan.setUpdateDate(new Date());
                            smsUserInspectionPlan.setUpdateUser(currentUserId);
                            smsUserInspectionPlanService.update(smsUserInspectionPlan);
                        }
                        calendar.setTime(nextBeginDate);
                    }
                    break;
                // 专项检查生成详细计划
                case SPECIAL:
                    smsUserInspectionPlanList = new ArrayList<>();
                    SmsUserInspectionPlan smsUserInspectionPlan = smsUserInspectionPlanService.findByCycleIsNullAndPlanId(smsInspectionPlan.getId());
                    if (null == smsUserInspectionPlan) {
                        smsUserInspectionPlanList = this.addUserInspectionPlan(smsUserInspectionPlanList, null, smsInspectionPlan, sdf_second.parse(sdf_day.format(smsInspectionPlan.getBeginDate()) + BEGIN_SECOND), sdf_second.parse(sdf_day.format(smsInspectionPlan.getEndDate()) + END_SECOND), currentUserId);
                    } else {
                        smsUserInspectionPlan.setUpdateDate(new Date());
                        smsUserInspectionPlan.setUpdateUser(currentUserId);
                        smsUserInspectionPlanService.update(smsUserInspectionPlan);
                    }
                    break;
            }
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
        return smsUserInspectionPlanList;
    }

    /**
     * 添加对象到集合中
     *
     * @param smsUserInspectionPlanList
     * @param cycle
     * @param smsInspectionPlan
     * @param beginDate
     * @param endDate
     * @param currentUserId
     * @return
     */
    private List<SmsUserInspectionPlan> addUserInspectionPlan(List<SmsUserInspectionPlan> smsUserInspectionPlanList, String cycle, SmsInspectionPlan smsInspectionPlan, Date beginDate, Date endDate, String currentUserId) {
        SmsUserInspectionPlan smsUserInspectionPlan = new SmsUserInspectionPlan();
        smsUserInspectionPlan.setCycle(cycle);
        smsUserInspectionPlan.setInspectionId(smsInspectionPlan.getId());
        smsUserInspectionPlan.setStatus(PlanStatusEnums.WILL.getKey());
        smsUserInspectionPlan.setPersonLiableId(smsInspectionPlan.getPersonLiableId());
        smsUserInspectionPlan.setBeginDate(beginDate);
        smsUserInspectionPlan.setEndDate(endDate);
        smsUserInspectionPlan.setCreateDate(new Date());
        smsUserInspectionPlan.setCreateUser(currentUserId);
        smsUserInspectionPlan.setDelFlag(false);
        smsUserInspectionPlanList.add(smsUserInspectionPlan);
        return smsUserInspectionPlanList;
    }

    /**
     * 物理删除
     *
     * @param ids
     * @return
     */
    @ResponseBody
    @DeleteMapping("/smsInspectionPlan/delete")
    @ApiOperation(value = "物理删除", notes = "物理删除方法,可执行批量删除,参数用\",\"隔开")
    public ResultData delete(String ids) {
        try {
            smsInspectionPlanService.deleteByIdIn(ids.split(","));
        } catch (Exception e) {
            return ResultData.getFailResult(e.getMessage());
        }
        return ResultData.getSuccessResult();
    }

    /**
     * 逻辑删除
     *
     * @param request
     * @param ids
     * @return
     */
    @ResponseBody
    @DeleteMapping("/smsInspectionPlan/remove")
    @ApiOperation(value = "逻辑删除", notes = "逻辑删除方法,可执行批量删除,参数用\",\"隔开")
    public ResultData remove(HttpServletRequest request, String ids) {
        try {
            String[] idStr = ids.split(",");
            // 获取当前登录用户
            String userId = TokenUtils.getUserIdByRequest(request);
            for (String id : idStr) {
                // 根据ID查询数据
                SmsInspectionPlan smsInspectionPlan = smsInspectionPlanService.findOne(id);
                if (null != smsInspectionPlan) {
                    // 删除生成的所有用户计划
                    List<SmsUserInspectionPlan> smsUserInspectionPlanList = smsUserInspectionPlanService.findByInspectionId(smsInspectionPlan.getId());
                    if(null != smsUserInspectionPlanList && smsUserInspectionPlanList.size() > 0){
                        for (SmsUserInspectionPlan smsUserInspectionPlan : smsUserInspectionPlanList) {
                            List<SmsInspectionStatus> smsInspectionStatusList = smsInspectionStatusService.findByUserInspectionPlanId(smsUserInspectionPlan.getId());
                            if(null != smsInspectionStatusList && smsInspectionStatusList.size() > 0){
                                for (SmsInspectionStatus smsInspectionStatus : smsInspectionStatusList) {
                                    smsInspectionStatus.setUpdateUser(userId);
                                    smsInspectionStatus.setUpdateDate(new Date());
                                    smsInspectionStatus.setDelFlag(true);
                                }
                                smsInspectionStatusService.save(smsInspectionStatusList);
                            }
                            smsUserInspectionPlan.setUpdateUser(userId);
                            smsUserInspectionPlan.setUpdateDate(new Date());
                            smsUserInspectionPlan.setDelFlag(true);
                        }
                        smsUserInspectionPlanService.save(smsUserInspectionPlanList);
                    }
                    // 删除计划绑定的设备分类信息
                    List<SmsPlanEquipmentClass> smsPlanEquipmentClassList = smsPlanEquipmentService.findByPlanId(smsInspectionPlan.getId());
                    for (SmsPlanEquipmentClass smsPlanEquipmentClass : smsPlanEquipmentClassList) {
                        smsPlanEquipmentClass.setUpdateUser(userId);
                        smsPlanEquipmentClass.setUpdateDate(new Date());
                        smsPlanEquipmentClass.setDelFlag(true);
                    }
                    smsPlanEquipmentService.save(smsPlanEquipmentClassList);
                    // 设置删除标识为真
                    smsInspectionPlan.setDelFlag(true);
                    smsInspectionPlan.setUpdateDate(new Date());
                    smsInspectionPlan.setUpdateUser(userId);
                    smsInspectionPlanService.update(smsInspectionPlan);
                }
            }
        } catch (Exception e) {
            return ResultData.getFailResult(e.getMessage());
        }
        return ResultData.getSuccessResult();
    }

    /**
     * 获取计划绑定的设备
     *
     * @author Gaodl
     * 方法名称: findBindEquipments
     * 参数： [planId]
     * 返回值： java.util.List<com.dico.modules.domain.SmsInspectionTarget>
     * 创建时间: 2019/4/19
     */
    @GetMapping("/smsInspectionPlan/findBindEquipmentClass")
    @ApiOperation(value = "获取计划绑定的设备", notes = "获取计划绑定的设备")
    public ResultData findBindEquipments(String planId) {
        List<SmsEquipmentClass> smsEquipmentClassList = null;
        try {
            if (StringUtils.isBlank(planId)) {
                return ResultData.getFailResult("计划ID不能为空");
            }
            List<SmsPlanEquipmentClass> smsPlanEquipmentClassList = smsPlanEquipmentService.findByPlanId(planId);
            List<String> equipmentClassIdList = new ArrayList<>();
            smsPlanEquipmentClassList.forEach(smsPlanEquipment -> {
                equipmentClassIdList.add(smsPlanEquipment.getEquipmentClassId());
            });
            String[] ids = new String[equipmentClassIdList.size()];
            equipmentClassIdList.toArray(ids);
            smsEquipmentClassList = smsEquipmentClassService.findByIds(ids);
        } catch (Exception e) {
            return ResultData.getFailResult(e.getMessage());
        }
        return ResultData.getSuccessResult(smsEquipmentClassList);
    }

    /**
     * 删除计划绑定的设备
     *
     * @author Gaodl
     * 方法名称: removeBindEquipments
     * 参数： [request, planId, equipmentIds]
     * 返回值： com.dico.result.ResultData
     * 创建时间: 2019/4/26
     */
    @ResponseBody
    @DeleteMapping("/smsInspectionPlan/removeBindEquipments")
    @ApiOperation(value = "删除计划绑定的设备类型", notes = "删除计划绑定的设备类型")
    public ResultData removeBindEquipments(HttpServletRequest request, String planId, String[] equipmentClassIds) {
        if (StringUtils.isBlank(planId)) {
            return ResultData.getFailResult("计划ID不能为空");
        }
        if (equipmentClassIds.length == 0) {
            return ResultData.getFailResult("请选择需要解除绑定的设备类型");
        }
        String userId = TokenUtils.getUserIdByRequest(request);
        for (int i = 0; i < equipmentClassIds.length; i++) {
            SmsPlanEquipmentClass smsPlanEquipmentClass = smsPlanEquipmentService.findByPlanIdAndEquipmentClassId(planId, equipmentClassIds[i]);
            if (null != smsPlanEquipmentClass) {
                smsPlanEquipmentClass.setDelFlag(true);
                smsPlanEquipmentClass.setUpdateDate(new Date());
                smsPlanEquipmentClass.setUpdateUser(userId);
                smsPlanEquipmentService.update(smsPlanEquipmentClass);
            }
        }
        return ResultData.getSuccessResult();
    }

    /**
     * 根据用户ID查询所负责的计划
     *
     * @param userId
     * @return
     */
    @ResponseBody
    @GetMapping("/smsInspectionPlan/findByUserId")
    @ApiOperation(value = "根据用户ID查询所负责的计划", notes = "根据用户ID查询所负责的计划")
    public ResultData findByUserId(String userId) {
        List<SmsInspectionPlan> listData;
        try {
            Map<String, Object> queryMap = new HashMap<>(3);
            if (StringUtils.isNotBlank(userId)) {
                queryMap.put("personLiableId_EQ", userId);
            }
            queryMap.put("delFlag_EQ", false);
            Sort sort = new Sort(Sort.Direction.DESC, "createDate");
            listData = smsInspectionPlanService.findAll(queryMap, sort);
        } catch (Exception e) {
            return ResultData.getFailResult(e.getMessage());
        }
        return ResultData.getSuccessResult(listData);
    }

    /**
     * 根据计划ID查询周期计划
     *
     * @param planId
     * @return
     */
    @ResponseBody
    @GetMapping("/smsInspectionPlan/findUserPlanByPlanId")
    @ApiOperation(value = "根据计划ID查询周期计划", notes = "根据计划ID查询周期计划")
    public ResultData findUserPlanByPlanId(HttpServletRequest request, String planId) {
        List<Map<String, Object>> listData;
        try {
            Map<String, Object> queryMap = new HashMap<>(3);
            if (StringUtils.isNotBlank(planId)) {
                queryMap.put("inspectionId_EQ", planId);
            }
            queryMap.put("delFlag_EQ", false);
            Sort sort = new Sort(Sort.Direction.DESC, "createDate");
            listData = smsUserInspectionPlanService.findAll(queryMap, sort, request);
            for (Map<String, Object> listDatum : listData) {
                String personLiableId = null;
                if(null != listDatum.get("personLiableId")){
                    personLiableId = listDatum.get("personLiableId").toString();
                }
                listDatum.put("personLiableId", null);
                listDatum.put("personLiableName", null);
                listDatum.put("organizationId", null);
                listDatum.put("organizationName", null);
                if(StringUtils.isNotBlank(personLiableId)){
                    SysUser sysUser = dicoBaseClient.findUserById(personLiableId);
                    if(null != sysUser){
                        listDatum.put("personLiableId", sysUser.getId());
                        listDatum.put("personLiableName", sysUser.getName());
                        listDatum.put("organizationId", sysUser.getOrganizationId());
                        listDatum.put("organizationName", sysUser.getOrganizationName());
                    }
                }
            }
        } catch (Exception e) {
            return ResultData.getFailResult(e.getMessage());
        }
        return ResultData.getSuccessResult(listData);
    }
}

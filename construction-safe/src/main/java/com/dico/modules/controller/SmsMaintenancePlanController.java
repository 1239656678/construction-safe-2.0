package com.dico.modules.controller;

import com.dico.enums.MaintenanceStatusEnums;
import com.dico.enums.MaintenanceTypeEnums;
import com.dico.enums.PlanStatusEnums;
import com.dico.enums.PlanTypeEnums;
import com.dico.modules.domain.SmsInspectionPlan;
import com.dico.modules.domain.SmsMaintenancePlan;
import com.dico.modules.domain.SmsUserInspectionPlan;
import com.dico.modules.domain.SmsUserMaintenancePlan;
import com.dico.modules.service.SmsMaintenancePlanService;
import com.dico.modules.service.SmsUserMaintenancePlanService;
import com.dico.result.ResultData;
import com.dico.result.ValidatedResult;
import com.dico.util.DateUtils;
import com.dico.util.TokenUtils;
import com.dico.util.TransmitUtils;
import com.dico.util.ValiDatedUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.*;

@Slf4j
@RestController
@Api(tags = "保养计划模块", produces = "保养计划模块Api")
public class SmsMaintenancePlanController {


    private static final String BEGIN_SECOND = " 00:00:00";
    private static final String END_SECOND = " 23:59:59";

    @Autowired
    private SmsMaintenancePlanService smsMaintenancePlanService;

    @Autowired
    private SmsUserMaintenancePlanService smsUserMaintenancePlanService;

    /**
     * 分页查询方法
     */
    @ResponseBody
    @GetMapping("/smsMaintenancePlan/dataPage")
    @ApiOperation(value = "获取分页数据", notes = "获取分页数据")
    public ResultData dataPage(@RequestParam(required = false, defaultValue = "0", value = "pageNum") int pageNum, @RequestParam(required = false, defaultValue = "10", value = "pageSize") int pageSize) {
        Page<SmsMaintenancePlan> smsMaintenancePlanPage;
        try {
            Map<String, Object> queryMap = new HashMap<>(2);
            queryMap.put("delFlag_EQ", false);
            Sort sort = new Sort(Sort.Direction.DESC, "createDate");
            smsMaintenancePlanPage = smsMaintenancePlanService.findAll(queryMap, pageNum, pageSize, sort);
        } catch (Exception e) {
            return ResultData.getFailResult(e.getMessage());
        }
        return ResultData.getSuccessResult(smsMaintenancePlanPage);
    }

    /**
     * 不分页查询方法
     */
    @ResponseBody
    @GetMapping("/smsMaintenancePlan/dataList")
    @ApiOperation(value = "获取数据", notes = "获取数据方法")
    public ResultData dataList(HttpServletRequest request) {
        return ResultData.getSuccessResult(smsMaintenancePlanService.findList());
    }

    /**
     * 获取数据详情
     */
    @ResponseBody
    @GetMapping("/smsMaintenancePlan/dataInfo")
    @ApiOperation(value = "获取数据详情", notes = "获取数据详情")
    public ResultData dataInfo(String smsMaintenancePlanId) {
        SmsMaintenancePlan smsMaintenancePlan = smsMaintenancePlanService.getById(smsMaintenancePlanId);
        if (null == smsMaintenancePlan) {
            return ResultData.getFailResult("数据不存在");
        }
        return ResultData.getSuccessResult(smsMaintenancePlan);
    }

    /**
     * 新增方法
     */
    @ResponseBody
    @Transactional
    @PostMapping("/smsMaintenancePlan/save")
    @ApiOperation(value = "保存", notes = "保存方法")
    public ResultData save(HttpServletRequest request, @RequestBody SmsMaintenancePlan smsMaintenancePlan) {
        try {
            // 获取当前登录用户
            String currentUserId = TokenUtils.getUserIdByRequest(request);
            // 校验实体类，如果校验不通过，则返回错误信息
            ValidatedResult validatedResult = ValiDatedUtils.valiDatedBean(smsMaintenancePlan);
            if (!validatedResult.isStatus()) {
                return ResultData.getFailResult(validatedResult.getMessage());
            }
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            log.error("beginDate------------------>{}", sdf.format(smsMaintenancePlan.getBeginDate()));
            log.error("endDate------------------>{}", sdf.format(smsMaintenancePlan.getEndDate()));
            smsMaintenancePlan.setCreateUser(currentUserId);
            smsMaintenancePlan.setCreateDate(new Date());
            smsMaintenancePlan.setDelFlag(false);
            smsMaintenancePlanService.save(smsMaintenancePlan);
            List<SmsUserMaintenancePlan> smsUserMaintenancePlanList = this.generatorUserMaintenancePlan(currentUserId, smsMaintenancePlan);
            if (null == smsUserMaintenancePlanList || smsUserMaintenancePlanList.size() == 0) {
                throw new RuntimeException("生成用户保养计划详细列表失败");
            }
            smsUserMaintenancePlanService.save(smsUserMaintenancePlanList);
        } catch (Exception e) {
            return ResultData.getFailResult(e.getMessage());
        }
        return ResultData.getSuccessResult();
    }

    /**
     * 生成用户保养计划详细列表
     *
     * @param smsMaintenancePlan
     * @return
     */
    private List<SmsUserMaintenancePlan> generatorUserMaintenancePlan(String currentUserId, SmsMaintenancePlan smsMaintenancePlan) {
        SimpleDateFormat sdf_second = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat sdf_day = new SimpleDateFormat("yyyy-MM-dd");
        smsUserMaintenancePlanService.deleteByMaintenancePlanId(smsMaintenancePlan.getId());
        List<SmsUserMaintenancePlan> smsUserMaintenancePlanList = null;
        try {
            Calendar calendar = Calendar.getInstance();//定义日期实例
            switch (MaintenanceTypeEnums.getMaintenanceTypeEnums(smsMaintenancePlan.getCycle())) {
                // 周保养生成详细计划
                case WEEK:
                    List<String> weekList = DateUtils.getWeekBetweenDateList(sdf_day.format(smsMaintenancePlan.getBeginDate()), sdf_day.format(smsMaintenancePlan.getEndDate()));
                    smsUserMaintenancePlanList = new ArrayList<>();
                    calendar.setTime(smsMaintenancePlan.getBeginDate());//设置日期起始时间
                    for (String week : weekList) {
                        Date beginTime = calendar.getTime();
                        Date beginDate = sdf_second.parse(sdf_day.format(beginTime) + BEGIN_SECOND);
                        int currentDayOfWeek = DateUtils.dateToWeek(sdf_day.format(smsMaintenancePlan.getBeginDate()));
                        calendar.add(Calendar.DAY_OF_MONTH, 6 - currentDayOfWeek);
                        Date endTime = calendar.getTime();
                        Date endDate = sdf_second.parse(sdf_day.format(endTime) + END_SECOND);
                        if (endDate.after(smsMaintenancePlan.getEndDate())) {
                            endDate = smsMaintenancePlan.getEndDate();
                        }
                        SmsUserMaintenancePlan smsUserMaintenancePlan = smsUserMaintenancePlanService.findByCycleAndPlanId(week, smsMaintenancePlan.getId());
                        if (null == smsUserMaintenancePlan) {
                            smsUserMaintenancePlanList = this.addUserMaintenancePlan(smsUserMaintenancePlanList, week, smsMaintenancePlan, beginDate, endDate, currentUserId);
                        } else {
                            smsUserMaintenancePlan.setBeginDate(beginDate);
                            smsUserMaintenancePlan.setEndDate(endDate);
                            smsUserMaintenancePlan.setUpdateDate(new Date());
                            smsUserMaintenancePlan.setUpdateUser(currentUserId);
                            smsUserMaintenancePlanService.update(smsUserMaintenancePlan);
                        }
                        calendar.add(Calendar.DAY_OF_MONTH, 7 - currentDayOfWeek);
                    }
                    break;
                // 月保养生成详细计划
                case MONTH:
                    List<String> monthList = DateUtils.getMonthBetweenDateList(sdf_day.format(smsMaintenancePlan.getBeginDate()), sdf_day.format(smsMaintenancePlan.getEndDate()));
                    smsUserMaintenancePlanList = new ArrayList<>();
                    calendar.setTime(smsMaintenancePlan.getBeginDate());//设置日期起始时间
                    for (String month : monthList) {
                        Date beginTime = calendar.getTime();
                        Date beginDate = sdf_second.parse(sdf_day.format(beginTime) + BEGIN_SECOND);
                        Calendar lastDayCalendar = Calendar.getInstance();
                        lastDayCalendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, 0);
                        Date endTime = lastDayCalendar.getTime();
                        Date endDate = sdf_second.parse(sdf_day.format(endTime) + END_SECOND);
                        if (endDate.after(sdf_day.parse(sdf_day.format(smsMaintenancePlan.getEndDate()) + END_SECOND))) {
                            endDate = sdf_day.parse(sdf_day.format(smsMaintenancePlan.getEndDate()) + END_SECOND);
                        }
                        SmsUserMaintenancePlan smsUserMaintenancePlan = smsUserMaintenancePlanService.findByCycleAndPlanId(month, smsMaintenancePlan.getId());
                        if (null == smsUserMaintenancePlan) {
                            smsUserMaintenancePlanList = this.addUserMaintenancePlan(smsUserMaintenancePlanList, month, smsMaintenancePlan, beginDate, endDate, currentUserId);
                        } else {
                            smsUserMaintenancePlan.setBeginDate(beginDate);
                            smsUserMaintenancePlan.setEndDate(endDate);
                            smsUserMaintenancePlan.setUpdateDate(new Date());
                            smsUserMaintenancePlan.setUpdateUser(currentUserId);
                            smsUserMaintenancePlanService.update(smsUserMaintenancePlan);
                        }
                        calendar.set(lastDayCalendar.get(Calendar.YEAR), lastDayCalendar.get(Calendar.MONTH), lastDayCalendar.get(Calendar.DAY_OF_MONTH) + 1);
                    }
                    break;
                // 季度保养生成详细计划
                case QUARTER:
                    List<String> quarterList = DateUtils.getQuarterList(sdf_day.format(smsMaintenancePlan.getBeginDate()), sdf_day.format(smsMaintenancePlan.getEndDate()));
                    smsUserMaintenancePlanList = new ArrayList<>();
                    calendar.setTime(smsMaintenancePlan.getBeginDate());//设置日期起始时间
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
                        if (endDate.after(sdf_second.parse(sdf_day.format(smsMaintenancePlan.getEndDate()) + END_SECOND))) {
                            endDate = sdf_second.parse(sdf_day.format(smsMaintenancePlan.getEndDate()) + END_SECOND);
                        }
                        SmsUserMaintenancePlan smsUserMaintenancePlan = smsUserMaintenancePlanService.findByCycleAndPlanId(month, smsMaintenancePlan.getId());
                        if (null == smsUserMaintenancePlan) {
                            smsUserMaintenancePlanList = this.addUserMaintenancePlan(smsUserMaintenancePlanList, month, smsMaintenancePlan, beginDate, endDate, currentUserId);
                        } else {
                            smsUserMaintenancePlan.setBeginDate(beginDate);
                            smsUserMaintenancePlan.setEndDate(endDate);
                            smsUserMaintenancePlan.setUpdateDate(new Date());
                            smsUserMaintenancePlan.setUpdateUser(currentUserId);
                            smsUserMaintenancePlanService.update(smsUserMaintenancePlan);
                        }
                        calendar.setTime(nextBeginDate);
                    }
                    break;
                // 年度保养生成详细计划
                case YEAR:
                    List<String> yearList = DateUtils.getYearList(sdf_day.format(smsMaintenancePlan.getBeginDate()), sdf_day.format(smsMaintenancePlan.getEndDate()));
                    smsUserMaintenancePlanList = new ArrayList<>();
                    calendar.setTime(smsMaintenancePlan.getBeginDate());//设置日期起始时间
                    for (String year : yearList) {
                        Date beginTime = calendar.getTime();
                        Date beginDate = sdf_second.parse(sdf_day.format(DateUtils.getYearFirstDay(beginTime)) + BEGIN_SECOND);
                        Date nextBeginTime = DateUtils.getNextYearBeginDate(beginDate);
                        Date nextBeginDate = sdf_second.parse(sdf_day.format(nextBeginTime) + BEGIN_SECOND);
                        Calendar lastDayCalendar = Calendar.getInstance();
                        lastDayCalendar.setTime(nextBeginTime);
                        lastDayCalendar.set(lastDayCalendar.get(Calendar.YEAR), lastDayCalendar.get(Calendar.MONTH), 0);
                        Date endTime = lastDayCalendar.getTime();
                        Date endDate = sdf_second.parse(sdf_day.format(endTime) + END_SECOND);
                        if (endDate.after(sdf_second.parse(sdf_day.format(smsMaintenancePlan.getEndDate()) + END_SECOND))) {
                            endDate = sdf_second.parse(sdf_day.format(smsMaintenancePlan.getEndDate()) + END_SECOND);
                        }
                        SmsUserMaintenancePlan smsUserMaintenancePlan = smsUserMaintenancePlanService.findByCycleAndPlanId(year, smsMaintenancePlan.getId());
                        if (null == smsUserMaintenancePlan) {
                            smsUserMaintenancePlanList = this.addUserMaintenancePlan(smsUserMaintenancePlanList, year, smsMaintenancePlan, beginDate, endDate, currentUserId);
                        } else {
                            smsUserMaintenancePlan.setBeginDate(beginDate);
                            smsUserMaintenancePlan.setEndDate(endDate);
                            smsUserMaintenancePlan.setUpdateDate(new Date());
                            smsUserMaintenancePlan.setUpdateUser(currentUserId);
                            smsUserMaintenancePlanService.update(smsUserMaintenancePlan);
                        }
                        calendar.setTime(nextBeginDate);
                    }
                    break;
            }
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
        return smsUserMaintenancePlanList;
    }

    /**
     * 添加对象到集合中
     *
     * @param smsUserMaintenancePlanList
     * @param cycle
     * @param smsMaintenancePlan
     * @param beginDate
     * @param endDate
     * @param currentUserId
     * @return
     */
    private List<SmsUserMaintenancePlan> addUserMaintenancePlan(List<SmsUserMaintenancePlan> smsUserMaintenancePlanList, String cycle, SmsMaintenancePlan smsMaintenancePlan, Date beginDate, Date endDate, String currentUserId) {
        SmsUserMaintenancePlan smsUserMaintenancePlan = new SmsUserMaintenancePlan();
        smsUserMaintenancePlan.setCycle(cycle);
        smsUserMaintenancePlan.setMaintenancePlanId(smsMaintenancePlan.getId());
        smsUserMaintenancePlan.setStatus(MaintenanceStatusEnums.WILL.getKey());
        smsUserMaintenancePlan.setBeginDate(beginDate);
        smsUserMaintenancePlan.setEndDate(endDate);
        smsUserMaintenancePlan.setCreateDate(new Date());
        smsUserMaintenancePlan.setCreateUser(currentUserId);
        smsUserMaintenancePlan.setDelFlag(false);
        smsUserMaintenancePlanList.add(smsUserMaintenancePlan);
        return smsUserMaintenancePlanList;
    }

    /**
     * 更新方法
     */
    @ResponseBody
    @PutMapping("/smsMaintenancePlan/update")
    @ApiOperation(value = "修改", notes = "修改方法")
    public ResultData update(HttpServletRequest request, @RequestBody SmsMaintenancePlan smsMaintenancePlan) {
        try {
            // 获取当前登录用户
            String currentUserId = TokenUtils.getUserIdByRequest(request);
            // 根据ID查询数据
            SmsMaintenancePlan _smsMaintenancePlan = smsMaintenancePlanService.getById(smsMaintenancePlan.getId());
            if (null == _smsMaintenancePlan) {
                return ResultData.getFailResult("该信息不存在");
            }
            // 校验实体类，如果校验不通过，则返回错误信息
            ValidatedResult validatedResult = ValiDatedUtils.valiDatedBean(smsMaintenancePlan);
            if (!validatedResult.isStatus()) {
                return ResultData.getFailResult(validatedResult.getMessage());
            }
            _smsMaintenancePlan.setUpdateUser(currentUserId);
            _smsMaintenancePlan.setUpdateDate(new Date());
            TransmitUtils.sources2destination(smsMaintenancePlan, _smsMaintenancePlan);
            smsMaintenancePlanService.update(_smsMaintenancePlan);
            List<SmsUserMaintenancePlan> smsUserMaintenancePlanList = this.generatorUserMaintenancePlan(currentUserId, smsMaintenancePlan);
            if (null == smsUserMaintenancePlanList || smsUserMaintenancePlanList.size() == 0) {
                throw new RuntimeException("生成用户保养计划详细列表失败");
            }
            smsUserMaintenancePlanService.save(smsUserMaintenancePlanList);
        } catch (Exception e) {
            return ResultData.getFailResult(e.getMessage());
        }
        return ResultData.getSuccessResult();
    }

    /**
     * 物理删除
     */
    @ResponseBody
    @DeleteMapping("/smsMaintenancePlan/delete")
    @ApiOperation(value = "物理删除", notes = "物理删除方法,可执行批量删除,参数用\",\"隔开")
    public ResultData delete(String ids) {
        try {
            smsMaintenancePlanService.deleteByIdIn(ids.split(","));
        } catch (Exception e) {
            return ResultData.getFailResult(e.getMessage());
        }
        return ResultData.getSuccessResult();
    }

    /**
     * 逻辑删除
     */
    @ResponseBody
    @DeleteMapping("/smsMaintenancePlan/remove")
    @ApiOperation(value = "逻辑删除", notes = "逻辑删除方法,可执行批量删除,参数用\",\"隔开")
    public ResultData remove(HttpServletRequest request, String ids) {
        try {
            String[] idStr = ids.split(",");
            // 获取当前登录用户
            String userId = TokenUtils.getUserIdByRequest(request);
            for (String id : idStr) {
                // 根据ID查询数据
                SmsMaintenancePlan smsMaintenancePlan = smsMaintenancePlanService.getById(id);
                if (null != smsMaintenancePlan) {
                    // 设置删除标识为真
                    smsMaintenancePlan.setDelFlag(true);
                    smsMaintenancePlan.setUpdateDate(new Date());
                    smsMaintenancePlan.setUpdateUser(userId);
                    smsMaintenancePlanService.update(smsMaintenancePlan);
                }
            }
        } catch (Exception e) {
            return ResultData.getFailResult(e.getMessage());
        }
        return ResultData.getSuccessResult();
    }
}

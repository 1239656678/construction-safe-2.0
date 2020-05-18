package com.dico.modules.controller;

import com.dico.modules.domain.SmsInspectionStatus;
import com.dico.modules.dto.SmsInspectionStatusDto;
import com.dico.modules.service.SmsInspectionStatusService;
import com.dico.result.ResultData;
import com.dico.result.ValidatedResult;
import com.dico.util.TokenUtils;
import com.dico.util.TransmitUtils;
import com.dico.util.ValiDatedUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

@RestController
@Api(tags = "巡检状态模块", produces = "巡检状态模块Api")
public class SmsInspectionStatusController {

    @Autowired
    private SmsInspectionStatusService smsInspectionStatusService;

    /**
     * 分页查询方法
     */
    @ResponseBody
    @GetMapping("/smsInspectionStatus/dataPage")
    @ApiOperation(value = "获取分页数据", notes = "获取分页数据")
    public ResultData dataPage(@RequestParam(required = false, defaultValue = "0", value = "pageNum") int pageNum, @RequestParam(required = false, defaultValue = "10", value = "pageSize") int pageSize) {
        Page<SmsInspectionStatus> smsInspectionStatusPage;
        try {
            Map<String, Object> queryMap = new HashMap<>(2);
            queryMap.put("delFlag_EQ", false);
            Sort sort = new Sort(Sort.Direction.DESC, "createDate");
            smsInspectionStatusPage = smsInspectionStatusService.findAll(queryMap, pageNum, pageSize, sort);
        } catch (Exception e) {
            return ResultData.getFailResult(e.getMessage());
        }
        return ResultData.getSuccessResult(smsInspectionStatusPage);
    }

    /**
     * 不分页查询方法
     */
    @ResponseBody
    @GetMapping("/smsInspectionStatus/dataList")
    @ApiOperation(value = "获取数据", notes = "获取数据方法")
    public ResultData dataList(HttpServletRequest request) {
        List<Map<String, Object>> listDataMap;
        try {
            Map<String, Object> queryMap = new HashMap<>(2);
            queryMap.put("delFlag_EQ", false);
            Sort sort = new Sort(Sort.Direction.DESC, "createDate");
            listDataMap = smsInspectionStatusService.findAll(queryMap, sort, request);
        } catch (Exception e) {
            return ResultData.getFailResult(e.getMessage());
        }
        return ResultData.getSuccessResult(listDataMap);
    }

    /**
     * 获取数据详情
     */
    @ResponseBody
    @GetMapping("/smsInspectionStatus/dataInfo")
    @ApiOperation(value = "获取数据详情", notes = "获取数据详情")
    public ResultData dataInfo(String smsInspectionStatusId) {
        SmsInspectionStatus smsInspectionStatus = smsInspectionStatusService.getByIdAndDelFlagIsFalse(smsInspectionStatusId);
        if (null == smsInspectionStatus) {
            return ResultData.getFailResult("数据不存在");
        }
        return ResultData.getSuccessResult(smsInspectionStatus);
    }

    /**
     * 新增方法
     */
    @ResponseBody
    @PostMapping("/smsInspectionStatus/save")
    @ApiOperation(value = "保存", notes = "保存方法")
    public ResultData save(HttpServletRequest request, @RequestBody SmsInspectionStatus smsInspectionStatus) {
        try {
            // 获取当前登录用户
            String userId = TokenUtils.getUserIdByRequest(request);
            // 校验实体类，如果校验不通过，则返回错误信息
            ValidatedResult validatedResult = ValiDatedUtils.valiDatedBean(smsInspectionStatus);
            if (!validatedResult.isStatus()) {
                return ResultData.getFailResult(validatedResult.getMessage());
            }
            smsInspectionStatus.setCreateUser(userId);
            smsInspectionStatus.setCreateDate(new Date());
            smsInspectionStatus.setDelFlag(false);
            smsInspectionStatusService.save(smsInspectionStatus);
        } catch (Exception e) {
            return ResultData.getFailResult(e.getMessage());
        }
        return ResultData.getSuccessResult();
    }

    /**
     * 更新方法
     */
    @ResponseBody
    @PutMapping("/smsInspectionStatus/update")
    @ApiOperation(value = "修改", notes = "修改方法")
    public ResultData update(HttpServletRequest request, @RequestBody SmsInspectionStatus smsInspectionStatus) {
        try {
            // 获取当前登录用户
            String userId = TokenUtils.getUserIdByRequest(request);
            // 根据ID查询数据
            SmsInspectionStatus _smsInspectionStatus = smsInspectionStatusService.getByIdAndDelFlagIsFalse(smsInspectionStatus.getId());
            if (null == _smsInspectionStatus) {
                return ResultData.getFailResult("该信息不存在");
            }
            // 校验实体类，如果校验不通过，则返回错误信息
            ValidatedResult validatedResult = ValiDatedUtils.valiDatedBean(smsInspectionStatus);
            if (!validatedResult.isStatus()) {
                return ResultData.getFailResult(validatedResult.getMessage());
            }
            _smsInspectionStatus.setUpdateUser(userId);
            _smsInspectionStatus.setUpdateDate(new Date());
            TransmitUtils.sources2destination(smsInspectionStatus, _smsInspectionStatus);
            smsInspectionStatusService.update(_smsInspectionStatus);
        } catch (Exception e) {
            return ResultData.getFailResult(e.getMessage());
        }
        return ResultData.getSuccessResult();
    }

    /**
     * 物理删除
     */
    @ResponseBody
    @DeleteMapping("/smsInspectionStatus/delete")
    @ApiOperation(value = "物理删除", notes = "物理删除方法,可执行批量删除,参数用\",\"隔开")
    public ResultData delete(String ids) {
        try {
            smsInspectionStatusService.deleteByIdIn(ids.split(","));
        } catch (Exception e) {
            return ResultData.getFailResult(e.getMessage());
        }
        return ResultData.getSuccessResult();
    }

    /**
     * 逻辑删除
     */
    @ResponseBody
    @DeleteMapping("/smsInspectionStatus/remove")
    @ApiOperation(value = "逻辑删除", notes = "逻辑删除方法,可执行批量删除,参数用\",\"隔开")
    public ResultData remove(HttpServletRequest request, String ids) {
        try {
            String[] idStr = ids.split(",");
            // 获取当前登录用户
            String userId = TokenUtils.getUserIdByRequest(request);
            for (String id : idStr) {
                // 根据ID查询数据
                SmsInspectionStatus smsInspectionStatus = smsInspectionStatusService.findOne(id);
                if (null != smsInspectionStatus) {
                    // 设置删除标识为真
                    smsInspectionStatus.setDelFlag(true);
                    smsInspectionStatus.setUpdateDate(new Date());
                    smsInspectionStatus.setUpdateUser(userId);
                    smsInspectionStatusService.update(smsInspectionStatus);
                }
            }
        } catch (Exception e) {
            return ResultData.getFailResult(e.getMessage());
        }
        return ResultData.getSuccessResult();
    }

    /**
     * 根据周期计划ID查询巡检详情
     */
    @ResponseBody
    @GetMapping("/smsInspectionStatus/findInspectionInfoByUserPlanId")
    @ApiOperation(value = "根据周期计划ID查询巡检详情", notes = "根据周期计划ID查询巡检详情")
    public ResultData findInspectionInfoByUserPlanId(HttpServletRequest request, String id) {
        List<SmsInspectionStatusDto> smsInspectionStatusDtoList = smsInspectionStatusService.findByUserPlanId(id);
        return ResultData.getSuccessResult(smsInspectionStatusDtoList);
    }
}

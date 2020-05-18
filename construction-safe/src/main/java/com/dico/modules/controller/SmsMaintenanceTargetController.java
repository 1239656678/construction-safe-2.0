package com.dico.modules.controller;

import com.dico.modules.domain.SmsClassMaintenanceTarget;
import com.dico.modules.domain.SmsEquipmentClassTarget;
import com.dico.modules.domain.SmsMaintenanceTarget;
import com.dico.modules.service.SmsClassMaintenanceTargetService;
import com.dico.modules.service.SmsMaintenanceTargetService;
import com.dico.result.ResultData;
import com.dico.result.ValidatedResult;
import com.dico.util.TokenUtils;
import com.dico.util.TransmitUtils;
import com.dico.util.ValiDatedUtils;
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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.*;

@RestController
@Api(tags = "保养项", produces = "保养项")
public class SmsMaintenanceTargetController {

    @Autowired
    private SmsMaintenanceTargetService smsMaintenanceTargetService;
    @Autowired
    private SmsClassMaintenanceTargetService smsClassMaintenanceTargetService;

    /**
     * 分页查询方法
     */
    @ResponseBody
    @GetMapping("/smsMaintenanceTarget/dataPage")
    @ApiOperation(value = "获取分页数据", notes = "获取分页数据")
    public ResultData dataPage(@RequestParam(required = false, defaultValue = "0", value = "pageNum") int pageNum, @RequestParam(required = false, defaultValue = "10", value = "pageSize") int pageSize) {
        Page<SmsMaintenanceTarget> smsMaintenanceTargetPage;
        try {
            Map<String, Object> queryMap = new HashMap<>(2);
            queryMap.put("delFlag_EQ", false);
            Sort sort = new Sort(Sort.Direction.DESC, "createDate");
            smsMaintenanceTargetPage = smsMaintenanceTargetService.findAll(queryMap, pageNum, pageSize, sort);
        } catch (Exception e) {
            return ResultData.getFailResult(e.getMessage());
        }
        return ResultData.getSuccessResult(smsMaintenanceTargetPage);
    }

    /**
     * 不分页查询方法
     */
    @ResponseBody
    @GetMapping("/smsMaintenanceTarget/dataList")
    @ApiOperation(value = "获取数据", notes = "获取数据方法")
    public ResultData dataList(HttpServletRequest request) {
        List<Map<String, Object>> listDataMap;
        try {
            Map<String, Object> queryMap = new HashMap<>(2);
            queryMap.put("delFlag_EQ", false);
            Sort sort = new Sort(Sort.Direction.DESC, "createDate");
            listDataMap = smsMaintenanceTargetService.findAll(queryMap, sort, request);
        } catch (Exception e) {
            return ResultData.getFailResult(e.getMessage());
        }
        return ResultData.getSuccessResult(listDataMap);
    }

    /**
     * 获取数据详情
     */
    @ResponseBody
    @GetMapping("/smsMaintenanceTarget/dataInfo")
    @ApiOperation(value = "获取数据详情", notes = "获取数据详情")
    public ResultData dataInfo(String smsMaintenanceTargetId) {
        SmsMaintenanceTarget smsMaintenanceTarget = smsMaintenanceTargetService.getById(smsMaintenanceTargetId);
        if (null == smsMaintenanceTarget) {
            return ResultData.getFailResult("数据不存在");
        }
        return ResultData.getSuccessResult(smsMaintenanceTarget);
    }

    /**
     * 新增方法
     */
    @ResponseBody
    @PostMapping("/smsMaintenanceTarget/save")
    @ApiOperation(value = "保存", notes = "保存方法")
    public ResultData save(HttpServletRequest request, @RequestBody SmsMaintenanceTarget smsMaintenanceTarget) {
        try {
            // 获取当前登录用户
            String userId = TokenUtils.getUserIdByRequest(request);
            // 校验实体类，如果校验不通过，则返回错误信息
            ValidatedResult validatedResult = ValiDatedUtils.valiDatedBean(smsMaintenanceTarget);
            if (!validatedResult.isStatus()) {
                return ResultData.getFailResult(validatedResult.getMessage());
            }
            smsMaintenanceTarget.setCreateUser(userId);
            smsMaintenanceTarget.setCreateDate(new Date());
            smsMaintenanceTarget.setDelFlag(false);
            smsMaintenanceTargetService.save(smsMaintenanceTarget);
        } catch (Exception e) {
            return ResultData.getFailResult(e.getMessage());
        }
        return ResultData.getSuccessResult();
    }

    /**
     * 更新方法
     */
    @ResponseBody
    @PutMapping("/smsMaintenanceTarget/update")
    @ApiOperation(value = "修改", notes = "修改方法")
    public ResultData update(HttpServletRequest request, @RequestBody SmsMaintenanceTarget smsMaintenanceTarget) {
        try {
            // 获取当前登录用户
            String userId = TokenUtils.getUserIdByRequest(request);
            // 根据ID查询数据
            SmsMaintenanceTarget _smsMaintenanceTarget = smsMaintenanceTargetService.getById(smsMaintenanceTarget.getId());
            if (null == _smsMaintenanceTarget) {
                return ResultData.getFailResult("该信息不存在");
            }
            // 校验实体类，如果校验不通过，则返回错误信息
            ValidatedResult validatedResult = ValiDatedUtils.valiDatedBean(smsMaintenanceTarget);
            if (!validatedResult.isStatus()) {
                return ResultData.getFailResult(validatedResult.getMessage());
            }
            _smsMaintenanceTarget.setUpdateUser(userId);
            _smsMaintenanceTarget.setUpdateDate(new Date());
            TransmitUtils.sources2destination(smsMaintenanceTarget, _smsMaintenanceTarget);
            smsMaintenanceTargetService.update(_smsMaintenanceTarget);
        } catch (Exception e) {
            return ResultData.getFailResult(e.getMessage());
        }
        return ResultData.getSuccessResult();
    }

    /**
     * 物理删除
     */
    @ResponseBody
    @DeleteMapping("/smsMaintenanceTarget/delete")
    @ApiOperation(value = "物理删除", notes = "物理删除方法,可执行批量删除,参数用\",\"隔开")
    public ResultData delete(String ids) {
        try {
            smsMaintenanceTargetService.deleteByIdIn(ids.split(","));
        } catch (Exception e) {
            return ResultData.getFailResult(e.getMessage());
        }
        return ResultData.getSuccessResult();
    }

    /**
     * 逻辑删除
     */
    @ResponseBody
    @DeleteMapping("/smsMaintenanceTarget/remove")
    @ApiOperation(value = "逻辑删除", notes = "逻辑删除方法,可执行批量删除,参数用\",\"隔开")
    public ResultData remove(HttpServletRequest request, String ids) {
        try {
            String[] idStr = ids.split(",");
            // 获取当前登录用户
            String userId = TokenUtils.getUserIdByRequest(request);
            for (String id : idStr) {
                // 根据ID查询数据
                SmsMaintenanceTarget smsMaintenanceTarget = smsMaintenanceTargetService.getById(id);
                if (null != smsMaintenanceTarget) {
                    // 设置删除标识为真
                    smsMaintenanceTarget.setDelFlag(true);
                    smsMaintenanceTarget.setUpdateDate(new Date());
                    smsMaintenanceTarget.setUpdateUser(userId);
                    smsMaintenanceTargetService.update(smsMaintenanceTarget);
                }
            }
        } catch (Exception e) {
            return ResultData.getFailResult(e.getMessage());
        }
        return ResultData.getSuccessResult();
    }
    /**
     * 设备类型绑定保养项
     *
     * @author Core.Mrsz
     * 方法名称: bindTargets
     * 参数： [equipmentClassId, targetIds]
     * 返回值： com.dico.result.ResultData
     * 创建时间: 2020/3/31
     */
    @ResponseBody
    @PostMapping("/smsMaintenanceTarget/bindTargets")
    @ApiOperation(value = "设备类型绑定保养项", notes = "设备类型绑定保养项")
    public ResultData bindTargets(HttpServletRequest request, @RequestParam(value = "equipmentClassId") String equipmentClassId, @RequestParam(value = "targetIds") String targetIds) {
        String[] targetIdArray = null;
        if (StringUtils.isNotBlank(targetIds)) {
            targetIdArray = targetIds.split(",");
        }
        return this.bindTargets(request, equipmentClassId, targetIdArray);
    }
    private ResultData bindTargets(HttpServletRequest request, String equipmentClassId, String[] targetIds) {
        try {
            String userId = TokenUtils.getUserIdByRequest(request);
            // 校验信息
            if (!StringUtils.isNotBlank(equipmentClassId)) {
                return ResultData.getFailResult("设备类型不能为空");
            }
            if (targetIds.length == 0) {
                return ResultData.getFailResult("请选择需要关联的保养项");
            }
            List<SmsClassMaintenanceTarget> smsClassMaintenanceTargetsOldList=smsClassMaintenanceTargetService.findSmsClassMaintenanceTargetByEquipmentClassId(equipmentClassId);
           if(null!=smsClassMaintenanceTargetsOldList) {
               for (int i = 0; i < smsClassMaintenanceTargetsOldList.size(); i++) {
                   smsClassMaintenanceTargetService.deleteById(smsClassMaintenanceTargetsOldList.get(i).getId());
               }
           }
            List<SmsClassMaintenanceTarget> smsClassMaintenanceTargetsList = new ArrayList<>();
            for (int i = 0; i < targetIds.length; i++) {
            // 查询该巡检项是否已经绑定，如果已经绑定则不再次绑定
                SmsClassMaintenanceTarget smsClassTarget = smsClassMaintenanceTargetService.findByEquipmentClassIdEqualsAndTargetIdEqualsAndDelFlagIsFalse(equipmentClassId, targetIds[i]);
                if (null == smsClassTarget) {
                    SmsClassMaintenanceTarget smsClassMaintenanceTarget = new SmsClassMaintenanceTarget();
                    smsClassMaintenanceTarget.setEquipmentClassId(equipmentClassId);
                    smsClassMaintenanceTarget.setTargetId(targetIds[i]);
                    smsClassMaintenanceTarget.setDelFlag(false);
                    smsClassMaintenanceTarget.setCreateUser(userId);
                    smsClassMaintenanceTarget.setCreateDate(new Date());
                    smsClassMaintenanceTargetsList.add(smsClassMaintenanceTarget);
                }
            }

            smsClassMaintenanceTargetService.save(smsClassMaintenanceTargetsList);
        } catch (Exception e) {
            return ResultData.getFailResult(e.getMessage());
        }
        return ResultData.getSuccessResult();
    }
}

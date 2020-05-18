package com.dico.modules.controller;

import com.dico.modules.domain.SmsInspectionTarget;
import com.dico.modules.service.SmsInspectionTargetService;
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
import java.util.*;

/**
 * 检查项模块
 */
@RestController
@Api(tags = "检查项模块", produces = "检查项模块Api")
public class SmsInspectionTargetController {

    @Autowired
    private SmsInspectionTargetService smsInspectionTargetService;

    /**
     * 分页查询方法
     *
     * @author Gaodl
     * 方法名称: dataPage
     * 参数： [request, equipmentClassId, pageNum, pageSize]
     * 返回值： com.dico.result.ResultData
     * 创建时间: 2019/4/12
     */
    @ResponseBody
    @GetMapping("/smsInspectionTarget/dataPage")
    @ApiOperation(value = "获取分页数据", notes = "获取分页数据")
    public ResultData dataPage(HttpServletRequest request, String equipmentClassId, String targetCode, String targetName, @RequestParam(required = false, defaultValue = "0", value = "pageNum") int pageNum, @RequestParam(required = false, defaultValue = "10", value = "pageSize") int pageSize) {
        Page<SmsInspectionTarget> smsInspectionTargetPage;
        try {
            Map<String, Object> queryMap = new HashMap<>(3);
            queryMap.put("delFlag_EQ", false);
            if (StringUtils.isNotBlank(targetCode)) {
                queryMap.put("targetCode_LIKE", targetCode);
            }
            if (StringUtils.isNotBlank(targetName)) {
                queryMap.put("targetName_LIKE", targetName);
            }
            if (StringUtils.isNotBlank(equipmentClassId)) {
                queryMap.put("equipmentClassId_EQ", equipmentClassId);
            }
            Sort sort = new Sort(Sort.Direction.DESC, "createDate");
            smsInspectionTargetPage = smsInspectionTargetService.findAll(queryMap, pageNum, pageSize, sort);
        } catch (Exception e) {
            return ResultData.getFailResult(e.getMessage());
        }
        return ResultData.getSuccessResult(smsInspectionTargetPage);
    }

    /**
     * 不分页查询方法
     *
     * @author Gaodl
     * 方法名称: dataList
     * 参数： [request, equipmentClassId]
     * 返回值： com.dico.result.ResultData
     * 创建时间: 2019/4/12
     */
    @ResponseBody
    @GetMapping("/smsInspectionTarget/dataList")
    @ApiOperation(value = "获取数据", notes = "获取数据方法")
    public ResultData dataList(HttpServletRequest request, @RequestParam(required = false) String equipmentClassId, @RequestParam(required = false) String targetCode, @RequestParam(required = false) String targetName) {
        List<Map<String, Object>> listDataMap;
        try {
            Map<String, Object> queryMap = new HashMap<>(4);
            queryMap.put("delFlag_EQ", false);
            if (StringUtils.isNotBlank(equipmentClassId)) {
                queryMap.put("equipmentClassId_EQ", equipmentClassId);
            }
            if (StringUtils.isNotBlank(targetCode)) {
                queryMap.put("targetCode_LIKE", targetCode);
            }
            if (StringUtils.isNotBlank(targetName)) {
                queryMap.put("targetName_LIKE", targetName);
            }
            Sort sort = new Sort(Sort.Direction.DESC, "createDate");
            listDataMap = smsInspectionTargetService.findAll(queryMap, sort, request);
        } catch (Exception e) {
            return ResultData.getFailResult(e.getMessage());
        }
        return ResultData.getSuccessResult(listDataMap);
    }

    /**
     * 获取数据详情
     *
     * @author Gaodl
     * 方法名称: dataInfo
     * 参数： [smsInspectionTargetId]
     * 返回值： com.dico.result.ResultData
     * 创建时间: 2019/4/12
     */
    @ResponseBody
    @GetMapping("/smsInspectionTarget/dataInfo")
    @ApiOperation(value = "获取数据详情", notes = "获取数据详情")
    public ResultData dataInfo(String smsInspectionTargetId) {
        SmsInspectionTarget smsInspectionTarget = smsInspectionTargetService.getSmsInspectionTargetByIdAndDelFlagIsFalse(smsInspectionTargetId);
        if (null == smsInspectionTarget) {
            return ResultData.getFailResult("数据不存在");
        }
        return ResultData.getSuccessResult(smsInspectionTarget);
    }

    /**
     * 新增方法
     *
     * @author Gaodl
     * 方法名称: save
     * 参数： [request, smsInspectionTarget]
     * 返回值： com.dico.result.ResultData
     * 创建时间: 2019/4/12
     */
    @ResponseBody
    @PostMapping("/smsInspectionTarget/save")
    @ApiOperation(value = "保存", notes = "保存方法")
    public ResultData save(HttpServletRequest request, @RequestBody SmsInspectionTarget smsInspectionTarget) {
        try {
            // 获取当前登录用户
            String userId = TokenUtils.getUserIdByRequest(request);
            // 校验实体类，如果校验不通过，则返回错误信息
            ValidatedResult validatedResult = ValiDatedUtils.valiDatedBean(smsInspectionTarget);
            if (!validatedResult.isStatus()) {
                return ResultData.getFailResult(validatedResult.getMessage());
            }
            smsInspectionTarget.setCreateUser(userId);
            smsInspectionTarget.setCreateDate(new Date());
            smsInspectionTarget.setDelFlag(false);
            smsInspectionTargetService.save(smsInspectionTarget);
        } catch (Exception e) {
            return ResultData.getFailResult(e.getMessage());
        }
        return ResultData.getSuccessResult();
    }

    /**
     * 更新方法
     *
     * @author Gaodl
     * 方法名称: update
     * 参数： [request, smsInspectionTarget]
     * 返回值： com.dico.result.ResultData
     * 创建时间: 2019/4/12
     */
    @ResponseBody
    @PutMapping("/smsInspectionTarget/update")
    @ApiOperation(value = "修改", notes = "修改方法")
    public ResultData update(HttpServletRequest request, @RequestBody SmsInspectionTarget smsInspectionTarget) {
        try {
            // 获取当前登录用户
            String userId = TokenUtils.getUserIdByRequest(request);
            // 根据ID查询数据
            SmsInspectionTarget _smsInspectionTarget = smsInspectionTargetService.getSmsInspectionTargetByIdAndDelFlagIsFalse(smsInspectionTarget.getId());
            if (null == _smsInspectionTarget) {
                return ResultData.getFailResult("该信息不存在");
            }
            // 校验实体类，如果校验不通过，则返回错误信息
            ValidatedResult validatedResult = ValiDatedUtils.valiDatedBean(smsInspectionTarget);
            if (!validatedResult.isStatus()) {
                return ResultData.getFailResult(validatedResult.getMessage());
            }
            _smsInspectionTarget.setUpdateUser(userId);
            _smsInspectionTarget.setUpdateDate(new Date());
            TransmitUtils.sources2destination(smsInspectionTarget, _smsInspectionTarget);
            smsInspectionTargetService.update(_smsInspectionTarget);
        } catch (Exception e) {
            return ResultData.getFailResult(e.getMessage());
        }
        return ResultData.getSuccessResult();
    }

    /**
     * 物理删除
     *
     * @author Gaodl
     * 方法名称: delete
     * 参数： [ids]
     * 返回值： com.dico.result.ResultData
     * 创建时间: 2019/4/12
     */
    @ResponseBody
    @DeleteMapping("/smsInspectionTarget/delete")
    @ApiOperation(value = "物理删除", notes = "物理删除方法,可执行批量删除,参数用\",\"隔开")
    public ResultData delete(String ids) {
        try {
            smsInspectionTargetService.deleteByIdIn(ids.split(","));
        } catch (Exception e) {
            return ResultData.getFailResult(e.getMessage());
        }
        return ResultData.getSuccessResult();
    }

    /**
     * 逻辑删除
     *
     * @author Gaodl
     * 方法名称: remove
     * 参数： [request, ids]
     * 返回值： com.dico.result.ResultData
     * 创建时间: 2019/4/12
     */
    @ResponseBody
    @DeleteMapping("/smsInspectionTarget/remove")
    @ApiOperation(value = "逻辑删除", notes = "逻辑删除方法,可执行批量删除,参数用\",\"隔开")
    public ResultData remove(HttpServletRequest request, String ids) {
        try {
            String[] idStr = ids.split(",");
            // 获取当前登录用户
            String userId = TokenUtils.getUserIdByRequest(request);
            for (String id : idStr) {
                // 根据ID查询数据
                SmsInspectionTarget smsInspectionTarget = smsInspectionTargetService.findOne(id);
                if (null != smsInspectionTarget) {
                    smsInspectionTarget.setUpdateDate(new Date());
                    smsInspectionTarget.setUpdateUser(userId);
                    // 设置删除标识为真
                    smsInspectionTarget.setDelFlag(true);
                    smsInspectionTargetService.update(smsInspectionTarget);
                }
            }
        } catch (Exception e) {
            return ResultData.getFailResult(e.getMessage());
        }
        return ResultData.getSuccessResult();
    }
}
